package com.codeanthem.instagram.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.codeanthem.instagram.R;
import com.codeanthem.instagram.model.AdsModel;
import com.codeanthem.instagram.util.EndPoints;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAdsAdapter extends RecyclerView.Adapter {

    Context context;
    List<AdsModel> list;

    MyAdsCallBack myAdsCallBack;

    public interface MyAdsCallBack{
        public void deleteCallBack(int position);
        public void editCallBack(int position);
    }

    public MyAdsAdapter(Context context, Fragment fragment, List<AdsModel> list){
        this.context = context;
        this.list = list;
        this.myAdsCallBack = (MyAdsCallBack) fragment;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_custom_my_ads, null);

        MyAdsViewHolder holder = new MyAdsViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyAdsViewHolder myHolder = (MyAdsViewHolder) holder;

        AdsModel adsModel = list.get(position);

        Picasso.get().load(EndPoints.BASE_URL + adsModel.getCoverImage())
                .placeholder(R.drawable.img_placeholder).into(myHolder.ivAdsCoverImage);

        myHolder.tvAdsTitle.setText(adsModel.getTitle());

        myHolder.tvAdsCategory.setText(adsModel.getCategory());

        myHolder.tvAdsDateTime.setText(adsModel.getPublishDate());

        myHolder.ivMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu menu = new PopupMenu(context, view);
                menu.inflate(R.menu.my_ads_item_menu);
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        switch (menuItem.getItemId()){
                            case R.id.menu_edit:
                                myAdsCallBack.editCallBack(position);
                                break;
                            case R.id.menu_delete:
                                myAdsCallBack.deleteCallBack(position);
                                break;
                        }

                        return true;
                    }
                });

                menu.show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyAdsViewHolder extends RecyclerView.ViewHolder{

        // View Declaration
        ImageView ivAdsCoverImage;
        TextView tvAdsTitle;
        TextView tvAdsCategory;
        TextView tvAdsDateTime;
        ImageView ivMore;

        public MyAdsViewHolder(@NonNull View itemView) {
            super(itemView);

            // View Initialise
            ivAdsCoverImage = itemView.findViewById(R.id.ivAdsCoverImage);
            tvAdsTitle = itemView.findViewById(R.id.tvAdsTitle);
            tvAdsCategory = itemView.findViewById(R.id.tvAdsCategory);
            tvAdsDateTime = itemView.findViewById(R.id.tvAdsDateTime);
            ivMore = itemView.findViewById(R.id.ivMore);

        }
    }
}
