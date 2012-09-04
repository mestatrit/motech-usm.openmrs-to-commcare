package org.motechproject.openmrs.ws.resource;

import org.motechproject.openmrs.ws.resource.model.Patient;
import org.motechproject.openmrs.ws.resource.model.PatientListResult;

public interface PatientResource {

    Patient createPatient(Patient patient);
    
    PatientListResult queryForPatient(String term);

    Patient getPatientById(String patientId);

    String getMotechPatientIdentifierUuid();
}
