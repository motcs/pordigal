package com.prodigal.core.sensitive;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2023-06-17 星期六
 */
@Service
@RequiredArgsConstructor
public class SensitiveInitBaseService {

    private final SensitiveRepository sensitiveRepository;
    /**
     * 敏感词校验的内容
     */
    public Map<String, List<String>> TENANT_SENSITIVE;

    @PostConstruct
    public void init() {
        TENANT_SENSITIVE = this.sensitiveRepository.findByIsEnable(true).stream()
                .collect(Collectors.groupingBy(Sensitive::getTenantUuid,
                        Collectors.mapping(Sensitive::getTreeValue, Collectors.toList())));
    }

    /**
     * 根据租户重新加载敏感词
     *
     * @param tenantCode 租户编码
     */
    public void init(String tenantCode) {
        List<String> sensitives = this.sensitiveRepository.findByTenantUuid(tenantCode)
                .stream().map(Sensitive::getTreeValue).collect(Collectors.toList());
        TENANT_SENSITIVE.put(tenantCode, sensitives);
    }

}
