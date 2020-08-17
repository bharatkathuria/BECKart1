package com.example.beckart.view;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.beckart.R;
import com.example.beckart.ViewModel.CartViewModel;
import com.example.beckart.adapter.CartAdapter;
import com.example.beckart.databinding.ActivityCartBinding;
import com.example.beckart.model.Product;
import com.example.beckart.storage.LoginUtils;

import java.util.List;

import static com.example.beckart.storage.LanguageUtils.loadLocale;
import static com.example.beckart.utils.Constant.PRODUCT;
import static com.example.beckart.utils.InternetUtils.isNetworkConnected;

public class CartActivity extends AppCompatActivity {

    private ActivityCartBinding binding;
    private CartAdapter cartAdapter;
    private List<Product> productList;
    private CartViewModel cartViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cart);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.cart));

        setUpRecyclerView();

        getProductsInCart();
    }

    private void setUpRecyclerView() {
        binding.productsInCart.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.productsInCart.setHasFixedSize(true);
        cartViewModel = ViewModelProviders.of(this).get(CartViewModel.class);
    }

    private void getProductsInCart() {
        if (isNetworkConnected(this)) {
            cartViewModel.getProductsInCart(LoginUtils.getInstance(this).getUserInfo().getId()).observe((LifecycleOwner) this, cartApiResponse -> {
                if (cartApiResponse != null) {
                    productList = cartApiResponse.getProductsInCart();
                    if (productList.size() == 0) {
                        binding.noBookmarks.setVisibility(View.VISIBLE);
                        binding.emptyCart.setVisibility(View.VISIBLE);
                    } else {
                        binding.bottomBar.setVisibility(View.VISIBLE);
                        binding.productsInCart.setVisibility(View.VISIBLE);
                    }
                    cartAdapter = new CartAdapter(getApplicationContext(), productList, product -> {
                        Intent intent = new Intent(CartActivity.this, com.example.beckart.view.DetailsActivity.class);
                        // Pass an object of product class
                        intent.putExtra(PRODUCT, (product));
                        startActivity(intent);
                    }, this,binding);
                }
                binding.totalPrice.setText(String.valueOf(getTotal(productList)));
                binding.checkout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent shippingIntent = new Intent(CartActivity.this, com.example.beckart.view.ShippingAddressActivity.class);
//                        shippingIntent.putExtra(PRODUCTID, product.getProductId());
                        startActivity(shippingIntent);
                        Log.d("ONCLICK","Heeeeeeeeeeeeeeloi");
                    }
                });
                binding.loadingIndicator.setVisibility(View.GONE);
                binding.productsInCart.setAdapter(cartAdapter);
                cartAdapter.notifyDataSetChanged();
            });
        } else {
            binding.emptyCart.setVisibility(View.VISIBLE);
            binding.loadingIndicator.setVisibility(View.GONE);
            binding.emptyCart.setText(getString(R.string.no_internet_connection));
        }
    }

    private double getTotal(List<Product> productList) {
        double total =0;
        for(Product p:productList){
            total+=p.getProductPrice();
        }
        return total;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getProductsInCart();
    }



}
