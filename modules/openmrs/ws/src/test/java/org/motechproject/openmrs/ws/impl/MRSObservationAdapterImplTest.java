package org.motechproject.openmrs.ws.impl;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.text.ParseException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.mrs.exception.ObservationNotFoundException;
import org.motechproject.mrs.model.MRSObservation;
import org.motechproject.openmrs.ws.HttpException;
import org.motechproject.openmrs.ws.resource.ObservationResource;
import org.motechproject.openmrs.ws.util.DateUtil;
import org.springframework.http.HttpStatus;

public class MRSObservationAdapterImplTest {

    @Mock
    private MRSPatientAdapterImpl patientAdapter;

    @Mock
    private ObservationResource obsResource;

    private MRSObservationAdapterImpl impl;

    @Before
    public void setUp() {
        initMocks(this);
        impl = new MRSObservationAdapterImpl(obsResource, patientAdapter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnNullMotechId() {
        impl.findObservation(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnEmptyMotechId() {
        impl.findObservation("", null);
    }

    @Test
    public void shouldReturnNullOnNoPatientFound() {
        when(patientAdapter.getPatientByMotechId("555")).thenReturn(null);
        MRSObservation obs = impl.findObservation("555", "concept");

        assertNull(obs);
    }

    private MRSObservation makeExpectedObservation() throws ParseException {
        return new MRSObservation("OOO", DateUtil.parseOpenMrsDate("1962-01-01T00:00:00.000+0000"), "CCC", "VVV");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnVoidWithNullObservation() throws ObservationNotFoundException {
        impl.voidObservation(null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnVoidWithEmptyIdObservation() throws ObservationNotFoundException {
        impl.voidObservation(new MRSObservation("", null, null, null), null, null);
    }

    @Test(expected = ObservationNotFoundException.class)
    public void shouldThrowObservationNotFound() throws HttpException, ObservationNotFoundException {
        doThrow(new HttpException(null, HttpStatus.NOT_FOUND)).when(obsResource).deleteObservation("AAA", null);
        impl.voidObservation(new MRSObservation("AAA", null, null, null), null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnObservationsWithNullMotechId() throws ObservationNotFoundException {
        impl.findObservations(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnObservationsWIthEmptyMotechId() throws ObservationNotFoundException {
        impl.findObservations("", null);
    }
}
