package com.app.partners.activities.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.app.partners.R;
import com.app.partners.activities.utils.UserUtils;
import com.app.partners.activities.welcome.WelcomeActivity;

public class LandLord extends AppCompatActivity {

    TextView signOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_land_lord);

        signOut = findViewById(R.id.sign_out);

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
    }

    public void signOut() {
        UserUtils.signOut();
        startActivity(new Intent(getBaseContext(), WelcomeActivity.class));
    }
}
