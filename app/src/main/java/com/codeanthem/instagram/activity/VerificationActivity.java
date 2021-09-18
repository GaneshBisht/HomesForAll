package com.codeanthem.instagram.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.FrameLayout;
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

public class VerificationActivity extends AppCompatActivity implements View.OnClickListener {

    // View Declaration
    TextInputEditText etVerificationCode;
    MaterialButton btNext;
    MaterialTextView tvResend;

    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_verification);

        // View Initialisation
        etVerificationCode = findViewById(R.id.etVerificationCode);
        btNext = findViewById(R.id.btNext);
        tvResend = findViewById(R.id.tvResend);

        btNext.setOnClickListener(this);

        setupUI();

    }

    void setupUI(){

        username = getIntent().getStringExtra("username");

        SpannableString spannableString = new SpannableString("Enter the 6-digit confirmation code we sent to +91" + username + ". Request a new one.");

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Toast.makeText(VerificationActivity.this, "Request a new one.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                ds.setColor(Color.BLUE);
                ds.setUnderlineText(false);
            }
        };

        spannableString.setSpan(clickableSpan, 62,80, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvResend.setText(spannableString);
        tvResend.setMovementMethod(LinkMovementMethod.getInstance());

        /*
        CoordinatorLayout cl = findViewById(R.id.cl);
        Snackbar snackbar = Snackbar.make(cl,"An SMS confirmation has been sent.", Snackbar.LENGTH_INDEFINITE);
        TextView sbTv = snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
        snackbar.setBackgroundTint(Color.GREEN);
        sbTv.setTextColor(Color.WHITE);
        sbTv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        snackbar.show();
        */



    }

    private void displayVerificationSuccessDialog(){

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_verified);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Point screenSize = new Point();
        getWindowManager().getDefaultDisplay().getSize(screenSize);
        ConstraintLayout v = dialog.findViewById(R.id.dialog_head);
        v.setLayoutParams(new FrameLayout.LayoutParams(screenSize.x - (int)(screenSize.x * 0.2),
                FrameLayout.LayoutParams.WRAP_CONTENT));

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {

                Intent iVerification = new Intent(VerificationActivity.this, CreatePasswordActivity.class);
                iVerification.putExtra("username", username);
                startActivity(iVerification);

                finish();

            }
        });

        dialog.show();
    }

    @Override
    public void onClick(View view) {

        verification();

    }

    private void verification(){

        String verificationCode = etVerificationCode.getText().toString();

        if(isValidate(verificationCode)){

            RequestQueue queue = Volley.newRequestQueue(this);

            JSONObject object = new JSONObject();

            try {
                object.put("username", username);
                object.put("verification_code", Integer.parseInt(verificationCode));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    EndPoints.VERIFICATION_URL,
                    object,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {

                                int success = response.getInt("success");

                                if(success == 1){

                                    displayVerificationSuccessDialog();

                                }else{

                                    Toast.makeText(VerificationActivity.this, response.getString("msg"), Toast.LENGTH_SHORT).show();

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(VerificationActivity.this, error.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
            );

            queue.add(request);

        }
    }

    private boolean isValidate(String verificationCode){

        TextInputLayout tilVerificationCode = findViewById(R.id.tilVerificationCode);

        if(verificationCode.isEmpty()){
            tilVerificationCode.setError("Confirmation Code required");
            return false;
        }
        else if(verificationCode.length() != 6){
            tilVerificationCode.setError("Confirmation Code must contains six digits");
            return false;
        }
        else{
            tilVerificationCode.setError(null);
        }

        return true;
    }
}