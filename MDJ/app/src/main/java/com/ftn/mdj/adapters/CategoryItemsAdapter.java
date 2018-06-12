package com.ftn.mdj.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ftn.mdj.R;
import com.ftn.mdj.dto.CategoryItemDTO;

import java.util.ArrayList;
import java.util.List;

public class CategoryItemsAdapter extends  RecyclerView.Adapter<CategoryItemsAdapter.ViewHolder> {

    private Context context;
    private List<CategoryItemDTO> listData = new ArrayList<>();

    public CategoryItemsAdapter(Context context){
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryItemsAdapter.ViewHolder holder, int position) {
        final CategoryItemDTO categoryItemDTO = listData.get(position);
        holder.category_item_name.setText(categoryItemDTO.getItemName());
        //holder.category_item_id.setText((int)categoryItemDTO.getId());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void setListData(List<CategoryItemDTO> listData) {
        this.listData = listData;
    }

    public List<CategoryItemDTO> getListData() {
        return listData;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView category_item_name;
        private TextView category_item_id;

        public ViewHolder(View itemView) {
            super(itemView);
            category_item_name = (TextView)itemView.findViewById(R.id.cat_item_name);
            //category_item_id = (TextView)itemView.findViewById(R.id.cat_item_id);
        }
    }
}
