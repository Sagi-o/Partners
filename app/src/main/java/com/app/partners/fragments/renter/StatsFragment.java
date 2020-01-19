package com.app.partners.fragments.renter;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.partners.R;
import com.app.partners.activities.main.Renter;
import com.app.partners.activities.utils.NameValue;
import com.app.partners.adapters.StatsAdapter;
import com.app.partners.adapters.TasksAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatsFragment extends Fragment {

    TextView title;

    String apId;
    DatabaseReference statsRef;

    StatsAdapter statsAdapter;
    RecyclerView recyclerView;

    public StatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_stats, container, false);
        recyclerView = v.findViewById(R.id.recyclerView);
        title = v.findViewById(R.id.title);

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

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        NameValue nv = ds.getValue(NameValue.class);
                        nameValues.add(nv);

                        sum += nv.value;
                    }

                    title.setText("Total spent: " + sum + " ₪");
                    statsAdapter = new StatsAdapter(nameValues);
                    recyclerView.setAdapter(statsAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        statsRef.addValueEventListener(listener);
    }

//    private void init() {
//        String uid = ((Renter)getActivity()).userId;
//        DatabaseReference debtRef = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
//        ValueEventListener listener = new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                User user = dataSnapshot.getValue(User.class);
//                debt.setText(""+user.debtToApartment);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        }; debtRef.addValueEventListener(listener);
//    }

}
