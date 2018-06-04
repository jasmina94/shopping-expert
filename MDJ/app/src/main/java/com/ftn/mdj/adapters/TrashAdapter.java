package com.ftn.mdj.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.ftn.mdj.R;
import com.ftn.mdj.activities.ShoppingListActivity;
import com.ftn.mdj.dto.ShoppingListDTO;
import com.ftn.mdj.threads.DeleteListThread;
import com.ftn.mdj.threads.RestoreListThread;
import com.ftn.mdj.utils.SharedPreferencesManager;

import java.util.List;

public class TrashAdapter extends RecyclerView.Adapter<TrashAdapter.ViewHolder> {
    private  List<ShoppingListDTO> archivedShoppingLists;
    private Context context;

    private SharedPreferencesManager sharedPreferenceManager;
    private Boolean isUserLogedIn;

    public TrashAdapter(List<ShoppingListDTO> archivedShoppingLists, Context context) {
        this.archivedShoppingLists = archivedShoppingLists;
        this.context = context;
        sharedPreferenceManager = SharedPreferencesManager.getInstance(context);
        this.isUserLogedIn = sharedPreferenceManager.getInt(SharedPreferencesManager.Key.USER_ID.name()) != 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trash_list_item, parent, false);
        view.setOnClickListener(view1 -> context.startActivity(new Intent(context, ShoppingListActivity.class)));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ShoppingListDTO shoppingListDTO = archivedShoppingLists.get(position);

        holder.txt_name.setText(shoppingListDTO.getListName());
        holder.txt_status.setText(shoppingListDTO.getBoughtItems() + "/" + shoppingListDTO.getNumberOfItems());

        holder.txt_option_mnu.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(context, holder.txt_option_mnu);
            popupMenu.inflate(R.menu.trash_option_menu);
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                switch (menuItem.getItemId()){
                    case R.id.mnu_restore:
                        RestoreListThread restoreListThread = new RestoreListThread(shoppingListDTO.getId());
                        restoreListThread.start();
                        Message msg = Message.obtain();
                        restoreListThread.getHandler().sendMessage(msg);
                        break;
                    case R.id.mnu_delete:
                        DeleteListThread deleteListThread = new DeleteListThread(shoppingListDTO.getId());
                        deleteListThread.start();
                        Message msgg = Message.obtain();
                        deleteListThread.getHandler().sendMessage(msgg);
                        break;
                }
                return false;
            });
            popupMenu.show();
        });
    }

    @Override
    public int getItemCount() {
        return archivedShoppingLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView txt_name;
        private TextView txt_status;
        private TextView txt_option_mnu;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_name = (TextView) itemView.findViewById(R.id.txt_list_name_trash);
            txt_status = (TextView) itemView.findViewById(R.id.txt_status_trash);
            txt_option_mnu = (TextView) itemView.findViewById(R.id.txt_option_mnu_trash);
        }
    }
}
