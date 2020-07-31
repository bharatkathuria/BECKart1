package com.example.beckart.view;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.beckart.R;
import com.example.beckart.ViewModel.ShippingViewModel;
import com.example.beckart.databinding.ActivityShippingAddressBinding;
import com.example.beckart.model.GenericServerResponse;
import com.example.beckart.model.Shipping;
import com.example.beckart.storage.LoginUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;

import static com.example.beckart.utils.Constant.PRODUCTID;

public class ShippingAddressActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "ShippingAddressActivity";
    private ActivityShippingAddressBinding binding;

    private ShippingViewModel shippingViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_shipping_address);

        shippingViewModel = ViewModelProviders.of(this).get(ShippingViewModel.class);

        binding.proceed.setOnClickListener(this);

        binding.txtName.setText(LoginUtils.getInstance(this).getUserInfo().getName());
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.proceed) {
            addShippingAddress();
        }
    }

    private void addShippingAddress() {
        String address = binding.address.getText().toString().trim();
        String city = binding.city.getText().toString().trim();
        String country = binding.country.getText().toString().trim();
        String zip = binding.zip.getText().toString().trim();
        String phone = binding.phone.getText().toString().trim();
        int userId = LoginUtils.getInstance(this).getUserInfo().getId();
        Intent intent = getIntent();
        int productId = intent.getIntExtra(PRODUCTID, 0);

        Shipping shipping = new Shipping(address, city, country, zip, phone,userId, productId);

        shippingViewModel.addShippingAddress(shipping).observe((LifecycleOwner) this, responseBody -> {

            try {
                JSONObject obj = new JSONObject(responseBody.string());
                if(obj.getBoolean("error")){
                    Toast.makeText(ShippingAddressActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(ShippingAddressActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                    Intent orderProductIntent = new Intent(ShippingAddressActivity.this, OrderProductActivity.class);
                    orderProductIntent.putExtra(PRODUCTID,productId);
                    startActivity(orderProductIntent);
                }
          } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        });
    }
}
