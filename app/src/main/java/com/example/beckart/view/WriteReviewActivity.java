package com.example.beckart.view;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;
import com.example.beckart.R;
import com.example.beckart.ViewModel.WriteReviewViewModel;
import com.example.beckart.databinding.ActivityWriteReviewBinding;
import com.example.beckart.model.Review;
import com.example.beckart.model.User;
import com.example.beckart.storage.LoginUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static com.example.beckart.storage.LanguageUtils.loadLocale;
import static com.example.beckart.utils.Constant.PRODUCT_ID;
public class WriteReviewActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "WriteReviewActivity";

    private ActivityWriteReviewBinding binding;
    private int productId;

    private WriteReviewViewModel writeReviewViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_write_review);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.write_review));

        writeReviewViewModel = ViewModelProviders.of(this).get(WriteReviewViewModel.class);

        binding.btnSubmit.setOnClickListener(this);
        binding.txtName.setText(LoginUtils.getInstance(this).getUserInfo().getName());

        getCurrentTextLength();

        Intent intent = getIntent();
        productId = intent.getIntExtra(PRODUCT_ID, 0);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnSubmit) {
            writeReview();
        }
    }

    private void writeReview() {
        User user = LoginUtils.getInstance(this).getUserInfo();
        int userId = user.getId();
        String userName = user.getName();
        String feedback = binding.editFeedback.getText().toString().trim();
        float rate = binding.rateProduct.getRating();

        // Check if there are no empty values
        if (TextUtils.isEmpty(feedback) || rate == 0.0f) {
            Toast.makeText(this, getString(R.string.required_data), Toast.LENGTH_SHORT).show();
            return;
        }

        Review review = new Review(userId, userName, productId, rate, feedback);
        writeReviewViewModel.writeReview(review).observe((LifecycleOwner) this, responseBody -> {
                try {
                    JSONObject obj = new JSONObject(responseBody.string());
                    if(obj.getBoolean("error")){
                        Toast.makeText(this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
        });
    }

    private void getCurrentTextLength(){
        binding.editFeedback.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int textLength = 150;
                int writtenTextLength = s.toString().length();
                binding.textLength.setText(String.valueOf(textLength - writtenTextLength));
            }
        });
    }
}
