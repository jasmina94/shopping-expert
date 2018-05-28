package com.ftn.mdj.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.ftn.mdj.R;
import com.ftn.mdj.activities.MapsActivity;
import com.ftn.mdj.dto.ShoppingListShowDTO;
import com.ftn.mdj.fragments.MainFragment;
import com.ftn.mdj.threads.WorkerThreadArchiveList;
import com.ftn.mdj.threads.WorkerThreadRenameList;
import com.ftn.mdj.threads.WorkerThreadSecretList;

import java.util.List;

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
            String secret = shoppingList.getIsSecret() ? "Make public" : "Make secret";
            popupMenu.getMenu().getItem(4).setTitle(secret);
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                switch (menuItem.getItemId()){
                    case R.id.mnu_rename:
                        AlertDialog.Builder renameBuilder = new AlertDialog.Builder(context);
                        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                        View dialogView = inflater.inflate( R.layout.dialog_one_field, null );
                        Button dismiss = dialogView.findViewById( R.id.dialog_dismiss );
                        TextView dialogTitle = dialogView.findViewById(R.id.dialog_title);
                        dialogTitle.setText("Rename list");
                        Button rename = dialogView.findViewById( R.id.dialog_submit );
                        rename.setText("Rename");
                        EditText editName = dialogView.findViewById(R.id.dialog_text);
                        editName.setText(shoppingList.getListName());
                        renameBuilder.setView(dialogView);
                        AlertDialog renameAlertDialog = renameBuilder.create();
                        renameAlertDialog.setCanceledOnTouchOutside(false);

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
                        WorkerThreadArchiveList workerThreadArchiveList = new WorkerThreadArchiveList(shoppingList.getId(), mainFragment, context);
                        workerThreadArchiveList.start();
                        Message msg = Message.obtain();
                        workerThreadArchiveList.getHandler().sendMessage(msg);
                        break;
                    case R.id.mnu_share:
                        Toast.makeText(context, "Shared", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.mnu_copy:
                        Toast.makeText(context, "Copied", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.mnu_secret:
                        AlertDialog.Builder secretBuilder = new AlertDialog.Builder(context);
                        LayoutInflater inflaterSecret = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                        View dialogSecretView = inflaterSecret.inflate( R.layout.dialog_one_field, null );
                        Button dismissSecret = dialogSecretView.findViewById( R.id.dialog_dismiss );
                        TextView dialogSecretTitle = dialogSecretView.findViewById(R.id.dialog_title);
                        dialogSecretTitle.setText("Secret password");
                        Button submitPass = dialogSecretView.findViewById( R.id.dialog_submit );
                        submitPass.setText("Submit");
                        EditText password = dialogSecretView.findViewById(R.id.dialog_text);
                        password.setHint("Your password here");
                        secretBuilder.setView(dialogSecretView);
                        AlertDialog passwordAlertDialog = secretBuilder.create();
                        passwordAlertDialog.setCanceledOnTouchOutside(false);

                        submitPass.setOnClickListener(view1 -> {
                            if(password.getText().toString().isEmpty()) {
                                Toast.makeText(context, "Password can not be empty!", Toast.LENGTH_SHORT).show();
                            } else {
                                WorkerThreadSecretList workerThreadRenameList = new WorkerThreadSecretList(shoppingList.getId(), password.getText().toString(),!shoppingList.getIsSecret(), context, mainFragment);
                                workerThreadRenameList.start();
                                Message msgSecret = Message.obtain();
                                workerThreadRenameList.getHandler().sendMessage(msgSecret);
                                passwordAlertDialog.cancel();
                            }
                        });
                        dismissSecret.setOnClickListener(view12 -> {
                            passwordAlertDialog.cancel();
                        });
                        passwordAlertDialog.show();
                        break;
                    case R.id.mnu_location:
                        Intent intent = new Intent(context, MapsActivity.class);
                        context.startActivity(intent);
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

}
