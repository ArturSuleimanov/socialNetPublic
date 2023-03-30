package artur.practicing.social.service;


import artur.practicing.social.domain.Comment;
import artur.practicing.social.domain.Message;
import artur.practicing.social.domain.User;
import artur.practicing.social.domain.Views;
import artur.practicing.social.dto.EventType;
import artur.practicing.social.dto.ObjectType;
import artur.practicing.social.repo.CommentRepo;
import artur.practicing.social.util.WsSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.View;
import java.util.function.BiConsumer;

@Service
public class CommentService {
    private final CommentRepo commentRepo;
    private final BiConsumer<EventType, Comment> wsSender;

    @Autowired
    public CommentService(CommentRepo commentRepo, WsSender wsSender) {
        this.commentRepo = commentRepo;
        this.wsSender = wsSender.getSender(ObjectType.COMMENT, Views.FullComment.class);
    }

    public Comment create(Comment comment, User user) {
        comment.setAuthor(user);
        Comment commentFromDb = commentRepo.save(comment);
        wsSender.accept(EventType.CREATE, commentFromDb);

        return comment;
    }
}