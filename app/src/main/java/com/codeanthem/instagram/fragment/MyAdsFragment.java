package com.codeanthem.instagram.fragment;


import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.codeanthem.instagram.R;
import com.codeanthem.instagram.adapter.AdsAdapter;
import com.codeanthem.instagram.adapter.MyAdsAdapter;
import com.codeanthem.instagram.model.AdsModel;
import com.codeanthem.instagram.util.EndPoints;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyAdsFragment extends Fragment implements MyAdsAdapter.MyAdsCallBack {

    // View Declaration
    RecyclerView rvMyAds;
    SwipeRefreshLayout swipeRefreshLayout;

    List<AdsModel> listMyAds = new ArrayList<>();

    String username;

    MyAdsAdapter myAdsAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_ads, null);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        // View Initialization
        rvMyAds = view.findViewById(R.id.rvMyAds);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        // RecyclerView Layout

        // 1. LinearLayoutManager (horizontal/vertical)
        // 2. GridLayoutManager
        // 3. StaggeredLayoutManager

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvMyAds.setLayoutManager(manager);

        // adapter
        myAdsAdapter = new MyAdsAdapter(getActivity(),MyAdsFragment.this, listMyAds);
        rvMyAds.setAdapter(myAdsAdapter);

        // Event
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                loadMyAds();

                swipeRefreshLayout.setRefreshing(false);
            }
        });




        SharedPreferences preferences = getActivity().getSharedPreferences("app_info", MODE_PRIVATE);
        username = preferences.getString("username", "NA");

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        loadMyAds();
    }

    private void loadMyAds(){

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                EndPoints.GET_MY_ADS_URL + "?username=" + username,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            int success = response.getInt("success");

                            if(success == 1){

                                if(listMyAds != null){
                                    listMyAds.clear();
                                }


                                JSONArray array = response.getJSONArray("ads");

                                if(array.length() > 0) {

                                    for (int row = 0; row < array.length(); row++) {

                                        JSONObject object = array.getJSONObject(row);

                                        int adsId = object.getInt("ad_id");
                                        String publisherId = object.getString("publisher_id");
                                        double price = object.getDouble("price");
                                        String category = object.getString("category");
                                        String title = object.getString("title");
                                        String address = object.getString("address");
                                        int noOfBedroom = object.getInt("no_of_bedroom");
                                        int noOfBathroom = object.getInt("no_of_bathroom");
                                        int carpetArea = object.getInt("carpet_area");
                                        String description = object.getString("description");
                                        float rating = (float) object.getDouble("rating");
                                        String coverImage = object.getString("cover_image");
                                        double security = object.getDouble("security_deposit");
                                        String availability = object.getString("availability");
                                        int wifi = object.getInt("wifi");
                                        int furnished = object.getInt("furnished");
                                        String postDate = object.getString("post_date");

                                        AdsModel adsModel = new AdsModel(adsId, publisherId, price, category, title,
                                                address, noOfBedroom, noOfBathroom, carpetArea, description, rating, coverImage,
                                                security, availability, wifi, furnished, postDate, false);

                                        listMyAds.add(adsModel);

                                    }
                                }else{
                                    Toast.makeText(getActivity(), "No ads published", Toast.LENGTH_SHORT).show();
                                }


                                myAdsAdapter.notifyDataSetChanged();

                            }
                            else{
                                Toast.makeText(getActivity(), "Unable to Fetch", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

        queue.add(request);

    }

    private void deleteMyAds(AdsModel adsModel){

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                EndPoints.DELETE_MY_ADS_URL + "?ads_id=" + adsModel.getAdId(),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            int success = response.getInt("success");

                            if(success == 1){

                                listMyAds.remove(adsModel);

                                myAdsAdapter.notifyDataSetChanged();

                            }
                            else{
                                Toast.makeText(getActivity(), response.getString("msg"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

        queue.add(request);

    }

    @Override
    public void deleteCallBack(int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                deleteMyAds(listMyAds.get(position));
            }
        });

        builder.setNegativeButton("No", null);

        AlertDialog dialog = builder.create();

        dialog.show();

    }

    @Override
    public void editCallBack(int position) {

    }
}
