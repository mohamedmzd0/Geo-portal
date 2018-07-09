package com.example.mohamedabdelaziz.geo.medicalrep.taskdetaila;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.mohamedabdelaziz.geo.Constants;
import com.example.mohamedabdelaziz.geo.R;
import com.example.mohamedabdelaziz.geo.map.MapActivity;

public class TasksActivity extends AppCompatActivity {
    private TextView mTextView_name, mTextView_price, mTextView_prec, mTextView_description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        setUpViews();
        mTextView_name.setText(getIntent().getStringExtra(Constants.PRODUCT_NAME));

        mTextView_prec.setText(getIntent().getStringExtra(Constants.PRECAUTIONS) + " $");

        mTextView_price.setText(getIntent().getStringExtra(Constants.PRODUCT_PRICE));

        mTextView_description.setText(getIntent().getStringExtra(Constants.PRODUCT_DESCR));


    }

    private void setUpViews() {
        mTextView_name = findViewById(R.id.tv_medicine_name);
        mTextView_description = findViewById(R.id.tv_description);
        mTextView_price = findViewById(R.id.tv_price);
        mTextView_prec = findViewById(R.id.tv_prec);

    }

    public void openMap(View view) {
        Intent intent = new Intent(getApplicationContext(), MapActivity.class);
        intent.putExtra(Constants.LONG, getIntent().getStringExtra(Constants.LONG));
        intent.putExtra(Constants.LAT, getIntent().getStringExtra(Constants.LAT));
        startActivity(intent);
        finish();
    }


}
