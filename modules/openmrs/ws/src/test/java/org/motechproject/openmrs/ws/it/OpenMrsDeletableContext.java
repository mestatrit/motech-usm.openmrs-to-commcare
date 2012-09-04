package org.motechproject.openmrs.ws.it;

import java.util.ArrayList;
import java.util.List;

import org.motechproject.mrs.model.MRSFacility;
import org.motechproject.mrs.model.MRSPatient;
import org.motechproject.openmrs.ws.resource.model.Concept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

@Component
public class OpenMrsDeletableContext {

    private final RestOperations restOperations;
    private final String openmrsUrl;

    private final List<Deletable> deletables = new ArrayList<>();

    @Autowired
    public OpenMrsDeletableContext(RestOperations restOperations, @Value("${openmrs.url}") String openmrsUrl) {
        this.restOperations = restOperations;
        this.openmrsUrl = openmrsUrl;
    }

    interface Deletable {
        void delete();
    }

    public void addFacilityToDelete(final MRSFacility created) {
        if (created != null && created.getId() != null) {
            Deletable deletable = new Deletable() {
                @Override
                public void delete() {
                    restOperations.delete(openmrsUrl + "/ws/rest/v1/location/{uuid}?purge", created.getId());
                }
            };
            deletables.add(deletable);
        }
    }

    public void deleteAllDataInContext() {
        for (Deletable deletable : deletables) {
            deletable.delete();
        }
    }

    public void addPatientToDelete(final MRSPatient created) {
        if (created == null || created.getId() != null) {
            Deletable deletable = new Deletable() {
                @Override
                public void delete() {
                    restOperations.delete(openmrsUrl + "/ws/rest/v1/patient/{uuid}?purge", created.getId());
                }
            };
            deletables.add(deletable);
        }
    }

    public void addConceptToDelete(final Concept created) {
        if (created == null || created.getUuid() != null) {
            Deletable deletable = new Deletable() {
                @Override
                public void delete() {
                    restOperations.delete(openmrsUrl + "/ws/rest/v1/concept/{uuid}?purge", created.getUuid());
                }
            };
            deletables.add(deletable);
        }
    }

}
