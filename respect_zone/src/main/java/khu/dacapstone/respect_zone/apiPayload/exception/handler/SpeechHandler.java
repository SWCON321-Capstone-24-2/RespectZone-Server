package khu.dacapstone.respect_zone.apiPayload.exception.handler;

import khu.dacapstone.respect_zone.apiPayload.code.BaseErrorCode;
import khu.dacapstone.respect_zone.apiPayload.exception.GeneralException;

public class SpeechHandler extends GeneralException {
    public SpeechHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
