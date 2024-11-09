package khu.dacapstone.respect_zone.apiPayload.exception;

import khu.dacapstone.respect_zone.apiPayload.code.BaseErrorCode;
import khu.dacapstone.respect_zone.web.dto.ErrorReasonDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {

    private BaseErrorCode code;

    public ErrorReasonDto getErrorReason() {
        return this.code.getReason();
    }

    public ErrorReasonDto getErrorReasonHttpStatus() {
        return this.code.getReasonHttpStatus();
    }
}