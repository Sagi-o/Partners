package com.app.partners.fragments.renter;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.partners.R;
import com.app.partners.activities.main.Renter;
import com.app.partners.activities.utils.IdValue;
import com.app.partners.models.Expense;
import com.app.partners.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatsFragment extends Fragment {


    TextView debt;
    public StatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_stats, container, false);

        debt = v.findViewById(R.id.debt);
        init();
        return v;


    }

    private void init() {
        String uid = ((Renter)getActivity()).userId;
        DatabaseReference debtRef = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                debt.setText(""+user.debtToApartment);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }; debtRef.addValueEventListener(listener);
    }

}
