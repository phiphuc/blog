package com.blog.repository;

import com.blog.domain.GoogleSearchConfig;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the GoogleSearchConfig entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GoogleSearchConfigRepository extends JpaRepository<GoogleSearchConfig,Long> {
    
}
