package com.prodigal.commons;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.util.List;

/**
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2023-02-10 星期五
 */
@Data
@Schema(title = "结果封装返回")
public class Result implements Serializable {

    private static Result instance;

    @Schema(title = "操作状态")
    private Boolean successful;

    @Schema(title = "返回所携带的参数")
    private Object resultValue;

    @Schema(title = "提示语")
    private Object resultHint;

    public static Result getInstance() {
        if (instance == null) {
            instance = new Result();
        }
        return instance;
    }

    public static Result of(Boolean successful, Object resultValue, Object resultHint) {
        Result result = getInstance();
        result.setSuccessful(successful);
        result.setResultValue(resultValue);
        result.setResultHint(resultHint);
        return result;
    }

    public static Result of(Boolean successful, Object resultValue) {
        Result result = getInstance();
        result.setSuccessful(successful);
        result.setResultValue(resultValue);
        result.setResultHint(successful ? "操作成功" : "操作失败");
        return result;
    }

    public static Result of(Boolean successful) {
        Result result = getInstance();
        result.setSuccessful(successful);
        result.setResultValue(List.of());
        result.setResultHint(successful ? "操作成功" : "操作失败");
        return result;
    }

    public static <T> List<T> pageValue(Page<T> page) {
        if (ObjectUtils.isEmpty(page)) {
            return null;
        }
        return page.getContent();
    }

    public static Object totalValue(Page<?> page) {
        if (ObjectUtils.isEmpty(page)) {
            return 0;
        }
        return page.getTotalElements();
    }

}
