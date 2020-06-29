package com.codeup.blog.daos;

import com.codeup.blog.models.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ImagesRepository extends JpaRepository<PostImage, Long> {
}
