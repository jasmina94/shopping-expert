package com.ftn.mdj.services;

import com.ftn.mdj.utils.GenericResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface IListService {

    String USER_PREFIX = "/api/lists";

    @POST(USER_PREFIX + "/create/{listName}")
    Call<GenericResponse> create(@Path("listName") String listName);

    @GET(USER_PREFIX + "/listsByStatus/{isArchived}")
    Call<GenericResponse> listsByStatus(@Path("isArchived") Boolean isArchived);

}
