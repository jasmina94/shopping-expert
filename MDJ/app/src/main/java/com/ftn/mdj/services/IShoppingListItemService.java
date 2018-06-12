package com.ftn.mdj.services;

import com.ftn.mdj.dto.ShoppingListItemDTO;
import com.ftn.mdj.utils.GenericResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IShoppingListItemService {

    String LIST_ITEMS_PREFIX = "/api/listItems";

    @GET(LIST_ITEMS_PREFIX + "/{listId}")
    Call<GenericResponse<List<ShoppingListItemDTO>>> getItemsForList(@Path("listId") long listId);

    @POST(LIST_ITEMS_PREFIX + "/{listId}")
    Call<GenericResponse<Boolean>> addItemToList(@Path("listId") long listId, @Body ShoppingListItemDTO registrationDTO);

    @POST(LIST_ITEMS_PREFIX + "/buy/{shoppingListItemId}")
    Call<GenericResponse<Boolean>> buy(@Path("shoppingListItemId") long shoppingListItemId);
}
