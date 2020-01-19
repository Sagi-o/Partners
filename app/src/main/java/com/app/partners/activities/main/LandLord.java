package com.app.partners.activities.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.app.partners.R;
import com.app.partners.UserPageStateAdapter;
import com.app.partners.activities.utils.CustomizedViewPager;
import com.app.partners.activities.utils.UserUtils;
import com.app.partners.activities.welcome.WelcomeActivity;
import com.app.partners.fragments.landlord.ApartmentsFragment;
import com.app.partners.fragments.landlord.HomeFragment;
import com.app.partners.fragments.landlord.NoApartmentsFragment;
import com.app.partners.models.Apartment;
import com.app.partners.models.LandLordIdToApartments;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LandLord extends AppCompatActivity {

    TextView signOut;
    CustomizedViewPager viewPager;

    private DatabaseReference userIdToApartmentRef;
    private DatabaseReference apartmentRef;
    private UserPageStateAdapter landLordPageStateAdapter;

    // Local User State
    public ArrayList<Apartment> apartmentIds;
    public String userId;
    public String userName;
    public Apartment myApartment = new Apartment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_land_lord);


        setupViewPager();

        FirebaseUser firebaseUser = UserUtils.getUser();
        if (firebaseUser == null) {
            navigateToWelcome();
        }

        String uid = firebaseUser.getUid();

        userName = getIntent().getStringExtra("name");
        Toast.makeText(getApplicationContext(), userName, Toast.LENGTH_SHORT).show();
        userId = uid;
        userIdToApartmentRef = FirebaseDatabase.getInstance().getReference().child("userId_to_apartment").child(uid);

        checkIfApartmentsExists();



        signOut = findViewById(R.id.signOut);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
    }

    private void checkIfApartmentsExists() {

        ValueEventListener apartmentListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    // go to creating new ap
//                    Toast.makeText(getApplicationContext(), "Apartment Not Found", Toast.LENGTH_SHORT).show();

                    // No apartment found
                    viewPager.setCurrentItem(0); //go to no Apartments Fragment
                    return;
                }

//                Toast.makeText(getApplicationContext(), "Apartment Found ", Toast.LENGTH_SHORT).show();



              /*  if (dataSnapshot.hasChildren()) {
                    Toast.makeText(getApplicationContext(), "Null apartments found ", Toast.LENGTH_SHORT).show();
                    apartmentIds.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Apartment apartment = ds.getValue(Apartment.class);
                        apartmentIds.add(apartment.id);
                    }
                }*/

                System.out.println("here");

                navigateToHome();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
//                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        userIdToApartmentRef.addValueEventListener(apartmentListener);
    }


    private void setupViewPager() {

        viewPager = findViewById(R.id.container);
        landLordPageStateAdapter = new UserPageStateAdapter(getSupportFragmentManager(), 0);

        landLordPageStateAdapter.addFragment(new NoApartmentsFragment()); //0
        landLordPageStateAdapter.addFragment(new HomeFragment()); //1
        landLordPageStateAdapter.addFragment(new ApartmentsFragment());// 2

        viewPager.setAdapter(landLordPageStateAdapter);

    }


    private void navigateToWelcome() {
        Intent i = new Intent(getBaseContext(), WelcomeActivity.class);
        startActivity(i);
        finish();
    }

    private void navigateToHome() {
        viewPager.setCurrentItem(2);
    }

    public void signOut() {
        UserUtils.signOut();
        startActivity(new Intent(getBaseContext(), WelcomeActivity.class));
    }
}
