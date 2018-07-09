package com.example.mohamedabdelaziz.geo.ecommerce;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mohamedabdelaziz.geo.Constants;
import com.example.mohamedabdelaziz.geo.CreateAPiServices;
import com.example.mohamedabdelaziz.geo.MessageResponse;
import com.example.mohamedabdelaziz.geo.MyOrdersResponse;
import com.example.mohamedabdelaziz.geo.R;
import com.example.mohamedabdelaziz.geo.dbhelper.DbHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyProductsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DbHelper dbHelper;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_products);
        dbHelper = new DbHelper(getApplicationContext());
        recyclerView = findViewById(R.id.myOrdersRecylcerView);
        swipeRefreshLayout = findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMyorders();
            }
        });
        getMyorders();
    }

    private void getMyorders() {
        CreateAPiServices.createInterface().getMyorders("get_user_orders.php?userid=" +
                getSharedPreferences(Constants.SESSION, Context.MODE_PRIVATE).getInt(Constants.UID, 0))
                .enqueue(new Callback<MyOrdersResponse>() {
                    @Override
                    public void onResponse(Call<MyOrdersResponse> call, Response<MyOrdersResponse> response) {
                        if (response.code() == 200) {
                            recyclerView.setAdapter(new OrdersAdapter(response.body().getProducts()));
                            if (response.body().getProducts() != null) {
                                dbHelper.addEcommerceOrders(response.body().getProducts());
                            }
                        } else
                            findViewById(R.id.no_data_textview).setVisibility(View.VISIBLE);
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(Call<MyOrdersResponse> call, Throwable t) {
                        recyclerView.setAdapter(new OrdersAdapter(dbHelper.getEcommerceOrders()));
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
    }


    class OrdersAdapter extends RecyclerView.Adapter<OrderHolder> {

        ArrayList<MyOrdersResponse.MyProduct> body;

        public OrdersAdapter(ArrayList<MyOrdersResponse.MyProduct> body) {
            this.body = body;
        }


        @NonNull
        @Override
        public OrderHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new OrderHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_item, null));
        }

        @Override
        public void onBindViewHolder(@NonNull OrderHolder orderHolder, final int i) {
            orderHolder.itemView.setHasTransientState(true);
            orderHolder.name.setText(body.get(i).getMedcineName());
            orderHolder.price.setText("const : " + body.get(i).getPrice() + " $");
            orderHolder.quantity.setText("required qunatity : " + body.get(i).getQuantity());
            orderHolder.date.setText(body.get(i).getDate());
            orderHolder.cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cancelOrder(body.get(i).getId());
                }
            });
        }

        @Override
        public int getItemCount() {
            if (body != null )
                return body.size();
            return 0;
        }
    }

    private void cancelOrder(final String id) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("Are you sure you want to cancel the order?");
        alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                CreateAPiServices.createInterface().deleteMyOrder("delete_my_order.php?order_id=" + id).enqueue(new Callback<MessageResponse>() {
                    @Override
                    public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                        if (response.code() == 200)
                            Toast.makeText(MyProductsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        getMyorders();
                    }

                    @Override
                    public void onFailure(Call<MessageResponse> call, Throwable t) {
                        Toast.makeText(MyProductsActivity.this, "check connection", Toast.LENGTH_SHORT).show();
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

    class OrderHolder extends RecyclerView.ViewHolder {

        TextView name, date, quantity, price;
        Button cancel;

        public OrderHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.order_name);
            quantity = itemView.findViewById(R.id.order_qantity);
            price = itemView.findViewById(R.id.order_price);
            date = itemView.findViewById(R.id.order_date);
            cancel = itemView.findViewById(R.id.cancel);
        }
    }
}
