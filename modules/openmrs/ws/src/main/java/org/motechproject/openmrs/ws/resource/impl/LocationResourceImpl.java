package org.motechproject.openmrs.ws.resource.impl;

import org.apache.log4j.Logger;
import org.motechproject.mrs.exception.MRSException;
import org.motechproject.openmrs.ws.HttpException;
import org.motechproject.openmrs.ws.OpenMrsInstance;
import org.motechproject.openmrs.ws.RestClient;
import org.motechproject.openmrs.ws.resource.LocationResource;
import org.motechproject.openmrs.ws.resource.model.Location;
import org.motechproject.openmrs.ws.resource.model.LocationListResult;
import org.motechproject.openmrs.ws.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Component
public class LocationResourceImpl implements LocationResource {
    private static final Logger LOGGER = Logger.getLogger(LocationResourceImpl.class);

    private final RestClient restClient;
    private final OpenMrsInstance openmrsInstance;

    @Autowired
    public LocationResourceImpl(RestClient restClient, OpenMrsInstance openmrsInstance) {
        this.restClient = restClient;
        this.openmrsInstance = openmrsInstance;
    }

    @Override
    public LocationListResult getAllLocations() {
        try {
            String json = restClient.getJson(openmrsInstance.toInstancePath("/location?v=full"));
            LocationListResult result = (LocationListResult) JsonUtils.readJson(json, LocationListResult.class);
            return result;
        } catch (HttpException e) {
            LOGGER.error("Failed to retrieve all facilities");
            throw new MRSException(e);
        }

    }

    @Override
    public LocationListResult queryForLocationByName(String locationName) {
        try {
            String json = restClient.getJson(openmrsInstance.toInstancePathWithParams("/location?q={name}&v=full",
                    locationName));
            LocationListResult result = (LocationListResult) JsonUtils.readJson(json, LocationListResult.class);
            return result;
        } catch (HttpException e) {
            LOGGER.error("Failed to retrieve all facilities by location name: " + locationName);
            throw new MRSException(e);
        }
    }

    @Override
    public Location getLocationById(String uuid) {
        try {
            String json = restClient.getJson(openmrsInstance.toInstancePathWithParams("/location/{uuid}", uuid));
            Location location = (Location) JsonUtils.readJson(json, Location.class);
            return location;
        } catch (HttpException e) {
            LOGGER.error("Failed to fetch information about location with uuid: " + uuid);
            throw new MRSException(e);
        }
    }

    @Override
    public Location createLocation(Location location) {
        try {
            Gson gson = new GsonBuilder().create();
            String jsonRequest = gson.toJson(location);
            String jsonResponse = restClient.postForJson(openmrsInstance.toInstancePath("/location"), jsonRequest);
            return (Location) JsonUtils.readJson(jsonResponse, Location.class);
        } catch (HttpException e) {
            LOGGER.error("Could not create location with name: " + location.getName());
            throw new MRSException(e);
        }
    }
}
