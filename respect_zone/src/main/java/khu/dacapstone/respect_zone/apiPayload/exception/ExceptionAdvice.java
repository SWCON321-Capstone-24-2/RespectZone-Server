package khu.dacapstone.respect_zone.apiPayload.exception;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.validation.ConstraintViolationException;
import khu.dacapstone.respect_zone.apiPayload.ApiResponse;
import khu.dacapstone.respect_zone.apiPayload.code.ErrorStatus;
import khu.dacapstone.respect_zone.apiPayload.exception.handler.socket.WebSocketHandshakeException;
import khu.dacapstone.respect_zone.web.dto.ErrorReasonDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice(annotations = { RestController.class })
public class ExceptionAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> validation(ConstraintViolationException e, WebRequest request) {
        String errorMessage = e.getConstraintViolations().stream()
                .map(constraintViolation -> constraintViolation.getMessage())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("ConstraintViolationException 추출 도중 에러 발생"));

        return handleExceptionInternalConstraint(e, ErrorStatus._BAD_REQUEST, HttpHeaders.EMPTY, request);
    }

    // @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e, HttpHeaders headers, HttpStatus status, WebRequest request) {

        Map<String, String> errors = new LinkedHashMap<>();

        e.getBindingResult().getFieldErrors().forEach(fieldError -> {
            String fieldName = fieldError.getField();
            String errorMessage = Optional.ofNullable(fieldError.getDefaultMessage()).orElse("");
            errors.merge(fieldName, errorMessage,
                    (existingErrorMessage, newErrorMessage) -> existingErrorMessage + ", " + newErrorMessage);
        });

        return handleExceptionInternalArgs(e, headers, ErrorStatus._BAD_REQUEST, request, errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> exception(Exception e, WebRequest request) {
        e.printStackTrace();
        return handleExceptionInternalFalse(e, ErrorStatus._INTERNAL_SERVER_ERROR, HttpHeaders.EMPTY,
                ErrorStatus._INTERNAL_SERVER_ERROR.getHttpStatus(), request, e.getMessage());
    }

    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<Object> onThrowException(GeneralException generalException, WebRequest request) {
        ErrorReasonDto errorReasonHttpStatus = generalException.getErrorReasonHttpStatus();
        return handleExceptionInternal(generalException, errorReasonHttpStatus, null, request);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<Object> handleMissingRequestHeader(MissingRequestHeaderException e, WebRequest request) {
        String errorMessage = "필수 헤더가 누락되었습니다: " + e.getHeaderName();
        return handleExceptionInternalFalse(e, ErrorStatus._MISSING_REQUEST_HEADER, HttpHeaders.EMPTY,
                HttpStatus.BAD_REQUEST, request, errorMessage);
    }

    @ExceptionHandler(WebSocketHandshakeException.class)
    public ResponseEntity<Object> handleWebSocketHandshakeException(WebSocketHandshakeException e, WebRequest request) {
        log.error("WebSocket Handshake 실패: {}", e.getMessage());

        return handleExceptionInternalFalse(e, ErrorStatus._BAD_REQUEST, HttpHeaders.EMPTY,
                HttpStatus.BAD_REQUEST, request, e.getMessage());
    }

    private ResponseEntity<Object> handleExceptionInternal(Exception e, ErrorReasonDto reason,
            HttpHeaders headers, WebRequest request) {
        ApiResponse<Object> body = ApiResponse.onFailure(reason.getCode(), reason.getMessage(), null);
        return new ResponseEntity<>(body, headers, reason.getHttpStatus());
    }

    private ResponseEntity<Object> handleExceptionInternalFalse(Exception e, ErrorStatus errorCommonStatus,
            HttpHeaders headers, HttpStatus status, WebRequest request, String errorPoint) {
        ApiResponse<Object> body = ApiResponse.onFailure(errorCommonStatus.getCode(), errorCommonStatus.getMessage(),
                errorPoint);
        return new ResponseEntity<>(body, headers, status);
    }

    private ResponseEntity<Object> handleExceptionInternalArgs(Exception e, HttpHeaders headers,
            ErrorStatus errorCommonStatus,
            WebRequest request, Map<String, String> errorArgs) {
        ApiResponse<Object> body = ApiResponse.onFailure(errorCommonStatus.getCode(), errorCommonStatus.getMessage(),
                errorArgs);
        return new ResponseEntity<>(body, headers, errorCommonStatus.getHttpStatus());
    }

    private ResponseEntity<Object> handleExceptionInternalConstraint(Exception e, ErrorStatus errorCommonStatus,
            HttpHeaders headers, WebRequest request) {
        ApiResponse<Object> body = ApiResponse.onFailure(errorCommonStatus.getCode(), errorCommonStatus.getMessage(),
                null);
        return new ResponseEntity<>(body, headers, errorCommonStatus.getHttpStatus());
    }
}
