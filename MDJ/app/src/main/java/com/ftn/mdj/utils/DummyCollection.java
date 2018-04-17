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
        ShoppingListDTO shoppingListDTO1 = new ShoppingListDTO("Shopping list 1");
        ShoppingListDTO shoppingListDTO2 = new ShoppingListDTO("Shopping list 2");
        ShoppingListDTO shoppingListDTO3 = new ShoppingListDTO("Shopping list 3");

        dummies.add(shoppingListDTO1);
        dummies.add(shoppingListDTO2);
        dummies.add(shoppingListDTO3);
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
