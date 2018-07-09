package com.example.mohamedabdelaziz.geo.ecommerce;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mohamedabdelaziz.geo.Constants;
import com.example.mohamedabdelaziz.geo.CreateAPiServices;
import com.example.mohamedabdelaziz.geo.LoginResponse;
import com.example.mohamedabdelaziz.geo.R;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetail extends AppCompatActivity {

    ImageView mImageView_product_image;
    TextView mTextView_product_name, mTextView_product_price;
    Button mButton_order;
    AppCompatEditText mEditText_quantity;
    String product_id = "0";
    TabLayout tabLayout;
    TextView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        mImageView_product_image = findViewById(R.id.product_image);
        mButton_order = findViewById(R.id.order);
        mTextView_product_name = findViewById(R.id.product_name);
        mTextView_product_price = findViewById(R.id.product_price);
        mEditText_quantity = findViewById(R.id.et_quantity);
        tabLayout = findViewById(R.id.tabs);
        content = findViewById(R.id.container);
        if (getIntent() != null) {
            product_id = getIntent().getStringExtra(Constants.PRODUCT_ID);

            try {
                Picasso.with(getApplicationContext()).load("http://10.0.2.2/graduation/ecommerce/admin/uploads/medicin_images/" + getIntent().getStringExtra(Constants.PRODUCT_IMAGE))
                        .into(mImageView_product_image);

            } catch (Exception e) {

            }
            content.setText(getIntent().getStringExtra(Constants.PRODUCT_DESCR));
            tabLayout.setOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    if (tab.getPosition() == 0)
                        content.setText(getIntent().getStringExtra(Constants.PRODUCT_DESCR));
                    else if (tab.getPosition() == 1)
                        content.setText(getIntent().getStringExtra(Constants.PRECAUTIONS));
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

            mTextView_product_price.setText(getIntent().getStringExtra(Constants.PRODUCT_PRICE) + " $ ");
            mTextView_product_name.setText(getIntent().getStringExtra(Constants.PRODUCT_NAME));


        }


        mButton_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(mEditText_quantity.getText().toString()))
                    OrderRequest();
            }
        });
    }

    private void OrderRequest() {
        Call<LoginResponse> response = CreateAPiServices.createInterface().requestOrder("request_product.php?user_id=" +
                getSharedPreferences(Constants.SESSION, Context.MODE_PRIVATE).getInt(Constants.UID, 0)
                + "&product_id=" + product_id + "&quantity=" + mEditText_quantity.getText().toString());
        response.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                Toast.makeText(ProductDetail.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                if (response.code() == 200 && response.body().getSuccess() == 1)
                    mEditText_quantity.setText("");
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(ProductDetail.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
