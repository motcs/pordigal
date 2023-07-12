package com.prodigal.security.authority.resources;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2023-06-28 星期三
 */
public interface ResourcesRepository extends JpaRepository<Resources, Long>, JpaSpecificationExecutor<Resources> {

    Resources findByPath(String path);

}
