package org.motechproject.openmrs.ws;

import java.net.URI;
import java.net.URISyntaxException;

import org.motechproject.MotechException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriTemplate;

/**
 * Represents a single OpenMRS Web Application instance
 */
@Component
public class OpenMrsInstance {

    private static final String OPENMRS_WEB_SERVICE_PATH = "/ws/rest/v1";

    private final String openmrsUrl;
    private final String motechPatientIdentifierTypeName;
    private final ApiVersion apiVersion;

    public static enum ApiVersion {
        API_1_8, API_1_9;
    }

    @Autowired
    public OpenMrsInstance(@Value("${openmrs.url}") String openmrsUrl,
            @Value("${openmrs.motechIdName}") String motechPatientIdentifierTypeName,
            @Value("${openmrs.apiVersion}") String apiVersionUsed) {
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

    public URI toInstancePath(String path) {
        try {
            return new URI(openmrsUrl + path);
        } catch (URISyntaxException e) {
            throw new MotechException("Bad URI");
        }
    }

    public URI toInstancePathWithParams(String path, Object... params) {
        return new UriTemplate(openmrsUrl + path).expand(params);
    }
}
