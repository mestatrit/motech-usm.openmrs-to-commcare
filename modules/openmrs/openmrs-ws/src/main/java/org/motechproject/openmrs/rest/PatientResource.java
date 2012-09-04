package org.motechproject.openmrs.rest;

import org.motechproject.openmrs.rest.model.Patient;
import org.motechproject.openmrs.rest.model.PatientIdentifierListResult;
import org.motechproject.openmrs.rest.model.PatientListResult;

public interface PatientResource {

    Patient savePatient(Patient patient);
    
    PatientListResult searchForPatient(String term);

    Patient getPatientById(String patientId);

    PatientIdentifierListResult getAllPatientIdentifierTypes();
}
