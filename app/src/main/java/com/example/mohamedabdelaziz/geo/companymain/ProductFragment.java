package com.example.mohamedabdelaziz.geo.companymain;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mohamedabdelaziz.geo.CompanyProductsResponse;
import com.example.mohamedabdelaziz.geo.Constants;
import com.example.mohamedabdelaziz.geo.CreateAPiServices;
import com.example.mohamedabdelaziz.geo.Product;
import com.example.mohamedabdelaziz.geo.ProductsModel;
import com.example.mohamedabdelaziz.geo.R;
import com.example.mohamedabdelaziz.geo.dbhelper.DbHelper;
import com.example.mohamedabdelaziz.geo.medicalrep.fragment.TVHolder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProductFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private FloatingActionButton add_product;
    private DbHelper dbHelper;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.members_fragment, null);
        mRecyclerView = rootView.findViewById(R.id.recycler_members);
        add_product = rootView.findViewById(R.id.add_member);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        mRecyclerView.setHasFixedSize(false);
        dbHelper = new DbHelper(getContext());
        swipeRefreshLayout = rootView.findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData();
            }
        });
        add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ProductDetailsActivity.class));
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchData();
    }

    private void fetchData() {
        CreateAPiServices.createInterface().companyProduct("get_products.php").
                enqueue(new Callback<ProductsModel>() {
                    @Override
                    public void onResponse(Call<ProductsModel> call, Response<ProductsModel> response) {
                        if (response.code() == 200 && response.body().getSuccess() == 1) {
                            mRecyclerView.setAdapter(new RecyclerAdapter(response.body().getProducts()));
                            dbHelper.addEcommerceProducts(response.body().getProducts());
                        } else
                            Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(Call<ProductsModel> call, Throwable t) {
                        swipeRefreshLayout.setRefreshing(false);
                        mRecyclerView.setAdapter(new RecyclerAdapter(dbHelper.getEcommerceProducts()));
                    }
                });
    }

    class RecyclerAdapter extends RecyclerView.Adapter<TVHolder> {
        private List<Product> products;


        public RecyclerAdapter(List<Product> products) {
            this.products = products;
        }

        @Override
        public TVHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_tv, null);
            return new TVHolder(view);
        }

        @Override
        public void onBindViewHolder(TVHolder holder, final int i) {
            holder.title.setText(products.get(i).getMedcineName());
            holder.tv_item_detail.setText(products.get(i).getPrice() + " $");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), ProductDetailsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(Constants.PRODUCT_ID, products.get(i).getId());
                    intent.putExtra(Constants.PRODUCT_NAME, products.get(i).getMedcineName());
                    intent.putExtra(Constants.PRODUCT_PRICE, products.get(i).getPrice());
                    intent.putExtra(Constants.PRODUCT_DESCR, products.get(i).getDescription());
                    intent.putExtra(Constants.PRECAUTIONS, products.get(i).getPrecautions());
                    intent.putExtra(Constants.PRODUCT_IMAGE, products.get(i).getImage());
                    startActivity(intent);
                }
            });


        }

        @Override
        public int getItemCount() {
            return products.size();
        }
    }
}
