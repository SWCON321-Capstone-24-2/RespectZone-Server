package khu.dacapstone.respect_zone.domain.enums;

import lombok.Getter;

public enum SentenceType {
    GOOD_SENTENCE("좋은 문장"),
    GENDER_HATE("성별 혐오"),
    AGE_HATE("연령 혐오"),
    OTHER_HATE("기타 혐오"),
    SWEAR_EXPRESSION("욕설 표현");

    @Getter
    private final String value;

    SentenceType(String value) {
        this.value = value;
    }
}
