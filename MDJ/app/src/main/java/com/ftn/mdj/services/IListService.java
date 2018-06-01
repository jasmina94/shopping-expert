package com.ftn.mdj.services;

import com.ftn.mdj.dto.ShoppingListDTO;
import com.ftn.mdj.utils.GenericResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface IListService {

    String USER_PREFIX = "/api/lists";

    @POST(USER_PREFIX + "/create/{listName}/{userId}")
    Call<GenericResponse<ShoppingListDTO>> create(@Path("listName") String listName, @Path("userId") Long userId);

    @POST(USER_PREFIX + "/uploadList/{userId}")
    Call<GenericResponse> uploadList(@Path("userId") Long userId, @Body List<ShoppingListDTO> shoppingListDTOS);

    @GET(USER_PREFIX + "/listsByStatus/{isArchived}/{userId}")
    Call<GenericResponse<List<ShoppingListDTO>>> listsByStatus(@Path("isArchived") Boolean isArchived, @Path("userId") Long userId);

    @DELETE(USER_PREFIX + "/archive/{listId}")
    Call<GenericResponse<ShoppingListDTO>> archive(@Path("listId") Long listId);

    @PUT(USER_PREFIX + "/updateName/{listId}/{listName}")
    Call<GenericResponse<Boolean>> rename(@Path("listId") Long listId, @Path("listName") String listName);

    @PUT(USER_PREFIX + "/makeSecret/{listId}/{password}")
    Call<GenericResponse<Boolean>> makeSecret(@Path("listId") Long listId, @Path("password") String password);

    @PUT(USER_PREFIX + "/makePublic/{listId}/{password}")
    Call<GenericResponse<Boolean>> makePublic(@Path("listId") Long listId, @Path("password") String password);

    @PUT(USER_PREFIX + "/updateLocation/{listId}/{latitude}/{longitude}")
    Call<GenericResponse> updateLocation(@Path("listId") Long listId, @Path("latitude") Double latitude, @Path("longitude") Double longitude);

}
