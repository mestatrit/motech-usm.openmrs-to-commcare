package org.motechproject.openmrs.ws.resource;

import org.motechproject.openmrs.ws.resource.model.Location;
import org.motechproject.openmrs.ws.resource.model.LocationListResult;

public interface LocationResource {

    LocationListResult getAllLocations();

    LocationListResult queryForLocationByName(String locationName);

    Location getLocationById(String uuid);

    Location createLocation(Location location);

}
