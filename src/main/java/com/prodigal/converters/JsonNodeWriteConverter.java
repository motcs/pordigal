package com.prodigal.converters;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.prodigal.annotation.exception.RestJsonProcessingException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2023-05-26 星期五
 */
@Log4j2
@Component
@ControllerAdvice
@WritingConverter
@RequiredArgsConstructor
public class JsonNodeWriteConverter implements Converter<ObjectNode, String> {

    private final ObjectMapper objectMapper;

    @Override
    public String convert(@NonNull ObjectNode source) {
        if (ObjectUtils.isEmpty(source) || source.isNull() || source.isEmpty()) {
            return null;
        }
        try {
            return this.objectMapper.writeValueAsString(source);
        } catch (JsonProcessingException e) {
            log.error("Json 转换错误,信息: {}", e.getMessage());
            throw RestJsonProcessingException.withError(e);
        }
    }
}