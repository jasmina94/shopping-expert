package com.ftn.mdj.activities;

import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;


import com.ftn.mdj.R;
import com.ftn.mdj.adapters.MainAdapter;
import com.ftn.mdj.dto.ShoppingListDTO;
import com.ftn.mdj.utils.DummyCollection;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String SHOPPING_LIST_FILE = "shopping_lists.txt";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private FloatingActionButton btn_add_list;

    private List<ShoppingListDTO> activeShoppingLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true); //svaki item ima fiksnu velicinu
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DummyCollection dummyCollection = new DummyCollection();
        write_lists(dummyCollection.getDummies());
        activeShoppingLists = read_lists();

        adapter = new MainAdapter(activeShoppingLists, this);
        recyclerView.setAdapter(adapter);

        btn_add_list = (FloatingActionButton)findViewById(R.id.btn_add_shopping_list);
        btn_add_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, AddListActivity.class);
                startActivity(intent);
            }
        });
    }

    public void write_lists(List<ShoppingListDTO> list){
        String json = new Gson().toJson(list);
        try {
            FileOutputStream fos = openFileOutput(SHOPPING_LIST_FILE, Context.MODE_PRIVATE);
            fos.write(json.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<ShoppingListDTO> read_lists(){
        String text = "";
        List<ShoppingListDTO> shoppingLists = new ArrayList<>();
        try {
            FileInputStream fis = openFileInput(SHOPPING_LIST_FILE);
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
        if(!text.isEmpty()){
            shoppingLists = new Gson().fromJson(text, new TypeToken<List<ShoppingListDTO>>(){}.getType());
        }
        return shoppingLists;
    }
}
