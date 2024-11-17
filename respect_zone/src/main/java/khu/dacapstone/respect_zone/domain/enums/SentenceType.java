package khu.dacapstone.respect_zone.domain.enums;

import lombok.Getter;

public enum SentenceType {
    GOOD_SENTENCE("clean"),
    GENDER_HATE("gender"),
    AGE_HATE("age"),
    OTHER_HATE("other"),
    SWEAR_EXPRESSION("swear");

    @Getter
    private final String value;

    SentenceType(String value) {
        this.value = value;
    }
}
