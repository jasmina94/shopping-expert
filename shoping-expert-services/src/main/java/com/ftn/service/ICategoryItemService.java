package com.ftn.service;

import com.ftn.dto.CategoryItemDTO;
import jdk.internal.dynalink.linker.LinkerServices;

import java.util.List;

/**
 * Created by Jasmina on 11/06/2018.
 */
public interface ICategoryItemService {

    List<CategoryItemDTO> readAll();

    List<CategoryItemDTO> readAllForCategory(long categoryId);
}
