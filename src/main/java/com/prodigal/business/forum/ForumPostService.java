package com.prodigal.business.forum;

import com.prodigal.annotation.RestServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2023-07-11 星期二
 */
@Service
@RequiredArgsConstructor
public class ForumPostService {

    private final ForumPostRepository forumPostRepository;

    public Page<ForumPost> page(ForumPostRequest request, Pageable pageable) {
        return forumPostRepository.findAll(request.specification(), pageable);
    }

    public List<ForumPost> search(ForumPostRequest request) {
        return forumPostRepository.findAll(request.specification());
    }

    public void delete(List<Long> ids) {
        forumPostRepository.deleteAllById(ids);
    }

    public ForumPost operation(ForumPostRequest request) {
        return this.save(request.toForumPost());
    }

    private ForumPost save(ForumPost forumPost) {
        if (forumPost.isNew()) {
            return forumPostRepository.save(forumPost);
        }
        assert forumPost.getId() != null;
        Optional<ForumPost> optionalForumPost = forumPostRepository.findById(forumPost.getId());
        if (optionalForumPost.isPresent()) {
            ForumPost existingForumPost = optionalForumPost.get();
            forumPost.setCreatedTime(existingForumPost.getCreatedTime());
            return forumPostRepository.saveAndFlush(forumPost);
        }

        throw RestServerException.withMsg("No forum post found with ID: " + forumPost.getId());
    }

}
