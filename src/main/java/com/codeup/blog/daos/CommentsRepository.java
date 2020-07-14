package com.codeup.blog.daos;


import com.codeup.blog.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentsRepository extends JpaRepository<Comment, Long> {
}
