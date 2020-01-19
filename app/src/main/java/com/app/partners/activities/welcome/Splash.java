package com.app.partners.activities.welcome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.app.partners.R;
import com.app.partners.activities.main.LandLord;
import com.app.partners.activities.main.Renter;
import com.app.partners.activities.utils.Utils;
import com.app.partners.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;

public class Splash extends AppCompatActivity implements Serializable {


    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference usersRef;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference().child("users");
        getCurrentUser();
    }

    private void getCurrentUser(){
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null){
            uid = firebaseUser.getUid();
            getUserFromDb(uid);
        }
        
        else {
            //  Nav to Welcome
            Intent i = new Intent(getBaseContext(), WelcomeActivity.class);
            startActivity(i);
        }
    }

    public void getUserFromDb(final String uid) {
        if (!uid.isEmpty()) {
            ValueEventListener userListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        User user = dataSnapshot.getValue(User.class);
                        Log.d("UserName", user.firstName + " " + user.lastName);
                        if (user.isLandLord){
                            // navigate to landlord Activity
                            // Extras: User
                            Intent i = new Intent(getBaseContext(), LandLord.class);
                            i.putExtra("name", user.firstName + " " + user.lastName);
                            i.putExtra("phone", user.phone);

                            startActivity(i);
                            finish();
                        } else {
                            Intent i = new Intent(getBaseContext(), Renter.class);
                            i.putExtra("name", user.firstName + " " + user.lastName);
                            i.putExtra("phone", user.phone);

                            startActivity(i);
                            finish();
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
            mDatabase.getReference("users/" + uid).addListenerForSingleValueEvent(userListener);
        }
    }


    // getCurrentUser() - mAuth if user is not null
    // After getting userId, check in db his user type
    // Fetch relevant data
    // Navigations


}
