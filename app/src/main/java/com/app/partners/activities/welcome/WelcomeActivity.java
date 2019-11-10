package com.app.partners.activities.welcome;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.app.partners.R;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    public void navigateToRegister(View v) {
        Intent i = new Intent(getBaseContext(), RegisterActivity.class);
        startActivity(i);
    }
}
