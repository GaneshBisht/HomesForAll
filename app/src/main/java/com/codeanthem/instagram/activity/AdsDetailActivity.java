package com.codeanthem.instagram.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.codeanthem.instagram.R;
import com.codeanthem.instagram.adapter.ImageViewPagerAdapter;
import com.codeanthem.instagram.model.AdsModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import java.util.ArrayList;
import java.util.List;
import me.relex.circleindicator.CircleIndicator3;

public class AdsDetailActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {

   // View Declaration
    ViewPager2 vpImages;
    CircleIndicator3 circleIndicator;
    MaterialTextView tvTag, tvTitle, tvPrice, tvSecurityDeposit, tvDescription, tvAvailability, tvCarpetArea, tvNoOfBedroom,
            tvNoOfBathroom,tvWifi, tvFurnished, tvPrice2;
    MaterialButton btCall, btSMS;
    MaterialToolbar toolbar;

    // Data Sources
    List<Integer> imageList;

    AdsModel adsModel;

    GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads_detail);

        // View Initialisation
        toolbar = findViewById(R.id.toolbar);
        vpImages = findViewById(R.id.vpImages);
        circleIndicator = findViewById(R.id.circleIndicator);
        tvTag = findViewById(R.id.tvTag);
        tvTitle = findViewById(R.id.tvTitle);
        tvPrice = findViewById(R.id.tvPrice);
        tvSecurityDeposit = findViewById(R.id.tvSecurityDeposit);
        tvDescription = findViewById(R.id.tvDescription);
        tvAvailability = findViewById(R.id.tvAvailability);
        tvCarpetArea = findViewById(R.id.tvCarpetArea);
        tvNoOfBedroom = findViewById(R.id.tvNoOfBedroom);
        tvNoOfBathroom = findViewById(R.id.tvNoOfBathroom);
        tvWifi = findViewById(R.id.tvWifi);
        tvFurnished = findViewById(R.id.tvFurnished);
        tvPrice2 = findViewById(R.id.tvPrice2);
        btCall = findViewById(R.id.btCall);
        btSMS = findViewById(R.id.btSms);

        // Listener
        btCall.setOnClickListener(this);
        btSMS.setOnClickListener(this);

        initialiseImageList();

        setupUI();

    }

    private void setupUI(){

        // toolbar
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Adapter
        ImageViewPagerAdapter adapter = new ImageViewPagerAdapter(this,imageList);
        vpImages.setAdapter(adapter);

        circleIndicator.setViewPager(vpImages);

        adsModel =  (AdsModel)getIntent().getSerializableExtra("ads");

       tvTag.setText(adsModel.getNoOfBedroom() + " ROOMS \u22C6 " + adsModel.getNoOfBathroom() +
               " BATHROOM \u22C6 " + adsModel.getCarpetArea() + " SQFT");
       tvTitle.setText(adsModel.getTitle());
       tvPrice.setText("$" + adsModel.getPrice() + "/month");
       tvSecurityDeposit.setText("$" + adsModel.getSecurityDeposit());
       tvDescription.setText(adsModel.getDescription());
       tvAvailability.setText(adsModel.getAvailability());
       tvCarpetArea.setText(adsModel.getCarpetArea() + " SQFT");
       tvNoOfBedroom.setText(String.valueOf(adsModel.getNoOfBedroom()));
       tvNoOfBathroom.setText(String.valueOf(adsModel.getNoOfBathroom()));
       tvWifi.setText(String.valueOf(adsModel.getWifi()));
       tvFurnished.setText(String.valueOf(adsModel.getFurnished()));
       tvPrice2.setText("$" + adsModel.getPrice() + " USD/month");

       // Google Map
        FragmentManager manager = getSupportFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment) manager.findFragmentById(R.id.fragmentMap);
        mapFragment.getMapAsync(this);
    }

    private void initialiseImageList(){

        imageList = new ArrayList<>();
        imageList.add(R.drawable.img_placeholder);
        imageList.add(R.drawable.img_placeholder);
        imageList.add(R.drawable.img_placeholder);
        imageList.add(R.drawable.img_placeholder);
        imageList.add(R.drawable.img_placeholder);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btCall:
                Intent iCall = new Intent(Intent.ACTION_DIAL);
                iCall.setData(Uri.parse("tel:" + adsModel.getPublisherId()));
                startActivity(iCall);
                break;
            case R.id.btSms:
                Intent iSms = new Intent(Intent.ACTION_MAIN);
                iSms.addCategory(Intent.CATEGORY_DEFAULT);
                iSms.putExtra("address", adsModel.getPublisherId());
                iSms.setType("vnd.android-dir/mms-sms");
                startActivity(iSms);
                break;
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        this.googleMap = googleMap;

        LatLng latLng = new LatLng(30,78);

        // Marker
        MarkerOptions options = new MarkerOptions();
        options.title("Dehradun");
        options.snippet("GMS Road");
        options.position(latLng);

        this.googleMap.addMarker(options);

        // Camera
        this.googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));

        // UI setting
        UiSettings uiSettings = this.googleMap.getUiSettings();

        uiSettings.setMapToolbarEnabled(true);
    }
}