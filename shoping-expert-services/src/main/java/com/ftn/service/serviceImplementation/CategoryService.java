package com.ftn.service.serviceImplementation;

import com.ftn.dto.CategoryDTO;
import com.ftn.repository.CategoryRepository;
import com.ftn.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Jasmina on 11/06/2018.
 */
@Service
public class CategoryService implements ICategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<CategoryDTO> readAll() {
        return categoryRepository.findAll().stream().map(category -> new CategoryDTO(category)).collect(Collectors.toList());
    }
}
