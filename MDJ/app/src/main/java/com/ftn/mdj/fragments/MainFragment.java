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
import java.util.Optional;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    public static MainFragment instance;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private FloatingActionButton mBtnAddList;
    private TextView mEmptyView;
    private ImageView mEmptyImgView;

    private List<ShoppingListDTO> activeShoppingLists;

    public MainFragment(){
        this.instance = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));

        mAdapter = new MainAdapter(activeShoppingLists, rootView.getContext(), this);
        mRecyclerView.setAdapter(mAdapter);

        mEmptyView = (TextView) rootView.findViewById(R.id.empty_view);
        mEmptyImgView = (ImageView) rootView.findViewById(R.id.empty_view_img);

        if(activeShoppingLists.size()!=0){
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.INVISIBLE);
            mEmptyImgView.setVisibility(View.INVISIBLE);
        }else {
            mRecyclerView.setVisibility(View.INVISIBLE);
            mEmptyView.setVisibility(View.VISIBLE);
            mEmptyImgView.setVisibility(View.VISIBLE);
        }

        mBtnAddList = (FloatingActionButton)rootView.findViewById(R.id.btn_add_shopping_list);
        mBtnAddList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, AddListActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }

    public void setActiveShoppingLists(List<ShoppingListDTO> shoppingLists){ //throws IOException {
        activeShoppingLists = shoppingLists;
//        Type listType = new TypeToken<ArrayList<ShoppingListDTO>>() {
//        }.getType();
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
//        activeShoppingLists = mapper.readValue(mapper.writeValueAsString(shoppingLists), new TypeReference<List<ShoppingListDTO>>(){});
//        activeShoppingLists = new Gson().fromJson(mapper.writeValueAsString(shoppingLists), listType);
    }

    public void archiveListUI(Long shoppingListId) {
        Optional<ShoppingListDTO> shoppingListShowDTO = activeShoppingLists.stream().filter(e -> e.getId() == shoppingListId).findFirst();
        activeShoppingLists.remove(shoppingListShowDTO.get());
    }

    public void renameListInArray(Long shoppingListId, String listName) {
        activeShoppingLists.forEach(e -> {
            if(e.getId() == shoppingListId) {
                e.setListName(listName);
                return;
            }
        });
    }

    public void changeListPrivacy(Long shoppingListId, Boolean isPublic) {
        activeShoppingLists.forEach(e -> {
            if(e.getId() == shoppingListId) {
                e.setIsSecret(isPublic);
                return;
            }
        });
    }

    public void restartFragment() {
        getFragmentManager().beginTransaction().detach(this).attach(this).commit();
    }
}
