package com.example.mohamedabdelaziz.geo.companymain;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mohamedabdelaziz.geo.Constants;
import com.example.mohamedabdelaziz.geo.CreateAPiServices;
import com.example.mohamedabdelaziz.geo.MessageResponse;
import com.example.mohamedabdelaziz.geo.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddMedicalRepDialog extends AppCompatActivity {

    EditText user, email, pass, phone;
    Button add;

    String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medical_rep_dialog);
        user = findViewById(R.id.ed_user);
        email = findViewById(R.id.ed_email);
        pass = findViewById(R.id.ed_pass);
        phone = findViewById(R.id.ed_tele);
        add = findViewById(R.id.add_btn);
        userType = getIntent().getStringExtra(Constants.USER_TYPE);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputsIsValid()) {
                    if (userType.equals(Constants.MEDRIP))
                        addMember(user.getText().toString(), pass.getText().toString(), email.getText().toString(), phone.getText().toString());
                    else if (userType.equals(Constants.USER))
                        addUser(user.getText().toString(), pass.getText().toString(), email.getText().toString(), phone.getText().toString());

                }
            }
        });

    }

    private void addUser(String name, String pass, String email, String phone) {
        CreateAPiServices.createInterface().add_user(name, pass, email, phone)
                .enqueue(new Callback<MessageResponse>() {
                    @Override
                    public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                        if (response.code() == 200 && response.body().getSuccess() == 1) {
                            finish();
                        }
                        Toast.makeText(AddMedicalRepDialog.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<MessageResponse> call, Throwable t) {
                        Toast.makeText(AddMedicalRepDialog.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addMember(String name, String pass, String email, String phone) {
        CreateAPiServices.createInterface().add_member(name, pass, email, phone, String.valueOf(getSharedPreferences(Constants.SESSION, Context.MODE_PRIVATE).
                getInt(Constants.UID, 0)))
                .enqueue(new Callback<MessageResponse>() {
                    @Override
                    public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                        if (response.code() == 200 && response.body().getSuccess() == 1) {
                            finish();
                        }
                        Toast.makeText(AddMedicalRepDialog.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<MessageResponse> call, Throwable t) {
                        Toast.makeText(AddMedicalRepDialog.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean inputsIsValid() {
        boolean valid = true;
        if (TextUtils.isEmpty(user.getText())) {
            user.setError("required field");
            valid = false;
        }
        if (TextUtils.isEmpty(email.getText())) {
            email.setError("required field");
            valid = false;
        }
        if (TextUtils.isEmpty(pass.getText())) {
            pass.setError("required field");
            valid = false;
        }
        if (TextUtils.isEmpty(phone.getText())) {
            phone.setError("required field");
            valid = false;
        }
        return valid;
    }
}
