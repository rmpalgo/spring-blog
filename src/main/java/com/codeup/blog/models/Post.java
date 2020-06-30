package com.codeup.blog.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 240)
    private String title;

    @Column(nullable = false)
    private String body;

    @OneToOne
    private User user;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "post")
    private List<PostImage> images;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    // Spring uses the empty constructor
    public Post() {

    }

    // read
    public Post(long id, String title, String body, User user, List<PostImage> images) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.user = user;
        this.images = images;
    }

    // insert
    public Post(String title, String body, User user, List<PostImage> images) {
        this.title = title;
        this.body = body;
        this.user = user;
        this.images = images;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<PostImage> getImages() {
        return images;
    }

    public void setImages(List<PostImage> images) {
        this.images = images;
    }
}
