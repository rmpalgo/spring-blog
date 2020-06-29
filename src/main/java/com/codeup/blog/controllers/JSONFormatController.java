package com.codeup.blog.controllers;


import com.codeup.blog.daos.BlogsRepository;
import com.codeup.blog.models.Post;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class JSONFormatController {

    private BlogsRepository postsDao;

    public JSONFormatController(BlogsRepository blogsRepository) {
        postsDao = blogsRepository;
    }

    @GetMapping("/json")
    public List<Post> getAllPost() {
        return postsDao.findAll();
    }
}
