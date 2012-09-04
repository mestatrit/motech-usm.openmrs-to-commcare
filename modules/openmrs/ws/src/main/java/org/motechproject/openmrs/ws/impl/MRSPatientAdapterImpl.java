package org.motechproject.openmrs.ws.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.motechproject.mrs.exception.MRSException;
import org.motechproject.mrs.exception.PatientNotFoundException;
import org.motechproject.mrs.model.MRSFacility;
import org.motechproject.mrs.model.MRSPatient;
import org.motechproject.mrs.model.MRSPerson;
import org.motechproject.mrs.services.MRSFacilityAdapter;
import org.motechproject.mrs.services.MRSPatientAdapter;
import org.motechproject.openmrs.ws.OpenMrsInstance;
import org.motechproject.openmrs.ws.OpenMrsInstance.ApiVersion;
import org.motechproject.openmrs.ws.resource.PatientResource;
import org.motechproject.openmrs.ws.resource.model.Identifier;
import org.motechproject.openmrs.ws.resource.model.IdentifierType;
import org.motechproject.openmrs.ws.resource.model.Location;
import org.motechproject.openmrs.ws.resource.model.Patient;
import org.motechproject.openmrs.ws.resource.model.PatientListResult;
import org.motechproject.openmrs.ws.resource.model.Person;
import org.motechproject.openmrs.ws.util.ConverterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("patientAdapter")
public class MRSPatientAdapterImpl implements MRSPatientAdapter {

    private static Logger logger = LoggerFactory.getLogger(MRSPatientAdapterImpl.class);

    private final PatientResource patientResource;
    private final OpenMrsInstance openmrsInstance;
    private final MRSPersonAdapterImpl personAdapter;
    private final MRSFacilityAdapter facilityAdapter;

    @Autowired
    public MRSPatientAdapterImpl(PatientResource patientResource, OpenMrsInstance instance,
            MRSPersonAdapterImpl personAdapter, MRSFacilityAdapter facilityAdapter) {
        this.patientResource = patientResource;
        this.openmrsInstance = instance;
        this.personAdapter = personAdapter;
        this.facilityAdapter = facilityAdapter;
    }

    @Override
    public Integer getAgeOfPatientByMotechId(String arg0) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MRSPatient getPatientByMotechId(String motechId) {
        Validate.notEmpty(motechId, "Motech Id cannot be empty");
        
        PatientListResult patientList = patientResource.queryForPatient(motechId);
        if (patientList.getResults().size() == 0) {
            return null;
        } else if (patientList.getResults().size() > 1) {
            logger.warn("Search for patient by id returned more than 1 result");
        }

        return getPatient(patientList.getResults().get(0).getUuid());
    }

    @Override
    public MRSPatient getPatient(String patientId) {
        Validate.notEmpty(patientId, "Patient Id cannot be empty");

        Patient patient = patientResource.getPatientById(patientId);
        Identifier identifier = patient.getIdentifierByIdentifierType(patientResource.getMotechPatientIdentifierUuid());

        if (identifier == null) {
            logger.error("No MoTeCH Id found on Patient with id: " + patient.getUuid());
            return null;
        }

        // since OpenMRS 1.9, patient identifiers no longer require an explicit location
        // this means the identifier's location could be null
        // this is a guard against this situation
        if (identifier.getLocation() != null) {
            String facililtyUuid = identifier.getLocation().getUuid();
            return convertToMrsPatient(patient, identifier, facilityAdapter.getFacility(facililtyUuid));
        } else {
            return convertToMrsPatient(patient, identifier, null);            
        }
    }

    private MRSPatient convertToMrsPatient(Patient patient, Identifier identifier, MRSFacility facility) {
        MRSPatient converted = new MRSPatient(patient.getUuid(), identifier.getIdentifier(),
                ConverterUtils.convertToMrsPerson(patient.getPerson()), facility);
        return converted;
    }

    @Override
    public MRSPatient savePatient(MRSPatient patient) {
        validatePatientBeforeSave(patient);
        
        MRSPerson savedPerson = personAdapter.savePerson(patient.getPerson());

        Patient converted = fromMrsPatient(patient, savedPerson);
        Patient created = patientResource.createPatient(converted);

        return new MRSPatient(created.getUuid(), patient.getMotechId(), savedPerson, patient.getFacility());
    }

