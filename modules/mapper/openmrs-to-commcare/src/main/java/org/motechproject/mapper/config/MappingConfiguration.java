package org.motechproject.mapper.config;

import java.util.List;

public class MappingConfiguration {
    private String matchOnEncounterType;
    private List<MappingConfig> mappings;

    public String getMatchOnEncounterType() {
        return matchOnEncounterType;
    }

    public void setMatchOnEncounterType(String matchOnEncounterType) {
        this.matchOnEncounterType = matchOnEncounterType;
    }

    public List<MappingConfig> getMappings() {
        return mappings;
    }

    public void setMappings(List<MappingConfig> mappings) {
        this.mappings = mappings;
    }
}
