package com.example.mohamedabdelaziz.geo.companymain;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mohamedabdelaziz.geo.Constants;
import com.example.mohamedabdelaziz.geo.CreateAPiServices;
import com.example.mohamedabdelaziz.geo.MedicalRepsResponse;
import com.example.mohamedabdelaziz.geo.MessageResponse;
import com.example.mohamedabdelaziz.geo.R;
import com.example.mohamedabdelaziz.geo.UsersResponse;
import com.example.mohamedabdelaziz.geo.dbhelper.DbHelper;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsersFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private FloatingActionButton add;
    private DbHelper dbHelper;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.members_fragment, null);
        mRecyclerView = rootView.findViewById(R.id.recycler_members);
        add = rootView.findViewById(R.id.add_member);
        dbHelper = new DbHelper(getContext());
        swipeRefreshLayout = rootView.findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getUsers();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddMedicalRepDialog.class);
                intent.putExtra(Constants.USER_TYPE, Constants.USER);
                startActivity(intent);
            }
        });
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        mRecyclerView.setHasFixedSize(false);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getUsers();
    }

    private void getUsers() {
        CreateAPiServices.createInterface().getUsers().enqueue(new Callback<UsersResponse>() {
            @Override
            public void onResponse(Call<UsersResponse> call, Response<UsersResponse> response) {
                if (response.code() == 200 && response.body().getSuccess() == 1) {
                    dbHelper.addUsers(response.body().getUsers());
                    mRecyclerView.setAdapter(new RecyclerAdapter(response.body().getUsers()));
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<UsersResponse> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                mRecyclerView.setAdapter(new RecyclerAdapter(dbHelper.getUsers()));
            }
        });

    }

    class RecyclerAdapter extends RecyclerView.Adapter<MedicalHolder> {

        List<UsersResponse.User> users;

        public RecyclerAdapter(List<UsersResponse.User> users) {
            this.users = users;
        }

        @Override
        public MedicalHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_members, null);
            return new MedicalHolder(view);
        }

        @Override
        public void onBindViewHolder(MedicalHolder holder, final int i) {
            holder.email.setText(users.get(i).getEmail());
            holder.name.setText(users.get(i).getName());
            holder.phone.setText(users.get(i).getTelphone());
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteUser(Integer.parseInt(users.get(i).getId()));
                }
            });
        }

        private void deleteUser(final int id) {
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            alert.setMessage("Are you sure you want to delete this medical rep?");
            alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    CreateAPiServices.createInterface().deleteMyMedicals("delete_user.php?userid=" + id)
                            .enqueue(new Callback<MessageResponse>() {
                                @Override
                                public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    getUsers();
                                }

                                @Override
                                public void onFailure(Call<MessageResponse> call, Throwable t) {

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
            return users.size();
        }
    }

    private class MedicalHolder extends RecyclerView.ViewHolder {

        TextView name, phone, email;
        Button delete;

        public MedicalHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            phone = itemView.findViewById(R.id.telphone);
            email = itemView.findViewById(R.id.email);
            delete = itemView.findViewById(R.id.delete);

        }
    }
}
