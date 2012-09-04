package org.motechproject.openmrs.ws.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
import org.motechproject.mrs.model.MRSFacility;
import org.motechproject.mrs.services.MRSFacilityAdapter;
import org.motechproject.openmrs.ws.resource.LocationResource;
import org.motechproject.openmrs.ws.resource.model.Location;
import org.motechproject.openmrs.ws.resource.model.LocationListResult;
import org.motechproject.openmrs.ws.util.ConverterUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("facilityAdapter")
public class MRSFacilityAdapterImpl implements MRSFacilityAdapter {
    private static final Logger LOGGER = Logger.getLogger(MRSFacilityAdapterImpl.class);

    private final LocationResource locationResource;

    @Autowired
    public MRSFacilityAdapterImpl(LocationResource locationResource) {
        this.locationResource = locationResource;
    }

    @Override
    public List<MRSFacility> getFacilities() {
        LocationListResult result = locationResource.getAllLocations();
        return mapLocationToMrsFacility(result.getResults());
    }

    private List<MRSFacility> mapLocationToMrsFacility(List<Location> facilities) {
        List<MRSFacility> mrsFacilities = new ArrayList<MRSFacility>();
        for (Location location : facilities) {
            mrsFacilities.add(ConverterUtils.convertLocationToMrsLocation(location));
        }
        return mrsFacilities;
    }

    @Override
    public List<MRSFacility> getFacilities(String locationName) {
        Validate.notEmpty(locationName, "Location name cannot be empty");
        LocationListResult result = locationResource.queryForLocationByName(locationName);
        return mapLocationToMrsFacility(result.getResults());
    }

    @Override
    public MRSFacility getFacility(String facilityId) {
        Validate.notEmpty(facilityId, "Facility id cannot be empty");
        Location location = locationResource.getLocationById(facilityId);
        return ConverterUtils.convertLocationToMrsLocation(location);
    }

    @Override
    public MRSFacility saveFacility(MRSFacility facility) {
        Validate.notNull(facility, "Facility cannot be null");

        // The uuid cannot be included with the request, otherwise OpenMRS will
        // fail
        facility.setId(null);
        Location location = convertMrsFacilityToLocation(facility);
        Location saved = locationResource.createLocation(location);
        return ConverterUtils.convertLocationToMrsLocation(saved);
    }

    private Location convertMrsFacilityToLocation(MRSFacility facility) {
        Location location = new Location();
        location.setAddress6(facility.getRegion());
        location.setCountry(facility.getCountry());
        location.setCountyDistrict(facility.getCountyDistrict());
        location.setName(facility.getName());
        location.setStateProvince(facility.getStateProvince());
        location.setUuid(facility.getId());
        return location;
    }
}
