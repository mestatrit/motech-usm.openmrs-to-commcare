package org.motechproject.openmrs.ws.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.mrs.model.MRSEncounter;
import org.motechproject.mrs.model.MRSFacility;
import org.motechproject.mrs.model.MRSObservation;
import org.motechproject.mrs.model.MRSPerson;
import org.motechproject.mrs.model.MRSUser;
import org.motechproject.mrs.services.MRSPatientAdapter;
import org.motechproject.openmrs.ws.HttpException;
import org.motechproject.openmrs.ws.RestClient;
import org.motechproject.openmrs.ws.resource.EncounterResource;
import org.motechproject.openmrs.ws.util.DateUtil;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

public class MRSEncounterAdapterImplTest {

    private static final int DAY = 1;
    private static final int MONTH = 6;
    private static final int YEAR = 2012;

    @Mock
    private MRSPatientAdapter patientAdapter;

    @Mock
    private RestClient restfulClient;

    @Mock
    private MRSPersonAdapterImpl personAdapter;

    @Mock
    private MRSConceptAdapterImpl conceptAdapter;

    @Mock
    private EncounterResource encounterResource;

    private MRSEncounterAdapterImpl impl;

    @Before
    public void setUp() {
        initMocks(this);
        impl = new MRSEncounterAdapterImpl(encounterResource, patientAdapter, personAdapter, conceptAdapter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnNullEncounter() {
        impl.createEncounter(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnNullPatient() {
        MRSEncounter encounter = new MRSEncounter.MRSEncounterBuilder().withId(null).withProvider(new MRSPerson())
                .withCreator(new MRSUser()).withFacility(new MRSFacility("")).withDate(new Date()).withPatient(null)
                .withObservations(null).withEncounterType("ADULTINITIAL").build();
        impl.createEncounter(encounter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnNullPatientId() {
        MRSEncounter encounter = new MRSEncounter.MRSEncounterBuilder().withProviderId("A").withCreatorId("A")
                .withFacilityId("A").withDate(new Date()).withPatientId(null).withObservations(null)
                .withEncounterType("ADULTINITIAL").build();
        impl.createEncounter(encounter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnEmptyPatientId() {
        MRSEncounter encounter = new MRSEncounter.MRSEncounterBuilder().withProviderId("A").withCreatorId("A")
                .withFacilityId("A").withDate(new Date()).withPatientId(null).withObservations(null)
                .withEncounterType("ADULTINITIAL").build();
        impl.createEncounter(encounter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnNullDateTime() {
        MRSEncounter encounter = new MRSEncounter.MRSEncounterBuilder().withProviderId("A").withCreatorId("A")
                .withFacilityId("A").withDate(null).withPatientId("A").withObservations(null)
                .withEncounterType("ADULTINITIAL").build();
        impl.createEncounter(encounter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnNullEncounterType() {
        MRSEncounter encounter = new MRSEncounter.MRSEncounterBuilder().withProviderId("A").withCreatorId("A")
                .withFacilityId("A").withDate(new Date()).withPatientId("A").withObservations(null)
                .withEncounterType(null).build();
        impl.createEncounter(encounter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnEmptyEncounterType() {
        MRSEncounter encounter = new MRSEncounter.MRSEncounterBuilder().withProviderId("A").withCreatorId("A")
                .withFacilityId("A").withDate(new Date()).withPatientId("A").withObservations(null)
                .withEncounterType("").build();
        impl.createEncounter(encounter);
    }
}
