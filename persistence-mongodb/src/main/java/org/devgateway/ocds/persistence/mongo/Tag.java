package org.devgateway.ocds.persistence.mongo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

public enum Tag {
    PLANNING("planning"),

    TENDER("tender"),

    TENDER_AMENDMENT("tenderAmendment"),

    TENDER_UPDATE("tenderUpdate"),

    TENDER_CANCELLATION("tenderCancellation"),

    AWARD("award"),

    AWARD_UPDATE("awardUpdate"),

    AWARD_CANCELLATION("awardCancellation"),

    CONTRACT("contract"),

    CONTRACT_UPDATE("contractUpdate"),

    CONTRACT_AMENDMENT("contractAmendment"),

    IMPLEMENTATION("implementation"),

    IMPLEMENTATION_UPDATE("implementationUpdate"),

    CONTRACT_TERMINATION("contractTermination"),

    COMPILED("compiled");

    private final String value;

    private final static Map<String, Tag> CONSTANTS = new HashMap<String, Tag>();

    static {
        for (Tag c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    Tag(String value) {
        this.value = value;
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }

    @JsonCreator
    public static Tag fromValue(String value) {
        Tag constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}
