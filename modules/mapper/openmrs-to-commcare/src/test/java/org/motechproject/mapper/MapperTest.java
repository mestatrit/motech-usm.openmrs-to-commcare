package org.motechproject.mapper;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.junit.Test;
import org.motechproject.commcare.domain.CaseTask;
import org.motechproject.commcare.domain.UpdateTask;
import org.motechproject.mrs.model.MRSEncounter;
import org.motechproject.mrs.model.MRSEncounter.MRSEncounterBuilder;
import org.motechproject.mrs.model.MRSObservation;

public class MapperTest {

    @Test
    public void shouldMapEncounterObsToCaseElement() {
        Mapper mapper = new Mapper("visit");
        Mapping mapping = mapper.map("PATIENT REFERRED", "referral_given");
        mapping.addValueTranslation("true", "yes");

        MRSObservation obs = new MRSObservation(null, "PATIENT REFERRED", "true");
        HashSet<MRSObservation> set = new HashSet<>();
        set.add(obs);
        MRSEncounter encounter = new MRSEncounterBuilder().withEncounterType("visit").withObservations(set).build();

        UpdateTask update = new UpdateTask();
        Map<String, String> fieldValues = new HashMap<>();
        fieldValues.put("referral_given", "yes");
        update.setFieldValues(fieldValues);
        CaseTask expectedCaseTask = new CaseTask();
        expectedCaseTask.setUpdateTask(update);

        CaseTask caseTask = mapper.mapEncounterToCaseTask(encounter);
        
        assertEquals(expectedCaseTask, caseTask);
    }
    
    @Test
    public void shouldMapMultipleObsToSingleCaseElement() {
        Mapper mapper = new Mapper("visit");
        Mapping mapping = mapper.map("EDUCATION/COUNSELING ORDERS", "counsel_type");
        mapping.addValueTranslation("NUTRITION COUNSELING", "nutrition");
        mapping.addValueTranslation("BEDNET COUNSELING", "bednet");
        
        HashSet<MRSObservation> set = new HashSet<>();
        MRSObservation obs = new MRSObservation(null, "EDUCATION/COUNSELING ORDERS", "NUTRITION COUNSELING");
        set.add(obs);
        obs = new MRSObservation(null, "EDUCATION/COUNSELING ORDERS", "BEDNET COUNSELING");
        set.add(obs);
        MRSEncounter encounter = new MRSEncounterBuilder().withEncounterType("visit").withObservations(set).build();
        
        UpdateTask update = new UpdateTask();
        Map<String, String> fieldValues = new HashMap<>();
        fieldValues.put("counsel_type", "nutrition bednet");
        update.setFieldValues(fieldValues);
        CaseTask expectedCaseTask = new CaseTask();
        expectedCaseTask.setUpdateTask(update);
        
        CaseTask caseTask = mapper.mapEncounterToCaseTask(encounter);
        
        assertEquals(expectedCaseTask, caseTask);
    }
    
    @Test
    public void shouldInferCaseElement() {
        Mapper mapper = new Mapper("visit");
        Mapping mapping = mapper.map("EDUCATION/COUNSELING ORDERS", "counsel_type");
        mapping.addValueTranslation("Counseling for expectant mother", "birthplan");
        mapping.addCaseElementOnValue("birthplan", "birthplan");

        HashSet<MRSObservation> set = new HashSet<>();
        MRSObservation obs = new MRSObservation(null, "EDUCATION/COUNSELING ORDERS", "Counseling for expectant mother");
        set.add(obs);
        MRSEncounter encounter = new MRSEncounterBuilder().withEncounterType("visit").withObservations(set).build();
        
        UpdateTask update = new UpdateTask();
        Map<String, String> fieldValues = new HashMap<>();
        fieldValues.put("counsel_type", "birthplan");
        fieldValues.put("birthplan", "");
        update.setFieldValues(fieldValues);
        CaseTask expectedCaseTask = new CaseTask();
        expectedCaseTask.setUpdateTask(update);

        CaseTask caseTask = mapper.mapEncounterToCaseTask(encounter);
        
        assertEquals(expectedCaseTask, caseTask);
    }
}
