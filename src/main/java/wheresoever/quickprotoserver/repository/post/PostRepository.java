package wheresoever.quickprotoserver.repository.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wheresoever.quickprotoserver.domain.Category;
import wheresoever.quickprotoserver.domain.Post;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    Optional<Post> findByCategory(Category category);

    Page<Post> findAllByOrderByIdDesc(Pageable pageable);
}
