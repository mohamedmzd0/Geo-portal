package com.example.mohamedabdelaziz.geo.splashactivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.mohamedabdelaziz.geo.Constants;
import com.example.mohamedabdelaziz.geo.login.LoginActivity;
import com.example.mohamedabdelaziz.geo.R;
import com.example.mohamedabdelaziz.geo.companymain.CompanyMainActivity;
import com.example.mohamedabdelaziz.geo.ecommerce.MainEcommerce;
import com.example.mohamedabdelaziz.geo.medicalrep.MedicalRepMain;

public class SplashActivity extends AppCompatActivity {


    private final static int SPLASH_TIME = 1000;
    private Runnable mRunnable;
    private Handler mHandler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (!getSharedPreferences(Constants.SESSION, MODE_PRIVATE).getBoolean(Constants.IS_LOGGED_IN, false)) {

            mRunnable = new Runnable() {
                @Override
                public void run() {
                    showUserLayout();
                }
            };
            mHandler = new Handler();


        } else

        {
            if (getSharedPreferences(Constants.SESSION, MODE_PRIVATE).getString(Constants.USER_TYPE, Constants.MEDRIP).equals(Constants.MEDRIP)) {
                startActivity(new Intent(getApplicationContext(), MedicalRepMain.class));
                finish();
            } else if (getSharedPreferences(Constants.SESSION, MODE_PRIVATE).getString(Constants.USER_TYPE, Constants.MEDRIP).equals(Constants.COMPANY)) {
                startActivity(new Intent(getApplicationContext(), CompanyMainActivity.class));
                finish();
            } else {
                startActivity(new Intent(getApplicationContext(), MainEcommerce.class));
                finish();
            }
        }


    }


    @Override
    protected void onStart() {
        super.onStart();
        if (mHandler != null)
            mHandler.postDelayed(mRunnable, SPLASH_TIME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mHandler != null)
            mHandler.removeCallbacks(mRunnable);
    }

    public void launchMedical(View view) {
        medicaluser();
    }

    private void medicaluser() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.putExtra(Constants.USER_TYPE, Constants.MEDRIP);
        startActivity(intent);
        finish();
    }

    public void LaucnCompany(View view) {
        companyUser();
    }

    private void companyUser() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.putExtra(Constants.USER_TYPE, Constants.COMPANY);
        startActivity(intent);
        finish();
    }

    public void showUserLayout() {
        (findViewById(R.id.userLayout)).animate().setDuration(800).translationY((findViewById(R.id.logoLayout).getHeight() / 2));
        (findViewById(R.id.logoLayout)).animate().setDuration(800).translationY(-(findViewById(R.id.logoLayout).getHeight() / 2));
    }

    public void LaucnPharmacy(View view) {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.putExtra(Constants.USER_TYPE, Constants.USER);
        startActivity(intent);
        finish();
    }
}