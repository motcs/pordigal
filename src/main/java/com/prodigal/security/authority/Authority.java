package com.prodigal.security.authority;

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
@Table(name = "prodigal_authority")
@Schema(title = "权限与接口对应关系表")
public class Authority implements Serializable, BaseEntity<Long> {

    @Id
    private Long id;

    @Schema(title = "角色")
    private String authority;

    @Schema(title = "请求路径")
    private String interfacePath;

    @Schema(title = "创建时间")
    private LocalDateTime createdTime;

    @Schema(title = "修改时间")
    private LocalDateTime updatedTime;

}
