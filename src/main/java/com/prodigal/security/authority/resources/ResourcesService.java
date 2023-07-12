package com.prodigal.security.authority.resources;

import com.prodigal.annotation.RestServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public class ResourcesService {

    private final ResourcesRepository resourcesRepository;

    public Page<Resources> page(ResourcesRequest request, Pageable pageable) {
        return this.resourcesRepository.findAll(request.specification(), pageable);
    }

    public List<Resources> search(ResourcesRequest request) {
        return this.resourcesRepository.findAll(request.specification());
    }

    public void delete(List<Long> ids) {
        this.resourcesRepository.deleteAllById(ids);
    }

    public Resources operation(ResourcesRequest request) {
        return this.save(request.toResources());
    }

    private Resources save(Resources resources) {
        Resources findPath = this.resourcesRepository.findByPath(resources.getPath());
        if (!ObjectUtils.isEmpty(findPath)) {
            throw RestServerException.withMsg("接口资源已存在:" + resources.getPath());
        }
        if (resources.isNew()) {
            return this.resourcesRepository.save(resources);
        }
        assert resources.getId() != null;
        Optional<Resources> byId = this.resourcesRepository.findById(resources.getId());
        if (byId.isPresent()) {
            Resources resources1 = byId.get();
            resources.setCreatedTime(resources1.getCreatedTime());
            return this.resourcesRepository.saveAndFlush(resources);
        }
        throw RestServerException.withMsg("没有ID为:" + resources.getId() + "的数据信息");
    }

}
