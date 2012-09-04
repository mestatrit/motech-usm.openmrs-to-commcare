package org.motechproject.openmrs.ws.resource.impl;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.motechproject.mrs.exception.MRSException;
import org.motechproject.openmrs.ws.HttpException;
import org.motechproject.openmrs.ws.OpenMrsInstance;
import org.motechproject.openmrs.ws.RestClient;
import org.motechproject.openmrs.ws.resource.EncounterResource;
import org.motechproject.openmrs.ws.resource.model.Concept;
import org.motechproject.openmrs.ws.resource.model.Concept.ConceptSerializer;
import org.motechproject.openmrs.ws.resource.model.Encounter;
import org.motechproject.openmrs.ws.resource.model.Encounter.EncounterType;
import org.motechproject.openmrs.ws.resource.model.Encounter.EncounterTypeSerializer;
import org.motechproject.openmrs.ws.resource.model.EncounterListResult;
import org.motechproject.openmrs.ws.resource.model.Location;
import org.motechproject.openmrs.ws.resource.model.Location.LocationSerializer;
import org.motechproject.openmrs.ws.resource.model.Observation.ObservationValue;
import org.motechproject.openmrs.ws.resource.model.Observation.ObservationValueDeserializer;
import org.motechproject.openmrs.ws.resource.model.Observation.ObservationValueSerializer;
import org.motechproject.openmrs.ws.resource.model.Patient;
import org.motechproject.openmrs.ws.resource.model.Patient.PatientSerializer;
import org.motechproject.openmrs.ws.resource.model.Person;
import org.motechproject.openmrs.ws.resource.model.Person.PersonSerializer;
import org.motechproject.openmrs.ws.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Component
public class EncounterResourceImpl implements EncounterResource {
    private static final Logger LOGGER = Logger.getLogger(EncounterResourceImpl.class);

    private final RestClient restClient;
    private final OpenMrsInstance openmrsInstance;

    @Autowired
    public EncounterResourceImpl(RestClient restClient, OpenMrsInstance openmrsInstance) {
        this.restClient = restClient;
        this.openmrsInstance = openmrsInstance;
    }

    @Override
    public Encounter createEncounter(Encounter encounter) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Location.class, new LocationSerializer())
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
                .registerTypeAdapter(Patient.class, new PatientSerializer())
                .registerTypeAdapter(Person.class, new PersonSerializer())
                .registerTypeAdapter(Concept.class, new ConceptSerializer())
                .registerTypeAdapter(EncounterType.class, new EncounterTypeSerializer())
                .registerTypeAdapter(ObservationValue.class, new ObservationValueSerializer()).create();

        String requestJson = gson.toJson(encounter);

        try {
            String responseJson = restClient.postForJson(openmrsInstance.toInstancePath("/encounter"), requestJson);
            Encounter response = (Encounter) JsonUtils.readJson(responseJson, Encounter.class);
            return response;
        } catch (HttpException e) {
            LOGGER.error("Could not create encounter: " + e.getMessage());
            throw new MRSException(e);
        }
    }

    @Override
    public EncounterListResult queryForAllEncountersByPatientId(String id) {
        String responseJson = null;
        try {
            responseJson = restClient.getJson(openmrsInstance.toInstancePathWithParams("/encounter?patient={id}", id));
        } catch (HttpException e) {
            LOGGER.error("Error retrieving encounters for patient: " + id);
            throw new MRSException(e);
        }

        Map<Type, Object> adapters = new HashMap<Type, Object>();
        adapters.put(ObservationValue.class, new ObservationValueDeserializer());
        EncounterListResult result = (EncounterListResult) JsonUtils.readJsonWithAdapters(responseJson,
                EncounterListResult.class, adapters);

        return result;
    }

}
