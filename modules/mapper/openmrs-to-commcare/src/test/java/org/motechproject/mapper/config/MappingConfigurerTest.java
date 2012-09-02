package org.motechproject.mapper.config;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;
import org.motechproject.mapper.Mapper;
import org.motechproject.mapper.Mapping;
import org.motechproject.mapper.config.MappingConfigurer;
import org.springframework.core.io.ClassPathResource;

public class MappingConfigurerTest {

    @Test
    public void shouldCreateMapping() throws IOException {
        MappingConfigurer configurer = new MappingConfigurer();
        Mapper mapper = configurer.configureMapper(new ClassPathResource("mapping-config.json").getInputStream());
        
        Mapper expected = new Mapper("visit");
        
        Mapping mapping = expected.map("PATIENT REFERRED", "referral_given");
        mapping.addValueTranslation("true", "yes");
        
        mapping = expected.map("EDUCATION/COUNSELING ORDERS", "counsel_type");
        mapping.addValueTranslation("NUTRITION COUNSELING", "nutrition");
        mapping.addValueTranslation("BEDNET COUNSELING", "bednet");
        mapping.addValueTranslation("Counseling for expectant mother", "birthplan");
        mapping.addCaseElementOnValue("birthplan", "birthplan");
        
        assertEquals(expected, mapper);
    }
}
