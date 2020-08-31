package com.codeup.blog.controllers;

import com.codeup.blog.daos.BlogsRepository;
import com.codeup.blog.daos.CommentsRepository;
import com.codeup.blog.daos.ImagesRepository;
import com.codeup.blog.daos.UsersRepository;
import com.codeup.blog.models.Comment;
import com.codeup.blog.models.Post;
import com.codeup.blog.models.PostImage;
import com.codeup.blog.models.User;
import com.codeup.blog.services.EmailService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Long.parseLong;

@Controller
public class PostController {

    //dependency injections
    private BlogsRepository postsDao;
    private UsersRepository usersDao;
    private ImagesRepository imagesDao;
    private CommentsRepository commentsDao;
    private final EmailService emailService;

    public PostController(BlogsRepository blogsRepository, UsersRepository usersRepository, ImagesRepository imagesRepository, CommentsRepository commentsRepository, EmailService emailService) {
        this.postsDao = blogsRepository;
        this.usersDao = usersRepository;
        this.imagesDao = imagesRepository;
        this.commentsDao = commentsRepository;
        this.emailService = emailService;
    }

    @GetMapping("/posts")
    public String showAll(Model model) {
        List<Post> posts = postsDao.findAll();
        model.addAttribute("noPostsFound", posts.size() == 0);
        model.addAttribute("posts", posts);
        return "posts/index";
    }

    @GetMapping("/posts/{id}")
    public String showOne(@PathVariable long id, Model model) {
        Post post = postsDao.getOne(id);
        List<Comment> comments = commentsDao.findAll();
        model.addAttribute("comment", new Comment());
        model.addAttribute("post", post);
        model.addAttribute("No Comments", comments.size() == 0);
        model.addAttribute("comments", comments);
        return "posts/show";
    }

    @GetMapping("/posts/{id}/edit")
    public String showEditForm(Model model, @PathVariable long id) {
        //find an ad
        Post postToEdit = postsDao.getOne(id);
        model.addAttribute("post", postToEdit);
        return "posts/edit";
    }

    @PostMapping("/posts/edit")
    public String update(@ModelAttribute Post postEdited) {
        System.out.println("PostEdited: " + postEdited.getId());
        Post postToBeUpdated = postsDao.getOne(postEdited.getId());
        postToBeUpdated.setTitle(postEdited.getTitle());
        postToBeUpdated.setBody(postEdited.getBody());
        postsDao.save(postToBeUpdated); // update ads set title = ? where id = ?
        return "redirect:/posts/" + postEdited.getId();
    }

    @GetMapping("/posts/create")
    public String showForm(Model viewModel) {
        viewModel.addAttribute("post", new Post());
        return "posts/create";
    }

    @PostMapping("/posts/create")
    public String save(@ModelAttribute Post postToBeSaved,
                       @RequestParam(name = "image") String image) {
        PostImage imageUrl = new PostImage();
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        imageUrl.setDescription("photo");
        imageUrl.setPath(image);
        postToBeSaved.setUser(currentUser);
        List<PostImage> images = new ArrayList<>();
        images.add(imageUrl);
        postToBeSaved.setImages(images);
        imageUrl.setPost(postToBeSaved);
        Post savedPost = postsDao.save(postToBeSaved);
        emailService.prepareAndSend(savedPost, "A new Post", "A Post has been created with an id" + savedPost.getId());
        return "redirect:/posts/" + postToBeSaved.getId();
    }

    @PostMapping("/posts/{id}/delete")
    public String destroy(Model model, @PathVariable long id) {
        postsDao.deleteById(id);
        return "redirect:/posts";
    }

    @GetMapping("/posts.json")
    public @ResponseBody List<Post> getPostsAsJSON() {
         return postsDao.findAll();
    }

}
