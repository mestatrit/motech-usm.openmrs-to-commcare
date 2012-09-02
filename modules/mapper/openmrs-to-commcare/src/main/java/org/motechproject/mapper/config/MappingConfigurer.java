package org.motechproject.mapper.config;

import java.io.InputStream;
import java.io.InputStreamReader;

import org.motechproject.mapper.Mapper;
import org.motechproject.mapper.Mapping;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MappingConfigurer {

    public Mapper configureMapper(InputStream inputStream) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Translations.class, new Translations.TranslationsDeserializer())
                .create();
        MappingConfiguration configuration = gson.fromJson(new InputStreamReader(inputStream),
                MappingConfiguration.class);
        return buildMapperFromConfiguration(configuration);
    }

    private Mapper buildMapperFromConfiguration(MappingConfiguration configuration) {
        Mapper mapper = new Mapper(configuration.getMatchOnEncounterType());
        
        for (MappingConfig config : configuration.getMappings()) {
            Mapping mapping = mapper.map(config.getConceptName(), config.getCaseElementName());
            for (Translation translation : config.getTranslationMappings().getTranslations()) {
                mapping.addValueTranslation(translation.getFrom(), translation.getTo());
                if (translation.getInferredElements() != null) {
                    for (String inferredElement : translation.getInferredElements()) {
                        mapping.addCaseElementOnValue(translation.getTo(), inferredElement);
                    }
                }
            }
        }
        
        return mapper;
    }

}
