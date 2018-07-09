package com.example.mohamedabdelaziz.geo.companymain;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mohamedabdelaziz.geo.ApiServices;
import com.example.mohamedabdelaziz.geo.Constants;
import com.example.mohamedabdelaziz.geo.CreateAPiServices;
import com.example.mohamedabdelaziz.geo.MessageResponse;
import com.example.mohamedabdelaziz.geo.R;
import com.example.mohamedabdelaziz.geo.UsersResponse;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailsActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1000;
    private TextView name, desc, prec, price;
    private Button button_add_delete;
    private ImageButton add_image;
    private Uri filePath;
    private Bitmap bitmap;
    private boolean isImageUpdated = false;
    String image = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        name = findViewById(R.id.product_name);
        desc = findViewById(R.id.desc);
        prec = findViewById(R.id.prec);
        price = findViewById(R.id.price);
        button_add_delete = findViewById(R.id.button_add_delete);
        add_image = findViewById(R.id.add_image);
        add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE);
            }
        });
        if (getIntent() != null && getIntent().getStringExtra(Constants.PRODUCT_NAME) != null) {
            name.setText(getIntent().getStringExtra(Constants.PRODUCT_NAME));
            desc.setText(getIntent().getStringExtra(Constants.PRODUCT_DESCR));
            prec.setText(getIntent().getStringExtra(Constants.PRECAUTIONS));
            price.setText(getIntent().getStringExtra(Constants.PRODUCT_PRICE));
            button_add_delete.setText("delete");
            add_image.setEnabled(false);
            image = getIntent().getStringExtra(Constants.PRODUCT_IMAGE);
            button_add_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CreateAPiServices.createInterface().deleteProduct("delete_product.php?productid=" + getIntent().getStringExtra(Constants.PRODUCT_ID)).enqueue(new Callback<UsersResponse>() {
                        @Override
                        public void onResponse(Call<UsersResponse> call, Response<UsersResponse> response) {
                            finish();
                            Toast.makeText(ProductDetailsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<UsersResponse> call, Throwable t) {
                            Toast.makeText(ProductDetailsActivity.this, "check your connection", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            try {
                Picasso.with(getApplicationContext()).load("http://10.0.2.2/graduation/ecommerce/admin/uploads/medicin_images/" + getIntent().getStringExtra(Constants.PRODUCT_IMAGE))
                        .into(add_image);

            } catch (Exception e) {

            }
        } else {

            button_add_delete.setText("Add");
            button_add_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isInputsValid())
                        add();
                }
            });
        }
    }

    private void add() {
        if (isImageUpdated)
            image = imageToString();
        CreateAPiServices.createInterfaceForImage().add_product(name.getText().toString(), desc.getText().toString(),
                prec.getText().toString(), price.getText().toString(), image, String.valueOf(new Date().getTime() + ".jpg"),
                getSharedPreferences(Constants.SESSION, Context.MODE_PRIVATE).getInt(Constants.UID, 0) + "").enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                finish();
                Toast.makeText(ProductDetailsActivity.this, response.message(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                Toast.makeText(ProductDetailsActivity.this, "failed", Toast.LENGTH_SHORT).show();
                Log.d("ERROR", t.getLocalizedMessage());
            }
        });
    }

    private void update(String id) {
        int updated = 0;
        Toast.makeText(this, "updateeeee", Toast.LENGTH_SHORT).show();
        if (isImageUpdated) {
            image = imageToString();
            updated = 1;
        }

        CreateAPiServices.createInterfaceForImage().update_product(name.getText().toString(), desc.getText().toString(),
                prec.getText().toString(), price.getText().toString(), image, String.valueOf(new Date().getTime() + ".jpg"),
                getSharedPreferences(Constants.SESSION, Context.MODE_PRIVATE).getInt(Constants.UID, 0) + "", updated, id).enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                finish();
                Toast.makeText(ProductDetailsActivity.this, response.message(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                Toast.makeText(ProductDetailsActivity.this, "failed", Toast.LENGTH_SHORT).show();
                Log.d("ERROR", t.getLocalizedMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                isImageUpdated = true;
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                add_image.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String imageToString() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imagebyte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imagebyte, Base64.DEFAULT);

    }

    private boolean isInputsValid() {
        boolean valid = true;
        if (TextUtils.isEmpty(name.getText())) {
            name.setError("required field");
            valid = false;
        }
        if (TextUtils.isEmpty(desc.getText())) {
            desc.setError("required field");
            valid = false;
        }
        if (TextUtils.isEmpty(prec.getText())) {
            prec.setError("required field");
            valid = false;
        }
        if (TextUtils.isEmpty(price.getText())) {
            price.setError("required field");
            valid = false;
        }

        return valid;

    }
}