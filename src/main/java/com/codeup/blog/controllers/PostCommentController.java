package com.codeup.blog.controllers;

import com.codeup.blog.daos.BlogsRepository;
import com.codeup.blog.daos.CommentsRepository;
import com.codeup.blog.daos.UsersRepository;
import com.codeup.blog.models.Comment;
import com.codeup.blog.models.Post;
import com.codeup.blog.models.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;
import java.util.List;

@Controller
public class PostCommentController {

    private BlogsRepository postsDao;
    private UsersRepository usersDao;
    private CommentsRepository commentsDao;

    public PostCommentController(BlogsRepository blogsRepository, UsersRepository usersRepository, CommentsRepository commentsRepository) {
        this.postsDao = blogsRepository;
        this.usersDao = usersRepository;
        this.commentsDao = commentsRepository;
    }

    @PostMapping("/comments/create")
    public String createComment( @ModelAttribute Comment commentToBeSaved, Principal principal) {
        Post post = postsDao.getOne(1L);
        User user = usersDao.getOne(1L);
        commentToBeSaved.setPost(post);
        commentToBeSaved.setUser(user);
        Date currentDate = new Date();
        commentToBeSaved.setCreatedAt(currentDate);
        Comment savedComment = commentsDao.save(commentToBeSaved);
        return "redirect:/posts/" + savedComment.getPost().getId();
    }


    @GetMapping("/comments/create")
    public String showCommentForm(Model viewModel) {
        viewModel.addAttribute("comment", new Comment());
        return "posts/comments";
    }

    @GetMapping("/comments")
    public String showAll(Model model) {
        List<Comment> comments = commentsDao.findAll();
        model.addAttribute("comments", comments);
        return "posts/viewComments";
    }

}
