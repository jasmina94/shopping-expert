package com.ftn.mdj.services;

import com.ftn.mdj.dto.ShoppingListDTO;
import com.ftn.mdj.utils.GenericResponse;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface IListService {

    String USER_PREFIX = "/api/lists";

    @POST(USER_PREFIX + "/create/{listName}/{userId}")
    Call<GenericResponse<ShoppingListDTO>> create(@Path("listName") String listName, @Path("userId") Long userId);

    @POST(USER_PREFIX + "/uploadList/{userId}")
    Call<GenericResponse> uploadList(@Path("userId") Long userId, @Body List<ShoppingListDTO> shoppingListDTOS);

    @GET(USER_PREFIX + "/listsByStatus/{isArchived}/{userId}")
    Call<GenericResponse<List<ShoppingListDTO>>> listsByStatus(@Path("isArchived") Boolean isArchived, @Path("userId") Long userId);

    @GET(USER_PREFIX + "/getFriendList/{listId}/{userId}")
    Call<GenericResponse<Map<String, Boolean>>> getFriendList(@Path("listId") Long listId, @Path("userId") Long userId);

    @DELETE(USER_PREFIX + "/archive/{listId}")
    Call<GenericResponse<ShoppingListDTO>> archive(@Path("listId") Long listId);

    @PUT(USER_PREFIX + "/updateName/{listId}/{listName}")
    Call<GenericResponse<Boolean>> rename(@Path("listId") Long listId, @Path("listName") String listName);

    @PUT(USER_PREFIX + "/makeSecret/{listId}/{password}")
    Call<GenericResponse<Boolean>> makeSecret(@Path("listId") Long listId, @Path("password") String password);

    @PUT(USER_PREFIX + "/makePublic/{listId}")
    Call<GenericResponse<Boolean>> makePublic(@Path("listId") Long listId);

    @PUT(USER_PREFIX + "/updateLocation/{listId}/{latitude}/{longitude}")
    Call<GenericResponse<Boolean>> updateLocation(@Path("listId") Long listId, @Path("latitude") Double latitude, @Path("longitude") Double longitude);

    @PUT(USER_PREFIX + "/restore/{listId}")
    Call<GenericResponse<Boolean>> restore(@Path("listId") Long listId);

    @PUT(USER_PREFIX + "/shareList/{listId}/{sharedWith}")
    Call<GenericResponse<List<String>>> shareList(@Path("listId") Long listId, @Path("sharedWith") String sharedWith);

    @PUT(USER_PREFIX + "/unShareList/{listId}/{sharedWith}")
    Call<GenericResponse<Boolean>> unShareList(@Path("listId") Long listId, @Path("sharedWith") String sharedWith);

    @DELETE(USER_PREFIX + "/deleteList/{listId}")
    Call<GenericResponse<Boolean>> deleteList(@Path("listId") Long listId);

    @PUT(USER_PREFIX + "/addReminder/{listId}")
    Call<GenericResponse<Boolean>> addReminder(@Path("listId") Long listId, @Body Map<String, String> timeAndDate);

    @PUT(USER_PREFIX + "/removeReminder/{listId}")
    Call<GenericResponse<Boolean>> removeReminder(@Path("listId") Long listId);

}
