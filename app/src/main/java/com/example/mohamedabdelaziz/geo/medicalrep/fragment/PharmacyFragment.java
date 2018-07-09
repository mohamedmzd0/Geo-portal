package com.example.mohamedabdelaziz.geo.medicalrep.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mohamedabdelaziz.geo.Constants;
import com.example.mohamedabdelaziz.geo.CreateAPiServices;
import com.example.mohamedabdelaziz.geo.GovernatesResponse;
import com.example.mohamedabdelaziz.geo.PharmacyResponse;
import com.example.mohamedabdelaziz.geo.R;
import com.example.mohamedabdelaziz.geo.dbhelper.DbHelper;
import com.example.mohamedabdelaziz.geo.map.MapActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Mohamed Abd Elaziz on 3/8/2018.
 */

public class PharmacyFragment extends Fragment {
    RecyclerView mRecyclerView;
    CardView mCardView_dialog;
    TextView mTextView_dialog_title;
    TextView mTextView_ok;
    FloatingActionButton mFloatingActionButton_search;
    private Spinner mSpinner_governate, mSpinner_city;
    private List<GovernatesResponse.Governate> governates = new ArrayList<>();
    private List<GovernatesResponse.Governate> countries = new ArrayList<>();
    String selectCountryID = "0";
    private DbHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_hospital, null);
        mRecyclerView = rootView.findViewById(R.id.recylcer);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        mRecyclerView.setHasFixedSize(false);
        mCardView_dialog = rootView.findViewById(R.id.dialog_card);
        mTextView_dialog_title = rootView.findViewById(R.id.dialog_title);
        mTextView_ok = rootView.findViewById(R.id.ok);
        dbHelper = new DbHelper(getContext());
        mFloatingActionButton_search = rootView.findViewById(R.id.search_fab);
        mFloatingActionButton_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCardView_dialog.animate().setDuration(400).translationY(0);
                mFloatingActionButton_search.setVisibility(View.GONE);
            }
        });
        mTextView_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCardView_dialog.animate().setDuration(400).translationY(getActivity().getWindowManager().getDefaultDisplay().getHeight());
                mFloatingActionButton_search.setVisibility(View.VISIBLE);

                CreateAPiServices.createInterface().medicalrepPharmacies("request_pharmacies.php?country=" + selectCountryID).
                        enqueue(new Callback<PharmacyResponse>() {
                            @Override
                            public void onResponse(Call<PharmacyResponse> call, Response<PharmacyResponse> response) {
                                if (response.code() == 200 && response.body().getSuccess() == 1) {
                                    mRecyclerView.setAdapter(new RecyclerAdapter(response.body().getPharmacyProducts()));
                                    dbHelper.addPharmacies(response.body().getPharmacyProducts());
                                } else
                                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<PharmacyResponse> call, Throwable t) {
                                mRecyclerView.setAdapter(new RecyclerAdapter(dbHelper.getPharmacies()));
                            }
                        });
            }
        });

        mSpinner_governate = rootView.findViewById(R.id.sp_governate);
        mSpinner_city = rootView.findViewById(R.id.sp_city);
        CreateAPiServices.createInterface().requestGovernates().enqueue(new Callback<GovernatesResponse>() {
            @Override
            public void onResponse(Call<GovernatesResponse> call, Response<GovernatesResponse> response) {
                if (response.code() == 200 && response.body().getSuccess() == 1) {
                    governates = response.body().getGovernates();
                    governates.add(0, new GovernatesResponse.Governate("0", "select your governate"));
                    mSpinner_governate.setAdapter(new SpinnerAdapter(getContext(), governates));
                    dbHelper.addGovernates(response.body().getGovernates());
                }
            }

            @Override
            public void onFailure(Call<GovernatesResponse> call, Throwable t) {
                governates = dbHelper.getGovernates();
                mSpinner_governate.setAdapter(new SpinnerAdapter(getContext(), governates));
            }
        });
        mSpinner_governate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0)
                    return;
                CreateAPiServices.createInterface().requestCountry("request_country.php?gov=" + governates.get(i).getId()).enqueue(new Callback<GovernatesResponse>() {
                    @Override
                    public void onResponse(Call<GovernatesResponse> call, Response<GovernatesResponse> response) {
                        if (response.code() == 200) {
                            countries = response.body().getGovernates();
                            dbHelper.addCountries(countries);
                            mSpinner_city.setAdapter(new SpinnerAdapter(getContext(), countries));
                        }
                    }

                    @Override
                    public void onFailure(Call<GovernatesResponse> call, Throwable t) {
                        countries = dbHelper.getCountries();
                        mSpinner_city.setAdapter(new SpinnerAdapter(getContext(), countries));
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mSpinner_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectCountryID = countries.get(i).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return rootView;
    }


    class RecyclerAdapter extends RecyclerView.Adapter<HospitalHolder> {
        private List<PharmacyResponse.PharmacyProduct> pharmacyProducts;

        public RecyclerAdapter(List<PharmacyResponse.PharmacyProduct> pharmacyProducts) {
            this.pharmacyProducts = pharmacyProducts;
        }

        @Override
        public HospitalHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.hospital_recycler_item, null);
            return new HospitalHolder(view);
        }

        @Override
        public void onBindViewHolder(HospitalHolder holder, final int i) {
            holder.id.setText(String.valueOf(i + 1));
            holder.textView_close_open.setText("open : " + pharmacyProducts.get(i).getOpen() + ", close :" + pharmacyProducts.get(i).getClose());
            holder.content.setText(pharmacyProducts.get(i).getName());
            holder.content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), MapActivity.class);
                    intent.putExtra(Constants.LAT, pharmacyProducts.get(i).getLat());
                    intent.putExtra(Constants.LONG, pharmacyProducts.get(i).getLang());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return pharmacyProducts.size();
        }
    }


}
