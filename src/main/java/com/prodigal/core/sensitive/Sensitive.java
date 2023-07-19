package com.prodigal.core.sensitive;

import com.prodigal.security.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2022-06-28 10:44:25 星期二
 */
@Data
@Entity
@Schema(title = "敏感词")
@Table(name = "network_sensitive_check")
public class Sensitive implements BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Schema(title = "主键")
    private Long id;

    @Schema(title = "租户uuid")
    private String tenantUuid;

    @Schema(title = "比对的数值")
    private String treeValue;

    @Schema(title = "是否参与比对")
    private Boolean isEnable;

    @Schema(title = "创建人用户")
    private String creator;

    @Schema(title = "修改人用户")
    private String updater;

    @Schema(title = "创建时间")
    @CreatedDate
    private LocalDateTime createdTime;

    @Schema(title = "修改时间")
    @LastModifiedDate
    private LocalDateTime updatedTime;

    public void tenantCode(String tenantCode) {
        this.setId(null);
        this.setTenantUuid(tenantCode);
        this.setCreator(tenantCode);
        this.setUpdater(tenantCode);
        this.setCreatedTime(LocalDateTime.now());
        this.setUpdatedTime(LocalDateTime.now());
    }

}
