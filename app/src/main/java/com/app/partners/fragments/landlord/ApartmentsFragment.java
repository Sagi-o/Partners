package com.app.partners.fragments.landlord;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.partners.R;
import com.app.partners.activities.main.LandLord;
import com.app.partners.activities.utils.UserUtils;
import com.app.partners.adapters.ApartmentsAdapter;
import com.app.partners.models.Apartment;
import com.app.partners.models.UserIdApartment;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ApartmentsFragment extends Fragment {

    RecyclerView recyclerView;
    String landLordId;
    ArrayList<Apartment> apartments = new ArrayList<>();

    public ApartmentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_apartments, container, false);
        recyclerView = v.findViewById(R.id.recyclerView);
        landLordId = ((LandLord)getActivity()).userId;
        initApartmentsList();
        return v;
    }

    private void initApartmentsList() {

        final DatabaseReference landLordToApartmentsRef = FirebaseDatabase.getInstance().getReference().child("land_lord_to_apartments").child(landLordId);
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    apartments.clear();

                    final ArrayList<String> apartmentIds = new ArrayList<>();

                    if (dataSnapshot.exists()) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            UserIdApartment apId = ds.getValue(UserIdApartment.class);
//                            getApartment(apId, apartments);
                            apartmentIds.add(apId.apartmentId);
                        }

                        getApartments(apartmentIds);

//                        recyclerView.setAdapter(new ApartmentsAdapter(apartments));
//                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

        };
        landLordToApartmentsRef.addValueEventListener(listener);


    }

    private void getApartments(final ArrayList<String> apIds) {
        for (String apId : apIds) {
            final DatabaseReference apRef = FirebaseDatabase.getInstance().getReference().child("apartments").child(apId);

            ValueEventListener listener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Apartment apartment = dataSnapshot.getValue(Apartment.class);
                    apartments.add(apartment);

                    if (apartments.size() == apIds.size()) {
                        recyclerView.setAdapter(new ApartmentsAdapter(apartments));
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };

            apRef.addListenerForSingleValueEvent(listener);
        }
    }

    private void getApartment(final UserIdApartment apId, final ArrayList<Apartment> apartments) {
        final DatabaseReference apartmentsRef = FirebaseDatabase.getInstance().getReference().child("apartments");
        ValueEventListener apListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.getKey().equals(apId.apartmentId)) {
                        Apartment apartment = ds.getValue(Apartment.class);
                        apartments.add(apartment);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        apartmentsRef.addListenerForSingleValueEvent(apListener);
    }

}


