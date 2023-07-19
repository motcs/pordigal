package com.prodigal.core.sensitive;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2022-06-28 11:22:20 星期二
 */
@Data
public class SensitiveRequest implements Serializable {

    @Schema(title = "主键")
    private Long id;

    @Schema(title = "租户主键")
    private String tenantUuid;

    @Schema(title = "租户编码")
    private String tenantCode;

    @Schema(title = "比对的数值")
    private String treeValue;

    @Schema(title = "是否参与比对")
    private Boolean isEnable;

    @Schema(title = "创建人用户")
    private String creator;

    @Schema(title = "修改人用户")
    private String updater;

    public Sensitive toSensitive() {
        Sensitive sensitive = new Sensitive();
        BeanUtils.copyProperties(this, sensitive);
        return sensitive;
    }

    public Specification<Sensitive> toSpecification() {
        return (root, query, cb) -> {
            List<Predicate> list = new ArrayList<>();
            if (!ObjectUtils.isEmpty(this.id)) {
                list.add(cb.equal(root.get("id").as(Long.class), this.id));
            }
            if (StringUtils.hasLength(this.tenantUuid)) {
                list.add(cb.equal(root.get("tenantUuid").as(String.class), this.tenantUuid));
            }
            if (!ObjectUtils.isEmpty(this.isEnable)) {
                list.add(cb.equal(root.get("isEnable").as(Boolean.class), this.isEnable));
            } else {
                list.add(cb.equal(root.get("isEnable").as(Boolean.class), true));
            }
            if (StringUtils.hasLength(this.treeValue)) {
                list.add(cb.equal(root.get("treeValue").as(String.class), "%" + this.treeValue + "%"));
            }
            if (StringUtils.hasLength(this.updater)) {
                list.add(cb.equal(root.get("updater").as(String.class), this.updater));
            }
            if (StringUtils.hasLength(this.creator)) {
                list.add(cb.equal(root.get("creator").as(String.class), this.creator));
            }
            query.where(list.toArray(new Predicate[0]));
            return query.getRestriction();
        };
    }
}
