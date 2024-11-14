package khu.dacapstone.respect_zone.domain.enums;

import lombok.Getter;

public enum SentenceType {
    GOOD_SENTENCE("clean"),
    GENDER_HATE("성별"),
    AGE_HATE("연령"),
    OTHER_HATE("기타혐오"),
    SWEAR_EXPRESSION("악플/욕설");

    @Getter
    private final String value;

    SentenceType(String value) {
        this.value = value;
    }
}
