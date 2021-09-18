package com.codeanthem.instagram.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.palette.graphics.Palette;

import com.codeanthem.instagram.R;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class ImageFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_image, null);

        // get argument
        Bundle bundle = getArguments();

        ImageView ivImage = v.findViewById(R.id.ivImage);

        Integer idImage = bundle.getInt("src");

        ivImage.setImageResource(idImage);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), idImage);

        // Asynchronous
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            public void onGenerated(Palette palette) {
                // Use generated instance
                int vibrant = palette.getVibrantColor(0x000000);
                int vibrantLight = palette.getLightVibrantColor(0x000000);
                int vibrantDark = palette.getDarkVibrantColor(0x000000);
                int muted = palette.getMutedColor(0x000000);
                int mutedLight = palette.getLightMutedColor(0x000000);
                int mutedDark = palette.getDarkMutedColor(0x000000);

                CollapsingToolbarLayout ctbl = getActivity().findViewById(R.id.collapsingToolbarLayout);
                ctbl.setContentScrimColor(vibrantDark);
            }
        });




        return v;
    }
}
