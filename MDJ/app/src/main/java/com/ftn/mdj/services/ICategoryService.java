package com.ftn.mdj.services;

import com.ftn.mdj.dto.CategoryDTO;
import com.ftn.mdj.dto.UserDTO;
import com.ftn.mdj.utils.GenericResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ICategoryService {

    String USER_PREFIX = "/api/categories";

    @GET(USER_PREFIX)
    Call<GenericResponse<List<CategoryDTO>>> getCategories();
}
