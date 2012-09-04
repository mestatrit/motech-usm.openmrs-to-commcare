package org.motechproject.openmrs.ws.resource;

import org.motechproject.openmrs.ws.resource.model.Encounter;
import org.motechproject.openmrs.ws.resource.model.EncounterListResult;

public interface EncounterResource {

    Encounter createEncounter(Encounter encounter);
    
    EncounterListResult queryForAllEncountersByPatientId(String id);
    
}
