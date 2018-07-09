package com.example.mohamedabdelaziz.geo.chart;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mohamedabdelaziz.geo.CreateAPiServices;
import com.example.mohamedabdelaziz.geo.GovernatesResponse;
import com.example.mohamedabdelaziz.geo.R;
import com.example.mohamedabdelaziz.geo.SalesResponse;
import com.example.mohamedabdelaziz.geo.dbhelper.DbHelper;
import com.example.mohamedabdelaziz.geo.medicalrep.fragment.SpinnerAdapter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChartFragment extends Fragment {

    ArrayList<Float> ydata = new ArrayList<Float>();
    ArrayList<String> Xdata = new ArrayList<>();
    PieChart mPieChart;
    private BarChart mBarChart;
    private Spinner mSpinner_governate, mSpinner_city;
    private List<GovernatesResponse.Governate> governates = new ArrayList<>();
    private List<GovernatesResponse.Governate> countries = new ArrayList<>();
    private String selectCountryID = "0";
    private CardView mCardView_dialog;
    private TextView mTextView_dialog_title;
    private TextView mTextView_ok;
    private DbHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_chart, container, false);
        mBarChart = (BarChart) rootView.findViewById(R.id.barchart);
        mPieChart = (PieChart) rootView.findViewById(R.id.pieChart);
        mCardView_dialog = rootView.findViewById(R.id.dialog_card);
        mTextView_dialog_title = rootView.findViewById(R.id.dialog_title);
        mTextView_ok = rootView.findViewById(R.id.ok);
        dbHelper = new DbHelper(getContext());
        mTextView_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCardView_dialog.animate().setDuration(400).translationY(getActivity().getWindowManager().getDefaultDisplay().getHeight());
                CreateAPiServices.createInterface().companySales("get_my_sales.php?userid=" + selectCountryID).
                        enqueue(new Callback<SalesResponse>() {
                            @Override
                            public void onResponse(Call<SalesResponse> call, Response<SalesResponse> response) {
                                if (response.code() == 200 && response.body().getSuccess() == 1) {
                                    formatData(response.body().getSalesProducts());
                                    dbHelper.addSales(response.body().getSalesProducts());
                                } else
                                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<SalesResponse> call, Throwable t) {
                                formatData(dbHelper.getSales());
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
                            mSpinner_city.setAdapter(new SpinnerAdapter(getContext(), countries));
                            dbHelper.addCountries(countries);
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

    private void formatData(List<SalesResponse.SalesProduct> salesProducts) {
        Xdata.clear();
        ydata.clear();
        for (int i = 0; i < salesProducts.size(); i++) {
            ydata.add(Float.valueOf(salesProducts.get(i).getSales()));
            Xdata.add(salesProducts.get(i).getTitle());
        }


        drawDataBarChar();
        drawDataPieChar();
    }


    private void drawDataPieChar() {
        ArrayList<PieEntry> y = new ArrayList<>();
        for (int i = 0; i < ydata.size(); i++) {
            y.add(new PieEntry(ydata.get(i), i));
        }
        ArrayList<String> x = new ArrayList<>();
        for (int i = 0; i < Xdata.size(); i++) {
            x.add(Xdata.get(i));
        }

        PieDataSet pieDataSet = new PieDataSet(y, "data");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData pieData = new PieData(pieDataSet);
        mPieChart.setData(pieData);
        mPieChart.invalidate();
    }

    private void drawDataBarChar() {
        ArrayList<BarEntry> y = new ArrayList<>();
        for (int i = 0; i < ydata.size(); i++) {
            y.add(new BarEntry(ydata.get(i), i));
        }
        ArrayList<String> x = new ArrayList<>();
        for (int i = 0; i < Xdata.size(); i++) {
            x.add(Xdata.get(i));
        }

        BarDataSet barDataSet = new BarDataSet(y, " data");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData barData = new BarData(barDataSet);
        barData.setValueTextColor(Color.BLACK);
        barData.setValueTextSize(13f);
        mBarChart.setData(barData);
        mBarChart.invalidate();
    }
}
