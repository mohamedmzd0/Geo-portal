package com.example.mohamedabdelaziz.geo.login.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.mohamedabdelaziz.geo.Constants;
import com.example.mohamedabdelaziz.geo.CreateAPiServices;
import com.example.mohamedabdelaziz.geo.login.SwitchFragment;
import com.example.mohamedabdelaziz.geo.LoginResponse;
import com.example.mohamedabdelaziz.geo.R;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Mohamed Abd Elaziz on 1/25/2018.
 */

public class RegisterFragment extends Fragment implements View.OnClickListener {
    private SwitchFragment swf;
    private CircularProgressButton mButton_regsiter;
    private ImageButton mImageButton_back;
    private EditText mEditText_username, mEditText_password, mEditText_confirm, mEditText_email, mEditText_full_name;
    private String USER_TYPE = null;

    public void setSwf(SwitchFragment swf) {
        this.swf = swf;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView;
        rootView = inflater.inflate(R.layout.register, null);
        USER_TYPE = getArguments().getString(Constants.USER_TYPE);
        setUpViews(rootView);
        mButton_regsiter.setOnClickListener(this);
        mImageButton_back.setOnClickListener(this);
        if (USER_TYPE.equals(Constants.USER))
            mEditText_full_name.setHint("full name");
        else
            mEditText_full_name.setHint("telephone");
        return rootView;
    }

    private void setUpViews(View rootView) {
        mButton_regsiter = rootView.findViewById(R.id.btn_register);
        mImageButton_back = rootView.findViewById(R.id.btn_back);
        mEditText_username = rootView.findViewById(R.id.et_username);
        mEditText_email = rootView.findViewById(R.id.et_email);
        mEditText_password = rootView.findViewById(R.id.et_password);
        mEditText_confirm = rootView.findViewById(R.id.et_confirm_password);
        mEditText_full_name = rootView.findViewById(R.id.et_full_name);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_back)
            swf.back_login();
        else if (v.getId() == R.id.btn_register) {
            if (validData()) {
                if (USER_TYPE.equals(Constants.COMPANY))
                    CompanyRegister();
                else if (USER_TYPE.equals(Constants.USER))
                    userRegister();
                else if (USER_TYPE.equals(Constants.MEDRIP))
                    medicalRepRegister();

                mButton_regsiter.startAnimation();
            }

        }
    }

    private boolean validData() {
        boolean valid = true;
        if (TextUtils.isEmpty(mEditText_username.getText()) || mEditText_username.getText().toString().trim().length() < 1) {
            mEditText_username.setError("username require");
            valid = false;
        }
        if (TextUtils.isEmpty(mEditText_email.getText())) {
            mEditText_email.setError("email require");
            valid = false;
        }
        if (TextUtils.isEmpty(mEditText_password.getText())) {
            mEditText_password.setError("password require");
            valid = false;
        }
        if (TextUtils.isEmpty(mEditText_confirm.getText().toString())) {
            mEditText_confirm.setError("confirm password require");
            valid = false;
        }
        if (!mEditText_password.getText().toString().equals(mEditText_confirm.getText().toString())) {
            mEditText_confirm.setError("password not match");
            valid = false;
        }
        if (TextUtils.isEmpty(mEditText_full_name.getText())) {
            mEditText_full_name.setError("full name require");
            valid = false;
        }
        if (!(mEditText_email.getText().toString().length() > 3 && mEditText_email.getText().toString().contains("@") && mEditText_email.getText().toString().contains("."))) {
            mEditText_email.setError("email not valid");
            valid = false;
        }

        return valid;
    }

    private void userRegister() {
        Call<LoginResponse> response = CreateAPiServices.createInterface().user_register(mEditText_username.getText().toString(), mEditText_full_name.getText().toString(),
                mEditText_email.getText().toString(), mEditText_password.getText().toString(), mEditText_confirm.getText().toString());
        response.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, retrofit2.Response<LoginResponse> response) {
                if (response.code() == 200 && response.body().getSuccess() == 1)
                    swf.back_login();
                else
                    Snackbar.make(getView(), response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                stopLoading();
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                stopLoading();
                Snackbar.make(getView(), t.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();

            }
        });
    }

    private void CompanyRegister() {
        Call<LoginResponse> response = CreateAPiServices.createInterface().company_register(mEditText_username.getText().toString(), mEditText_full_name.getText().toString(),
                mEditText_email.getText().toString(), mEditText_password.getText().toString(), mEditText_confirm.getText().toString());
        response.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, retrofit2.Response<LoginResponse> response) {

                if (response.code() == 200 && response.body().getSuccess() == 1)
                    swf.back_login();
                else
                    Snackbar.make(getView(), response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                stopLoading();

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                stopLoading();
                Snackbar.make(getView(), t.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();

            }
        });
    }

    private void medicalRepRegister() {
        Call<LoginResponse> response = CreateAPiServices.createInterface().medical_register(mEditText_username.getText().toString(), mEditText_full_name.getText().toString(),
                mEditText_email.getText().toString(), mEditText_password.getText().toString(), mEditText_confirm.getText().toString());
        response.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, retrofit2.Response<LoginResponse> response) {
                if (response.code() == 200 && response.body().getSuccess() == 1)
                    swf.back_login();
                else
                    Snackbar.make(getView(), response.body().getMessage(), Snackbar.LENGTH_LONG).show();
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
        mButton_regsiter.revertAnimation();
        mButton_regsiter.stopAnimation();
    }



}
