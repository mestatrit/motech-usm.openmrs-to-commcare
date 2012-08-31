package org.motechproject.mapper;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;

public class Mapping {

    private String conceptName;
    private String caseElementName;
    private Map<String, String> translations = new HashMap<>();
    private Map<String, String> inferredElements = new HashMap<>();

    public Mapping(String conceptName, String caseElementName) {
        this.conceptName = conceptName;
        this.caseElementName = caseElementName;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Mapping)) {
            return false;
        }
        Mapping other = (Mapping) obj;
        return ObjectUtils.equals(conceptName, other.conceptName)
                && ObjectUtils.equals(caseElementName, other.caseElementName)
                && ObjectUtils.equals(translations, other.translations)
                && ObjectUtils.equals(inferredElements, other.inferredElements);
    }
    
    @Override
    public int hashCode() {
        int hash = 1;
        hash = hash * 31 + ObjectUtils.hashCode(conceptName);
        hash = hash * 31 + ObjectUtils.hashCode(caseElementName);
        hash = hash * 31 + ObjectUtils.hashCode(translations);
        hash = hash * 31 + ObjectUtils.hashCode(inferredElements);
        
        return hash;
    }

    public void addValueTranslation(String incomingValue, String outgoingValue) {
        translations.put(incomingValue, outgoingValue);
    }

    public String translate(String incomingValue) {
        String translation = translations.get(incomingValue);
        return translation;
    }

    public String getCaseElementName() {
        return caseElementName;
    }

    public void addCaseElementOnValue(String incomingValue, String elementToCreate) {
        inferredElements.put(incomingValue, elementToCreate);
    }

    public boolean hasInferredElement(String conceptName) {
        return inferredElements.containsKey(conceptName);
    }

    public String getInferredElement(String translated) {
        return inferredElements.get(translated);
    }

    public String getInferredElementValue(String translated) {
        return "";
    }

}
