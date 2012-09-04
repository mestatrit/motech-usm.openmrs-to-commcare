package org.motechproject.openmrs.rest.it;

import java.util.ArrayList;
import java.util.List;

import org.motechproject.mrs.model.MRSFacility;
import org.motechproject.mrs.model.MRSPatient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

@Component
public class OpenMrsPersistedContext {

    private final RestOperations restOperations;
    private final String openmrsUrl;

    private final List<Deletable> deletables = new ArrayList<>();

    @Autowired
    public OpenMrsPersistedContext(RestOperations restOperations, @Value("${openmrs.url}") String openmrsUrl) {
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

    public void deleteData() {
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

}
