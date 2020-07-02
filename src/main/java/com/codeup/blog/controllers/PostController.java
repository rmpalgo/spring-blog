package com.codeup.blog.controllers;

import com.codeup.blog.daos.BlogsRepository;
import com.codeup.blog.daos.ImagesRepository;
import com.codeup.blog.daos.UsersRepository;
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
    private final EmailService emailService;

    public PostController(BlogsRepository blogsRepository, UsersRepository usersRepository, ImagesRepository imagesRepository, EmailService emailService) {
        this.postsDao = blogsRepository;
        this.usersDao = usersRepository;
        this.imagesDao = imagesRepository;
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
        PostImage images = imagesDao.getOne(id);
        model.addAttribute("images", images);
        model.addAttribute("post", post);
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
    public String update(@ModelAttribute Post postToEdit) {
        //find an ad
        User currentUser = usersDao.getOne(1L); // select * from posts where id = ?
        //edit the post
        postToEdit.setUser(currentUser);
        //save the changes
        postsDao.save(postToEdit); // update ads set title = ? where id = ?
        return "redirect:/posts/" + postToEdit.getId();
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
        List<Post> posts = postsDao.findAll();
        model.addAttribute("posts", posts);
        return "posts/index";
    }
}
