package wheresoever.quickprotoserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wheresoever.quickprotoserver.domain.Comment;
import wheresoever.quickprotoserver.domain.CommentChild;

import java.util.Optional;

@Repository
public interface CommentChildRepository extends JpaRepository<CommentChild, Long> {
    Optional<CommentChild> findById(Long commentChildId);
}
