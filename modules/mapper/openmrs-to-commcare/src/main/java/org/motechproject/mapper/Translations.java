package org.motechproject.mapper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class Translations {
    List<Translation> translations = new ArrayList<Translation>();

    public static class TranslationsDeserializer implements JsonDeserializer<Translations> {

        @Override
        public Translations deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            Translations translationList = new Translations();
            
            JsonObject obj = json.getAsJsonObject();
            Set<Entry<String, JsonElement>> entries = obj.entrySet();
            for (Entry<String, JsonElement> entry : entries) {
                Translation translation = new Translation();
                translation.setFrom(entry.getKey());

                if (entry.getValue().isJsonPrimitive()) {
                    translation.setTo(entry.getValue().getAsString());
                } else {
                    JsonObject jsonobj = entry.getValue().getAsJsonObject();
                    translation.setTo(jsonobj.get("value").getAsString());

                    translation.setInferredElements(new ArrayList<String>());
                    JsonArray inferredElements = jsonobj.get("inferredElements").getAsJsonArray();
                    for (int i = 0; i < inferredElements.size(); i++) {
                        translation.getInferredElements().add(inferredElements.get(i).getAsString());
                    }
                }
                translationList.translations.add(translation);
            }
            
            return translationList;
        }
    }    
    
    public List<Translation> getTranslations() {
        return translations;
    }

    public void setTranslations(List<Translation> translations) {
        this.translations = translations;
    }
}
