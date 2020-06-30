package com.codeup.blog.controllers;

import com.codeup.blog.daos.BlogsRepository;
import com.codeup.blog.daos.ImagesRepository;
import com.codeup.blog.daos.UsersRepository;
import com.codeup.blog.models.Post;
import com.codeup.blog.models.PostImage;
import com.codeup.blog.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Controller
public class PostController {

    private BlogsRepository postsDao;
    private UsersRepository usersDao;
    private ImagesRepository imagesDao;

    public PostController(BlogsRepository blogsRepository, UsersRepository usersRepository, ImagesRepository imagesRepository) {
        postsDao = blogsRepository;
        usersDao = usersRepository;
        imagesDao = imagesRepository;
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

    @PostMapping("/posts/{id}/edit")
    public String update(@PathVariable long id,
                         @RequestParam(name = "title") String title,
                         @RequestParam(name = "body") String body) {
        //find an ad
        Post foundPost = postsDao.getOne(id); // select * from posts where id = ?
        //edit the post
        foundPost.setTitle(title);
        foundPost.setBody(body);
        //save the changes
        postsDao.save(foundPost); // update ads set title = ? where id = ?
        return "posts/index";
    }

    @GetMapping("/posts/create")
    public String showForm() {
        return "posts/create";
    }

    @PostMapping("/posts/create")
    public String save(Model model,
                       @RequestParam(name = "title") String title,
                       @RequestParam(name = "body") String body,
                       @RequestParam(name = "user") Long user,
                       @RequestParam(name = "image") String image) {
        PostImage imageUrl = new PostImage();
        imageUrl.setDescription("photo");
        imageUrl.setPath(image);
        Long userId = user;
        User userObj = usersDao.getOne(userId);
        List<PostImage> images = new ArrayList<>();
        images.add(imageUrl);
        Post newPost = new Post(title, body, userObj, images);
        imageUrl.setPost(newPost);
        Post savedPost = postsDao.save(newPost);
        return "redirect:/posts/" + savedPost.getId();
    }

    @PostMapping("/posts/{id}/delete")
    public String destroy(Model model, @PathVariable long id) {
        postsDao.deleteById(id);
        List<Post> posts = postsDao.findAll();
        model.addAttribute("posts", posts);
        return "posts/index";
    }
}
