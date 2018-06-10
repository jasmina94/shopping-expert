package com.ftn.mdj.adapters;

import android.content.Context;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ftn.mdj.R;
import com.ftn.mdj.activities.ShareListActivity;
import com.ftn.mdj.threads.ShareListThread;
import com.ftn.mdj.threads.UnShareListThread;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShareListAdapter extends RecyclerView.Adapter<ShareListAdapter.ViewHolder> {
    private Map<String, Boolean> friendList;
    private List<String> keys = new ArrayList<>();
    private Context context;

    public ShareListAdapter(Map<String, Boolean> friendList, Context context) {
        this.friendList = friendList;
        this.keys.addAll(friendList.keySet());
        this.context = context;
    }

    @Override
    public ShareListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friend_list_item, parent, false);
        return new ShareListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ShareListAdapter.ViewHolder holder, int position) {
        String key = keys.get(position);
        Boolean value = friendList.get(key);

        holder.txt_friend.setText(key);

        holder.btn_share_list.setVisibility(value ? View.INVISIBLE : View.VISIBLE);
        holder.btn_unshare_list.setVisibility(!value ? View.INVISIBLE : View.VISIBLE);

        holder.btn_share_list.setOnClickListener(view -> {
            ShareListThread shareListThread = new ShareListThread(ShareListActivity.instance.getShoppingListId(), key);
            shareListThread.start();
            Message msg = Message.obtain();
            shareListThread.getHandler().sendMessage(msg);
        });

        holder.btn_unshare_list.setOnClickListener(view -> {
            UnShareListThread unShareListThread = new UnShareListThread(ShareListActivity.instance.getShoppingListId(), key);
            unShareListThread.start();
            Message msg = Message.obtain();
            unShareListThread.getHandler().sendMessage(msg);
        });

    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView txt_friend;
        private FloatingActionButton btn_share_list;
        private FloatingActionButton btn_unshare_list;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_friend = itemView.findViewById(R.id.txt_friend);
            btn_share_list = itemView.findViewById(R.id.btn_share_list);
            btn_unshare_list = itemView.findViewById(R.id.btn_unshare_list);
        }
    }
}
