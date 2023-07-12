package com.prodigal.security.authority.resources;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2023-06-28 星期三
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ResourcesRequest extends Resources implements Serializable {

    public Resources toResources() {
        Resources resources = new Resources();
        BeanUtils.copyProperties(this, resources);
        return resources;
    }

    public Specification<Resources> specification() {
        return (root, query, cb) -> {
            List<Predicate> list = new ArrayList<>();
            if (StringUtils.hasLength(this.getPath())) {
                list.add(cb.equal(root.get("path").as(String.class), this.getPath()));
            }
            if (StringUtils.hasLength(this.getName())) {
                list.add(cb.like(root.get("name").as(String.class), "%" + this.getName() + "%"));
            }
            query.where(list.toArray(new Predicate[0]));
            return query.getRestriction();
        };
    }

}
