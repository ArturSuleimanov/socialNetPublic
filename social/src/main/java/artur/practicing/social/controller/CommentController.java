package artur.practicing.social.controller;


import artur.practicing.social.domain.Comment;
import artur.practicing.social.domain.User;
import artur.practicing.social.domain.Views;
import artur.practicing.social.repo.UserDetailsRepo;
import artur.practicing.social.service.CommentService;
import com.fasterxml.jackson.annotation.JsonView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("comment")
public class CommentController {
    private final CommentService commentService;

    @Autowired
    private UserDetailsRepo userDetailsRepo;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    @JsonView(Views.FullComment.class)
    public Comment create(
            @RequestBody Comment comment,
            @AuthenticationPrincipal DefaultOidcUser user
    ) {
        User userFromDb = userDetailsRepo.findById(user.getName()).orElseThrow();

        return commentService.create(comment, userFromDb);
    }
}
