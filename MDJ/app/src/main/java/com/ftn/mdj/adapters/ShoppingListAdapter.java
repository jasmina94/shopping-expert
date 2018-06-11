package com.ftn.mdj.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.ftn.mdj.R;
import com.ftn.mdj.dto.ShoppingListItemDTO;
import com.ftn.mdj.utils.SharedPreferencesManager;

import java.util.List;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ViewHolder> {

    private List<ShoppingListItemDTO> shoppingListItemDTOS;
    private Context context;
    private SharedPreferencesManager sharedPreferenceManager;

    public ShoppingListAdapter(List<ShoppingListItemDTO> shoppingListItemDTOS, Context context){
        this.shoppingListItemDTOS = shoppingListItemDTOS;
        this.context = context;
        this.sharedPreferenceManager = SharedPreferencesManager.getInstance(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_in_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ShoppingListItemDTO shoppingListItemDTO = shoppingListItemDTOS.get(position);
        holder.checkBox_item.setText(shoppingListItemDTO.getCategoryItemName());
    }

    @Override
    public int getItemCount() {
        return shoppingListItemDTOS.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private CheckBox checkBox_item;

        public ViewHolder(View itemView) {
            super(itemView);
            checkBox_item = (CheckBox)itemView.findViewById(R.id.shopping_list_item);
        }
    }
}
