package com.prodigal.annotation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 系统自定义错误信息
 *
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2023-05-31 星期三
 */
@Schema(title = "系统自定义错误类型")
@EqualsAndHashCode(callSuper = true)
@Data
public class RestServerException extends RuntimeException implements Serializable {

    protected Object msg;
    protected int code;

    public RestServerException(int code, Object msg) {
        super(msg.toString());
        this.msg = msg;
        this.code = code;
    }

    public static RestServerException withMsg(int code, Object msg) {
        return new RestServerException(code, msg);
    }

    public static RestServerException withMsg(Object msg) {
        return withMsg(1500, msg);
    }

}