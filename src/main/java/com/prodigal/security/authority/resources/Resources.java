package com.prodigal.security.authority.resources;

import com.prodigal.security.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2023-06-28 星期三
 */
@Data
@Entity
@Table(name = "prodigal_resources")
@Schema(title = "请求资源表")
public class Resources implements Serializable, BaseEntity<Long> {

    @Id
    private Long id;

    @Schema(title = "资源路径")
    private String path;

    @Schema(title = "资源描述")
    private String name;

    @Schema(title = "创建时间")
    private LocalDateTime createdTime;

    @Schema(title = "修改时间")
    private LocalDateTime updatedTime;

}
