package com.samsung.context.sdk.samsunganalytics.internal.util;

import java.util.Map;
import java.util.Map.Entry;

public class Delimiter<K, V> {

    public enum Depth {
        ONE_DEPTH("\u0002", "\u0003"),
        TWO_DEPTH("\u0004", "\u0005"),
        THREE_DEPTH("\u0006", "\u0007");
        
        private String collDlm;
        private String keyvalueDlm;

        private Depth(String coll, String keyvalue) {
            this.collDlm = coll;
            this.keyvalueDlm = keyvalue;
        }

        public String getCollectionDLM() {
            return this.collDlm;
        }

        public String getKeyValueDLM() {
            return this.keyvalueDlm;
        }
    }

    public String makeDelimiterString(Map<K, V> values, Depth depth) {
        String body = null;
        for (Entry log : values.entrySet()) {
            if (body == null) {
                body = log.getKey().toString();
            } else {
                body = (body + depth.getCollectionDLM()) + log.getKey();
            }
            body = (body + depth.getKeyValueDLM()) + log.getValue();
        }
        return body;
    }
}
