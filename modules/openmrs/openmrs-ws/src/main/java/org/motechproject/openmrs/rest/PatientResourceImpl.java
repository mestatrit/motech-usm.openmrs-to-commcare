package org.motechproject.openmrs.rest;

import java.util.List;

import org.apache.log4j.Logger;
import org.motechproject.mrs.exception.MRSException;
import org.motechproject.openmrs.rest.model.IdentifierType;
import org.motechproject.openmrs.rest.model.IdentifierType.IdentifierTypeSerializer;
import org.motechproject.openmrs.rest.model.Location;
import org.motechproject.openmrs.rest.model.Location.LocationSerializer;
import org.motechproject.openmrs.rest.model.Patient;
import org.motechproject.openmrs.rest.model.PatientIdentifierListResult;
import org.motechproject.openmrs.rest.model.PatientListResult;
import org.motechproject.openmrs.rest.model.Person;
import org.motechproject.openmrs.rest.model.Person.PersonSerializer;
import org.motechproject.openmrs.rest.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class PatientResourceImpl implements PatientResource {

    private static Logger logger = Logger.getLogger(PatientResourceImpl.class);

    private RestClient restfulClient;
    private OpenMrsInstance openmrsInstance;

    @Autowired
    public PatientResourceImpl(RestClient restClient, OpenMrsInstance instance) {
        this.restfulClient = restClient;
        this.openmrsInstance = instance;
    }

    @Override
    public Patient savePatient(Patient patient) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Person.class, new PersonSerializer())
                .registerTypeAdapter(IdentifierType.class, new IdentifierTypeSerializer())
                .registerTypeAdapter(Location.class, new LocationSerializer()).create();

        String requestJson = gson.toJson(patient);
        String responseJson = null;
        try {
            responseJson = restfulClient.postForJson(openmrsInstance.withInstanceUrl("/patient"), requestJson);
        } catch (HttpException e) {
            logger.error("Failed to create a patient in OpenMRS with MoTeCH Id: "
                    + patient.getIdentifiers().get(0).getIdentifier());
            throw new MRSException(e);
        }

        return (Patient) JsonUtils.readJson(responseJson, Patient.class);
    }

    @Override
    public PatientListResult searchForPatient(String motechId) {
        String responseJson = null;
        try {
            responseJson = restfulClient.getJson(openmrsInstance.resolveWebServicePathWithParams(
                    "/patient?q={motechId}", motechId));
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
            responseJson = restfulClient.getJson(openmrsInstance.resolveWebServicePathWithParams(
                    "/patient/{uuid}?v=full", patientId));
        } catch (HttpException e) {
            logger.error("Failed to get patient by id: " + patientId);
            throw new MRSException(e);
        }
        return (Patient) JsonUtils.readJson(responseJson, Patient.class);
    }

    @Override
    public PatientIdentifierListResult getAllPatientIdentifierTypes() {
        String responseJson = null;
        try {
            responseJson = restfulClient.getJson(openmrsInstance.withInstanceUrl("/patientidentifiertype?v=full"));
        } catch (HttpException e) {
            logger.error("There was an exception retrieving the MoTeCH Identifier Type UUID");
            throw new MRSException(e);
        }
        return (PatientIdentifierListResult) JsonUtils.readJson(responseJson, PatientIdentifierListResult.class);
    }

}
