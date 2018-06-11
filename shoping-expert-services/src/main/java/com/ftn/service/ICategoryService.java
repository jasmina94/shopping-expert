package com.ftn.service;

import com.ftn.dto.CategoryDTO;

import java.util.List;

/**
 * Created by Jasmina on 11/06/2018.
 */
public interface ICategoryService {

    List<CategoryDTO> readAll();
}
