package com.app.partners.activities.main;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.app.partners.R;
import com.app.partners.UserPageStateAdapter;
import com.app.partners.activities.utils.CustomizedViewPager;
import com.app.partners.activities.utils.UserUtils;
import com.app.partners.activities.welcome.WelcomeActivity;
import com.app.partners.fragments.renter.AddExpenseFragment;
import com.app.partners.fragments.renter.AddLandLordFragment;
import com.app.partners.fragments.renter.AddPartnerFragment;
import com.app.partners.fragments.renter.AddTaskFragment;
import com.app.partners.fragments.renter.CreateApartmentFragment;
import com.app.partners.fragments.renter.ExpensesFragment;
import com.app.partners.fragments.renter.NoApartmentFragment;
import com.app.partners.fragments.renter.MyApartmentFragment;
import com.app.partners.fragments.renter.StatsFragment;
import com.app.partners.fragments.renter.TasksFragment;
import com.app.partners.models.Apartment;
import com.app.partners.models.UserIdApartment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Renter extends AppCompatActivity {
    public CustomizedViewPager viewPager;

    private DatabaseReference userIdToApartmentRef;
    private DatabaseReference apartmentRef;
    private UserPageStateAdapter partnersPageStateAdapter;
    public BottomNavigationView bottomNavigationView;

    // Local User State
    public String apartmentId;
    public String userId;
    public String userName;
    public Apartment myApartment = new Apartment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renter);

        setupViewPager();
        setupNavBar();

        FirebaseUser firebaseUser = UserUtils.getUser();

        if (firebaseUser == null) {
            navigateToWelcome();
        }

        String uid = firebaseUser.getUid();

        userName = getIntent().getStringExtra("name");

        Toast.makeText(getApplicationContext(), userName, Toast.LENGTH_SHORT).show();

        userId = uid;

        userIdToApartmentRef = FirebaseDatabase.getInstance().getReference().child("userId_to_apartment").child(uid);

        checkIfApartmentExists();
    }

    private void setupNavBar() {
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.bottom_navigation_item_home:
                        navigateToApartment();
                        break;
                    case R.id.bottom_navigation_item_pie_chart:
                        navigateToStats();
                        break;
                    case R.id.bottom_navigation_item_shopping_cart:
                        navigateToShoppingList();
                        break;

                    case R.id.bottom_navigation_tasks:
                        navigateToTasks();
                }
                return true;
            }
        });
    }

    private void setupViewPager() {
        viewPager = findViewById(R.id.container);
        partnersPageStateAdapter = new UserPageStateAdapter(getSupportFragmentManager(), 0);

        partnersPageStateAdapter.addFragment(new NoApartmentFragment()); // 0
        partnersPageStateAdapter.addFragment(new CreateApartmentFragment()); // 1
        partnersPageStateAdapter.addFragment(new MyApartmentFragment()); // 2
        partnersPageStateAdapter.addFragment(new AddPartnerFragment()); // 3
        partnersPageStateAdapter.addFragment(new AddExpenseFragment()); // 4
        partnersPageStateAdapter.addFragment(new ExpensesFragment()); // 5
        partnersPageStateAdapter.addFragment(new AddTaskFragment()); //6
        partnersPageStateAdapter.addFragment(new TasksFragment()); //7
        partnersPageStateAdapter.addFragment(new AddLandLordFragment()); //8
        partnersPageStateAdapter.addFragment(new StatsFragment()); //9

        viewPager.setAdapter(partnersPageStateAdapter);
    }

    private void checkIfApartmentExists() {
        ValueEventListener apartmentListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    // go to creating new ap
//                    Toast.makeText(getApplicationContext(), "Apartment Not Found", Toast.LENGTH_SHORT).show();

                    // No apartment found
                    viewPager.setCurrentItem(0);
                    bottomNavigationView.setVisibility(View.INVISIBLE);

                    return;
                }
                bottomNavigationView.setVisibility(View.VISIBLE);

//                Toast.makeText(getApplicationContext(), "Apartment Found ", Toast.LENGTH_SHORT).show();

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
        userIdToApartmentRef.addValueEventListener(apartmentListener);
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

                Log.d("Renter", "Data for apartment has changed");

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

    private void navigateToTasks() {
        // send to apartment fragment with apId
//        this.apartmentId = apartmentId;
        viewPager.setCurrentItem(7);
    }

    private void navigateToStats() {
        viewPager.setCurrentItem(9);
    }

    private void navigateToShoppingList() {
        viewPager.setCurrentItem(5);
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
