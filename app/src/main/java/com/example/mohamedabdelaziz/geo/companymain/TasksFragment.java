package com.example.mohamedabdelaziz.geo.companymain;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mohamedabdelaziz.geo.Constants;
import com.example.mohamedabdelaziz.geo.CreateAPiServices;
import com.example.mohamedabdelaziz.geo.GovernatesResponse;
import com.example.mohamedabdelaziz.geo.MedicalRepsResponse;
import com.example.mohamedabdelaziz.geo.MessageResponse;
import com.example.mohamedabdelaziz.geo.Product;
import com.example.mohamedabdelaziz.geo.ProductsModel;
import com.example.mohamedabdelaziz.geo.R;
import com.example.mohamedabdelaziz.geo.TasksResponse;
import com.example.mohamedabdelaziz.geo.dbhelper.DbHelper;
import com.example.mohamedabdelaziz.geo.ecommerce.MainEcommerce;
import com.example.mohamedabdelaziz.geo.medicalrep.fragment.SpinnerAdapter;
import com.example.mohamedabdelaziz.geo.medicalrep.fragment.TVHolder;
import com.example.mohamedabdelaziz.geo.medicalrep.taskdetaila.TasksActivity;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Mohamed Abd Elaziz on 3/8/2018.
 */

public class TasksFragment extends Fragment {

    private Spinner medicine, country, rep;
    Button ok;
    DbHelper dbHelper;
    List<Product> products = new ArrayList<>();
    List<GovernatesResponse.Governate> countries = new ArrayList<>();
    List<MedicalRepsResponse.MedRep> reps = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.add_task_fragment, null);
        ok = rootView.findViewById(R.id.ok);
        dbHelper = new DbHelper(getContext());
        medicine = rootView.findViewById(R.id.medicine_name);
        rep = rootView.findViewById(R.id.medical_rep);
        country = rootView.findViewById(R.id.country);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (products != null && products.size() > 0 && countries != null && countries.size() > 0 && reps != null && reps.size() > 0)
                    setTask();
            }
        });
        return rootView;
    }

    private void setTask() {
        CreateAPiServices.createInterface().SetOrder("set_task.php?med_id=" + reps.get(rep.getSelectedItemPosition()).getId() +
                "&product_id=" + products.get(medicine.getSelectedItemPosition()).getId() +
                "&country_id=" + countries.get(country.getSelectedItemPosition()).getId()).enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                Toast.makeText(getContext(), "task success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                Toast.makeText(getContext(), "can't work offline", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getProducts();
        getMyMembers();
        getCounties();
    }

    private void getProducts() {
        Call<ProductsModel> response = CreateAPiServices.createInterface().getProducts();
        response.enqueue(new Callback<ProductsModel>() {
            @Override
            public void onResponse(Call<ProductsModel> call, Response<ProductsModel> response) {
                products = response.body().getProducts();
                setUpMedicine();
            }

            @Override
            public void onFailure(Call<ProductsModel> call, Throwable t) {
                Toast.makeText(getContext(), "check your connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpMedicine() {
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            strings.add(products.get(i).getMedcineName());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, strings);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        medicine.setAdapter(dataAdapter);
    }

    private void getCounties() {
        CreateAPiServices.createInterface().requestCountry("request_country.php?gov=" + "-1").enqueue(new Callback<GovernatesResponse>() {
            @Override
            public void onResponse(Call<GovernatesResponse> call, Response<GovernatesResponse> response) {
                if (response.code() == 200) {
                    countries = response.body().getGovernates();
                    dbHelper.addCountries(countries);
                    setUpCounties();
                }
            }

            @Override
            public void onFailure(Call<GovernatesResponse> call, Throwable t) {
                countries = dbHelper.getCountries();
                country.setAdapter(new SpinnerAdapter(getContext(), countries));
            }
        });

    }

    private void setUpCounties() {
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < countries.size(); i++) {
            strings.add(countries.get(i).getName());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, strings);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        country.setAdapter(dataAdapter);

    }

    private void getMyMembers() {
        CreateAPiServices.createInterface().getMyMembers("get_my_medical_reps.php?")
                .enqueue(new Callback<MedicalRepsResponse>() {
                    @Override
                    public void onResponse(Call<MedicalRepsResponse> call, Response<MedicalRepsResponse> response) {
                        reps = response.body().getMedReps();
                        setUpReps();
                    }

                    @Override
                    public void onFailure(Call<MedicalRepsResponse> call, Throwable t) {

                    }
                });

    }

    private void setUpReps() {
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < reps.size(); i++) {
            strings.add(reps.get(i).getName());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, strings);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rep.setAdapter(dataAdapter);
    }

}
