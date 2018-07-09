package com.example.mohamedabdelaziz.geo.login.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mohamedabdelaziz.geo.Constants;
import com.example.mohamedabdelaziz.geo.CreateAPiServices;
import com.example.mohamedabdelaziz.geo.LoginResponse;
import com.example.mohamedabdelaziz.geo.R;
import com.example.mohamedabdelaziz.geo.companymain.CompanyMainActivity;
import com.example.mohamedabdelaziz.geo.ecommerce.MainEcommerce;
import com.example.mohamedabdelaziz.geo.login.SwitchFragment;
import com.example.mohamedabdelaziz.geo.medicalrep.MedicalRepMain;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mohamed Abd Elaziz on 1/25/2018.
 */

public class LoginFragment extends Fragment implements View.OnClickListener {
    private Button mButton_create_account;
    private CircularProgressButton mButton_login;
    private SwitchFragment swf;
    private EditText mEditText_userName, mEditText_passwword;

    public void setSwf(SwitchFragment swf) {
        this.swf = swf;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.login, null);
        if (getActivity().getIntent().getSerializableExtra(Constants.USER_TYPE).equals(Constants.MEDRIP))
            ((CircleImageView) rootView.findViewById(R.id.user_image)).setImageResource(R.drawable.user);
        else if (getActivity().getIntent().getSerializableExtra(Constants.USER_TYPE).equals(Constants.COMPANY)) {
            ((CircleImageView) rootView.findViewById(R.id.user_image)).setImageResource(R.drawable.company);
        } else if (getActivity().getIntent().getSerializableExtra(Constants.USER_TYPE).equals(Constants.USER))
            ((CircleImageView) rootView.findViewById(R.id.user_image)).setImageResource(R.drawable.pharmacy_logo);
        setUpViews(rootView);
        if (!getActivity().getIntent().getSerializableExtra(Constants.USER_TYPE).equals(Constants.MEDRIP))
            mButton_create_account.setOnClickListener(this);
        else
            mButton_create_account.setVisibility(View.GONE);
        mButton_login.setOnClickListener(this);

        return rootView;
    }

    private void setUpViews(View view) {
        mButton_login = view.findViewById(R.id.btn_login);
        mButton_create_account = view.findViewById(R.id.btn_create_account);
        mEditText_userName = view.findViewById(R.id.et_username);
        mEditText_passwword = view.findViewById(R.id.et_password);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_create_account)
            swf.launch_create_account(getActivity().getIntent().getStringExtra(Constants.USER_TYPE));
        else if (v.getId() == R.id.btn_login && getActivity().getIntent().getSerializableExtra(Constants.USER_TYPE).equals(Constants.COMPANY)) {
            if (existsValidData()) {
                mButton_login.startAnimation();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        compLogin();
                    }
                }, 1000);
            }
        } else if (v.getId() == R.id.btn_login && getActivity().getIntent().getSerializableExtra(Constants.USER_TYPE).equals(Constants.MEDRIP)) {
            if (existsValidData()) {
                mButton_login.startAnimation();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        medicalLogin();
                    }
                }, 1000);
            }
        } else if (v.getId() == R.id.btn_login && getActivity().getIntent().getSerializableExtra(Constants.USER_TYPE).equals(Constants.USER)) {
            if (existsValidData()) {
                mButton_login.startAnimation();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        userLogin();
                    }
                }, 1000);

            }
        }
    }


    private void userLogin() {
        Call<LoginResponse> response = CreateAPiServices.createInterface().user_login(mEditText_userName.getText().toString(), mEditText_passwword.getText().toString());
        response.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, retrofit2.Response<LoginResponse> response) {
                if (response.body().getSuccess() == 1) {
                    startSession(response, Constants.USER);
                    startActivity(new Intent(getContext(), MainEcommerce.class));
                    getActivity().finish();
                } else
                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                stopLoading();
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                stopLoading();
                Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startSession(Response<LoginResponse> response, String userType) {
        SharedPreferences.Editor editor = getActivity().getSharedPreferences(Constants.SESSION, Context.MODE_PRIVATE).edit();
        editor.putBoolean(Constants.IS_LOGGED_IN, true);
        editor.putString(Constants.USER_TYPE, userType);
        editor.putInt(Constants.UID, response.body().getId());
        editor.putString(Constants.EMAIL, response.body().getEmail());
        editor.putString(Constants.FULL_NAME, response.body().getFullName());
        editor.putString(Constants.USER_NAME, response.body().getUsername());
        editor.putString(Constants.CITY, response.body().getCity());
        editor.apply();
        editor.commit();
    }

    private void compLogin() {
        Call<LoginResponse> response = CreateAPiServices.createInterface().comp_login(mEditText_userName.getText().toString(),
                mEditText_passwword.getText().toString());
        response.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, retrofit2.Response<LoginResponse> response) {
                if (response.code() == 200 && response.body().getSuccess() == 1) {
                    startSession(response, Constants.COMPANY);
                    startActivity(new Intent(getContext(), CompanyMainActivity.class));
                    getActivity().finish();
                } else
                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                stopLoading();
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                stopLoading();
                Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void medicalLogin() {
        Call<LoginResponse> response = CreateAPiServices.createInterface().medical_login(mEditText_userName.getText().toString(), mEditText_passwword.getText().toString());
        response.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, retrofit2.Response<LoginResponse> response) {
                if (response.code() == 200 && response.body().getSuccess() == 1) {
                    startSession(response, Constants.MEDRIP);
                    startActivity(new Intent(getContext(), MedicalRepMain.class));
                    getActivity().finish();
                } else {
                    Snackbar.make(getView(), response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                }
                stopLoading();
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                stopLoading();
                Snackbar.make(getView(), t.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();

            }
        });

    }

    private void stopLoading() {
        mButton_login.revertAnimation();
        mButton_login.stopAnimation();
    }

    private boolean existsValidData() {
        boolean valid = true;
        if (TextUtils.isEmpty(mEditText_userName.getText()) || (mEditText_userName.getText().toString().trim().length() < 1)) {
            mEditText_userName.setError("username require");
            valid = false;
        }
        if (TextUtils.isEmpty(mEditText_passwword.getText())) {
            mEditText_passwword.setError("username require");
            valid = false;
        }
        if (mEditText_userName.length() < 3) {
            mEditText_userName.setError("Username not valid");
            valid = false;
        }
        if (mEditText_passwword.length() < 3) {
            mEditText_passwword.setError("password not valid");
            valid = false;
        }
        return valid;
    }
}
