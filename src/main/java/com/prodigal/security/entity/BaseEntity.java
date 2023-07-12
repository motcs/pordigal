package com.prodigal.security.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.domain.Persistable;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2023/6/26 22:58-星期一
 */
public interface BaseEntity<T> extends Serializable, Persistable<T> {


    default void setCreatedTime(LocalDateTime localDateTime) {

    }

    default void setUpdatedTime(LocalDateTime localDateTime) {

    }

    /**
     * 默认新判断方法
     *
     * @return 是否
     */
    @JsonIgnore
    @Override
    default boolean isNew() {
        boolean isNew = ObjectUtils.isEmpty(getId());
        if (isNew) {
            this.setCreatedTime(LocalDateTime.now());
        }
        this.setUpdatedTime(LocalDateTime.now());
        return isNew;
    }

}
