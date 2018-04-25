package com.ftn.repository;

import com.ftn.entity.ShoppingList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by milca on 4/25/2018.
 */
@Repository
public interface ShoppingListRepository extends JpaRepository<ShoppingList, Long> {

    List<ShoppingList> findByCreatorIdAndIsArchived(Long loggedUserId, boolean archived);
}
