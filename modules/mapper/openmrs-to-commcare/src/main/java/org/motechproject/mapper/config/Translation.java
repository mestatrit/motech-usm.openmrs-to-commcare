package org.motechproject.mapper.config;

import java.util.List;

public class Translation {
    private String from;
    private String to;
    private List<String> inferredElements;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public List<String> getInferredElements() {
        return inferredElements;
    }

    public void setInferredElements(List<String> inferredElements) {
        this.inferredElements = inferredElements;
    }
}
