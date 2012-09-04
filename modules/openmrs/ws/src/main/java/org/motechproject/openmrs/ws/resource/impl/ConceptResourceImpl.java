package org.motechproject.openmrs.ws.resource.impl;

import org.apache.log4j.Logger;
import org.motechproject.mrs.exception.MRSException;
import org.motechproject.openmrs.ws.HttpException;
import org.motechproject.openmrs.ws.OpenMrsInstance;
import org.motechproject.openmrs.ws.RestClient;
import org.motechproject.openmrs.ws.resource.ConceptResource;
import org.motechproject.openmrs.ws.resource.model.Concept;
import org.motechproject.openmrs.ws.resource.model.Concept.ConceptClass;
import org.motechproject.openmrs.ws.resource.model.Concept.ConceptClassSerializer;
import org.motechproject.openmrs.ws.resource.model.Concept.DataType;
import org.motechproject.openmrs.ws.resource.model.Concept.DataTypeSerializer;
import org.motechproject.openmrs.ws.resource.model.ConceptListResult;
import org.motechproject.openmrs.ws.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Component
public class ConceptResourceImpl implements ConceptResource {
    private static final Logger LOGGER = Logger.getLogger(ConceptResourceImpl.class);

    private final OpenMrsInstance openmrsInstance;
    private final RestClient restClient;

    @Autowired
    public ConceptResourceImpl(RestClient restClient, OpenMrsInstance openmrsInstance) {
        this.restClient = restClient;
        this.openmrsInstance = openmrsInstance;
    }

    @Override
    public ConceptListResult queryForConceptsByName(String name) {
        String responseJson;
        try {
            responseJson = restClient.getJson(openmrsInstance
                    .toInstancePathWithParams("/concept?q={conceptName}", name));
        } catch (HttpException e) {
            LOGGER.error("There was an error retrieving the uuid of the concept with concept name: " + name);
            throw new MRSException(e);
        }
        ConceptListResult results = (ConceptListResult) JsonUtils.readJson(responseJson, ConceptListResult.class);
        return results;
    }

    @Override
    public Concept createConcept(Concept concept) throws HttpException {
        Gson gson = new GsonBuilder().registerTypeAdapter(ConceptClass.class, new ConceptClassSerializer())
                .registerTypeAdapter(DataType.class, new DataTypeSerializer()).create();
        String requestJson = gson.toJson(concept);
        String responseJson = restClient.postForJson(openmrsInstance.toInstancePath("/concept"), requestJson);
        Concept saved = (Concept) JsonUtils.readJson(responseJson, Concept.class);
        return saved;
    }

}
