package com.app.partners.activities.welcome;

import android.app.ProgressDialog;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.app.partners.R;
import com.app.partners.activities.main.LandLord;
import com.app.partners.activities.main.Renter;
import com.app.partners.activities.utils.Utils;
import com.app.partners.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WelcomeActivity extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private ProgressDialog progressDialog;

    private String email_st, password_st;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        mDatabase = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(this);
    }

    public void navigateToRegister(View v) {
        Intent i = new Intent(getBaseContext(), RegisterActivity.class);
        startActivity(i);
    }

    public void login(View v){
        Log.d("WelcomeActivity", "Login clicked");
        email_st = email.getText().toString();
        password_st = password.getText().toString();

        if (email_st.isEmpty() || password_st.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.show();
        progressDialog.setMessage("Signing In...");

        mAuth.signInWithEmailAndPassword(email_st, password_st)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();

                        progressDialog.setMessage("Getting User...");

                        String uid = firebaseUser.getUid();
                        getUserFromDb(uid);

                        // Check user type by fetching User object from db
                        // Navigate user to suitable activity
                        // Save user object on app
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("WelcomeActivity", "Login failed!" + e);
                        Toast.makeText(getApplicationContext(), "Login failed...", Toast.LENGTH_SHORT).show();
                    }
        });
    }

    public void getUserFromDb(String uid) {
        if (!uid.isEmpty()) {
            ValueEventListener userListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    progressDialog.dismiss();

                    if (dataSnapshot.exists()) {
                        User user = dataSnapshot.getValue(User.class);
                        if (user.isLandLord){
                            // navigate to landlord Activity
                            Intent i = new Intent(getBaseContext(), LandLord.class);
                            i.putExtra("name", user.firstName + " " + user.lastName);
                            startActivity(i);
                        } else {
                            Intent i = new Intent(getBaseContext(), Renter.class);
                            i.putExtra("name", user.firstName + " " + user.lastName);
                            startActivity(i);
                        }
                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    //  Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                    // ...
                }
            };
            mDatabase.getReference("users/" + uid).addValueEventListener(userListener);
        }
    }



}
