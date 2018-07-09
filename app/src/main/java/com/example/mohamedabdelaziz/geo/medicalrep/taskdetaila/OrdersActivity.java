package com.example.mohamedabdelaziz.geo.medicalrep.taskdetaila;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mohamedabdelaziz.geo.Constants;
import com.example.mohamedabdelaziz.geo.CreateAPiServices;
import com.example.mohamedabdelaziz.geo.MessageResponse;
import com.example.mohamedabdelaziz.geo.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdersActivity extends AppCompatActivity {
    public static final String INDEX_KEY = "INDEX_OF_ACTIVITY";
    private TextView mEditText_name, mEditText_order_date, mEditText_doctor, mEditText_quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        if (getIntent() != null && getIntent().getStringExtra(INDEX_KEY) != null)
            getSupportActionBar().setTitle(getIntent().getStringExtra(INDEX_KEY));
        setUpViews();

        if (getIntent() != null) {
            mEditText_name.setText(getIntent().getStringExtra(Constants.PRODUCT_NAME));
            mEditText_order_date.setText(getIntent().getStringExtra(Constants.PRODUCT_DATE));
            mEditText_quantity.setText(getIntent().getStringExtra(Constants.PRODUCT_QUANTITY));
            mEditText_doctor.setText(getIntent().getStringExtra(Constants.USER_NAME));


        }
    }

    private void setUpViews() {
        mEditText_name = findViewById(R.id.tv_medicine_name);
        mEditText_order_date = findViewById(R.id.tv_order_date);
        mEditText_doctor = findViewById(R.id.tv_doctor);
        mEditText_quantity = findViewById(R.id.tv_quantity);
    }

    public void approve(View view) {
        CreateAPiServices.createInterface().orderDeliverd("approve.php?itemid=" + getIntent().getStringExtra(Constants.PRODUCT_ID)).enqueue(
                new Callback<MessageResponse>() {
                    @Override
                    public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                        if (response.code() == 200 && response.body().getSuccess() == 1) {
                            finish();
                        }
                        Toast.makeText(OrdersActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<MessageResponse> call, Throwable t) {
                        Toast.makeText(OrdersActivity.this, "check connection", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
}
