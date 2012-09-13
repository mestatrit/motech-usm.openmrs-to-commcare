package org.motechproject.openmrs.ws.resource.impl;

import java.lang.reflect.Type;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.motechproject.mrs.exception.MRSException;
import org.motechproject.openmrs.ws.HttpException;
import org.motechproject.openmrs.ws.OpenMrsInstance;
import org.motechproject.openmrs.ws.RestClient;
import org.motechproject.openmrs.ws.resource.ObservationResource;
import org.motechproject.openmrs.ws.resource.model.Observation;
import org.motechproject.openmrs.ws.resource.model.Observation.ObservationValue;
import org.motechproject.openmrs.ws.resource.model.Observation.ObservationValueDeserializer;
import org.motechproject.openmrs.ws.resource.model.ObservationListResult;
import org.motechproject.openmrs.ws.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ObservationResourceImpl implements ObservationResource {
    private static final Logger LOGGER = Logger.getLogger(ObservationResourceImpl.class);

    private final RestClient restClient;
    private final OpenMrsInstance openmrsInstance;

    @Autowired
    public ObservationResourceImpl(RestClient restClient, OpenMrsInstance openmrsInstance) {
        this.restClient = restClient;
        this.openmrsInstance = openmrsInstance;
    }

    @Override
    public ObservationListResult queryForObservationsByPatientId(String id) {
        String responseJson = null;
        try {
            responseJson = restClient.getJson(openmrsInstance
                    .toInstancePathWithParams("/obs?patient={uuid}&v=full", id));
        } catch (HttpException e) {
            LOGGER.error("Could not retrieve observations for patient with uuid: " + id);
            throw new MRSException(e);
        }

        Map<Type, Object> adapters = getObsAdapters();
        ObservationListResult result = (ObservationListResult) JsonUtils.readJsonWithAdapters(responseJson,
                ObservationListResult.class, adapters);
        return result;
    }

    private Map<Type, Object> getObsAdapters() {
        Map<Type, Object> adapters = new HashMap<Type, Object>();
        adapters.put(ObservationValue.class, new ObservationValueDeserializer());
        return adapters;
    }

    @Override
    public void deleteObservation(String id, String reason) throws HttpException {
        URI uri = null;
        if (StringUtils.isEmpty(reason)) {
            uri = openmrsInstance.toInstancePathWithParams("/obs/{uuid}", id);
        } else {
            uri = openmrsInstance.toInstancePathWithParams("/obs/{uuid}?reason={reason}", id, reason);
        }

        restClient.delete(uri);
    }

    @Override
    public Observation getObservationById(String id) throws HttpException {
        Map<Type, Object> adapters = getObsAdapters();
        String responseJson = restClient.getJson(openmrsInstance.toInstancePathWithParams("/obs/{uuid}?v=full", id));
        Observation obs = (Observation) JsonUtils.readJsonWithAdapters(responseJson, Observation.class, adapters);
        return obs;
    }

}
