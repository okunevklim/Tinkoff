package com.okunev.tinkoffexam.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.okunev.tinkoffexam.fragments.GifStateFragment;
import com.okunev.tinkoffexam.models.GifType;

public class GifFragmentsAdapter extends FragmentStateAdapter {
    public GifFragmentsAdapter(FragmentActivity activity) {
        super(activity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return GifStateFragment.newInstance(GifType.LATEST);
            case 1:
                return GifStateFragment.newInstance(GifType.TOP);
            default:
                return GifStateFragment.newInstance(GifType.HOT);
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}