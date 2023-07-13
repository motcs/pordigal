package com.prodigal.commons;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.task.TaskExecutor;
import org.springframework.expression.ExpressionException;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2023-07-13 星期四
 */
@Log4j2
public abstract class BaseAutoToolsUtil {
    protected JdbcTemplate jdbcTemplate;
    protected ObjectMapper objectMapper;
    protected TaskExecutor executor;

    protected ConversionService conversionService;

    public String objectToJson(Object o) {
        try {
            return this.objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new ExpressionException("序列化对象JSON错误", e);
        }
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Autowired
    public void setExecutor(@Qualifier("taskScheduler") TaskExecutor execute) {
        this.executor = execute;
    }

    @Autowired(required = false)
    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Autowired(required = false)
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

}