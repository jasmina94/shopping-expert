package com.ftn.mdj.services;

import com.ftn.mdj.dto.CategoryDTO;
import com.ftn.mdj.dto.CategoryItemDTO;
import com.ftn.mdj.utils.GenericResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ICategoryItemService {

    String USER_PREFIX = "/api/categoryItems";

    @GET(USER_PREFIX)
    Call<GenericResponse<List<CategoryItemDTO>>> getAllCategoryItems();

    @GET(USER_PREFIX + "/{categoryId}")
    Call<GenericResponse<List<CategoryItemDTO>>> getItemsForCategory(@Path("categoryId") long categoryId);
}
