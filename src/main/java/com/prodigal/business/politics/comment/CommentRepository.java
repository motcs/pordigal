package com.prodigal.business.politics.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2022/4/8
 */
public interface CommentRepository extends JpaRepository<Comment, Long>, JpaSpecificationExecutor<Comment> {

    /**
     * 根据pid删除所有子集
     *
     * @param pid 父级ID
     */
    @Transactional(rollbackOn = {Exception.class})
    void deleteByPid(Long pid);

    /**
     * 根据类型以及数据ID查询评论
     *
     * @param type   类型
     * @param dataId 数据id
     * @return 返回评论
     */
    List<Comment> findAllByTypeAndDataId(String type, Long dataId);

}
