package com.prodigal.annotation;

import com.prodigal.annotation.exception.ClientRequestException;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * 全局错误信息拦截器,根据不同错误自定义错误返回定制信息
 *
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2023-05-31 星期三
 */
@Log4j2
@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBindException(BindException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
                .body(ErrorResponse.withErrors("请求参数验证错误!", ex.getMessage()).code(1400));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
                .body(ErrorResponse.withErrors("数据空指针错误!", ex.getMessage()).code(1404));
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponse> handleLockingFailureException(DataAccessException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
                .body(ErrorResponse.withErrors("数据库操作失败!", ex.getMessage()).code(1600));
    }

    @ExceptionHandler(ClientRequestException.class)
    public ResponseEntity<ErrorResponse> handleClientException(ClientRequestException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
                .body(ErrorResponse.withErrors("内部服务访问错误!", ex.getMessage()).code(ex.getCode()));
    }

    @ExceptionHandler(RestServerException.class)
    public ResponseEntity<ErrorResponse> handleRestServerException(RestServerException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
                .body(ErrorResponse.withErrors("自定义服务错误!", List.of(ex.getMessage())).code(ex.getCode()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnknownException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
                .body(ErrorResponse.withErrors("未知错误!", List.of(ex.getMessage())).code(500));
    }

}