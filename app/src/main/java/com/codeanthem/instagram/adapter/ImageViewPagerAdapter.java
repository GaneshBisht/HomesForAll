package com.codeanthem.instagram.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.codeanthem.instagram.fragment.ImageFragment;

import java.util.List;

public class ImageViewPagerAdapter extends FragmentStateAdapter {

    List<Integer> imageList;

    public ImageViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, List<Integer> imageList) {
        super(fragmentActivity);
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        ImageFragment fragment = new ImageFragment();

        // set argument (key-value pair as bundle object)
        Bundle bundle = new Bundle();
        bundle.putInt("src",imageList.get(position));
        fragment.setArguments(bundle);
        
        return fragment;
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }
}
