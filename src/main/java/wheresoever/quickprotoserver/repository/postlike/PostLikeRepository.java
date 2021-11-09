package wheresoever.quickprotoserver.repository.postlike;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wheresoever.quickprotoserver.domain.Member;
import wheresoever.quickprotoserver.domain.Post;
import wheresoever.quickprotoserver.domain.PostLike;

import java.util.Optional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long>, PostLikeRepositoryCustom {
    Optional<PostLike> findById(Long postLikeId);
    Optional<PostLike> findByPostAndMember(Post post, Member member);
    //Page<PostLike> findAllByPost(Post post, Pageable pageable);
}
