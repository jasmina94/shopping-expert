package com.ftn.mdj.services;

import com.ftn.mdj.dto.CategoryDTO;
import com.ftn.mdj.dto.CategoryItemDTO;
import com.ftn.mdj.utils.GenericResponse;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ICategoryItemService {

    String USER_PREFIX = "/api/categoryItems";

    @GET(USER_PREFIX)
    Call<GenericResponse<List<CategoryItemDTO>>> getAllCategoryItems();

    @GET(USER_PREFIX + "/{categoryId}")
    Call<GenericResponse<List<CategoryItemDTO>>> getItemsForCategory(@Path("categoryId") long categoryId);

    @GET(USER_PREFIX + "/map")
    Call<GenericResponse<HashMap<String,  List<CategoryItemDTO>>>> getMap();

    @POST(USER_PREFIX + "/{listId}")
    Call<GenericResponse<Boolean>> addNewCategoryAndShoppingListItem(@Body CategoryItemDTO categoryItemDTO, @Path("listId") long listId);

    @POST(USER_PREFIX + "/{categoryItemId}/{listToAdd}")
    Call<GenericResponse<Boolean>> addShoppingListItem(@Path("categoryItemId") long categoryItemId, @Path("listToAdd") long listId);
}
