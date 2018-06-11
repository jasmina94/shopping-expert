package com.ftn.repository;

import com.ftn.entity.CategoryItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Jasmina on 11/06/2018.
 */
public interface CategoryItemRepository extends JpaRepository<CategoryItem, Long> {

    List<CategoryItem> findByCategoryId(long categoryId);
}
