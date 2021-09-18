package com.codeanthem.instagram.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.codeanthem.instagram.util.EndPoints;
import com.codeanthem.instagram.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    // View Declaration
    TextInputEditText etUsername, etPassword;
    MaterialTextView tvSignUp, tvForgotPassword;
    MaterialButton btLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // View Initialization
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        tvSignUp = findViewById(R.id.tvSignUp);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        btLogin = findViewById(R.id.btLogin);

        // Set OnClickListener
        tvSignUp.setOnClickListener(this);
        tvForgotPassword.setOnClickListener(this);
        btLogin.setOnClickListener(this);

        setupUI();

    }

    void setupUI(){

        SpannableString sp1 = new SpannableString("Forgot your login details? Get help logging in.");
        sp1.setSpan(new ForegroundColorSpan(Color.BLUE),27,47, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvForgotPassword.setText(sp1);

        SpannableString sp2 = new SpannableString("Don't have an account? Sign up.");
        sp2.setSpan(new ForegroundColorSpan(Color.BLUE),23,31, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvSignUp.setText(sp2);

    }



    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.tvSignUp:

                Intent iSignUp = new Intent(this, SignUpActivity.class);
                startActivity(iSignUp);

                finish();

                break;

            case R.id.tvForgotPassword:

                Intent iLoginHelp = new Intent(this, LoginHelpActivity.class);
                startActivity(iLoginHelp);

                break;

            case R.id.btLogin:

                login();

                break;
        }

    }

     private void login(){

         String username = etUsername.getText().toString();
         String password = etPassword.getText().toString();

        if(isValidate(username, password)) {

            JSONObject object = new JSONObject();

            try {
                object.put("username", username);
                object.put("password", password);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            RequestQueue queue = Volley.newRequestQueue(this);

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    EndPoints.LOGIN_URL,
                    object,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {

                                int success = response.getInt("success");

                                if(success == 1){

                                    saveLoginInformation();

                                    Intent iHome = new Intent(LoginActivity.this, HomeActivity.class);
                                    startActivity(iHome);

                                    finish();
                                }
                                else{
                                    Toast.makeText(LoginActivity.this, response.getString("msg"), Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });

            queue.add(request);
        }

     }

     boolean isValidate(String username, String password){

         TextInputLayout tilUserName = findViewById(R.id.tilUsername);
         TextInputLayout tilPassword = findViewById(R.id.tilPassword);

        if(username.isEmpty()){
            tilUserName.setError("Phone number, email or username Required");
            return false;
        }
        else{
            tilUserName.setError(null);
        }

        if(password.isEmpty()){

            tilPassword.setError("Password Required");
            return false;
        }
        else{
            tilPassword.setError(null);
        }

        return true;
     }

    private void saveLoginInformation(){

        SharedPreferences preferences =  getSharedPreferences("app_info", MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();

        editor.putBoolean("isLogin", true);
        editor.putString("username", etUsername.getText().toString());

        editor.apply();
    }
}