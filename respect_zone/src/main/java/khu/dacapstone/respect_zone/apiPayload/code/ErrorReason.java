package khu.dacapstone.respect_zone.apiPayload.code;

import org.springframework.http.HttpStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorReason {
    private final String message;
    private final String code;
    private final boolean isSuccess;
    private HttpStatus httpStatus;

    public ErrorReason(String message, String code, boolean isSuccess) {
        this.message = message;
        this.code = code;
        this.isSuccess = isSuccess;
    }

    public ErrorReason(String message, String code, boolean isSuccess, HttpStatus httpStatus) {
        this.message = message;
        this.code = code;
        this.isSuccess = isSuccess;
        this.httpStatus = httpStatus;
    }
}