package khu.dacapstone.respect_zone.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseDto<T> {
    private final boolean isSuccess;
    private final String code;
    private final String message;
    private T data;
}