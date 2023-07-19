package com.prodigal.core.sensitive;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2022-06-28 11:09:14 星期二
 */
public interface SensitiveRepository extends JpaRepository<Sensitive, Long>, JpaSpecificationExecutor<Sensitive> {

    List<Sensitive> findByIsEnable(Boolean isEnable);

    @Query(name = "select * from network_sensitive_check where tenant_uuid =:tenantUuid and is_enable = true", nativeQuery = true)
    List<Sensitive> findByTenantUuid(String tenantUuid);

    List<Sensitive> findAllByTenantUuid(String tenantUuid);

}
