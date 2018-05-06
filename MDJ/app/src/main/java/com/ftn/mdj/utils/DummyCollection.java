package com.ftn.mdj.utils;

import com.ftn.mdj.dto.ShoppingListDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jasmina on 17/04/2018.
 */

public class DummyCollection {

    private static List<ShoppingListDTO> dummies;

    public DummyCollection(){
        dummies = new ArrayList<>();
        initialize();
    }

    public void initialize(){
        for(int i=0; i<10; i++){
            ShoppingListDTO shoppingList = new ShoppingListDTO("List " + i);
            dummies.add(shoppingList);
        }
    }

    public void addNewShoppingList(ShoppingListDTO shoppingListDTO){
        dummies.add(shoppingListDTO);
    }

    public List<ShoppingListDTO> getDummies(){
        return dummies;
    }

    public void setDummies(List<ShoppingListDTO> dummies){
        this.dummies = dummies;
    }
}
