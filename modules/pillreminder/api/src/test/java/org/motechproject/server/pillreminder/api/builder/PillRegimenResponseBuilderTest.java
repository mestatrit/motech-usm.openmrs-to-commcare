package org.motechproject.server.pillreminder.api.builder;

import org.junit.Test;
import org.motechproject.model.Time;
import org.motechproject.server.pillreminder.api.contract.PillRegimenResponse;
import org.motechproject.server.pillreminder.api.domain.DailyScheduleDetails;
import org.motechproject.server.pillreminder.api.domain.Dosage;
import org.motechproject.server.pillreminder.api.domain.Medicine;
import org.motechproject.server.pillreminder.api.domain.PillRegimen;

import java.util.HashSet;

import static org.junit.Assert.assertEquals;

public class PillRegimenResponseBuilderTest {

    private PillRegimenResponseBuilder builder = new PillRegimenResponseBuilder();

    @Test
    public void shouldCreateAPillRegimenResponse() {

        final Dosage dosage = new Dosage(new Time(10, 5), new HashSet<Medicine>());
        PillRegimen pillRegimen = new PillRegimen("123", new HashSet<Dosage>(){{this.add(dosage);}}, new DailyScheduleDetails(20, 5, 5));
        pillRegimen.setId("pillRegimenId");

        PillRegimenResponse pillRegimenResponse = builder.createFrom(pillRegimen);

        assertEquals("pillRegimenId", pillRegimenResponse.getPillRegimenId());
        assertEquals("123", pillRegimenResponse.getExternalId());
        assertEquals(5, pillRegimenResponse.getReminderRepeatWindowInHours());
        assertEquals(20, pillRegimenResponse.getReminderRepeatIntervalInMinutes());
        assertEquals(5, pillRegimenResponse.getBufferOverDosageTimeInMinutes());
        assertEquals(1, pillRegimenResponse.getDosages().size());
    }
}
