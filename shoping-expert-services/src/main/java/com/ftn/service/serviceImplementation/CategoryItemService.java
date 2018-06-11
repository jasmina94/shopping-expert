package com.ftn.service.serviceImplementation;

import com.ftn.dto.CategoryItemDTO;
import com.ftn.repository.CategoryItemRepository;
import com.ftn.service.ICategoryItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Jasmina on 11/06/2018.
 */
@Service
public class CategoryItemService implements ICategoryItemService {

    @Autowired
    private CategoryItemRepository categoryItemRepository;

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
}
