package khu.dacapstone.respect_zone.apiPayload.code;

import org.springframework.http.HttpStatus;

import khu.dacapstone.respect_zone.web.dto.ErrorReasonDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    // 가장 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    // 필수 헤더가 누락된 경우
    _MISSING_REQUEST_HEADER(HttpStatus.BAD_REQUEST, "COMMON400", "필수 헤더가 누락되었습니다."),

    // Speech 관련 에러
    SPEECH_NOT_FOUND(HttpStatus.NOT_FOUND, "SPEECH4001", "해당 id를 가진 Speech가 없습니다."),
    SPEECH_NOT_MATCH_DEVICE_ID(HttpStatus.NOT_FOUND, "SPEECH4002", "해당 id를 가진 Speech가 해당 deviceId와 일치하지 않습니다."),
    // 멤버 관련 에러
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER4001", "사용자가 없습니다."),
    NICKNAME_NOT_EXIST(HttpStatus.BAD_REQUEST, "MEMBER4002", "닉네임은 필수 입니다."),

    // 예시로 추가한 다른 응답
    ARTICLE_NOT_FOUND(HttpStatus.NOT_FOUND, "ARTICLE4001", "게시글이 없습니다."),

    // Food Category 못 찾음
    FOOD_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "FOOD_CATEGORY4001", "해당 음식 카테고리가 없습니다"),
    // Temp 관련 응답
    TEMP_EXCEPTION(HttpStatus.BAD_REQUEST, "TEMP4001", "이거는 테스트 예외입니다."),

    // WebSocket 관련 에러 추가
    WEBSOCKET_HANDSHAKE_FAILED(HttpStatus.BAD_REQUEST, "WS4001", "WebSocket 연결 실패"),
    WEBSOCKET_MISSING_HEADERS(HttpStatus.BAD_REQUEST, "WS4002", "필수 WebSocket 헤더가 누락됨");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDto getReason() {
        return ErrorReasonDto.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDto getReasonHttpStatus() {
        return ErrorReasonDto.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}