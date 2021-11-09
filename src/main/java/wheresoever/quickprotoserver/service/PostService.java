package wheresoever.quickprotoserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wheresoever.quickprotoserver.domain.*;
import wheresoever.quickprotoserver.dto.PostLikeDto;
import wheresoever.quickprotoserver.repository.CommentChildRepository;
import wheresoever.quickprotoserver.repository.CommentRepository;
import wheresoever.quickprotoserver.repository.postlike.PostLikeRepository;
import wheresoever.quickprotoserver.repository.post.PostRepository;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    @Autowired
    private final PostRepository postRepository;

    @Autowired
    private final CommentRepository commentRepository;

    @Autowired
    private final CommentChildRepository commentChildRepository;

    @Autowired
    private final PostLikeRepository postLikeRepository;

    @Transactional
    public Long joinPost(Post post) {
        // 게시글(post)는 중복체크 불필요
        return postRepository.save(post).getId();
    }

    public Page<Post> findAllPosts(Pageable pageable) {
        return postRepository.findAllByOrderByIdDesc(pageable);
    }

    public Post findPost(Long postId){
        Optional<Post> optional = postRepository.findById(postId);
        if (optional.isEmpty()) {
            throw new IllegalStateException("does not exist post");
        }
        return optional.get();
    }

    public Post findPostDetail(Long postId) {
        Optional<Post> optional = postRepository.findPostDetailById(postId);
        if (optional.isEmpty()) {
            throw new IllegalStateException("does not exist post");
        }
        return optional.get();
    }

    @Transactional
    public void updatePost(Long postId, Long memberId, Category category, String content) {
        Optional<Post> optional = postRepository.findById(postId);
        if (optional.isEmpty()) {
            throw new IllegalStateException("does not exist post");
        }

        Post post = optional.get();
        if(!post.getMember().getId().equals(memberId)){
            throw new IllegalStateException("unauthorized access"); // IllegalAccessException이 맞음 귀찮아서.
        }
        if (post.getCategory() != category) {
            post.setCategory(category);
        }
        if (!post.getContent().matches(content)) {
            post.setContent(content);
        }
    }

    @Transactional
    public void cancelPost(Long postId) {
        Optional<Post> optional = postRepository.findById(postId);
        if (optional.isEmpty()) {
            throw new IllegalStateException("does not exist post");
        }
        Post post = optional.get();
        post.cancel();
    }

    @Transactional
    public Long joinComment(Comment comment) {
        // 댓글(Comment)은 중복체크 불필요
        return commentRepository.save(comment).getId();
    }

    public Comment findComment(Long commentId){
        Optional<Comment> optional = commentRepository.findById(commentId);
        if (optional.isEmpty()) {
            throw new IllegalStateException("does not exist comment");
        }
        return optional.get();
    }

    @Transactional
    public void updateComment(Long postId, String content) {
        Optional<Comment> optional = commentRepository.findById(postId);
        if (optional.isEmpty()) {
            throw new IllegalStateException("does not exist comment");
        }

        Comment comment = optional.get();
        if (!comment.getContent().matches(content)) {
            comment.setContent(content);
        }
    }

    @Transactional
    public void cancelComment(Long commentId) {
        Optional<Comment> optional = commentRepository.findById(commentId);
        if (optional.isEmpty()) {
            throw new IllegalStateException("does not exist comment");
        }
        optional.get().cancel();
    }

    @Transactional
    public Long joinCommentChild(CommentChild commentChild) {
        // 대댓글(CommentChild)은 중복체크 불필요
        return commentChildRepository.save(commentChild).getId();
    }


    @Transactional
    public void updateCommentChild(Long commentId, String content) {
        Optional<CommentChild> optional = commentChildRepository.findById(commentId);
        if (optional.isEmpty()) {
            throw new IllegalStateException("does not exist commentChild");
        }

        CommentChild commentChild = optional.get();
        if (!commentChild.getContent().matches(content)) {
            commentChild.setContent(content);
        }
    }

    @Transactional
    public void cancelCommentChild(Long commentId) {
        Optional<CommentChild> optional = commentChildRepository.findById(commentId);
        if (optional.isEmpty()) {
            throw new IllegalStateException("does not exist commentChild");
        }
        optional.get().cancel();
    }

    @Transactional
    public Long joinPostLike(PostLike postLike) {
        // 좋아요(PostLike)는 중복체크 필요

        Optional<PostLike> optional = postLikeRepository
                .findByPostAndMember(postLike.getPost(), postLike.getMember());
        if (optional.isPresent()) {
            throw new IllegalStateException("duplicated postLike");
        }

        return postLikeRepository.save(postLike).getId();
    }

    public Page<PostLikeDto> findAllPostLikes (Pageable pageable, Long postId){
        Optional<Post> optional = postRepository.findById(postId);
        if (optional.isEmpty()) {
            throw new IllegalStateException("does not exist post");
        }
        Post post = optional.get();
        return postLikeRepository.findAllAsDtoByPost(post,pageable);
    }

    @Transactional
    public void cancelPostLike(Post post, Member member) {
        Optional<PostLike> optional = postLikeRepository.findByPostAndMember(post, member);
        if (optional.isEmpty()) {
            throw new IllegalStateException("does not exist postLike");
        }
        optional.get().cancel();
    }
}
