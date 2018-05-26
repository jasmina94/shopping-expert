package com.ftn.mdj.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.ftn.mdj.R;
import com.ftn.mdj.dto.ShoppingListShowDTO;
import com.ftn.mdj.fragments.MainFragment;
import com.ftn.mdj.threads.WorkerThreadRenameList;
import com.ftn.mdj.utils.GenericResponse;
import com.ftn.mdj.utils.ServiceUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Jasmina on 06/05/2018.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    List<ShoppingListShowDTO> activeShoppingLists;
    private Context context;
    private MainFragment mainFragment;


    public MainAdapter(List<ShoppingListShowDTO> activeShoppingLists, Context context, MainFragment mainFragment) {
        this.activeShoppingLists = activeShoppingLists;
        this.context = context;
        this.mainFragment = mainFragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ShoppingListShowDTO shoppingList = activeShoppingLists.get(position);

        holder.txt_name.setText(shoppingList.getListName());
        holder.txt_status.setText(shoppingList.getBoughtItems() + "/" + shoppingList.getNumberOfItems());
        holder.txt_option_mnu.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(context, holder.txt_option_mnu);
            popupMenu.inflate(R.menu.option_menu);
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                switch (menuItem.getItemId()){
                    case R.id.mnu_rename:
                        AlertDialog.Builder renameBuilder = new AlertDialog.Builder(context);
                        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                        View dialogView = inflater.inflate( R.layout.dialog_rename_list, null );
                        Button dismiss = dialogView.findViewById( R.id.btn_dismiss_rename );
                        Button rename = dialogView.findViewById( R.id.btn_rename );
                        EditText editName = dialogView.findViewById(R.id.rename_text);
                        editName.setHint(shoppingList.getListName());
                        renameBuilder.setView(dialogView);
                        AlertDialog renameAlertDialog = renameBuilder.create();
                        rename.setOnClickListener(view1 -> {
                            if(editName.getText().toString().isEmpty()) {
                                Toast.makeText(context, "List name must not be empty!", Toast.LENGTH_SHORT).show();
                            } else {
                                WorkerThreadRenameList workerThreadRenameList = new WorkerThreadRenameList(shoppingList.getId(), editName.getText().toString(), context, mainFragment);
                                workerThreadRenameList.start();
                                Message msg = Message.obtain();
                                workerThreadRenameList.getHandler().sendMessage(msg);
                                renameAlertDialog.cancel();
                            }
                        });
                        dismiss.setOnClickListener(view12 -> {
                            renameAlertDialog.cancel();
                        });
                        renameAlertDialog.show();
                        break;
                    case R.id.mnu_archive:
                        WorkerThread workerThread = new WorkerThread(shoppingList.getId());
                        workerThread.start();
                        Message msg = Message.obtain();
                        workerThread.handler.sendMessage(msg);
                        break;
                    case R.id.mnu_share:
                        Toast.makeText(context, "Shared", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.mnu_copy:
                        Toast.makeText(context, "Copied", Toast.LENGTH_LONG).show();
                        break;
                }
                return false;
            });
            popupMenu.show();
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

    private class WorkerThread extends Thread{
        private Handler handler;

        public WorkerThread(final Long shoppingListId){
            handler = new Handler(){

                @Override
                public void handleMessage(Message msg) {
                    ServiceUtils.listService.archive(shoppingListId).enqueue(new retrofit2.Callback<GenericResponse>(){

                        @Override
                        public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                            mainFragment.archiveListUI(shoppingListId);
//                            notifyDataSetChanged();

                            Toast.makeText(context, "Archived", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(Call<GenericResponse> call, Throwable t) {
                            Toast.makeText(context, "Error while archiving", Toast.LENGTH_LONG).show();

                            System.out.println("Error sending registration data!");
                        }
                    });
                    super.handleMessage(msg);
                }

            };
        }
    }
}
