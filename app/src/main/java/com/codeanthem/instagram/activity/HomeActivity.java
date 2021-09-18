package com.codeanthem.instagram.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.codeanthem.instagram.R;
import com.codeanthem.instagram.fragment.AccountFragment;
import com.codeanthem.instagram.fragment.FavouriteFragment;
import com.codeanthem.instagram.fragment.HomeFragment;
import com.codeanthem.instagram.fragment.MyAdsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    private static final int LOCATION_REQUEST_CODE = 100;

    // View Declaration
    BottomNavigationView bnv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // View Initialization
        bnv = findViewById(R.id.bottomNavigationView);

        // Event
        bnv.setOnNavigationItemSelectedListener(this);

        requestPermission();

    }

    private void requestPermission(){

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_REQUEST_CODE);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        switch (item.getItemId()){
            case R.id.menu_home:

                HomeFragment homeFragment = new HomeFragment();
                transaction.replace(R.id.fragmentContainer, homeFragment);
                break;
            case R.id.menu_favourite:

                FavouriteFragment favouriteFragment = new FavouriteFragment();
                transaction.replace(R.id.fragmentContainer, favouriteFragment);
                break;

            case R.id.menu_my_ads:

                MyAdsFragment myAdsFragment = new MyAdsFragment();
                transaction.replace(R.id.fragmentContainer, myAdsFragment);
                break;

            case R.id.menu_account:

                AccountFragment accountFragment = new AccountFragment();
                transaction.replace(R.id.fragmentContainer, accountFragment);
                break;
        }

        transaction.commit();

        return true;
    }

    /*
    private void initHome(){

        TextView tvUsername =  findViewById(R.id.tvUsername);
        Button btLogout = findViewById(R.id.btLogout);

        SharedPreferences preferences = getSharedPreferences("app_info", MODE_PRIVATE);

        String username = preferences.getString("username", "NA");

        tvUsername.setText("Welcome, " + username);

        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor = preferences.edit();

                //editor.clear();

                // clear login information
                editor.putBoolean("isLogin", false);
                editor.putString("username", null);

                editor.apply();

                Intent iMain = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(iMain);
                finish();

            }
        });
    }
     */
}