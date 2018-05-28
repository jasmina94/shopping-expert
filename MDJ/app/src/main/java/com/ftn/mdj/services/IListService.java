package com.ftn.mdj.services;

import com.ftn.mdj.utils.GenericResponse;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface IListService {

    String USER_PREFIX = "/api/lists";

    @POST(USER_PREFIX + "/create/{listName}/{userId}")
    Call<GenericResponse> create(@Path("listName") String listName, @Path("userId") Long userId);

    @GET(USER_PREFIX + "/listsByStatus/{isArchived}/{userId}")
    Call<GenericResponse> listsByStatus(@Path("isArchived") Boolean isArchived, @Path("userId") Long userId);

    @DELETE(USER_PREFIX + "/archive/{listId}")
    Call<GenericResponse> archive(@Path("listId") Long listId);

    @PUT(USER_PREFIX + "/updateName/{listId}/{listName}")
    Call<GenericResponse> updateName(@Path("listId") Long listId, @Path("listName") String listName);

    @PUT(USER_PREFIX + "/makeSecret/{listId}/{password}")
    Call<GenericResponse> makeSecret(@Path("listId") Long listId, @Path("password") String password);

    @PUT(USER_PREFIX + "/makePublic/{listId}/{password}")
    Call<GenericResponse> makePublic(@Path("listId") Long listId, @Path("password") String password);

}
