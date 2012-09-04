package org.motechproject.openmrs.rest.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.motechproject.mrs.model.MRSFacility;
import org.motechproject.mrs.services.MRSFacilityAdapter;
import org.springframework.beans.factory.annotation.Autowired;

public class MRSFacilityAdapterIT extends AbstractIT {

    @Autowired
    MRSFacilityAdapter facilityAdapter;

    @Test
    public void shouldSaveFacility() {
        MRSFacility created = createSingleFacilityInContext();

        assertNotNull(created.getId());
    }

    private MRSFacility createSingleFacilityInContext() {
        MRSFacility facilty = new MRSFacility("Test Facility", "US", "New York", "First", "Manhattan");
        MRSFacility created = facilityAdapter.saveFacility(facilty);
        context.addFacilityToDelete(created);
        return created;
    }

    @Test
    public void shouldFindMultipleFacilities() {
        createTwoFacilitiesWithinContext();
        List<MRSFacility> facilities = facilityAdapter.getFacilities();

        // Asserting 3 because of OpenMRS built-in Unknown Location
        assertEquals(3, facilities.size());
    }

    private void createTwoFacilitiesWithinContext() {
        MRSFacility created = facilityAdapter.saveFacility(new MRSFacility("Test Facility", "US", "New York", "First",
                "Manhattan"));
        MRSFacility created2 = facilityAdapter.saveFacility(new MRSFacility("Temporary", "US", "New York", "First",
                "Long Island"));
        context.addFacilityToDelete(created);
        context.addFacilityToDelete(created2);
    }

    @Test
    public void shouldFindSingleFacilityByName() {
        createTwoFacilitiesWithinContext();
        List<MRSFacility> facilities = facilityAdapter.getFacilities("Test Facility");

        assertEquals(1, facilities.size());
    }

    @Test
    public void shouldFindFacilityById() {
        MRSFacility facility = createSingleFacilityInContext();
        MRSFacility persisted = facilityAdapter.getFacility(facility.getId());

        assertNotNull(persisted);
    }
}
