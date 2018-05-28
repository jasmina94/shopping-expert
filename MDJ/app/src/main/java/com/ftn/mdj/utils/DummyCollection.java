package com.ftn.mdj.utils;

import android.content.Context;

import com.ftn.mdj.dto.ShoppingListShowDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jasmina on 17/04/2018.
 */

public class DummyCollection {

    private static final String SHOPPING_LIST_FILE = "shopping_lists.txt";
    private static List<ShoppingListShowDTO> dummies;

    public DummyCollection(){
        dummies = new ArrayList<>();
        initialize();
    }

    public void initialize(){
        for(int i=0; i<10; i++){
            ShoppingListShowDTO shoppingList = new ShoppingListShowDTO("List " + i);
            dummies.add(shoppingList);
        }
    }

    public void addNewShoppingList(ShoppingListShowDTO shoppingListDTO){
        dummies.add(shoppingListDTO);
    }

    public List<ShoppingListShowDTO> getDummies(){
        return dummies;
    }

    public void setDummies(List<ShoppingListShowDTO> dummies){
        this.dummies = dummies;
    }

    public void writeLists(List<ShoppingListShowDTO> list, Context context) {
        String json = new Gson().toJson(list);
        try {
            FileOutputStream fos = context.openFileOutput(SHOPPING_LIST_FILE, context.MODE_PRIVATE);
            fos.write(json.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<ShoppingListShowDTO> readLists(Context context) {
        String text = "";
        List<ShoppingListShowDTO> shoppingLists = new ArrayList<>();
        try {
            FileInputStream fis = context.openFileInput(SHOPPING_LIST_FILE);
            int size = fis.available();
            byte[] buffer = new byte[size];
            fis.read(buffer);
            fis.close();
            text = new String(buffer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!text.isEmpty()) {
            shoppingLists = new Gson().fromJson(text, new TypeToken<List<ShoppingListShowDTO>>() {
            }.getType());
        }
        return shoppingLists;
    }
}