    private void validatePatientBeforeSave(MRSPatient patient) {
        Validate.notNull(patient, "Patient cannot be null");
        Validate.isTrue(StringUtils.isNotEmpty(patient.getMotechId()), "You must provide a motech id to save a patient");
        Validate.notNull(patient.getPerson(), "Person cannot be null when saving a patient");
        
        if (openmrsInstance.getApiVersion().equals(ApiVersion.API_1_8)) {
            // openmrs 1.8.X requires a location when creating a patient
            // this is not the case in 1.9.X
            Validate.notNull(patient.getFacility(), "The patient must have a facility to be saved");
            Validate.notEmpty(patient.getFacility().getId(), "The patient facility must have an id");
        }
    }

    private Patient fromMrsPatient(MRSPatient patient, MRSPerson savedPerson) {
        Patient converted = new Patient();
        Person person = new Person();
        person.setUuid(savedPerson.getId());
        converted.setPerson(person);

        Location location = null;
        if (patient.getFacility() != null && StringUtils.isNotBlank(patient.getFacility().getId())) {
            location = new Location();
            location.setUuid(patient.getFacility().getId());
        }
        
        String motechPatientIdentiferTypeUuid = patientResource.getMotechPatientIdentifierUuid();
        if (StringUtils.isBlank(motechPatientIdentiferTypeUuid)) {
            throw new MRSException(new RuntimeException("Cannot save a patient until a MoTeCH Patient Idenitifer Type is created in the OpenMRS"));
        }
        IdentifierType type = new IdentifierType();
        type.setUuid(motechPatientIdentiferTypeUuid);

        Identifier identifier = new Identifier();
        identifier.setIdentifier(patient.getMotechId());
        identifier.setLocation(location);
        identifier.setIdentifierType(type);

        List<Identifier> identifiers = new ArrayList<Identifier>();
        identifiers.add(identifier);
        converted.setIdentifiers(identifiers);

        return converted;
    }

    @Override
    public List<MRSPatient> search(String name, String id) {
        Validate.notEmpty(name, "Name cannot be empty");

        PatientListResult result = patientResource.queryForPatient(name);
        List<MRSPatient> searchResults = new ArrayList<>();

        for (Patient partialPatient : result.getResults()) {
            MRSPatient patient = getPatient(partialPatient.getUuid());
            if (id == null) {
                searchResults.add(patient);
            } else {
                if (patient.getMotechId() != null && patient.getMotechId().contains(id)) {
                    searchResults.add(patient);
                }
            }
        }

        if (searchResults.size() > 0) {
            sortResults(searchResults);
        }

        return searchResults;
    }

    private void sortResults(List<MRSPatient> searchResults) {
        Collections.sort(searchResults, new Comparator<MRSPatient>() {
            @Override
            public int compare(MRSPatient patient1, MRSPatient patient2) {
                if (StringUtils.isNotEmpty(patient1.getMotechId()) && StringUtils.isNotEmpty(patient2.getMotechId())) {
                    return patient1.getMotechId().compareTo(patient2.getMotechId());
                } else if (StringUtils.isNotEmpty(patient1.getMotechId())) {
                    return -1;
                } else if (StringUtils.isNotEmpty(patient2.getMotechId())) {
                    return 1;
                }
                return 0;
            }
        });
    }

    @Override
    public MRSPatient updatePatient(MRSPatient patient) {
        Validate.notNull(patient, "Patient cannot be null");
        Validate.notEmpty(patient.getId(), "Patient Id may not be empty");

        MRSPerson person = patient.getPerson();

        personAdapter.updatePerson(person);
        // the openmrs web service requires an explicit delete request to remove
        // attributes. delete all previous attributes, and then
        // create any attributes attached to the patient
        personAdapter.deleteAllAttributes(person);
        personAdapter.saveAttributesForPerson(person);

        return patient;
    }

    @Override
    public void deceasePatient(String motechId, String conceptName, Date dateOfDeath, String comment)
            throws PatientNotFoundException {
        Validate.notEmpty(motechId, "MoTeCh id cannot be empty");

        MRSPatient patient = getPatientByMotechId(motechId);
        if (patient == null) {
            throw new MRSException(new RuntimeException("Cannot decease patient - No patient found with MoTeCH id: "
                    + motechId));
        }

        personAdapter.savePersonCauseOfDeath(patient.getId(), dateOfDeath, conceptName);
    }
}
