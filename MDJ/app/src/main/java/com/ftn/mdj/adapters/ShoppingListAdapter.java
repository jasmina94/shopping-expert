package com.ftn.mdj.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.ftn.mdj.R;
import com.ftn.mdj.activities.EditItemActivity;
import com.ftn.mdj.activities.ShoppingListActivity;
import com.ftn.mdj.activities.AddItemActivity;
import com.ftn.mdj.activities.ShoppingListActivity;
import com.ftn.mdj.dto.ShoppingListItemDTO;
import com.ftn.mdj.threads.BuyItemThread;
import com.ftn.mdj.utils.GenericResponse;
import com.ftn.mdj.utils.SharedPreferencesManager;
import com.ftn.mdj.utils.UtilHelper;

import java.util.List;


public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ViewHolder> {

    private List<ShoppingListItemDTO> shoppingListItemDTOS;
    private Context context;
    private SharedPreferencesManager sharedPreferenceManager;
    private Handler handler;

    public ShoppingListAdapter(List<ShoppingListItemDTO> shoppingListItemDTOS, Context context){
        this.shoppingListItemDTOS = shoppingListItemDTOS;
        this.context = context;
        this.sharedPreferenceManager = SharedPreferencesManager.getInstance(context);
        setHandler();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_in_list, parent, false);
        view.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View arg0) {
                Intent i = new Intent(context, EditItemActivity.class);
                CheckBox shopping_list_item = arg0.findViewById(R.id.shopping_list_item);
                TextView shopping_list_item_id = arg0.findViewById(R.id.shopping_list_item_id);
                TextView shopping_list_item_category = arg0.findViewById(R.id.shopping_list_item_category);
                String itemName = shopping_list_item.getText().toString();
                long itemId = Long.parseLong(shopping_list_item_id.getText().toString());
                String itemCategory = shopping_list_item_category.getText().toString();
                i.putExtra("ITEM_NAME",itemName);
                i.putExtra("ITEM_ID",itemId);
                i.putExtra("ITEM_CATEGORY",itemCategory);
                context.startActivity(i);
                return false;
            }
        });
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ShoppingListItemDTO shoppingListItemDTO = shoppingListItemDTOS.get(position);
        holder.checkBox_item.setText(shoppingListItemDTO.getCategoryItemName());
        if(shoppingListItemDTO.getIsPurchased()){
            holder.checkBox_item.setChecked(true);
        }else {
            holder.checkBox_item.setChecked(false);
        }


        holder.checkBox_item.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked )
                {
                    long id = Long.parseLong(holder.textView_item_id.getText().toString());
                    callBuyThread(id);
                }

            }
        });
        holder.textView_item_id.setText(""+shoppingListItemDTO.getId());
        holder.textView_item_category.setText(shoppingListItemDTO.getCategoryName());
    }

    @Override
    public int getItemCount() {
        return shoppingListItemDTOS.size();
    }

    private void setHandler(){
        handler = new android.os.Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                GenericResponse<Boolean> response = (GenericResponse<Boolean>) msg.obj;
                if (response.isSuccessfulOperation()) {
                    System.out.println("OK");
                }else {
                    UtilHelper.showToastMessage(context, response.getErrorMessage(), UtilHelper.ToastLength.SHORT);
                }
            }
        };
    }
    private void callBuyThread(long itemId){
        BuyItemThread buyItemThread = new BuyItemThread(handler, itemId);
        buyItemThread.start();
        Message msg = Message.obtain();
        buyItemThread.getHandler().sendMessage(msg);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private CheckBox checkBox_item;
        private TextView textView_item_id;
        private TextView textView_item_category;

        public ViewHolder(View itemView) {
            super(itemView);
            checkBox_item = (CheckBox)itemView.findViewById(R.id.shopping_list_item);
            textView_item_id = (TextView)itemView.findViewById(R.id.shopping_list_item_id);
            textView_item_category = (TextView)itemView.findViewById(R.id.shopping_list_item_category);
        }
    }
}
