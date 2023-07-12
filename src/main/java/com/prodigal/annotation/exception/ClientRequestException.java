package com.prodigal.annotation.exception;

import com.prodigal.annotation.RestServerException;

/**
 * 内部Client统一错误出里结果
 *
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2023-05-31 星期三
 */
public class ClientRequestException extends RestServerException {

    public ClientRequestException(int code, Object msg) {
        super(code, msg);
    }

    public static ClientRequestException withMsg(int code, Object msg) {
        return new ClientRequestException(code, msg);
    }

    public static ClientRequestException withMsg(Object msg) {
        return withMsg(1503, msg);
    }

}