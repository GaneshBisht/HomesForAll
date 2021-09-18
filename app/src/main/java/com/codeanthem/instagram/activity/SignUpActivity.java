package com.codeanthem.instagram.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Toast;
import android.widget.ViewSwitcher;

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

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    // View Declaration
    MaterialTextView tvPhone, tvEmail, tvCountryCode, tvSignIn;
    View vPhone, vEmail;
    ViewSwitcher viewSwitcher;
    MaterialButton btNext1, btNext2;
    TextInputEditText etPhone, etEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // View Initialization
        tvPhone = findViewById(R.id.tvPhone);
        tvEmail = findViewById(R.id.tvEmail);
        vPhone = findViewById(R.id.vPhone);
        vEmail = findViewById(R.id.vEmail);
        viewSwitcher = findViewById(R.id.viewSwitcher);
        tvCountryCode = findViewById(R.id.tvCountryCode);
        btNext1 = findViewById(R.id.btNext);
        btNext2 = findViewById(R.id.btNext2);
        tvSignIn = findViewById(R.id.tvLogin);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);

        // Set OnClickListener
        tvPhone.setOnClickListener(this);
        tvEmail.setOnClickListener(this);
        tvCountryCode.setOnClickListener(this);
        btNext1.setOnClickListener(this);
        btNext2.setOnClickListener(this);
        tvSignIn.setOnClickListener(this);

        setupUI();

    }

    void setupUI(){

        SpannableString spannableString = new SpannableString("Already have an account? Sign in.");
        spannableString.setSpan(new ForegroundColorSpan(Color.BLUE),25,33, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvSignIn.setText(spannableString);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.tvPhone:

                tvPhone.setTextColor(Color.BLACK);
                vPhone.setBackgroundColor(Color.BLACK);
                tvEmail.setTextColor(Color.LTGRAY);
                vEmail.setBackgroundColor(Color.LTGRAY);

                viewSwitcher.showNext();

                    break;

            case R.id.tvEmail:

                tvPhone.setTextColor(Color.LTGRAY);
                vPhone.setBackgroundColor(Color.LTGRAY);
                tvEmail.setTextColor(Color.BLACK);
                vEmail.setBackgroundColor(Color.BLACK);

                viewSwitcher.showNext();

                    break;

            case R.id.tvCountryCode:
                break;
            case R.id.btNext:

                  signUp();

                break;
            case R.id.btNext2:
                break;
            case R.id.tvLogin:

                Intent iLogin = new Intent(this, LoginActivity.class);
                startActivity(iLogin);

                finish();

                break;
        }
    }

    private void signUp(){

        String phoneNumber = etPhone.getText().toString();

        if(isValidate(phoneNumber)){

            RequestQueue queue = Volley.newRequestQueue(this);

            JSONObject object = new JSONObject();

            try {
                object.put("username", phoneNumber);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    EndPoints.SIGNUP_URL,
                    object,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {

                                int success = response.getInt("success");

                                if(success == 1){

                                    Intent iVerification = new Intent(SignUpActivity.this, VerificationActivity.class);
                                    iVerification.putExtra("username", phoneNumber);
                                    startActivity(iVerification);

                                    finish();

                                }else{

                                    Toast.makeText(SignUpActivity.this, response.getString("msg"), Toast.LENGTH_SHORT).show();

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(SignUpActivity.this, error.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
            );

            queue.add(request);
        }
    }

    private boolean isValidate(String phoneNumber){

        TextInputLayout tilPhone = findViewById(R.id.tilPhone);

        if(phoneNumber.isEmpty()){
            tilPhone.setError("Phone Number Required");
            return false;
        }
        else if(phoneNumber.length() != 10){
            tilPhone.setError("Phone Number must contain 10 digits");
            return false;
        }
        else{
            tilPhone.setError(null);
        }

        return true;
    }


}