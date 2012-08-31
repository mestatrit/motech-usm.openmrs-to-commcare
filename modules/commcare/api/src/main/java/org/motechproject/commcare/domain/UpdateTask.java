package org.motechproject.commcare.domain;

import java.util.Map;

import org.apache.commons.lang.ObjectUtils;

/**
 * A domain class to include in a case task in order to generate an upload case block in case xml.
 */
public class UpdateTask {
    private String caseType;
    private String caseName;
    private String dateOpened;
    private String ownerId;
    private Map<String, String> fieldValues;

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof UpdateTask)) {
            return false;
        }
        UpdateTask other = (UpdateTask) obj;
        return ObjectUtils.equals(caseType, other.caseType) && ObjectUtils.equals(caseName, other.caseName)
                && ObjectUtils.equals(dateOpened, other.dateOpened) && ObjectUtils.equals(ownerId, other.ownerId)
                && ObjectUtils.equals(fieldValues, other.fieldValues);
    }
    
    @Override
    public int hashCode() {
        int hash = 1;
        hash = hash * 31 + ObjectUtils.hashCode(caseType);
        hash = hash * 31 + ObjectUtils.hashCode(caseName);
        hash = hash * 31 + ObjectUtils.hashCode(dateOpened);
        hash = hash * 31 + ObjectUtils.hashCode(ownerId);
        hash = hash * 31 + ObjectUtils.hashCode(fieldValues);
        
        return hash;
    }

    public String getCaseType() {
        return this.caseType;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    public String getCaseName() {
        return this.caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public String getDateOpened() {
        return this.dateOpened;
    }

    public void setDateOpened(String dateOpened) {
        this.dateOpened = dateOpened;
    }

    public String getOwnerId() {
        return this.ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public Map<String, String> getFieldValues() {
        return this.fieldValues;
    }

    public void setFieldValues(Map<String, String> fieldValues) {
        this.fieldValues = fieldValues;
    }
}
