package com.fleetmatics.networkingworkapp.ui.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.fleetmatics.networkingworkapp.MyApplication;
import com.fleetmatics.networkingworkapp.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import eu.inloop.easygcm.EasyGcm;
import io.branch.referral.Branch;
import io.branch.referral.BranchShortLinkBuilder;

@EActivity(R.layout.activity_login)
public class LoginActivity extends AppCompatActivity {
    @ViewById
    Toolbar toolbar;
    @ViewById
    EditText username;
    @ViewById
    Button login;


    @AfterViews
    void setup() {
        setSupportActionBar(toolbar);
    }


    @Override
    protected void onStart() {
        super.onStart();
        Branch branch = Branch.getInstance();

        branch.initSession((referringParams, error) -> {
            if (error == null) {
                if (referringParams.has("customerId")) {

                    username.setText(referringParams.optString("customerId"));
                    login();
                }
            } else {

            }
        }, this.getIntent().getData(), this);


        BranchShortLinkBuilder shortLinkBuilder =
                new BranchShortLinkBuilder(
                        this)
                        .addParameters(
                                "customerId", "" +
                                        201);
        shortLinkBuilder.generateShortUrl((url, error) -> Log.d("MYLOG", url));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Click
    void login() {
        MyApplication.getInstance().setCustomerId(Integer.valueOf(username.getText().toString()));
        EasyGcm.resendRegistrationIdToBackend(this);
        finish();
    }
}
