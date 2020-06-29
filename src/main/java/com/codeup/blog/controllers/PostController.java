package com.codeup.blog.controllers;

import com.codeup.blog.daos.BlogsRepository;
import com.codeup.blog.daos.UsersRepository;
import com.codeup.blog.models.Post;
import com.codeup.blog.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PostController {

    private BlogsRepository postsDao;
    private UsersRepository usersDao;

    public PostController(BlogsRepository blogsRepository, UsersRepository UsersRepository) {
        postsDao = blogsRepository;
        usersDao = UsersRepository;
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
                       @RequestParam(name = "user") Long user) {
        Long userId = user;
        User userObj = usersDao.getOne(userId);
        Post newPost = new Post(title, body, userObj);
        postsDao.save(newPost);
        List<Post> posts = postsDao.findAll();
        model.addAttribute("posts", posts);
        return "posts/index";
    }

    @PostMapping("/posts/{id}/delete")
    public String destroy(Model model, @PathVariable long id) {
        postsDao.deleteById(id);
        List<Post> posts = postsDao.findAll();
        model.addAttribute("posts", posts);
        return "posts/index";
    }
}
