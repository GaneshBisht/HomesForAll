package com.codeanthem.instagram.fragment;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.codeanthem.instagram.R;
import com.codeanthem.instagram.activity.AdsDetailActivity;
import com.codeanthem.instagram.activity.HomeActivity;
import com.codeanthem.instagram.adapter.AdsAdapter;
import com.codeanthem.instagram.model.AdsModel;
import com.codeanthem.instagram.util.EndPoints;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, AdsAdapter.OnClickListener {

    private static final int AUTOCOMPLETE_REQUEST_CODE = 101;
    // View Declaration
    MaterialButton btHome, btVilla, btApartment, btPG, btHostel;
    ListView lvAds;
    MaterialTextView tvLocation;
    Dialog locationDialog;

    // Collection
    List<AdsModel> listAds = new ArrayList<>();

    // Adapter
    AdsAdapter adsAdapter;

    // Location Client
    FusedLocationProviderClient client;

    String username;

    public HomeFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = LocationServices.getFusedLocationProviderClient(getActivity());

        // Places API

        // Initialize the SDK
        Places.initialize(getActivity(),"AIzaSyB5OMdE7NAdmpPyB6T6rwVPCn7Ubt7NgOk");
        
        // Create a new PlacesClient instance
        PlacesClient placesClient = Places.createClient(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, null);

        return v;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        //view initialization
        btHome = view.findViewById(R.id.btHome);
        btVilla = view.findViewById(R.id.btVilla);
        btApartment = view.findViewById(R.id.btApartment);
        btPG = view.findViewById(R.id.btPG);
        btHostel = view.findViewById(R.id.btHostel);
        lvAds = view.findViewById(R.id.lvAds);
        tvLocation = view.findViewById(R.id.tvLocation);


        // event
        btHome.setOnClickListener(this);
        btVilla.setOnClickListener(this);
        btApartment.setOnClickListener(this);
        btPG.setOnClickListener(this);
        btHostel.setOnClickListener(this);
        lvAds.setOnItemClickListener(this);
        tvLocation.setOnClickListener(this);

        setupUI();

        SharedPreferences preferences = getActivity().getSharedPreferences("app_info", MODE_PRIVATE);
        username = preferences.getString("username", "NA");

        //   super.onViewCreated(view, savedInstanceState);

    }

    private void setupUI() {

        // ui configure
        buttonColorReset();
        btHome.setBackgroundColor(Color.argb(0xff, 0x01, 0x95, 0xf5));
        btHome.setTextColor(Color.WHITE);

        // initialise list & set adapter

    }

    @Override
    public void onResume() {
        super.onResume();

        loadAds("home");
    }

    private void loadAds(String category) {

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                EndPoints.GET_ADS_BY_CATEGORY_URL + "?category=" + category + "&username=" + username,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            int success = response.getInt("success");

                            if (success == 1) {

                                if (listAds != null) {
                                    listAds.clear();
                                }

                                listAds = new ArrayList<>();

                                JSONArray array = response.getJSONArray("ads");

                                if (array.length() > 0) {

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

                                        listAds.add(adsModel);

                                    }
                                } else {
                                    Snackbar.make(lvAds, "No ads available", Snackbar.LENGTH_SHORT).show();
                                }

                                adsAdapter = new AdsAdapter(HomeFragment.this, listAds);
                                lvAds.setAdapter(adsAdapter);
                            } else {

                                Snackbar.make(lvAds, "Error loading ads..", Snackbar.LENGTH_LONG)
                                        .setAction("Retry", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                loadAds(category);
                                            }
                                        }).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Snackbar.make(lvAds, "Network problem..", Snackbar.LENGTH_INDEFINITE)
                                .setAction("Retry", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        loadAds(category);
                                    }
                                }).show();

                    }
                });

        queue.add(request);

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btHome:
            case R.id.btVilla:
            case R.id.btApartment:
            case R.id.btPG:
            case R.id.btHostel:
                buttonColorReset();
                Button clickButton = (Button) view;
                clickButton.setBackgroundColor(Color.argb(0xff, 0x01, 0x95, 0xf5));
                clickButton.setTextColor(Color.WHITE);

                loadAds(clickButton.getText().toString());

                break;

            case R.id.tvLocation:
                locationDialog();
                break;

            case R.id.ivCross:
                locationDialog.dismiss();
                break;

            case R.id.btSearch:
                searchLocation();
                locationDialog.dismiss();
                break;

            case R.id.btCurrentLocation:
                getLastLocation();
                locationDialog.dismiss();
                break;

            case R.id.btAddAddress:
                break;
        }

    }

    private void locationDialog(){

        locationDialog = new Dialog(getActivity(), R.style.Theme_MaterialComponents_Light_Dialog_FullScreen);
        locationDialog.setContentView(R.layout.dialog_pick_location);

        ImageView ivCross = locationDialog.findViewById(R.id.ivCross);
        MaterialButton btSearch = locationDialog.findViewById(R.id.btSearch);
        MaterialButton btCurrentLocation = locationDialog.findViewById(R.id.btCurrentLocation);
        MaterialButton btAddress = locationDialog.findViewById(R.id.btAddAddress);

        ivCross.setOnClickListener(this);
        btSearch.setOnClickListener(this);
        btCurrentLocation.setOnClickListener(this);
        btAddress.setOnClickListener(this);

        locationDialog.show();

    }

    private void getLastLocation() {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        Task<Location> task1 = client.getLastLocation();

        task1.addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {

                if (task.isSuccessful()) {

                    Location location = task.getResult();

                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();

                        reverseGeoCoder(latitude, longitude);

                        Toast.makeText(getActivity(), "lat : " + latitude + " lng : " + longitude, Toast.LENGTH_SHORT).show();

                    } else {

                        // if location is not known then get current location
                        getCurrentLocation();

                    }

                } else {
                    Snackbar.make(lvAds, "Unable to get location", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        Task<Location> task = client.getCurrentLocation(LocationRequest.PRIORITY_LOW_POWER, null);

        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                if(location != null){
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    reverseGeoCoder(latitude, longitude);

                    Toast.makeText(getActivity(), "lat : " + latitude + " lng : " + longitude, Toast.LENGTH_SHORT).show();
                }
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Snackbar.make(lvAds, "Unable to get location", Snackbar.LENGTH_SHORT).show();
            }
        });

    }

    private void reverseGeoCoder(double latitude, double longitude){

        // reverse geocoding :- coordinate to address conversion
        Geocoder geocoder =  new Geocoder(getActivity());

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude,longitude, 1);

            if(addresses.size() > 0){

                Address address = addresses.get(0);

                String city = address.getSubAdminArea();

                tvLocation.setText(city != null ? city : "NA");

            }
            else{
                Snackbar.make(lvAds, "Unable to get address", Snackbar.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void buttonColorReset(){
        btHome.setBackgroundColor(Color.WHITE);
        btHome.setTextColor(Color.BLACK);
        btVilla.setBackgroundColor(Color.WHITE);
        btVilla.setTextColor(Color.BLACK);
        btApartment.setBackgroundColor(Color.WHITE);
        btApartment.setTextColor(Color.BLACK);
        btPG.setBackgroundColor(Color.WHITE);
        btPG.setTextColor(Color.BLACK);
        btHostel.setBackgroundColor(Color.WHITE);
        btHostel.setTextColor(Color.BLACK);
    }

    @Override
    public void onClick(int position) {

        changeFavourite(listAds.get(position).getAdId(), position);

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

                                AdsModel model = listAds.get(position);

                                listAds.get(position).setFavourite(model.isFavourite() ? false : true);

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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {

        Intent iDetail = new Intent(getActivity(), AdsDetailActivity.class);
        iDetail.putExtra("ads", listAds.get(index));
        startActivity(iDetail);

    }
    
    private void searchLocation(){

        // Set the fields to specify which types of place data to
        // return after the user has made a selection.
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);

        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .build(getActivity());
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);

        
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                Place place = Autocomplete.getPlaceFromIntent(data);

                tvLocation.setText(place.getName());
            }

            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
