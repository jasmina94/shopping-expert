package com.ftn.mdj.adapters;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.ftn.mdj.R;
import com.ftn.mdj.activities.AddReminder;
import com.ftn.mdj.activities.MainActivity;
import com.ftn.mdj.activities.MapsActivity;
import com.ftn.mdj.activities.ShareListActivity;
import com.ftn.mdj.activities.ShoppingListActivity;
import com.ftn.mdj.dto.ShoppingListDTO;
import com.ftn.mdj.fragments.MainFragment;
import com.ftn.mdj.reminder.Reminder;
import com.ftn.mdj.threads.ArchiveListThread;
import com.ftn.mdj.threads.RenameListThread;
import com.ftn.mdj.threads.SecretListThread;
import com.ftn.mdj.utils.DummyCollection;
import com.ftn.mdj.utils.SharedPreferencesManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Created by Jasmina on 06/05/2018.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    List<ShoppingListDTO> activeShoppingLists;
    private Context context;
    private MainFragment mainFragment;

    private Handler secretHandler;

    private AlertDialog renameAlertDialog;
    private AlertDialog secretAlertDialog;
    private SharedPreferencesManager sharedPreferenceManager;
    private Boolean isUserLogedIn;

    private Calendar myCalendar = Calendar.getInstance();
    private String myFormat = "MM/dd/yy";
    private SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);


    public MainAdapter(List<ShoppingListDTO> activeShoppingLists, Context context, MainFragment mainFragment) {
        this.activeShoppingLists = activeShoppingLists;
        this.context = context;
        this.mainFragment = mainFragment;
        sharedPreferenceManager = SharedPreferencesManager.getInstance(context);
        this.isUserLogedIn = sharedPreferenceManager.getInt(SharedPreferencesManager.Key.USER_ID.name()) != 0;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        view.setOnClickListener(view1 -> context.startActivity(new Intent(context, ShoppingListActivity.class)));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ShoppingListDTO shoppingListDTO = activeShoppingLists.get(position);
        boolean sharedList = isUserLogedIn && !sharedPreferenceManager.getString(SharedPreferencesManager.Key.USER_EMAIL.name()).equals(shoppingListDTO.getCreatorEmail());
        holder.txt_name.setText(shoppingListDTO.getListName());
        holder.txt_status.setText(shoppingListDTO.getBoughtItems() + "/" + shoppingListDTO.getNumberOfItems());
        holder.img_locker.setVisibility(shoppingListDTO.getIsSecret() ? View.VISIBLE : View.INVISIBLE);
        if(isUserLogedIn && sharedList) {
            holder.txt_creatorEmail.setVisibility(View.VISIBLE);
            holder.txt_creatorEmail.setText(shoppingListDTO.getCreatorEmail());
        }
        if(shoppingListDTO.getDate() != null) {
            setReminder(shoppingListDTO.getDate(), shoppingListDTO.getTime());
        }
        holder.txt_option_mnu.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(context, holder.txt_option_mnu);
            popupMenu.inflate(R.menu.option_menu);
            String secret = shoppingListDTO.getIsSecret() ? "Make public" : "Make secret";
            if(sharedList) {
                popupMenu.getMenu().getItem(0).setVisible(false);
                popupMenu.getMenu().getItem(1).setVisible(false);
                popupMenu.getMenu().getItem(2).setVisible(false);
                popupMenu.getMenu().getItem(3).setVisible(false);
            }
            if(!isUserLogedIn) {
                popupMenu.getMenu().getItem(2).setVisible(false);
                popupMenu.getMenu().getItem(4).setVisible(false);
                popupMenu.getMenu().getItem(5).setVisible(false);
            }

            popupMenu.getMenu().getItem(3).setTitle(secret);
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                switch (menuItem.getItemId()){
                    case R.id.mnu_rename:
                        renameList(shoppingListDTO);
                        break;
                    case R.id.mnu_archive:
                        archiveList(shoppingListDTO);
                        break;
                    case R.id.mnu_share:
                        shareList(shoppingListDTO.getId());
                        break;
                    case R.id.mnu_secret:
                        changeListPrivacy(shoppingListDTO);
                        break;
                    case R.id.mnu_location:
                        addLocation(shoppingListDTO);
                        break;
                    case R.id.mnu_reminder:
                        reminder(shoppingListDTO);
                        break;
                }
                return false;
            });
            popupMenu.show();
        });
    }

    private void setReminder(String date, String time) {


        Date dateFromString = null;
        try {
            dateFromString = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        myCalendar.setTime(dateFromString);

        int hour = Integer.parseInt(time.split(":")[0]);
        int minute = Integer.parseInt(time.split(":")[1]);
        myCalendar.set(Calendar.HOUR_OF_DAY, hour);
        myCalendar.set(Calendar.MINUTE, minute);

        setAlarm(myCalendar.getTimeInMillis(), date);
    }

    public void setAlarm(long time, String date) {
        AlarmManager am = (AlarmManager) MainActivity.instance.getSystemService(Context.ALARM_SERVICE);

        //creating a new intent specifying the broadcast receiver
        Intent i = new Intent(MainActivity.instance, Reminder.class);

        //creating a pending intent using the intent
        PendingIntent pi = PendingIntent.getBroadcast(MainActivity.instance, 0, i, 0);

        myCalendar.set(Calendar.HOUR_OF_DAY, 0);
        myCalendar.set(Calendar.MINUTE, 0);
        myCalendar.set(Calendar.SECOND, 0);
        myCalendar.set(Calendar.MILLISECOND, 0);

        String todayString = sdf.format(myCalendar.getTime());

        if(todayString.equals(date)) {
            am.set(AlarmManager.RTC, time, pi);
        }
        Toast.makeText(MainActivity.instance, "Alarm is set", Toast.LENGTH_SHORT).show();
    }

    private void shareList(Long listId) {
        Intent intent = new Intent(context, ShareListActivity.class);
        intent.putExtra("selectedShoppingListId", listId);
        context.startActivity(intent);
    }

    private void reminder(ShoppingListDTO list) {
        Intent intent = new Intent(context, AddReminder.class);
        intent.putExtra("selectedShoppingList", list);
        context.startActivity(intent);
    }

    private void archiveList(ShoppingListDTO shoppingListDTO){
        long listId = shoppingListDTO.getId();
        if(isUserLogedIn) {
            ArchiveListThread archiveListThread = new ArchiveListThread(listId);
            archiveListThread.start();
            Message msg = Message.obtain();
            archiveListThread.getHandler().sendMessage(msg);
        } else {
            List<ShoppingListDTO> lists = DummyCollection.readLists(context);
            Optional<ShoppingListDTO> toArchive = lists.stream().filter(l -> l.getId() == shoppingListDTO.getId()).findFirst();
            lists.remove(toArchive.get());
            DummyCollection.writeLists(lists, context);
            mainFragment.setActiveShoppingLists(lists);
            mainFragment.restartFragment();
        }
    }

    private void renameList(ShoppingListDTO shoppingListDTO){
        AlertDialog.Builder renameDialogBuilder =  new AlertDialog.Builder(this.context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View dialogView = inflater.inflate( R.layout.dialog_one_field, null );
        Button dismiss = dialogView.findViewById( R.id.dialog_dismiss );
        TextView dialogTitle = dialogView.findViewById(R.id.dialog_title);
        dialogTitle.setText("Rename list");
        Button rename = dialogView.findViewById( R.id.dialog_submit );
        rename.setText("Rename");
        EditText editName = dialogView.findViewById(R.id.dialog_text);
        editName.setText(shoppingListDTO.getListName());

        renameDialogBuilder.setView(dialogView);

        renameAlertDialog = renameDialogBuilder.create();
        renameAlertDialog.setCanceledOnTouchOutside(false);

        rename.setOnClickListener(view1 -> {
            if(editName.getText().toString().isEmpty()) {
                Toast.makeText(context, "List name must not be empty!", Toast.LENGTH_SHORT).show();
            } else {
                long loggedUserId = sharedPreferenceManager.getInt(SharedPreferencesManager.Key.USER_ID.name());
                if(loggedUserId != 0) {
                    RenameListThread renameListThread = new RenameListThread(shoppingListDTO.getId(), editName.getText().toString());
                    renameListThread.start();
                    Message msg = Message.obtain();
                    renameListThread.getHandler().sendMessage(msg);
                } else {
                    List<ShoppingListDTO> lists = DummyCollection.readLists(context);
                    lists.forEach(l -> {
                        if(l.getId() == shoppingListDTO.getId()) {
                            l.setListName(editName.getText().toString());
                            return;
                        }
                    });
                    DummyCollection.writeLists(lists, context);
                    mainFragment.setActiveShoppingLists(lists);
                    mainFragment.restartFragment();
                }
                renameAlertDialog.cancel();
            }
        });
        dismiss.setOnClickListener(view12 -> {
            renameAlertDialog.cancel();
        });

        renameAlertDialog.show();
    }

    private void changeListPrivacy(ShoppingListDTO shoppingListDTO){
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

        secretAlertDialog = secretBuilder.create();
        secretAlertDialog.setCanceledOnTouchOutside(false);

        submitPass.setOnClickListener(view1 -> {
            if(password.getText().toString().isEmpty()) {
                Toast.makeText(context, "Password can not be empty!", Toast.LENGTH_SHORT).show();
            } else {
                if(isUserLogedIn) {
                    if(shoppingListDTO.getIsSecret() && !password.getText().toString().equals(shoppingListDTO.getAccessPassword())) {
                        Toast.makeText(context, "Wrong password!", Toast.LENGTH_SHORT).show();
                    } else {
                        SecretListThread workerThreadRenameList = new SecretListThread(shoppingListDTO.getId(), password.getText().toString(), !shoppingListDTO.getIsSecret());
                        workerThreadRenameList.start();
                        Message msgSecret = Message.obtain();
                        workerThreadRenameList.getHandler().sendMessage(msgSecret);
                    }
                } else {
                    if(shoppingListDTO.getIsSecret() && !password.getText().toString().equals(shoppingListDTO.getAccessPassword())) {
                        Toast.makeText(context, "Wrong password!", Toast.LENGTH_SHORT).show();
                    } else {
                        List<ShoppingListDTO> lists = DummyCollection.readLists(context);
                        lists.forEach(l -> {
                            if (l.getId() == shoppingListDTO.getId()) {
                                l.setIsSecret(!shoppingListDTO.getIsSecret());
                                l.setAccessPassword(password.getText().toString());
                                return;
                            }
                        });
                        DummyCollection.writeLists(lists, context);
                        mainFragment.setActiveShoppingLists(lists);
                        mainFragment.restartFragment();
                    }
                }
                secretAlertDialog.cancel();
            }
        });
        dismissSecret.setOnClickListener(view12 -> {
            secretAlertDialog.cancel();
        });

        secretAlertDialog.show();
    }

    public void addLocation(ShoppingListDTO shoppingListDTO) {
        Intent intent = new Intent(context, MapsActivity.class);
        if(shoppingListDTO.getLatitude() != null && shoppingListDTO.getLongitude() != null){
            intent.putExtra("latitude",shoppingListDTO.getLatitude());
            intent.putExtra("longitude",shoppingListDTO.getLongitude());
        }
        intent.putExtra("listId",shoppingListDTO.getId());
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return activeShoppingLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView txt_name;
        private TextView txt_status;
        private TextView txt_option_mnu;
        private ImageView img_locker;
        private TextView txt_creatorEmail;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_name = itemView.findViewById(R.id.txt_list_name);
            txt_status = itemView.findViewById(R.id.txt_status);
            txt_option_mnu = itemView.findViewById(R.id.txt_option_mnu);
            img_locker = itemView.findViewById(R.id.img_lock);
            txt_creatorEmail = itemView.findViewById(R.id.listOwner);
        }
    }
}
