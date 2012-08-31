package org.motechproject.mapper;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.motechproject.commcare.domain.CaseTask;
import org.motechproject.commcare.domain.UpdateTask;
import org.motechproject.mrs.model.MRSEncounter;
import org.motechproject.mrs.model.MRSObservation;

public class Mapper {

    private String encounterType;
    private Map<String, Mapping> mappings = new HashMap<>();

    public Mapper(String encounterType) {
        this.encounterType = encounterType;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Mapper)) {
            return false;
        }
        Mapper other = (Mapper) obj;
        return ObjectUtils.equals(encounterType, other.encounterType) && ObjectUtils.equals(mappings, other.mappings);
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = hash * 31 + ObjectUtils.hashCode(encounterType);
        hash = hash * 31 + ObjectUtils.hashCode(mappings);
        return hash;
    }

    public Mapping map(String conceptName, String caseElementName) {
        Mapping mapping = new Mapping(conceptName, caseElementName);
        mappings.put(conceptName, mapping);
        return mapping;
    }

    public CaseTask mapEncounterToCaseTask(MRSEncounter encounter) {
        if (encounterType.equals(encounter.getEncounterType())) {
            UpdateTask updateTask = new UpdateTask();
            updateTask.setFieldValues(new HashMap<String, String>());

            for (MRSObservation obs : encounter.getObservations()) {
                Mapping mapping = mappings.get(obs.getConceptName());
                String translated = mapping.translate(obs.getValue().toString());
                addOrAppendFieldValue(updateTask.getFieldValues(), mapping.getCaseElementName(), translated);
                if (mapping.hasInferredElement(translated)) {
                    updateTask.getFieldValues().put(mapping.getInferredElement(translated),
                            mapping.getInferredElementValue(translated));
                }
            }

            CaseTask task = new CaseTask();
            task.setUpdateTask(updateTask);
            return task;
        }
        return null;
    }

    private void addOrAppendFieldValue(Map<String, String> fieldValues, String caseElementName, String translated) {
        if (fieldValues.get(caseElementName) == null) {
            fieldValues.put(caseElementName, translated);
        } else {
            String prevValue = fieldValues.get(caseElementName);
            fieldValues.put(caseElementName, prevValue.concat(" " + translated));
        }
    }
}
