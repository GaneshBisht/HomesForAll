package com.codeanthem.instagram.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.codeanthem.instagram.R;
import com.codeanthem.instagram.model.AdsModel;
import com.codeanthem.instagram.util.EndPoints;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdsAdapter extends BaseAdapter {

    Context context;
    List<AdsModel> list;

    OnClickListener favouriteListener;

    public static interface OnClickListener{
        public void onClick(int position);
    }

    public AdsAdapter(Context context, List<AdsModel> list){
        this.context = context;
        this.list = list;
         favouriteListener = (OnClickListener) context;
    }

    public AdsAdapter(Fragment fragment, List<AdsModel> list){
        this.context = fragment.getContext();
        this.list = list;
        favouriteListener = (OnClickListener) fragment;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        View v = LayoutInflater.from(context).inflate(R.layout.list_custom_ads, null);

        TextView tvTitle = v.findViewById(R.id.tvTitle);
        TextView tvCategory = v.findViewById(R.id.tvCategory);
        TextView tvPrice = v.findViewById(R.id.tvPrice);
        TextView tvRating = v.findViewById(R.id.tvRating);
        TextView tvAddress = v.findViewById(R.id.tvAddress);
        TextView tvNoOfBedroom = v.findViewById(R.id.tvNoOfBedroom);
        TextView tvNoOfBathroom = v.findViewById(R.id.tvNoOfBathroom);
        ImageView ivFavourite = v.findViewById(R.id.ivFavourite);
        ImageView ivCover = v.findViewById(R.id.ivCoverImage);

        AdsModel adsModel = list.get(position);

        tvTitle.setText(adsModel.getTitle());
        tvCategory.setText("\u22C6 " + adsModel.getCategory());
        tvPrice.setText(String.valueOf(adsModel.getPrice()));
        tvRating.setText("\u2605 " + String.valueOf(adsModel.getRating()));
        tvAddress.setText(adsModel.getAddress());
        tvNoOfBathroom.setText(String.valueOf(adsModel.getNoOfBathroom()));
        tvNoOfBedroom.setText(String.valueOf(adsModel.getNoOfBedroom()));

        if(adsModel.isFavourite()){
            ivFavourite.setImageResource(R.drawable.ic_favourite_like);
        }else{
            ivFavourite.setImageResource(R.drawable.ic_favourite_dislike);
        }

        Picasso.get().load(EndPoints.BASE_URL + adsModel.getCoverImage())
                .placeholder(R.drawable.img_placeholder).into(ivCover);

        ivFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(adsModel.isFavourite()){
                    ivFavourite.setImageResource(R.drawable.ic_favourite_dislike);
                }else{
                    ivFavourite.setImageResource(R.drawable.ic_favourite_like);
                }

                favouriteListener.onClick(position);
            }
        });

        return v;
    }
}
