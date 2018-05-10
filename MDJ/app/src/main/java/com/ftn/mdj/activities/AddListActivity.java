package com.ftn.mdj.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ftn.mdj.R;
import com.ftn.mdj.dto.ShoppingListDTO;
import com.ftn.mdj.utils.DummyCollection;

import java.io.Serializable;

public class AddListActivity extends AppCompatActivity {

    private Button btn_create_list;
    private Button btn_dismiss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list);

        btn_create_list = (Button) findViewById(R.id.new_list_btn);
        btn_create_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, MainActivity.class);

                String name = ((TextView) findViewById(R.id.new_list_name)).getText().toString().trim();
                ShoppingListDTO shoppingListDTO = new ShoppingListDTO(name);

                intent.putExtra("newList", shoppingListDTO);
                context.startActivity(intent);
            }
        });

        btn_dismiss = (Button) findViewById(R.id.btn_dismiss_create);
        btn_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
