package com.nuance.speechkitsample.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.json.JSONObject;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NLUresponse {

    protected String literal;

    public String getLiteral() {
        return literal;
    }

    public void setLiteral(String literal) {
        this.literal = literal;
    }

    protected Action action;
    protected Map<String, Object> concepts;

    public Map<String, Object> getConcepts() {
        return concepts;
    }

    public void setConcepts(Map<String, Object> concepts) {
        this.concepts = concepts;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Action {

        protected Intent intent;

        public Intent getIntent() {
            return intent;
        }

        public void setIntent(Intent intent) {
            this.intent = intent;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Intent {

            protected String value;
            protected double confidence;

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public double getConfidence() {
                return confidence;
            }

            public void setConfidence(double confidence) {
                this.confidence = confidence;
            }
        }
    }
}
