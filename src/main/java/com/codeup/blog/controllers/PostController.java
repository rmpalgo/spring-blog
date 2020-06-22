package com.codeup.blog.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PostController {

    @GetMapping("/posts")
    @ResponseBody
    public String showAll() {
        return "All posts!";
    }

    @GetMapping("/posts/{id}")
    @ResponseBody
    public String showOne(@PathVariable long id) {
        return "This is an individual posts with an id of : " + id;
    }

    @GetMapping("/posts/create")
    @ResponseBody
    public String create() {
        return "this is the create page.";
    }

    @PostMapping("/posts/create")
    @ResponseBody
    public String createPost() {
        return "this is a post";
    }
}
