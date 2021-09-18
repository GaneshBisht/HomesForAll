package com.codeanthem.instagram.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.codeanthem.instagram.R;
import com.codeanthem.instagram.activity.AdsDetailActivity;
import com.codeanthem.instagram.adapter.AdsAdapter;
import com.codeanthem.instagram.model.AdsModel;
import com.codeanthem.instagram.util.EndPoints;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FavouriteFragment extends Fragment implements AdapterView.OnItemClickListener, AdsAdapter.OnClickListener {

    // View Declaration
    ListView lvFavourite;

    List<AdsModel> listFavourite = new ArrayList<>();

    AdsAdapter adsAdapter;

    String username;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_favourite, null);




        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // View Initialization
        lvFavourite = view.findViewById(R.id.lvFavourite);

        // Event
        lvFavourite.setOnItemClickListener(this);

        SharedPreferences preferences = getActivity().getSharedPreferences("app_info", MODE_PRIVATE);
        username = preferences.getString("username", "NA");



    }

    @Override
    public void onResume() {
        super.onResume();

        loadAds();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {

        Intent iDetail = new Intent(getActivity(), AdsDetailActivity.class);
        iDetail.putExtra("ads", listFavourite.get(index));
        startActivity(iDetail);


    }

    private void loadAds(){

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                EndPoints.GET_FAVOURITE_ADS_URL + "?username=" + username,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            int success = response.getInt("success");

                            if(success == 1){

                                if(listFavourite != null){
                                    listFavourite.clear();
                                }

                                listFavourite = new ArrayList<>();

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
                                        String favouriteUsername = object.getString("username");

                                        AdsModel adsModel = new AdsModel(adsId, publisherId, price, category, title,
                                                address, noOfBedroom, noOfBathroom, carpetArea, description, rating, coverImage,
                                                security, availability, wifi, furnished, postDate, favouriteUsername != "null" ? true : false);

                                        listFavourite.add(adsModel);

                                    }
                                }else{
                                    Toast.makeText(getActivity(), "No data available", Toast.LENGTH_SHORT).show();
                                }

                                adsAdapter = new AdsAdapter(FavouriteFragment.this, listFavourite);
                                lvFavourite.setAdapter(adsAdapter);
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

    @Override
    public void onClick(int position) {

        changeFavourite(listFavourite.get(position).getAdId(), position);

    }

    private void changeFavourite(int adsId, int position){

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                EndPoints.CHANGE_FAVOURITE_URL + "?ads_id=" + adsId + "&username=" + username,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            int success = response.getInt("success");

                            if(success == 1){

                                Toast.makeText(getActivity(),  response.getString("msg"), Toast.LENGTH_SHORT).show();

                                AdsModel model = listFavourite.get(position);

                                listFavourite.remove(model);

                                adsAdapter.notifyDataSetChanged();

                            }
                            else{
                                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
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
}
