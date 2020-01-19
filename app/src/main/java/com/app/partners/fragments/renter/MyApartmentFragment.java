package com.app.partners.fragments.renter;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.partners.R;
import com.app.partners.activities.main.Renter;
import com.app.partners.activities.utils.NameValue;
import com.app.partners.adapters.StatsAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyApartmentFragment extends Fragment {

    TextView name_text, address_text, title, userName;
    FloatingActionButton plusButton;
    Button addPartner, addLandLord;
    Button addTask;
    String apId, name, uid;
    DatabaseReference statsRef;

    public MyApartmentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_apartment, container, false);

        name_text = v.findViewById(R.id.name);
        address_text = v.findViewById(R.id.address);
        plusButton = v.findViewById(R.id.plusButton);
        addPartner = v.findViewById(R.id.addPartner);
        addLandLord = v.findViewById(R.id.addLandLord);
        addTask = v.findViewById(R.id.addTask);
        title = v.findViewById(R.id.title);
        userName = v.findViewById(R.id.userName);


        name = ((Renter)getActivity()).myApartment.name;
        uid = ((Renter)getActivity()).userId;
        String address = ((Renter)getActivity()).myApartment.address;

        name_text.setText(name);
        address_text.setText(address);
        userName.setText(((Renter)getActivity()).userName);

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Renter)getActivity()).viewPager.setCurrentItem(4);
            }
        });

        addPartner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Renter)getActivity()).viewPager.setCurrentItem(3);
            }
        });

        addLandLord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Renter)getActivity()).viewPager.setCurrentItem(8);
            }
        });

        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Renter)getActivity()).viewPager.setCurrentItem(6);
            }
        });


        apId = ((Renter)getActivity()).apartmentId;
        statsRef = FirebaseDatabase.getInstance().getReference().child("apartmentExpenses").child(apId);

        getStats();

        return v;
    }

    public void getStats() {
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    final ArrayList<NameValue> nameValues = new ArrayList<>();

                    int sum = 0;
                    int mySpent = 0;

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        NameValue nv = ds.getValue(NameValue.class);
                        nameValues.add(nv);

                        String id = ds.getKey();

                        if (id.equals(uid)) {
                            mySpent = nv.value;
                        }

                        sum += nv.value;
                    }

                    int num = nameValues.size();

                    int avg = sum / num;

                    int debt = mySpent - avg;

                    if (debt > 0) {
                        title.setText("Apartment partners owe you: " + debt + " ₪");
                    } else {
                        title.setText("You owe: " + -1 * debt + " ₪");
                    }

//                    title.setText("Total spent: " + sum + " ₪");
//                    statsAdapter = new StatsAdapter(nameValues);
//                    recyclerView.setAdapter(statsAdapter);
//                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        statsRef.addValueEventListener(listener);
    }
}
