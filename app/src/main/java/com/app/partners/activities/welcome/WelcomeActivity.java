package com.app.partners.activities.welcome;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.app.partners.R;
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

    private String email_st, password_st;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        mDatabase = FirebaseDatabase.getInstance();
    }

    public void navigateToRegister(View v) {
        Intent i = new Intent(getBaseContext(), RegisterActivity.class);
        startActivity(i);
    }

    public void login(View v){
        Log.d("WelcomeActivity", "Login clicked");
        email_st = email.getText().toString();
        password_st = password.getText().toString();
        mAuth.signInWithEmailAndPassword(email_st, password_st)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();

                        Log.d("WelcomeActivity", "User is: " + firebaseUser);
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

                    }
        });
    }

    public void getUserFromDb(String uid) {
        if (!uid.isEmpty()) {
            ValueEventListener userListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {
                        User user = dataSnapshot.getValue(User.class);
                        if (user.isLandLord()){
                            // navigate to landlord Activity
                        } else {
                            // navigate to Renter Activity
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
