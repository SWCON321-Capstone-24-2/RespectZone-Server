package khu.dacapstone.respect_zone.web.dto;

import org.springframework.http.HttpStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorReasonDto {
    private final String message;
    private final String code;
    private final boolean isSuccess;
    private HttpStatus httpStatus;

    public ErrorReasonDto(String message, String code, boolean isSuccess, Object result) {
        this.message = message;
        this.code = code;
        this.isSuccess = isSuccess;
    }

    public ErrorReasonDto(String message, String code, boolean isSuccess, HttpStatus httpStatus) {
        this.message = message;
        this.code = code;
        this.isSuccess = isSuccess;
        this.httpStatus = httpStatus;
    }

    public ErrorReasonDto(String message, String code, boolean isSuccess, HttpStatus httpStatus, Object result) {
        this.message = message;
        this.code = code;
        this.isSuccess = isSuccess;
        this.httpStatus = httpStatus;
    }
}