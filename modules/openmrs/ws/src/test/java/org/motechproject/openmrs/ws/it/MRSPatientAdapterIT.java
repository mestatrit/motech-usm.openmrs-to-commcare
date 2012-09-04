package org.motechproject.openmrs.ws.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.motechproject.mrs.exception.PatientNotFoundException;
import org.motechproject.mrs.model.MRSPatient;
import org.motechproject.mrs.model.MRSPerson;
import org.motechproject.mrs.services.MRSPatientAdapter;
import org.motechproject.openmrs.ws.HttpException;
import org.motechproject.openmrs.ws.resource.ConceptResource;
import org.motechproject.openmrs.ws.resource.model.Concept;
import org.motechproject.openmrs.ws.resource.model.Concept.ConceptClass;
import org.motechproject.openmrs.ws.resource.model.Concept.ConceptName;
import org.motechproject.openmrs.ws.resource.model.Concept.DataType;
import org.springframework.beans.factory.annotation.Autowired;

public class MRSPatientAdapterIT extends AbstractIT {

    @Autowired
    MRSPatientAdapter patientAdapter;
    
    @Autowired
    ConceptResource conceptResource;

    @Test
    public void shouldCreatePatient() {
        MRSPatient created = createGenericPatient();

        assertNotNull(created.getId());
    }

    @Test
    public void shouldUpdatePatient() {
        MRSPatient saved = createGenericPatient();

        MRSPerson persisted = saved.getPerson();
        persisted.firstName("Changed Name");
        persisted.address("Changed Address");
        patientAdapter.updatePatient(saved);

        MRSPatient fetched = patientAdapter.getPatientByMotechId("558");

        assertEquals("Changed Name", fetched.getPerson().getFirstName());
        // Bug in OpenMRS Web Services does not currently allow updating address
        //assertEquals("Changed Address", fetched.getPerson().getAddress());
    }

    private MRSPatient createGenericPatient() {
        MRSPerson person = getGenericPerson();
        MRSPatient patient = new MRSPatient("558", person, null);
        MRSPatient saved = savePatientInContext(patient);
        return saved;
    }

    private MRSPatient savePatientInContext(MRSPatient patient) {
        MRSPatient saved = patientAdapter.savePatient(patient);
        context.addPatientToDelete(saved);
        return saved;
    }

    private MRSPerson getGenericPerson() {
        MRSPerson person = new MRSPerson().firstName("John").lastName("Smith").address("10 Fifth Avenue")
                .birthDateEstimated(false).gender("M").preferredName("Jonathan");
        return person;
    }
    
    @Test
    public void shouldGetPatientByMotechId() {
        createGenericPatient();
        
        MRSPatient patient = patientAdapter.getPatientByMotechId("558");
        assertNotNull(patient);
    }
    
    @Test
    public void shouldSearchForPatient() {
        createGenericPatient();
        
        MRSPerson person1 = getGenericPerson();
        person1.firstName("Bill");
        savePatientInContext(new MRSPatient("559", person1, null));
        
        List<MRSPatient> found = patientAdapter.search("John", null);
        
        assertEquals(1, found.size());
    }
    
    @Test
    public void shouldDeceasePerson() throws HttpException, PatientNotFoundException {
        createGenericPatient();
        
        Concept deathConcept = new Concept();
        
        ConceptName name = new ConceptName();
        name.setName("Death Concept");
        deathConcept.getNames().add(name);
        
        DataType dataType = new DataType();
        dataType.setDisplay("Text");
        deathConcept.setDatatype(dataType);
        
        ConceptClass conceptClass = new ConceptClass();
        conceptClass.setDisplay("Misc");
        deathConcept.setConceptClass(conceptClass);
        Concept saved = conceptResource.createConcept(deathConcept);
        context.addConceptToDelete(saved);
        
        patientAdapter.deceasePatient("558", "Death Concept", new Date(), null);
        
        MRSPatient patient = patientAdapter.getPatientByMotechId("558");
        assertTrue(patient.getPerson().isDead());
        
    }
}
