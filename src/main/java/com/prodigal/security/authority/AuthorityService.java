package com.prodigal.security.authority;

import com.prodigal.annotation.RestServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2023-06-28 星期三
 */
@Service
@RequiredArgsConstructor
public class AuthorityService {

    /**
     * 获取所有的权限和角色的对应关系
     */
    public static Map<String, List<String>> authorities;
    private final AuthorityRepository authorityRepository;

    @PostConstruct
    private void setAuthorities() {
        List<Authority> authorityList = this.authorityRepository.findAll();
        if (!ObjectUtils.isEmpty(authorityList)) {
            authorities = authorityList.stream().collect(Collectors.groupingBy(Authority::getInterfacePath,
                    Collectors.mapping(Authority::getAuthority, Collectors.toList())));
        } else {
            authorities = new HashMap<>(2);
        }
    }

    public Page<Authority> page(AuthorityRequest request, Pageable pageable) {
        return this.authorityRepository.findAll(request.specification(), pageable);
    }

    public List<Authority> search(AuthorityRequest request) {
        return this.authorityRepository.findAll(request.specification());
    }

    public void delete(List<Long> ids) {
        this.authorityRepository.deleteAllById(ids);
    }

    public Authority operation(AuthorityRequest request) {
        return this.save(request.toAuthority());
    }

    private Authority save(Authority authority) {
        Authority authorityPath = this.authorityRepository
                .findByAuthorityAndInterfacePath(authority.getAuthority(), authority.getInterfacePath());
        if (!ObjectUtils.isEmpty(authorityPath)) {
            throw RestServerException.withMsg("角色" + authority.getAuthority() + "已关联当前接口：" + authority.getInterfacePath());
        }
        if (authority.isNew()) {
            return this.authorityRepository.save(authority);
        }
        assert authority.getId() != null;
        Optional<Authority> byId = this.authorityRepository.findById(authority.getId());
        if (byId.isPresent()) {
            Authority authority1 = byId.get();
            authority.setCreatedTime(authority1.getCreatedTime());
            return this.authorityRepository.saveAndFlush(authority);
        }
        throw RestServerException.withMsg("没有ID为:" + authority.getId() + "的数据信息");
    }

}
