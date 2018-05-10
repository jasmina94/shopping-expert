package com.ftn.mdj.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ftn.mdj.R;
import com.ftn.mdj.activities.AddListActivity;
import com.ftn.mdj.adapters.MainAdapter;
import com.ftn.mdj.dto.ShoppingListDTO;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private FloatingActionButton btn_add_list;
    TextView emptyView;
    ImageView emptyImgView;

    private List<ShoppingListDTO> activeShoppingLists;

    public MainFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        recyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));

        adapter = new MainAdapter(activeShoppingLists, rootView.getContext());
        recyclerView.setAdapter(adapter);

        emptyView = (TextView) rootView.findViewById(R.id.empty_view);
        emptyImgView = (ImageView) rootView.findViewById(R.id.empty_view_img);

        if(activeShoppingLists.size()!=0){
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.INVISIBLE);
            emptyImgView.setVisibility(View.INVISIBLE);
        }else {
            recyclerView.setVisibility(View.INVISIBLE);
            emptyView.setVisibility(View.VISIBLE);
            emptyImgView.setVisibility(View.VISIBLE);
        }

        btn_add_list = (FloatingActionButton)rootView.findViewById(R.id.btn_add_shopping_list);
        btn_add_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, AddListActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }

    public void setActiveShoppingLists(List<ShoppingListDTO> shoppingLists){
        activeShoppingLists = shoppingLists;
    }
}
