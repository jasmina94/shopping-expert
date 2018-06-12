package com.ftn.mdj.adapters;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.ftn.mdj.R;
import com.ftn.mdj.dto.CategoryDTO;
import com.ftn.mdj.dto.CategoryItemDTO;
import com.ftn.mdj.threads.AddCategoryItemAsShoppingListItemThread;
import com.ftn.mdj.utils.GenericResponse;
import com.ftn.mdj.utils.UtilHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableCategoryListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<CategoryDTO> listDataHeader = new ArrayList<>(); // header titles
    private HashMap<String, List<CategoryItemDTO>> listDataChild = new HashMap<>(); // child data in format of header title, child title


    public ExpandableCategoryListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int i) {
        CategoryDTO categoryDTO = this.listDataHeader.get(i);
        String key = categoryDTO.getCategoryName();
        return this.listDataChild.get(key).size();
    }

    @Override
    public Object getGroup(int i) {
        return this.listDataHeader.get(i);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        CategoryDTO categoryDTO = this.listDataHeader.get(groupPosition);
        String key = categoryDTO.getCategoryName();
        return this.listDataChild.get(key).get(childPosition);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View convertView, ViewGroup viewGroup) {
        CategoryDTO categoryDTO = (CategoryDTO) getGroup(i);
        String headerTitle = categoryDTO.getCategoryName();

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.category_view, null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.txt_list_category_name);
        lblListHeader.setTypeface(null, android.graphics.Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,boolean isLastChild, View convertView, ViewGroup parent) {
        CategoryItemDTO child = (CategoryItemDTO) getChild(groupPosition, childPosition);
        final String childText = child.getItemName();
        final long childId = child.getId();

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.category_item_view, null);
        }

        TextView txtListChild = (TextView) convertView .findViewById(R.id.txt_list_category_item_name);
        TextView txtListChildId = (TextView) convertView .findViewById(R.id.txt_list_category_item_id);
        txtListChild.setText(childText);
        txtListChildId.setText(String.valueOf(childId));
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    public List<CategoryDTO> getListDataHeader() {
        return listDataHeader;
    }

    public HashMap<String, List<CategoryItemDTO>> getListDataChild() {
        return listDataChild;
    }

    public void setListDataHeader(List<CategoryDTO> listDataHeader) {
        this.listDataHeader = listDataHeader;
    }

    public void setListDataChild(HashMap<String, List<CategoryItemDTO>> listDataChild) {
        this.listDataChild = listDataChild;
    }
}
