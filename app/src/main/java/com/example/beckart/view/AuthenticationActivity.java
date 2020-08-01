package com.example.beckart.view;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.beckart.R;
import com.example.beckart.ViewModel.OtpViewModel;
import com.example.beckart.databinding.ActivityAuthenticationBinding;
import com.example.beckart.model.User;
import com.example.beckart.storage.LoginUtils;

import static com.example.beckart.utils.Constant.EMAIL;
import static com.example.beckart.utils.Constant.OTP;

public class AuthenticationActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "AuthenticationActivity";
    private ActivityAuthenticationBinding binding;
    private OtpViewModel otpViewModel;
    private String email;
    private String correctOtpCode;
    static boolean isActivityRunning = false;
    private int clickCount = 0;
    private String previousActivity;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_authentication);

        otpViewModel = ViewModelProviders.of(this).get(OtpViewModel.class);

        binding.proceed.setOnClickListener(this);
        binding.reSend.setOnClickListener(this);

        Intent intent = getIntent();
        email = intent.getStringExtra(EMAIL);
        correctOtpCode = intent.getStringExtra(OTP);
        previousActivity = intent.getStringExtra("activity");
        TextView authentication = findViewById(R.id.authentication);
        authentication.setText(R.string.description2);
        if(previousActivity.equals("SignUpActivity")){
            user = (User)intent.getSerializableExtra("User");
            generateOtp(email);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.proceed) {
            checkOtpCode();
        } else if (view.getId() == R.id.reSend) {
            clickCount = clickCount + 1;
            getAnotherOtpCode();
            if (clickCount >=2) {
                binding.reSend.setClickable(false);
                binding.numberOfClicks.setVisibility(View.VISIBLE);
            }
        }
    }
    private void generateOtp(String email) {

        otpViewModel.getOtpCode(email).observe((LifecycleOwner) this, responseBody -> {
            if (!responseBody.isError()) {
                correctOtpCode = responseBody.getOtp();
                Log.d("otp",correctOtpCode);
            } else {
                binding.otpCode.setError(getString(R.string.incorrect_email));
            }
        });
    }

    private void getAnotherOtpCode() {
        otpViewModel.getOtpCode(email).observe((LifecycleOwner) this, responseBody -> {
            if (!responseBody.isError()) {
                correctOtpCode = responseBody.getOtp();
                binding.reSend.setEnabled(false);
                binding.countDownTimer.setVisibility(View.VISIBLE);
                countDownTimer(binding.countDownTimer);
            }
        });
    }

    private void checkOtpCode() {
        String otpEntered = binding.otpCode.getText().toString();

        Log.d("otp",correctOtpCode);
        if (!otpEntered.equals(correctOtpCode)) {
            binding.otpCode.setError(getString(R.string.incorrect_code));
            binding.otpCode.requestFocus();
        } else if(previousActivity.equals("SignUpActivity")){
            LoginUtils.getInstance(this).saveUserInfo(user);
            Intent intent = new Intent(this, ProductActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        else if(previousActivity.equals("PasswordAssistant")){
            Intent passwordIntent = new Intent(this, com.example.beckart.view.PasswordActivity.class);
            startActivity(passwordIntent);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        isActivityRunning = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        isActivityRunning = false;
    }

    private void countDownTimer(TextView textView) {
        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                textView.setText(String.valueOf(millisUntilFinished / 1000));
            }

            public void onFinish() {
                Log.d(TAG, "onFinish: " + "done!");
                binding.reSend.setEnabled(true);
                binding.countDownTimer.setVisibility(View.INVISIBLE);
            }

        }.start();
    }
}