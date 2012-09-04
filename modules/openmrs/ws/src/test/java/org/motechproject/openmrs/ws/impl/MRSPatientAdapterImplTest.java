package org.motechproject.openmrs.ws.impl;

import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.net.URISyntaxException;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.dao.MotechJsonReader;
import org.motechproject.mrs.exception.MRSException;
import org.motechproject.mrs.model.MRSFacility;
import org.motechproject.mrs.model.MRSPatient;
import org.motechproject.mrs.model.MRSPerson;
import org.motechproject.mrs.services.MRSFacilityAdapter;
import org.motechproject.openmrs.ws.HttpException;
import org.motechproject.openmrs.ws.OpenMrsInstance;
import org.motechproject.openmrs.ws.resource.PatientResource;
import org.motechproject.openmrs.ws.resource.model.Patient;
import org.motechproject.openmrs.ws.resource.model.PatientListResult;

public class MRSPatientAdapterImplTest {

    @Mock
    private PatientResource patientResource;

    @Mock
    private MRSFacilityAdapter facilityAdapter;

    @Mock
    private MRSPersonAdapterImpl personAdapter;

    private MRSPatientAdapterImpl impl;

    private MotechJsonReader reader = new MotechJsonReader();

    @Before
    public void setUp() {
        initMocks(this);
        OpenMrsInstance instance = new OpenMrsInstance("http://localhost:8080/openmrs", "MoTeCH Id", "1.8");
        impl = new MRSPatientAdapterImpl(patientResource, instance, personAdapter, facilityAdapter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnNullPatient() {
        impl.savePatient(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnNullPatientMotechId() {
        impl.savePatient(new MRSPatient(null, null, null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnEmptyPatientMotechId() {
        impl.savePatient(new MRSPatient("", null, null));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowNullPerson() {
        impl.savePatient(new MRSPatient("558", null, new MRSFacility("")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnNullLocationWith18Api() {
        impl.savePatient(new MRSPatient("558", new MRSPerson(), null));
    }

    @Test
    public void shouldNotThrowExceptionOnNullLocationWith19Api() {
        OpenMrsInstance instance = new OpenMrsInstance("http://localhost:8080/openmrs", "MoTeCH Id", "1.9");
        MRSPatientAdapterImpl implFor19 = new MRSPatientAdapterImpl(patientResource, instance, personAdapter, facilityAdapter);

        MRSPerson person = new MRSPerson();
        person.id("AAA");

        when(personAdapter.savePerson(person)).thenReturn(person);
        when(patientResource.getMotechPatientIdentifierUuid()).thenReturn("III");
        when(patientResource.createPatient(any(Patient.class))).thenReturn(new Patient());
        
        implFor19.savePatient(new MRSPatient("558", person, null));
    }

    @Test(expected = MRSException.class)
    public void shouldThrowExceptionWhenMotechIdentifierIsNotFound() throws HttpException, URISyntaxException {
        MRSPerson person = new MRSPerson();
        person.id("AAA");
        
        MRSFacility facility = new MRSFacility("FFF");

        when(personAdapter.savePerson(person)).thenReturn(person);
        when(patientResource.getMotechPatientIdentifierUuid()).thenReturn(null);

        impl.savePatient(new MRSPatient("558", person, facility));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnNullMotechId() {
        impl.getPatientByMotechId(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnEmptyMotechId() {
        impl.getPatientByMotechId("");
    }

    @Test
    public void shouldReturnNullOnEmptyResults() throws HttpException {
        PatientListResult result = new PatientListResult();
        result.setResults(Collections.<Patient>emptyList());
        
        when(patientResource.queryForPatient("111")).thenReturn(result);

        MRSPatient patient = impl.getPatientByMotechId("111");

        assertNull(patient);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnNullPatientId() {
        impl.getPatient(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnEmptyPatientId() {
        impl.getPatient("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionNullPatientUpdate() {
        impl.updatePatient(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnNullPatientIdUpdate() {
        impl.updatePatient(new MRSPatient(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnEmptyPatientIdUpdate() {
        impl.updatePatient(new MRSPatient(""));
    }
    
    // @Test
    // public void shouldReturnOneResultFromSearch() throws IOException,
    // HttpException {
    // String patientSearchResult =
    // TestUtils.parseJsonFileAsString("json/patient-list-multiple-response.json");
    // String patientJson =
    // TestUtils.parseJsonFileAsString("json/patient-response.json");
    // String motechIdentifierJsonResponse = TestUtils
    // .parseJsonFileAsString("json/patient-identifier-list-response.json");
    // String patient2Json =
    // TestUtils.parseJsonFileAsString("json/patient-response2.json");
    //
    // when(client.getJson(any(URI.class))).thenReturn(patientSearchResult).thenReturn(patientJson)
    // .thenReturn(motechIdentifierJsonResponse).thenReturn(patient2Json);
    //
    // List<MRSPatient> patients = impl.search("Doe", "588");
    //
    // assertEquals(1, patients.size());
    // }
}
