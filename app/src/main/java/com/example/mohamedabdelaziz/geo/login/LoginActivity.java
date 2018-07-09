package com.example.mohamedabdelaziz.geo.login;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.mohamedabdelaziz.geo.Constants;
import com.example.mohamedabdelaziz.geo.login.Fragment.LoginFragment;
import com.example.mohamedabdelaziz.geo.login.Fragment.RegisterFragment;
import com.example.mohamedabdelaziz.geo.R;

public class LoginActivity extends AppCompatActivity implements SwitchFragment {

    boolean doubleBackToExitPressedOnce = false, register = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        launch_register();

    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        } else if (register)
            back_login();

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 1000);
    }

    @Override
    public void launch_register() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        LoginFragment loginFragment = new LoginFragment();
        loginFragment.setSwf(this);
        fragmentTransaction.replace(R.id.container, loginFragment, "").commit();
    }

    @Override
    public void launch_create_account(String user) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.USER_TYPE, user);
        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        RegisterFragment registerFragment = new RegisterFragment();
        registerFragment.setArguments(bundle);
        registerFragment.setSwf(this);
        register = true;
        fragmentTransaction.replace(R.id.container, registerFragment, "").commit();
    }

    @Override
    public void back_login() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.exit_back, R.anim.back, R.anim.pop_exit, R.anim.pop_enter);
        LoginFragment loginFragment = new LoginFragment();
        loginFragment.setSwf(this);
        register = false;
        fragmentTransaction.replace(R.id.container, loginFragment, "").commit();
    }

}
