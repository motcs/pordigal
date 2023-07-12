package com.prodigal.annotation.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.prodigal.annotation.RestServerException;

/**
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2023-05-31 星期三
 */
public class RestJsonProcessingException extends RestServerException {
    public RestJsonProcessingException(JsonProcessingException jsonProcessingException) {
        this(1101, jsonProcessingException);
    }

    public RestJsonProcessingException(int status, Object msg) {
        super(status, msg);
    }

    public static RestJsonProcessingException withError(JsonProcessingException jsonProcessingException) {
        return new RestJsonProcessingException(jsonProcessingException);
    }
}