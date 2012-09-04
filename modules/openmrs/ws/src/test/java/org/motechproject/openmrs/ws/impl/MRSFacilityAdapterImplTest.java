package org.motechproject.openmrs.ws.impl;

import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.openmrs.ws.resource.LocationResource;

public class MRSFacilityAdapterImplTest {

    @Mock
    private LocationResource locationResource;

    private MRSFacilityAdapterImpl impl;

    @Before
    public void setUp() {
        initMocks(this);
        impl = new MRSFacilityAdapterImpl(locationResource);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnEmptyFacilityId() {
        impl.getFacility("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnNullFacilityId() {
        impl.getFacility(null);
    }
}
