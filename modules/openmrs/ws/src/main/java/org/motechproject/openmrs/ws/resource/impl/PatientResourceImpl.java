package org.motechproject.openmrs.ws.resource.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.motechproject.mrs.exception.MRSException;
import org.motechproject.openmrs.ws.HttpException;
import org.motechproject.openmrs.ws.OpenMrsInstance;
import org.motechproject.openmrs.ws.RestClient;
import org.motechproject.openmrs.ws.resource.PatientResource;
import org.motechproject.openmrs.ws.resource.model.IdentifierType;
import org.motechproject.openmrs.ws.resource.model.Location;
import org.motechproject.openmrs.ws.resource.model.Patient;
import org.motechproject.openmrs.ws.resource.model.PatientIdentifierListResult;
import org.motechproject.openmrs.ws.resource.model.PatientListResult;
import org.motechproject.openmrs.ws.resource.model.Person;
import org.motechproject.openmrs.ws.resource.model.IdentifierType.IdentifierTypeSerializer;
import org.motechproject.openmrs.ws.resource.model.Location.LocationSerializer;
import org.motechproject.openmrs.ws.resource.model.Person.PersonSerializer;
import org.motechproject.openmrs.ws.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Component
public class PatientResourceImpl implements PatientResource {

    private static Logger logger = Logger.getLogger(PatientResourceImpl.class);

    private RestClient restfulClient;
    private OpenMrsInstance openmrsInstance;

    private String motechIdTypeUuid;

    @Autowired
    public PatientResourceImpl(RestClient restClient, OpenMrsInstance instance) {
        this.restfulClient = restClient;
        this.openmrsInstance = instance;
    }

    @Override
    public Patient createPatient(Patient patient) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Person.class, new PersonSerializer())
                .registerTypeAdapter(IdentifierType.class, new IdentifierTypeSerializer())
                .registerTypeAdapter(Location.class, new LocationSerializer()).create();

        String requestJson = gson.toJson(patient);
        String responseJson = null;
        try {
            responseJson = restfulClient.postForJson(openmrsInstance.toInstancePath("/patient"), requestJson);
        } catch (HttpException e) {
            logger.error("Failed to create a patient in OpenMRS with MoTeCH Id: "
                    + patient.getIdentifiers().get(0).getIdentifier());
            throw new MRSException(e);
        }

        return (Patient) JsonUtils.readJson(responseJson, Patient.class);
    }

    @Override
    public PatientListResult queryForPatient(String motechId) {
        String responseJson = null;
        try {
            responseJson = restfulClient.getJson(openmrsInstance.toInstancePathWithParams("/patient?q={motechId}",
                    motechId));
        } catch (HttpException e) {
            logger.error("Failed search for patient by MoTeCH Id: " + motechId);
            throw new MRSException(e);
        }

        return (PatientListResult) JsonUtils.readJson(responseJson, PatientListResult.class);
    }

    @Override
    public Patient getPatientById(String patientId) {
        String responseJson = null;
        try {
            responseJson = restfulClient.getJson(openmrsInstance.toInstancePathWithParams("/patient/{uuid}?v=full",
                    patientId));
        } catch (HttpException e) {
            logger.error("Failed to get patient by id: " + patientId);
            throw new MRSException(e);
        }
        return (Patient) JsonUtils.readJson(responseJson, Patient.class);
    }

    @Override
    public String getMotechPatientIdentifierUuid() {
        if (StringUtils.isNotEmpty(motechIdTypeUuid)) {
            return motechIdTypeUuid;
        }

        PatientIdentifierListResult result = getAllPatientIdentifierTypes();
        String motechPatientIdentifierTypeName = openmrsInstance.getMotechPatientIdentifierTypeName();
        for (IdentifierType type : result.getResults()) {
            if (motechPatientIdentifierTypeName.equals(type.getName())) {
                motechIdTypeUuid = type.getUuid();
                break;
            }
        }

        if (StringUtils.isEmpty(motechIdTypeUuid)) {
            logger.error("Could not find OpenMRS patient identifier with name MoTeCH Id");
            throw new MRSException(
                    new RuntimeException("Could not find OpenMRS patient identifier with name MoTeCH Id"));
        }
        return motechIdTypeUuid;
    }

    private PatientIdentifierListResult getAllPatientIdentifierTypes() {
        String responseJson = null;
        try {
            responseJson = restfulClient.getJson(openmrsInstance.toInstancePath("/patientidentifiertype?v=full"));
        } catch (HttpException e) {
            logger.error("There was an exception retrieving the MoTeCH Identifier Type UUID");
            throw new MRSException(e);
        }
        return (PatientIdentifierListResult) JsonUtils.readJson(responseJson, PatientIdentifierListResult.class);
    }
}
