package com.prodigal.converters;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.prodigal.annotation.exception.RestJsonProcessingException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2023-05-26 星期五
 */
@Log4j2
@Component
@ControllerAdvice
@ReadingConverter
@RequiredArgsConstructor
public class JsonNodeReadConverter implements Converter<String, ObjectNode> {

    private final ObjectMapper objectMapper;

    @Override
    public ObjectNode convert(@NonNull String source) {
        if (!StringUtils.hasLength(source)) {
            return null;
        }
        try {
            return this.objectMapper.readValue(source, ObjectNode.class);
        } catch (JsonProcessingException e) {
            log.error("Json 转换错误,信息: {}", e.getMessage());
            throw RestJsonProcessingException.withMsg("序列化数据Set为Json类型错误,信息: " + e.getMessage());
        }
    }

}