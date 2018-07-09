package com.example.mohamedabdelaziz.geo.medicalrep.fragment;

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
import android.widget.Toast;

import com.example.mohamedabdelaziz.geo.Constants;
import com.example.mohamedabdelaziz.geo.CreateAPiServices;
import com.example.mohamedabdelaziz.geo.R;
import com.example.mohamedabdelaziz.geo.TasksResponse;
import com.example.mohamedabdelaziz.geo.dbhelper.DbHelper;
import com.example.mohamedabdelaziz.geo.medicalrep.taskdetaila.TasksActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Mohamed Abd Elaziz on 3/8/2018.
 */

public class TasksFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private DbHelper dbHelper;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_task, null);
        mRecyclerView = rootView.findViewById(R.id.recylcer);
        dbHelper = new DbHelper(getContext());
        swipeRefreshLayout = rootView.findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData();
            }
        });
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        mRecyclerView.setHasFixedSize(false);


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchData();
    }

    private void fetchData() {
        Log.d("URL_IS", "get_tasks.php?med_id=" +
                getActivity().getSharedPreferences(Constants.SESSION, Context.MODE_PRIVATE).getInt(Constants.UID, 0));
        CreateAPiServices.createInterface().medicalrepTasks("get_tasks.php?med_id=" +
                getActivity().getSharedPreferences(Constants.SESSION, Context.MODE_PRIVATE).getInt(Constants.UID, 0)).
                enqueue(new Callback<TasksResponse>() {
                    @Override
                    public void onResponse(Call<TasksResponse> call, Response<TasksResponse> response) {
                        if (response.code() == 200 && response.body().getSuccess() == 1) {
                            mRecyclerView.setAdapter(new RecyclerAdapter(response.body().getTasks()));
                            dbHelper.addMedicalTasks(response.body().getTasks());
                        } else
                            Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(Call<TasksResponse> call, Throwable t) {
                        swipeRefreshLayout.setRefreshing(false);
                        mRecyclerView.setAdapter(new RecyclerAdapter(dbHelper.getMedicalTasks()));
                        Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    class RecyclerAdapter extends RecyclerView.Adapter<TVHolder> {

        List<TasksResponse.Task> tasks;

        public RecyclerAdapter(List<TasksResponse.Task> tasks) {
            this.tasks = tasks;
        }

        @Override
        public TVHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_tv, null);
            return new TVHolder(view);
        }

        @Override
        public void onBindViewHolder(TVHolder holder, final int i) {
            holder.title.setText(tasks.get(i).getName());
            holder.tv_item_detail.setText(tasks.get(i).getPrice() + " $");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), TasksActivity.class);
                    intent.putExtra(Constants.PRODUCT_ID, tasks.get(i).getId());
                    intent.putExtra(Constants.PRODUCT_NAME, tasks.get(i).getName());
                    intent.putExtra(Constants.PRODUCT_DESCR, tasks.get(i).getDescription());
                    intent.putExtra(Constants.PRODUCT_PRICE, tasks.get(i).getPrice());
                    intent.putExtra(Constants.PRECAUTIONS, tasks.get(i).getPrecautions());
                    intent.putExtra(Constants.LONG, tasks.get(i).getLng());
                    intent.putExtra(Constants.LAT, tasks.get(i).getLat());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return tasks.size();
        }
    }


}
