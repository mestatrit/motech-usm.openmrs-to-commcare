package org.motechproject.openmrs.rest.it;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.motechproject.mrs.model.MRSPatient;
import org.motechproject.mrs.model.MRSPerson;
import org.motechproject.mrs.services.MRSPatientAdapter;
import org.springframework.beans.factory.annotation.Autowired;

public class MRSPatientAdapterIT extends AbstractIT {

    @Autowired
    MRSPatientAdapter patientAdapter;

    @Test
    public void shouldCreatePatient() {
        MRSPerson person = new MRSPerson().firstName("John").lastName("Smith").address("10 Fifth Avenue")
                .birthDateEstimated(false).gender("M").preferredName("Jonathan");
        MRSPatient patient = new MRSPatient("558", person, null);
        MRSPatient created = patientAdapter.savePatient(patient);
        context.addPatientToDelete(created);

        assertNotNull(created.getId());

    }
}
