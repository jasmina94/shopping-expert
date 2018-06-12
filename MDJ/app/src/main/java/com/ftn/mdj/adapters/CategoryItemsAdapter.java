package com.ftn.mdj.adapters;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ftn.mdj.R;
import com.ftn.mdj.dto.CategoryItemDTO;
import com.ftn.mdj.fragments.ItemsFragment;
import com.ftn.mdj.threads.AddCategoryItemAsShoppingListItemThread;
import com.ftn.mdj.utils.GenericResponse;
import com.ftn.mdj.utils.UtilHelper;

import java.util.ArrayList;
import java.util.List;

public class CategoryItemsAdapter extends RecyclerView.Adapter<CategoryItemsAdapter.ViewHolder>{

    private List<CategoryItemDTO> categoryItemDTOS = new ArrayList<>();
    private Context context;
    private long listId;

    private Handler handler;

    public CategoryItemsAdapter(Context context, long listId){
        this.context = context;
        this.listId = listId;
        setHandler();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item_view_first, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        final CategoryItemDTO categoryItemDTO = categoryItemDTOS.get(position);
        holder.category_item.setText(categoryItemDTO.getItemName());
        holder.category_item_id.setText(String.valueOf(categoryItemDTO.getId()));

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView itemIdTxt = view.findViewById(R.id.txt_list_category_item_id_first);
                long id = Long.parseLong(itemIdTxt.getText().toString());
                callAddCategoryItemAsItem(id);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryItemDTOS.size();
    }

    public List<CategoryItemDTO> getCategoryItemDTOS() {
        return categoryItemDTOS;
    }

    public void setCategoryItemDTOS(List<CategoryItemDTO> categoryItemDTOS) {
        this.categoryItemDTOS = categoryItemDTOS;
    }

    private void setHandler(){
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                GenericResponse<Boolean> response = (GenericResponse<Boolean>) msg.obj;
                if (response.isSuccessfulOperation()) {
                    ItemsFragment.instance.getActivity().onBackPressed();
                }else {
                    UtilHelper.showToastMessage(context, response.getErrorMessage(), UtilHelper.ToastLength.LONG);
                }
            }
        };
    }

    private void callAddCategoryItemAsItem(long categoryItemId){
        AddCategoryItemAsShoppingListItemThread thread = new AddCategoryItemAsShoppingListItemThread(handler, categoryItemId, listId);
        thread.start();
        Message msg = Message.obtain();
        thread.getHandler().sendMessage(msg);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView category_item;
        private TextView category_item_id;
        private CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView)itemView.findViewById(R.id.card_view_list_category);
            category_item = (TextView) itemView.findViewById(R.id.txt_list_category_item_name_first);
            category_item_id = (TextView)itemView.findViewById(R.id.txt_list_category_item_id_first);
        }
    }
}
