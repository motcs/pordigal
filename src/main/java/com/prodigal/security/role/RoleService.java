package com.prodigal.security.role;

import com.prodigal.annotation.RestServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;

/**
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2023-06-28 星期三
 */
@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public List<Role> search() {
        return this.roleRepository.findAll();
    }

    public void delete(List<Long> ids) {
        this.roleRepository.deleteAllById(ids);
    }

    public Role operation(Role role) {
        Role byName = this.roleRepository.findByName(role.getName());
        if (!ObjectUtils.isEmpty(byName)) {
            throw RestServerException.withMsg("角色:" + role.getName() + "已存在");
        }
        if (ObjectUtils.isEmpty(role.getId())) {
            return this.roleRepository.save(role);
        } else {
            Optional<Role> byId = this.roleRepository.findById(role.getId());
            if (byId.isPresent()) {
                return this.roleRepository.saveAndFlush(role);
            }
            throw RestServerException.withMsg("没有ID为:" + role.getId() + "的数据信息");
        }
    }

}
