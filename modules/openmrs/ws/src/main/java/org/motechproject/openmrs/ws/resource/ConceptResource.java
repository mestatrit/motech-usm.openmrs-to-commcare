package org.motechproject.openmrs.ws.resource;

import org.motechproject.openmrs.ws.HttpException;
import org.motechproject.openmrs.ws.resource.model.Concept;
import org.motechproject.openmrs.ws.resource.model.ConceptListResult;

public interface ConceptResource {

    Concept createConcept(Concept concept) throws HttpException;
    
    ConceptListResult queryForConceptsByName(String name);

}
