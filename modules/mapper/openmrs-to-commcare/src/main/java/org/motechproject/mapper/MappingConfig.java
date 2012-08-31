package org.motechproject.mapper;

public class MappingConfig {
    private String conceptName;
    private String caseElementName;
    private Translations translationMappings;

    public String getConceptName() {
        return conceptName;
    }

    public void setConceptName(String conceptName) {
        this.conceptName = conceptName;
    }

    public String getCaseElementName() {
        return caseElementName;
    }

    public void setCaseElementName(String caseElementName) {
        this.caseElementName = caseElementName;
    }

    public Translations getTranslationMappings() {
        return translationMappings;
    }

    public void setTranslationMappings(Translations translations) {
        this.translationMappings = translations;
    }
}
