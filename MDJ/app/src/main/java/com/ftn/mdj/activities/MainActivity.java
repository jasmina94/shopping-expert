package com.ftn.mdj.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ftn.mdj.R;
import com.ftn.mdj.dto.ShoppingListDTO;
import com.ftn.mdj.utils.DummyCollection;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ShoppingListAdapter shoppingListAdapter = new ShoppingListAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView)findViewById(R.id.active_shopping_lists);
        listView.setAdapter(shoppingListAdapter);

        System.out.println(shoppingListAdapter.getCount());

        Intent intent = getIntent();
        if(intent.hasExtra("newList")){
            ShoppingListDTO shoppingListDTO = (ShoppingListDTO) intent.getSerializableExtra("newList");
            shoppingListAdapter.dummies.add(shoppingListDTO);
        }

        FloatingActionButton floatingActionButton = (FloatingActionButton)findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MainActivity.this, AddListActivity.class);
                startActivity(in);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_trash){
            System.out.println("Trash clicked");
            return true;
        }else if(item.getItemId() == R.id.action_logout){
            System.out.println("Logout clicked");
            return true;
        }else if(item.getItemId() == R.id.action_settings){
            System.out.println("Settings clicked");
            return true;
        }else {
            return false;
        }
    }

    private class ShoppingListAdapter extends BaseAdapter {

        private List<ShoppingListDTO> dummies;

        public ShoppingListAdapter(){
            dummies = new ArrayList<ShoppingListDTO>();
            initialize();
        }

        @Override
        public int getCount() {
            return dummies.size();
        }

        @Override
        public Object getItem(int i) {
            return dummies.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.custom_layout, null);
            TextView textViewListName = (TextView)view.findViewById(R.id.shopping_list_name);

            ShoppingListDTO shoppingListDTO = dummies.get(i);

            textViewListName.setText(shoppingListDTO.getName());

            return view;
        }

        private void initialize(){
            ShoppingListDTO shoppingListDTO1 = new ShoppingListDTO("Shopping list 1");
            ShoppingListDTO shoppingListDTO2 = new ShoppingListDTO("Shopping list 2");
            ShoppingListDTO shoppingListDTO3 = new ShoppingListDTO("Shopping list 3");

            dummies.add(shoppingListDTO1);
            dummies.add(shoppingListDTO2);
            dummies.add(shoppingListDTO3);
        }
    }
}
