package com.blog.repository;

import com.blog.domain.ImageBackground;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ImageBackground entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ImageBackgroundRepository extends JpaRepository<ImageBackground,Long> {
    
}
