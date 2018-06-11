package com.ftn.mdj.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ftn.mdj.fragments.CategoriesFragment;
import com.ftn.mdj.fragments.ItemsFragment;

public class ListItemPagerAdapter extends FragmentPagerAdapter {

    public ListItemPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ItemsFragment itemsFragment = new ItemsFragment();
                return itemsFragment;
            case 1:
                CategoriesFragment categoriesFragment = new CategoriesFragment();
                return categoriesFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
