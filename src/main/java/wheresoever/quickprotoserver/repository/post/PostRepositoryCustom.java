package wheresoever.quickprotoserver.repository.post;

import wheresoever.quickprotoserver.domain.Post;

import java.util.Optional;

public interface PostRepositoryCustom {
    public Optional<Post> findPostDetailById(Long postId);
}
