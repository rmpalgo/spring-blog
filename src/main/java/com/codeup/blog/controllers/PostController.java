package com.codeup.blog.controllers;

import com.codeup.blog.models.Post;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PostController {

    @GetMapping("/posts")
    public String showAll(Model model) {
        List<Post> posts = new ArrayList<>();
        posts.add(new Post("Post 1", "This is the body of post 1"));
        posts.add(new Post("Post 2", "This is the body of post 2"));
        posts.add(new Post("Post 3", "This is the body of post 3"));
        model.addAttribute("posts", posts);
        return "posts/index";
    }

    @GetMapping("/posts/{id}")
    public String showOne(@PathVariable long id, Model model) {
        Post post = new Post(id, "New Post", "This is a new post.");
        model.addAttribute("post", post);
        return "posts/show";
    }

    @GetMapping("/posts/create")
    @ResponseBody
    public String showForm() {
        return "view the form.";
    }

    @PostMapping("/posts/create")
    @ResponseBody
    public String save() {
        return "create a new form";
    }
}
