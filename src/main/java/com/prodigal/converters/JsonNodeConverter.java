package com.prodigal.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.prodigal.annotation.exception.RestJsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2023-05-26 星期五
 */
@Log4j2
@Component
@ControllerAdvice
@RequiredArgsConstructor
@Converter(autoApply = true)
public class JsonNodeConverter implements AttributeConverter<ObjectNode, String> {

    private final ObjectMapper objectMapper;

    @Override
    public String convertToDatabaseColumn(ObjectNode attribute) {
        if (ObjectUtils.isEmpty(attribute)
                || attribute.isNull() || attribute.isEmpty()) {
            return null;
        }
        try {
            return this.objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            log.error("Json 转换错误,信息: {}", e.getMessage());
            throw RestJsonProcessingException.withError(e);
        }
    }

    @Override
    public ObjectNode convertToEntityAttribute(String dbData) {
        if (!StringUtils.hasLength(dbData)) {
            return null;
        }
        try {
            return this.objectMapper.readValue(dbData, ObjectNode.class);
        } catch (JsonProcessingException e) {
            log.error("Json 转换错误,信息: {}", e.getMessage());
            throw RestJsonProcessingException.withError(e);
        }
    }

}