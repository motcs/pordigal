package com.prodigal.security.authority;

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
public class AuthorityRequest extends Authority implements Serializable {

    public Authority toAuthority() {
        Authority authority = new Authority();
        BeanUtils.copyProperties(this, authority);
        return authority;
    }

    public Specification<Authority> specification() {
        return (root, query, cb) -> {
            List<Predicate> list = new ArrayList<>();
            if (StringUtils.hasLength(this.getAuthority())) {
                list.add(cb.equal(root.get("authority").as(String.class), this.getAuthority()));
            }
            if (StringUtils.hasLength(this.getInterfacePath())) {
                list.add(cb.like(root.get("interfacePath").as(String.class), "%" + this.getInterfacePath() + "%"));
            }
            query.where(list.toArray(new Predicate[0]));
            return query.getRestriction();
        };
    }

}
