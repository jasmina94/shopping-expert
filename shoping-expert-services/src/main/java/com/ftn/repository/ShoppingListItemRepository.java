package com.ftn.repository;

import com.ftn.entity.ShoppingListItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by milca on 4/25/2018.
 */
@Repository
public interface ShoppingListItemRepository extends JpaRepository<ShoppingListItem, Long>{

    List<ShoppingListItem> findByIsPurchased(boolean isPurchased);
}
