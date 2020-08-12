package com.example.beckart.view;

import android.app.Dialog;
//import androidx.lifecycle.ViewModelProviders;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.content.Intent;
//import androidx.databinding.DataBindingUtil;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
//import androidx.appcompat.app.ActionBar;
//import androidx.appcompat.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.example.beckart.R;
//import com.example.beckart.databinding.ActivityAccountBinding;
import com.example.beckart.databinding.ActivityAccountBinding;
import com.example.beckart.storage.LoginUtils;

import static com.example.beckart.storage.LanguageUtils.getEnglishState;
import static com.example.beckart.storage.LanguageUtils.loadLocale;
import static com.example.beckart.storage.LanguageUtils.setEnglishState;
import static com.example.beckart.storage.LanguageUtils.setLocale;
import static com.example.beckart.utils.CommunicateUtils.rateAppOnGooglePlay;
import static com.example.beckart.utils.CommunicateUtils.shareApp;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener, LifecycleOwner {

    private static final String TAG = "AccountActivity";

    public static boolean historyIsDeleted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale(this);
        ActivityAccountBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_account);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.my_account));


        binding.nameOfUser.setText(LoginUtils.getInstance(this).getUserInfo().getName());
        binding.emailOfUser.setText(LoginUtils.getInstance(this).getUserInfo().getEmail());

        binding.myOrders.setOnClickListener(this);
        binding.myWishList.setOnClickListener(this);
        binding.helpCenter.setOnClickListener(this);
        binding.shareWithFriends.setOnClickListener(this);
        binding.rateUs.setOnClickListener(this);
        binding.changePassword.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.account, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_signOut) {
            signOut();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void signOut() {
        LoginUtils.getInstance(this).clearAll();
        Intent intent = new Intent(this, com.example.beckart.view.LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.myOrders:
                Intent ordersIntent = new Intent(this, com.example.beckart.view.OrdersActivity.class);
                startActivity(ordersIntent);
                break;
            case R.id.myWishList:
                Intent wishListIntent = new Intent(this, com.example.beckart.view.WishListActivity.class);
                startActivity(wishListIntent);
                break;
            case R.id.helpCenter:
                Intent helpCenterIntent = new Intent(this, com.example.beckart.view.HelpActivity.class);
                startActivity(helpCenterIntent);
                break;
            case R.id.shareWithFriends:
                shareApp(this);
                break;
            case R.id.rateUs:
                rateAppOnGooglePlay(this);
                break;
            case R.id.changePassword:
                Intent passwordIntent = new Intent(this, com.example.beckart.view.PasswordActivity.class);
                startActivity(passwordIntent);
                break;
        }
    }


    private void showCustomAlertDialog() {
        final Dialog dialog = new Dialog(AccountActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_language_dialog);

        Button english = dialog.findViewById(R.id.txtEnglish);
        Button arabic = dialog.findViewById(R.id.txtArabic);

        if(getEnglishState(this)){
            english.setEnabled(false);
            english.setAlpha(.5f);
            arabic.setEnabled(true);
        }else {
            arabic.setEnabled(false);
            arabic.setAlpha(.5f);
            english.setEnabled(true);
        }

        english.setOnClickListener(v -> {
            english.setEnabled(true);
            chooseEnglish();
            dialog.cancel();
        });

        arabic.setOnClickListener(v -> {
            arabic.setEnabled(true);
            chooseArabic();
            dialog.cancel();
        });

        dialog.show();
    }

    private void chooseEnglish() {
        setLocale(this,"en");
        recreate();
        Toast.makeText(this, "English", Toast.LENGTH_SHORT).show();
        setEnglishState(this, true);
    }

    private void chooseArabic() {
        setLocale(this,"ar");
        recreate();
        Toast.makeText(this, "Arabic", Toast.LENGTH_SHORT).show();
        setEnglishState(this, false);
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return null;
    }
}