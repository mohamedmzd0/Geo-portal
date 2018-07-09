package com.example.mohamedabdelaziz.geo.companymain;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mohamedabdelaziz.geo.CreateAPiServices;
import com.example.mohamedabdelaziz.geo.FeedBackResponse;
import com.example.mohamedabdelaziz.geo.MessageResponse;
import com.example.mohamedabdelaziz.geo.R;
import com.example.mohamedabdelaziz.geo.dbhelper.DbHelper;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FeedbackFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private DbHelper dbHelper;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_task, null);
        mRecyclerView = rootView.findViewById(R.id.recylcer);
        swipeRefreshLayout = rootView.findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData();
            }
        });
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        mRecyclerView.setHasFixedSize(false);
        dbHelper = new DbHelper(getContext());

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchData();
    }

    private void fetchData() {
        CreateAPiServices.createInterface().companyFeedback("get_feedback.php").
                enqueue(new Callback<FeedBackResponse>() {
                    @Override
                    public void onResponse(Call<FeedBackResponse> call, Response<FeedBackResponse> response) {
                        if (response.code() == 200) {
                            dbHelper.addFeedback(response.body().getFeedback());
                            mRecyclerView.setAdapter(new RecyclerAdapter(response.body().getFeedback()));
                        } else
                            Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(Call<FeedBackResponse> call, Throwable t) {
                        swipeRefreshLayout.setRefreshing(false);
                        mRecyclerView.setAdapter(new RecyclerAdapter(dbHelper.getFeedback()));
                    }
                });
    }


    class RecyclerAdapter extends RecyclerView.Adapter<FeedbackHolder> {
        private List<FeedBackResponse.Feedback> feedback;

        public RecyclerAdapter(List<FeedBackResponse.Feedback> feedback) {
            this.feedback = feedback;
        }

        @Override
        public FeedbackHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_feedback, null);
            return new FeedbackHolder(view);
        }

        @Override
        public void onBindViewHolder(FeedbackHolder holder, final int i) {
            holder.comment.setText(feedback.get(i).getContent());
            holder.date.setText(feedback.get(i).getDate());
            holder.user.setText(feedback.get(i).getUserName());
            holder.product.setText(feedback.get(i).getMedcineName());
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteComment(feedback.get(i).getId());
                }
            });

        }

        private void deleteComment(final String id) {
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            alert.setMessage("Are you sure you want to delete this comment?");
            alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    Log.d("URL_IS", "delete_comment.php?comment_id=" + id);
                    CreateAPiServices.createInterface().DelteComment("delete_comment.php?comment_id=" + id).enqueue(new Callback<MessageResponse>() {
                        @Override
                        public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                            Toast.makeText(getContext(), "success" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            fetchData();
                        }

                        @Override
                        public void onFailure(Call<MessageResponse> call, Throwable t) {
                            Toast.makeText(getContext(), "check connection", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            });
            alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // close dialog
                    dialog.cancel();
                }
            });
            alert.show();
        }

        @Override
        public int getItemCount() {
            if (feedback == null)
                return 0;
            return feedback.size();
        }
    }

    private class FeedbackHolder extends RecyclerView.ViewHolder {
        TextView comment, user, date, product;
        Button delete;

        public FeedbackHolder(@NonNull View itemView) {
            super(itemView);
            comment = itemView.findViewById(R.id.comment);
            user = itemView.findViewById(R.id.user);
            date = itemView.findViewById(R.id.date);
            product = itemView.findViewById(R.id.product);
            delete = itemView.findViewById(R.id.delete);
        }
    }
}
