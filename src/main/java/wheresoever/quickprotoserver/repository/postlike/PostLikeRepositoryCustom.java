package wheresoever.quickprotoserver.repository.postlike;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import wheresoever.quickprotoserver.domain.Post;
import wheresoever.quickprotoserver.dto.PostLikeDto;


public interface PostLikeRepositoryCustom {
    Page<PostLikeDto> findAllAsDtoByPost(Post post, Pageable pageable);
}
