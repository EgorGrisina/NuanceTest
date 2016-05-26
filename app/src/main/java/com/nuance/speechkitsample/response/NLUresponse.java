package com.nuance.speechkitsample.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NLUresponse {

    public Action action;

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Action {

        public Intent intent;

        public Intent getIntent() {
            return intent;
        }

        public void setIntent(Intent intent) {
            this.intent = intent;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Intent {

            public String value;
            public double confidence;

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
