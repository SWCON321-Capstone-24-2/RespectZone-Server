package khu.dacapstone.respect_zone.apiPayload.code;

import khu.dacapstone.respect_zone.web.dto.ErrorReasonDto;

public interface BaseErrorCode {
    ErrorReasonDto getReason();

    ErrorReasonDto getReasonHttpStatus();
}