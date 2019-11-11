package com.app.partners.activities.welcome;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.app.partners.R;

public class WelcomeActivity extends AppCompatActivity {
     EditText email;
     EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    public void navigateToRegister(View v) {
        Intent i = new Intent(getBaseContext(), RegisterActivity.class);
        startActivity(i);
    }

    public void login(View v){

    }


}
