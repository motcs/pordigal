package com.prodigal.annotation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2023-05-31 星期三
 */
@Schema(title = "自定义服务错误返回信息")
@Data
@NoArgsConstructor
public class ErrorResponse implements Serializable {

    private UUID requestId;

    private LocalDateTime time;

    private String message;

    private int code;

    private Object errors;

    @Builder
    public ErrorResponse(String message, Object errors) {
        this.message = message;
        this.errors = errors;
        this.requestId = UUID.randomUUID();
        this.time = LocalDateTime.now();
    }

    public static ErrorResponse withDefault(Object errors) {
        return new ErrorResponse("服务器运行时错误!", errors);
    }

    public static ErrorResponse withErrors(String message, Object errors) {
        ErrorResponse response = withDefault(errors);
        response.setMessage(message);
        return response;
    }

    public ErrorResponse code(int status) {
        this.code = status;
        return this;
    }
}