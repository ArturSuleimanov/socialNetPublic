package artur.practicing.social.controller;//package artur.practicing.social.controller;


import artur.practicing.social.domain.Message;
import artur.practicing.social.domain.User;
import artur.practicing.social.domain.Views;
import artur.practicing.social.dto.MessagePageDto;
import artur.practicing.social.repo.UserDetailsRepo;
import artur.practicing.social.service.MessageService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping("message")
public class MessageController {

    public static final int MESSAGES_PER_PAGE = 3;


    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @Autowired
    public UserDetailsRepo userRepo;

    @GetMapping
    @JsonView(Views.FullMessage.class)
    public MessagePageDto list(
            @PageableDefault(size = MESSAGES_PER_PAGE, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal DefaultOidcUser user
    ) {
        User userFromDb = userRepo.findById(user.getName()).orElseThrow();
        return messageService.findForUser(pageable, userFromDb);
    }

    @GetMapping("{id}")
    @JsonView(Views.FullMessage.class)
    public Message getOne(@PathVariable("id") Message message) {
        return message;
    }

    @PostMapping
    public Message create(
            @RequestBody Message message,
            @AuthenticationPrincipal DefaultOidcUser user
    ) throws IOException {
        return messageService.create(message, user);
    }

    @PutMapping("{id}")
    public Message update(
            @PathVariable("id") Message messageFromDb,
            @RequestBody Message message
    ) throws IOException {
        return messageService.update(messageFromDb, message);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Message message) {
        messageService.delete(message);
    }



}