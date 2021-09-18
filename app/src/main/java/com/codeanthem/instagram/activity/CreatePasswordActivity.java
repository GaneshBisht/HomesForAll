package com.codeanthem.instagram.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import org.json.JSONException;
import org.json.JSONObject;

public class CreatePasswordActivity extends AppCompatActivity implements View.OnClickListener {

    // View Declaration
    TextInputEditText etPassword, etConfirmPassword;
    MaterialButton btCreatePassword;

    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_password);

        // View Initialization
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btCreatePassword = findViewById(R.id.btCreatePassword);

        btCreatePassword.setOnClickListener(this);

        username = getIntent().getStringExtra("username");
    }

    @Override
    public void onClick(View view) {

        createPassword();
    }

    private void createPassword(){

        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();

        if(isValidate(password, confirmPassword)){

            RequestQueue queue = Volley.newRequestQueue(this);

            JSONObject object = new JSONObject();

            try {
                object.put("username", username);
                object.put("password", password);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    EndPoints.CREATE_PASSWORD_URL,
                    object,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {

                                int success = response.getInt("success");

                                if(success == 1){

                                    saveLoginInformation();

                                    Intent iHome = new Intent(CreatePasswordActivity.this, HomeActivity.class);
                                    startActivity(iHome);
                                    finish();

                                }else{

                                    Toast.makeText(CreatePasswordActivity.this, response.getString("msg"), Toast.LENGTH_SHORT).show();

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(CreatePasswordActivity.this, error.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
            );

            queue.add(request);
        }
    }

    private boolean isValidate(String password, String confirmPassword){

        TextInputLayout tilPassword = findViewById(R.id.tilPassword);
        TextInputLayout tilConfirmPassword =findViewById(R.id.tilConfirmPassword);

        if(password.isEmpty()){
            tilPassword.setError("Password is required");
            return false;
        }
        else{
            tilPassword.setError(null);
        }

        if(confirmPassword.isEmpty()){
            tilConfirmPassword.setError("Confirm Password required");
            return false;
        }
        else if(!password.equals(confirmPassword)){
            tilConfirmPassword.setError("Password and Confirm Password is different");
            return false;
        }
        else{
            tilConfirmPassword.setError(null);
        }

        return true;
    }

    private void saveLoginInformation(){

        SharedPreferences preferences = getSharedPreferences("app_info", MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();

        editor.putBoolean("isLogin", true); //  <boolean name="isLogin>true</boolean>

        editor.putString("username", username); // <string name="username">7017393669</string>

        editor.apply();

    }
}