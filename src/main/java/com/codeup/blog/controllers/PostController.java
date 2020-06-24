package com.codeup.blog.controllers;

import com.codeup.blog.daos.BlogsRepository;
import com.codeup.blog.models.Post;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PostController {

    private BlogsRepository postsDao;

    public PostController(BlogsRepository blogsRepository) {
        postsDao = blogsRepository;
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
        Post post = new Post(id, "New Post", "This is a new post.");
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
    public String save(@RequestParam(name = "title") String title,
                       @RequestParam(name = "body") String body) {
        Post newPost = new Post(title, body);
        postsDao.save(newPost);
        return "posts/index";
    }

    @PostMapping("/posts/{id}/delete")
    public String destroy(@PathVariable long id) {
        postsDao.deleteById(id);
        return "posts/index";
    }
}
