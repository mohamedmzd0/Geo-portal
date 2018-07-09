package com.example.mohamedabdelaziz.geo.companymain;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mohamedabdelaziz.geo.Constants;
import com.example.mohamedabdelaziz.geo.CreateAPiServices;
import com.example.mohamedabdelaziz.geo.MedicalReps;
import com.example.mohamedabdelaziz.geo.MedicalRepsResponse;
import com.example.mohamedabdelaziz.geo.MessageResponse;
import com.example.mohamedabdelaziz.geo.R;
import com.example.mohamedabdelaziz.geo.dbhelper.DbHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailsActivity extends AppCompatActivity {

    private EditText name, date, quantity, doctor, price;
    private Button redirect;
    private Spinner spinner_medicals;
    public List<MedicalRepsResponse.MedRep> medicalReps = new ArrayList<>();
    private String selectedID = null;
    private DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        name = findViewById(R.id.et_odername);
        date = findViewById(R.id.et_oderdate);
        quantity = findViewById(R.id.et_oderquantity);
        doctor = findViewById(R.id.et_oderdoctor);
        price = findViewById(R.id.et_oderprice);
        redirect = findViewById(R.id.redirect_btn);
        dbHelper = new DbHelper(getApplicationContext());
        spinner_medicals = findViewById(R.id.spinner_medicals);
        if (getIntent() != null) {
            name.setText(getIntent().getStringExtra(Constants.PRODUCT_NAME));
            date.setText(getIntent().getStringExtra(Constants.PRODUCT_DATE));
            quantity.setText(getIntent().getStringExtra(Constants.PRODUCT_QUANTITY));
            doctor.setText(getIntent().getStringExtra(Constants.DOCTOR_NAME));
            price.setText(getIntent().getStringExtra(Constants.PRODUCT_PRICE));
        }
        redirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedID != null) {
                    redirect();
                } else
                    Toast.makeText(OrderDetailsActivity.this, "no medical reps selected", Toast.LENGTH_SHORT).show();
            }
        });

        CreateAPiServices.createInterface().getMyMedReps("get_my_medical_reps.php").enqueue(new Callback<MedicalRepsResponse>() {
            @Override
            public void onResponse(Call<MedicalRepsResponse> call, Response<MedicalRepsResponse> response) {
                if (response.code() == 200 && response.body().getSuccess() == 1) {
                    medicalReps = response.body().getMedReps();
                    formatResponse();
                }
            }

            @Override
            public void onFailure(Call<MedicalRepsResponse> call, Throwable t) {
                medicalReps = dbHelper.getMembers();
                formatResponse();
            }
        });
        spinner_medicals.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    Snackbar.make(spinner_medicals, "please select the medical rep", Snackbar.LENGTH_SHORT).show();
                } else
                    selectedID = medicalReps.get(i - 1).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void redirect() {
        CreateAPiServices.createInterface().redirectProduct("redirect.php?itemid=" + getIntent().getStringExtra(Constants.PRODUCT_ID)
                + "&mid=" + selectedID).enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                Toast.makeText(OrderDetailsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                Toast.makeText(OrderDetailsActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void formatResponse() {
        ArrayList<String> strings = new ArrayList<>();
        strings.add("select medical rep");
        for (int i = 0; i < medicalReps.size(); i++) {
            strings.add(medicalReps.get(i).getName());
        }
        setUpSpinnerAdapter(strings);
    }

    private void setUpSpinnerAdapter(ArrayList<String> strings) {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, strings);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_medicals.setAdapter(dataAdapter);
    }


}
