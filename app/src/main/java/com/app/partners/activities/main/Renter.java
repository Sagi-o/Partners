package com.app.partners.activities.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.app.partners.R;
import com.app.partners.RenterPageStateAdapter;
import com.app.partners.activities.utils.UserUtils;
import com.app.partners.activities.welcome.WelcomeActivity;
import com.app.partners.fragments.renter.AddPartnerFragment;
import com.app.partners.fragments.renter.CreateApartmentFragment;
import com.app.partners.fragments.renter.NoApartmentFragment;
import com.app.partners.fragments.renter.MyApartmentFragment;
import com.app.partners.models.Apartment;
import com.app.partners.models.UserIdApartment;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Renter extends AppCompatActivity {
    public ViewPager viewPager;

    private DatabaseReference apartmentToUserIdRef;
    private DatabaseReference apartmentRef;
    private RenterPageStateAdapter renterPageStateAdapter;

    public String apartmentId;
    public Apartment myApartment = new Apartment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renter);

        setupViewPager();

        FirebaseUser firebaseUser = UserUtils.getUser();

        if (firebaseUser == null) {
            navigateToWelcome();
        }

        String uid = firebaseUser.getUid();

        apartmentToUserIdRef = FirebaseDatabase.getInstance().getReference().child("userId_to_apartment").child(uid);

        checkIfApartmentExists();
    }

    private void setupViewPager() {
        viewPager = findViewById(R.id.container);
        renterPageStateAdapter = new RenterPageStateAdapter(getSupportFragmentManager(), 0);

        renterPageStateAdapter.addFragment(new NoApartmentFragment());
        renterPageStateAdapter.addFragment(new CreateApartmentFragment());
        renterPageStateAdapter.addFragment(new MyApartmentFragment());
        renterPageStateAdapter.addFragment(new AddPartnerFragment());

        viewPager.setAdapter(renterPageStateAdapter);
    }

    private void checkIfApartmentExists() {
        ValueEventListener apartmentListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    // go to creating new ap
                    Toast.makeText(getApplicationContext(), "Apartment Not Found", Toast.LENGTH_SHORT).show();

                    // No apartment found
                    viewPager.setCurrentItem(0);

                    return;
                }

                Toast.makeText(getApplicationContext(), "Apartment Found ", Toast.LENGTH_SHORT).show();

                UserIdApartment userIdApartment = dataSnapshot.getValue(UserIdApartment.class);

                if (userIdApartment == null) {
                    Toast.makeText(getApplicationContext(), "Null apartment found ", Toast.LENGTH_SHORT).show();
                    return;
                }

                apartmentId = userIdApartment.apartmentId;

                Log.d("Renter", "id: " + apartmentId);

                getApartment(apartmentId);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
//                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        apartmentToUserIdRef.addListenerForSingleValueEvent(apartmentListener);
    }

    private void getApartment(String apartmentId) {
        apartmentRef = FirebaseDatabase.getInstance().getReference().child("apartments").child(apartmentId);

        ValueEventListener apartmentListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    // go to creating new ap
                    Toast.makeText(getApplicationContext(), "Error, Apartment Not Found", Toast.LENGTH_SHORT).show();

                    // No apartment found
                    viewPager.setCurrentItem(0);

                    return;
                }

                myApartment = dataSnapshot.getValue(Apartment.class);
                navigateToApartment();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
//                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        apartmentRef.addListenerForSingleValueEvent(apartmentListener);
    }

    private void navigateToApartment() {
        // send to apartment fragment with apId
//        this.apartmentId = apartmentId;
        viewPager.setCurrentItem(2);
    }

    public void signOut(View v) {
        UserUtils.signOut();
        navigateToWelcome();
    }

    public void navigateToWelcome() {
        Intent i = new Intent(getBaseContext(), WelcomeActivity.class);
        startActivity(i);
        finish();
    }
}
