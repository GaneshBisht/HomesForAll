package com.codeanthem.instagram.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.codeanthem.instagram.R;
import com.codeanthem.instagram.activity.HomeActivity;
import com.codeanthem.instagram.activity.LoginActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

public class MainActivity extends AppCompatActivity {

    CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        startCountDownTimer(2000);

        checkForAlreadyLogin();

    }

    private void startCountDownTimer(int milliseconds){

       timer = new CountDownTimer(milliseconds,100) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                Intent iLogin = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(iLogin);
                finish();

            }
        };

        timer.start();
    }

    private void checkForAlreadyLogin(){

        SharedPreferences preferences = getSharedPreferences("app_info", MODE_PRIVATE);

        boolean isLogin = preferences.getBoolean("isLogin", false);

        if(isLogin){

            timer.cancel();

            Intent iHome = new Intent(this, HomeActivity.class);
            startActivity(iHome);
            finish();
        }

    }


}