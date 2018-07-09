package com.example.mohamedabdelaziz.geo.ecommerce;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mohamedabdelaziz.geo.Constants;
import com.example.mohamedabdelaziz.geo.CreateAPiServices;
import com.example.mohamedabdelaziz.geo.Product;
import com.example.mohamedabdelaziz.geo.ProductsModel;
import com.example.mohamedabdelaziz.geo.R;
import com.example.mohamedabdelaziz.geo.dbhelper.DbHelper;
import com.example.mohamedabdelaziz.geo.splashactivity.SplashActivity;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.squareup.picasso.Picasso;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainEcommerce extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private MaterialSearchView searchView;
    private List<Product> products = new ArrayList<>();
    private DbHelper dbHelper;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ecommerce);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        swipeRefreshLayout = findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getProducts();
            }
        });
        searchView = findViewById(R.id.search_view);
        toolbar.setTitleTextColor(Color.WHITE);
        dbHelper = new DbHelper(getApplicationContext());
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search(newText);
                return false;
            }
        });
        searchView.setAnimationDuration(View.AUTOFILL_TYPE_TOGGLE);
        mRecyclerView = findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        mRecyclerView.setHasFixedSize(false);
        findViewById(R.id.empty_tv).setVisibility(View.VISIBLE);
        getProducts();
    }

    private void search(String query) {
        List list = new ArrayList<Product>();
        if (TextUtils.isEmpty(query))
            mRecyclerView.setAdapter(new ProductAdapter(products));
        else
            for (int i = 0; i < products.size(); i++) {
                if (products.get(i).getMedcineName().contains(query)) {
                    list.add(products.get(i));
                }
                mRecyclerView.setAdapter(new ProductAdapter(list));
            }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_item, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.myOrders:
                startActivity(new Intent(getApplicationContext(), MyProductsActivity.class));
                break;
            case R.id.logout: {
                SharedPreferences.Editor editor = getSharedPreferences(Constants.SESSION, Context.MODE_PRIVATE).edit();
                editor.putBoolean(Constants.IS_LOGGED_IN, false);
                editor.commit();
                startActivity(new Intent(getApplicationContext(), SplashActivity.class));
                finish();
            }
            break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {

                ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if (matches != null && matches.size() > 0) {
                    String searchWrd = matches.get(0);
                    if (!TextUtils.isEmpty(searchWrd)) {
                        searchView.setQuery(searchWrd, false);
                    }
                }

            }
            return;
        } catch (ActivityNotFoundException e) {
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void getProducts() {
        Call<ProductsModel> response = CreateAPiServices.createInterface().getProducts();
        response.enqueue(new Callback<ProductsModel>() {
            @Override
            public void onResponse(Call<ProductsModel> call, Response<ProductsModel> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    products = response.body().getProducts();
                    if (products == null || products.size() == 0) {
                        findViewById(R.id.empty_tv).setVisibility(View.VISIBLE);
                    } else {
                        mRecyclerView.setAdapter(new ProductAdapter(response.body().getProducts()));
                        setUpSuggesstions(response.body().getProducts());
                        dbHelper.addEcommerceProducts(response.body().getProducts());
                        findViewById(R.id.empty_tv).setVisibility(View.GONE);
                    }
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ProductsModel> call, Throwable t) {
                products = dbHelper.getEcommerceProducts();
                mRecyclerView.setAdapter(new ProductAdapter(products));
                setUpSuggesstions(products);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void setUpSuggesstions(List<Product> products) {

        if (products == null || products.size() == 0)
            return;
        String[] list = new String[products.size()];
        for (int i = 0; i < products.size(); i++) {
            list[i] = products.get(i).getMedcineName();
        }
        searchView.setSuggestions(list);
    }


    class ProductAdapter extends RecyclerView.Adapter<ProductHolder> {
        private List<Product> products;

        public ProductAdapter(List<Product> products) {
            this.products = products;
        }

        @Override
        public ProductHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_item, null);
            return new ProductHolder(view);
        }

        @Override
        public void onBindViewHolder(ProductHolder holder, final int i) {
            holder.itemView.setHasTransientState(true);
            holder.pName.setText(products.get(i).getMedcineName());
            holder.pPrice.setText(String.valueOf(products.get(i).getPrice() + " $ "));
            if (products.get(i).getDescription().length() > 20)
                holder.pdesc.setText(products.get(i).getDescription().substring(0, 20));
            else
                holder.pdesc.setText(products.get(i).getDescription());
            try {
                Picasso.with(getApplicationContext()).load("http://10.0.2.2/graduation/ecommerce/admin/uploads/medicin_images/" + products.get(i).getImage())
                        .into(holder.imageView);

            } catch (Exception e) {

            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), ProductDetail.class);
                    intent.putExtra(Constants.PRODUCT_ID, products.get(i).getId());
                    intent.putExtra(Constants.PRODUCT_NAME, products.get(i).getMedcineName());
                    intent.putExtra(Constants.PRODUCT_DESCR, products.get(i).getDescription());
                    intent.putExtra(Constants.PRODUCT_IMAGE, products.get(i).getImage());
                    intent.putExtra(Constants.PRODUCT_PRICE, products.get(i).getPrice());
                    intent.putExtra(Constants.PRECAUTIONS, products.get(i).getPrecautions());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            if (products == null)
                return 0;
            return products.size();
        }
    }

    class ProductHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView pName, pPrice, pdesc;

        public ProductHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.product_image);
            pName = itemView.findViewById(R.id.product_name);
            pdesc = itemView.findViewById(R.id.product_descr);
            pPrice = itemView.findViewById(R.id.product_price);
        }
    }
}
