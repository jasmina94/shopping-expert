package com.ftn.mdj.utils;

import com.ftn.mdj.dto.ShoppingListShowDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jasmina on 17/04/2018.
 */

public class DummyCollection {

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

    public void addNewShoppingList(ShoppingListShowDTO shoppingListShowDTO){
        dummies.add(shoppingListShowDTO);
    }

    public List<ShoppingListShowDTO> getDummies(){
        return dummies;
    }

    public void setDummies(List<ShoppingListShowDTO> dummies){
        this.dummies = dummies;
    }
}
