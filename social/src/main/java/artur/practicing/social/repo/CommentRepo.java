package artur.practicing.social.repo;

import artur.practicing.social.domain.Comment;
import artur.practicing.social.domain.Message;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepo extends JpaRepository<Comment, Long> {


}
