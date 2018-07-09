package com.example.mohamedabdelaziz.geo.medicalrep.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mohamedabdelaziz.geo.Constants;
import com.example.mohamedabdelaziz.geo.R;
import com.example.mohamedabdelaziz.geo.splashactivity.SplashActivity;

public class ProfielFragment extends Fragment {

    private TextView tvName, tvEmail, address;
    private FloatingActionButton mFloatingActionButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_medical_profile, container, false);

        tvName = rootView.findViewById(R.id.tvNumber1);
        tvEmail = rootView.findViewById(R.id.tvNumber3);
        mFloatingActionButton = rootView.findViewById(R.id.logout);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = getActivity().getSharedPreferences(Constants.SESSION, Context.MODE_PRIVATE).edit();
                editor.putBoolean(Constants.IS_LOGGED_IN, false);
                editor.commit();
                startActivity(new Intent(getContext(), SplashActivity.class));
                getActivity().finish();
            }
        });
        address = rootView.findViewById(R.id.tvNumber5);
        SharedPreferences preferences = getActivity().getSharedPreferences(Constants.SESSION, Context.MODE_PRIVATE);
        tvName.setText(preferences.getString(Constants.USER_NAME, "not available now"));
        tvEmail.setText(preferences.getString(Constants.EMAIL, "not available now"));
        address.setText(preferences.getString(Constants.FULL_NAME, "not available now"));

        return rootView;
    }
}

