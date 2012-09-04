package org.motechproject.openmrs.rest;

import java.net.URI;
import java.net.URISyntaxException;

import org.motechproject.MotechException;
import org.springframework.web.util.UriTemplate;

public class OpenMrsInstance {

    private static final String OPENMRS_WEB_SERVICE_PATH = "/ws/rest/v1";
    private final String openmrsUrl;
    private final String motechPatientIdentifierTypeName;
    private final ApiVersion apiVersion;

    public static enum ApiVersion {
        API_1_8, API_1_9;
    }

    public OpenMrsInstance(String openmrsUrl, String motechPatientIdentifierTypeName, String apiVersionUsed) {
        this.openmrsUrl = openmrsUrl + OPENMRS_WEB_SERVICE_PATH;
        this.motechPatientIdentifierTypeName = motechPatientIdentifierTypeName;
        if ("1.8".equals(apiVersionUsed)) {
            apiVersion = ApiVersion.API_1_8;
        } else if ("1.9".equals(apiVersionUsed)) {
            apiVersion = ApiVersion.API_1_9;
        } else {
            throw new MotechException("OpenMRS API Version must be set to either: 1.8 or 1.9");
        }
    }

    public String getOpenmrsUrl() {
        return openmrsUrl;
    }

    public String getMotechPatientIdentifierTypeName() {
        return motechPatientIdentifierTypeName;
    }

    public ApiVersion getApiVersion() {
        return apiVersion;
    }

    public URI withInstanceUrl(String path) {
        try {
            return new URI(openmrsUrl + path);
        } catch (URISyntaxException e) {
            throw new MotechException("Bad URI");
        }
    }

    public URI resolveWebServicePathWithParams(String path, Object... params) {
        return new UriTemplate(openmrsUrl + path).expand(params);
    }
}
