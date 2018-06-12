package com.ftn.service.serviceImplementation;

import com.ftn.dto.CategoryDTO;
import com.ftn.dto.CategoryItemDTO;
import com.ftn.entity.CategoryItem;
import com.ftn.entity.ShoppingListItem;
import com.ftn.repository.CategoryItemRepository;
import com.ftn.repository.CategoryRepository;
import com.ftn.repository.ShoppingListItemRepository;
import com.ftn.service.ICategoryItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Jasmina on 11/06/2018.
 */
@Service
public class CategoryItemService implements ICategoryItemService {

    @Autowired
    private CategoryItemRepository categoryItemRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ShoppingListItemRepository shoppingListItemRepository;

    @Override
    public List<CategoryItemDTO> readAll() {
        return categoryItemRepository.findAll().stream().
                map(categoryItem -> new CategoryItemDTO(categoryItem)).collect(Collectors.toList());
    }

    @Override
    public List<CategoryItemDTO> readAllForCategory(long categoryId) {
        return categoryItemRepository.findByCategoryId(categoryId).stream()
                .map(categoryItem -> new CategoryItemDTO(categoryItem)).collect(Collectors.toList());
    }

    @Override
    public HashMap<String, List<CategoryItemDTO>> readAllAsMap() {
        HashMap<String, List<CategoryItemDTO>> map = new HashMap<>();
        List<CategoryDTO> categoryDTOS = categoryService.readAll();
        List<CategoryItemDTO> categoryItemDTOS = readAll();

        for (CategoryDTO categoryDTO: categoryDTOS) {
            List<CategoryItemDTO> itemsForCategory = readAllForCategory(categoryDTO.getId());
            map.put(categoryDTO.getCategoryName(), itemsForCategory);
        }
        return map;
    }

    @Override
    public boolean createCategoryAndShoppingItem(CategoryItemDTO categoryItemDTO, long listId) {
        boolean success = true;
        CategoryItem categoryItem = new CategoryItem(categoryItemDTO);
        try {
            categoryItem = categoryItemRepository.save(categoryItem);
            ShoppingListItem shoppingListItem = new ShoppingListItem(categoryItem.getId(), listId, categoryItem.getItemName());
            shoppingListItem = shoppingListItemRepository.save(shoppingListItem);
        }catch (Exception e){
            success = false;
        }
        return success;
    }

    @Override
    public CategoryItemDTO findById(long id) {
        try {
            CategoryItem categoryItem = categoryItemRepository.findById(id).orElseThrow(NullPointerException::new);
            CategoryItemDTO categoryItemDTO = new CategoryItemDTO(categoryItem);
            return categoryItemDTO;
        }catch (NullPointerException e){
            return  null;
        }
    }
}
