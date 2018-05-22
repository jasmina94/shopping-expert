package com.ftn.mdj.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.ftn.mdj.R;
import com.ftn.mdj.dto.ShoppingListShowDTO;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jasmina on 06/05/2018.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    List<ShoppingListShowDTO> activeShoppingLists;
    private Context context;

    public MainAdapter(List<ShoppingListShowDTO> activeShoppingLists, Context context) {
        this.activeShoppingLists = activeShoppingLists;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Type listType = new TypeToken<ArrayList<ShoppingListShowDTO>>() {
        }.getType();
        List<ShoppingListShowDTO> keyPairBoolDataList = new Gson().fromJson(String.valueOf(activeShoppingLists), listType);
        ShoppingListShowDTO shoppingList = keyPairBoolDataList.get(position);

        holder.txt_name.setText(shoppingList.getListName());
        holder.txt_status.setText("0/10");
        holder.txt_option_mnu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, holder.txt_option_mnu);
                popupMenu.inflate(R.menu.option_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.mnu_rename:
                                Toast.makeText(context, "Renamed", Toast.LENGTH_LONG).show();
                                break;
                            case R.id.mnu_delete:
                                activeShoppingLists.remove(position);
                                notifyDataSetChanged();
                                Toast.makeText(context, "Deleted", Toast.LENGTH_LONG).show();
                                break;
                            case R.id.mnu_share:
                                Toast.makeText(context, "Shared", Toast.LENGTH_LONG).show();
                                break;
                            case R.id.mnu_copy:
                                Toast.makeText(context, "Copied", Toast.LENGTH_LONG).show();
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return activeShoppingLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView txt_name;
        private TextView txt_status;
        private TextView txt_option_mnu;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_name = (TextView) itemView.findViewById(R.id.txt_list_name);
            txt_status = (TextView) itemView.findViewById(R.id.txt_status);
            txt_option_mnu = (TextView) itemView.findViewById(R.id.txt_option_mnu);
        }
    }
}
