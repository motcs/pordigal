package com.prodigal.business.politics;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2023-07-13 星期四
 */
public interface PoliticsRepository extends JpaRepository<Politics, Long>, JpaSpecificationExecutor<Politics> {
}
