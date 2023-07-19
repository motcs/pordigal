package com.prodigal.core.sensitive;

import cn.hutool.dfa.WordTree;
import com.prodigal.annotation.RestServerException;
import com.prodigal.commons.BaseAutoToolsUtil;
import com.prodigal.commons.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2022-06-28 11:08:52 星期二
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class SensitiveService extends BaseAutoToolsUtil {


    private final SensitiveRepository sensitiveRepository;
    private final SensitiveInitBaseService sensitiveInitBaseService;

    public Page<Sensitive> page(SensitiveRequest request, Pageable pageable) {
        return this.sensitiveRepository.findAll(request.toSpecification(), pageable);
    }

    public List<Sensitive> search(SensitiveRequest request) {
        return this.sensitiveRepository.findAll(request.toSpecification());
    }

    public Sensitive operation(SensitiveRequest request) {
        Sensitive save = this.save(request.toSensitive());
        log.info("开始更新定义的敏感词库");
        super.executor.execute(() -> sensitiveInitBaseService.init(save.getTenantUuid()));
        return save;
    }

    public Result copy(SensitiveRequest request) {
        List<Sensitive> sensitiveList1 = this.sensitiveRepository
                .findAllByTenantUuid(request.getTenantCode());
        if (!ObjectUtils.isEmpty(sensitiveList1)) {
            return Result.of(false, null, "当前租户敏感词已存在数据，不可再复制其他租户");
        }
        // 将要复制的租户的敏感词全部获取到，启用|不启用全查
        List<Sensitive> sensitiveList = this.sensitiveRepository
                .findAllByTenantUuid(request.getTenantUuid());
        if (ObjectUtils.isEmpty(sensitiveList)) {
            return Result.of(false, null, "当前复制的租户下没有敏感词词库");
        }
        List<Sensitive> list = sensitiveList.stream().parallel()
                .peek(v -> v.tenantCode(request.getTenantCode())).collect(Collectors.toList());
        List<Sensitive> sensitives = this.sensitiveRepository.saveAll(list);
        log.info("开始更新定义的敏感词库");
        super.executor.execute(sensitiveInitBaseService::init);
        return Result.of(true, "共复制:" + sensitives.size() + "条", "复制成功");
    }

    public void deleteBatch(List<Long> ids) {
        this.sensitiveRepository.deleteAllById(ids);

    }

    public Result vocabularyDetection(VocabularyDetection detection) {
        //获取启用的敏感词汇
        List<String> search = this.sensitiveInitBaseService
                .TENANT_SENSITIVE.get(detection.getTenantUuid());
        if (ObjectUtils.isEmpty(search)) {
            search = this.sensitiveInitBaseService.TENANT_SENSITIVE.get("0");
        }
        if (!ObjectUtils.isEmpty(search)) {
            WordTree wordTree = new WordTree();
            wordTree.addWords(search);
            //匹配到最长关键词，跳过已经匹配的关键词
            List<String> match = wordTree.matchAll(detection.getText(), -1, false, true);
            return Result.of(match.size() == 0, match,
                    match.size() == 0 ? "不存在敏感词汇" : "存在敏感词汇,请重新编辑");
        }
        return Result.of(true, List.of(), "不存在敏感词汇");
    }

    private Sensitive save(Sensitive sensitive) {
        if (!sensitive.isNew()) {
            assert sensitive.getId() != null;
            Sensitive oldReport = this.sensitiveRepository.findById(sensitive.getId())
                    .orElseThrow(() -> RestServerException.withMsg(1404,
                            "没有找到id为:" + sensitive.getId() + "的敏感词汇,无法进行修改,请重试!"));
            sensitive.setCreator(oldReport.getCreator());
            sensitive.setCreatedTime(oldReport.getCreatedTime());
        }
        return this.sensitiveRepository.save(sensitive);
    }

}
