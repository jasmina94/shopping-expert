package com.ftn.repository;

import com.ftn.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Jasmina on 11/06/2018.
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
