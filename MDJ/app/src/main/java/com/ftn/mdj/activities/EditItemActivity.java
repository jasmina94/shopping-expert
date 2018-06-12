package com.ftn.mdj.activities;

import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ftn.mdj.R;
import com.ftn.mdj.dto.ShoppingListItemDTO;
import com.ftn.mdj.threads.EditItemThread;
import com.ftn.mdj.threads.GetBlockedUsersThread;

public class EditItemActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private long itemId;
    private String itemName;
    private String itemCategory;
    private Button editButton;
    private EditText name;
    private TextView category;
    private EditText price;
    private EditText quantity;
    private EditText note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            itemId = extras.getLong("ITEM_ID");
            itemName =  extras.getString("ITEM_NAME");
            itemCategory = extras.getString("ITEM_CATEGORY");
        }
        Toast.makeText(EditItemActivity.this,
                "EditItemActivity :"+itemId+" "+itemName+" "+itemCategory,
                Toast.LENGTH_SHORT).show();
        name = (EditText)findViewById(R.id.edit_item_name);
        name.setText(itemName);

        if(!itemCategory.equalsIgnoreCase("Other")){
            name.setEnabled(false);
        }

        category = findViewById(R.id.edit_category_name);
        category.setText(itemCategory);

        price = findViewById(R.id.edit_price);
        quantity = findViewById(R.id.edit_quantity);
        note = findViewById(R.id.edit_note);

        editButton = findViewById(R.id.edit_item_btn);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String iname = name.getText().toString();
                String inote = note.getText().toString();
                Double iprice = 0.0;

                if(!price.getText().toString().equals("")){
                    iprice = Double.parseDouble(price.getText().toString());
                }

                Integer iquantity = 1;
                if(!quantity.getText().toString().equals("")){
                    iquantity = Integer.parseInt(quantity.getText().toString());
                }

                ShoppingListItemDTO dto = new ShoppingListItemDTO();
                dto.setId(itemId);
                dto.setCategoryItemName(iname);
                dto.setPrice(iprice);
                dto.setNote(inote);
                dto.setQuantity(iquantity);

                EditItemThread editItemThread = new EditItemThread(dto);
                editItemThread.start();
                Message msg = Message.obtain();
                editItemThread.getHandler().sendMessage(msg);
            }
        });


        mToolbar = (Toolbar) findViewById(R.id.toolbar_edit_item);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id==android.R.id.home){
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
