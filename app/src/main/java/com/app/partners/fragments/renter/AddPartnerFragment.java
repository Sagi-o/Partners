package com.app.partners.fragments.renter;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.partners.R;
import com.app.partners.activities.main.LandLord;
import com.app.partners.activities.main.Renter;
import com.app.partners.activities.utils.UserUtils;
import com.app.partners.models.PhoneUserId;
import com.app.partners.models.User;
import com.app.partners.models.UserIdApartment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddPartnerFragment extends Fragment {

    EditText phone;
    Button enter;
    DatabaseReference phoneUserIdRef;
    DatabaseReference apartmentRef;
    DatabaseReference userIdToApartment;

    public AddPartnerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_partner, container, false);

        phone = v.findViewById(R.id.phone);
        enter = v.findViewById(R.id.enter);

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(getContext())
                        .setTitle("Add New Partner")
                        .setMessage("Are you sure you want to add " + phone.getText().toString() + " ?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                getUserIdFromPhone(phone.getText().toString());
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });

        return v;
    }

    private void getUserIdFromPhone(final String phone) {
        phoneUserIdRef = FirebaseDatabase.getInstance().getReference().child("phone_to_userId").child(phone);

        ValueEventListener phoneListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (!dataSnapshot.exists()) {
                    Toast.makeText(getActivity().getApplicationContext(), "User not found", Toast.LENGTH_SHORT).show();
                    return;
                }

                PhoneUserId phoneUserId = dataSnapshot.getValue(PhoneUserId.class);

                String targetUserId = phoneUserId.userId;

                addPartner(targetUserId);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                //  Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        phoneUserIdRef.addListenerForSingleValueEvent(phoneListener);
    }

    private void addPartner(final String targetUserId) {
        userIdToApartment = FirebaseDatabase.getInstance().getReference().child("userId_to_apartment").child(targetUserId);

        // Check if already have an apartment
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    addPartnerToApartment(targetUserId);
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Not added. User already has an apartment", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        userIdToApartment.addListenerForSingleValueEvent(listener);
    }


    public void addPartnerToApartment(String targetUserId) {
        String apId = (((Renter)getActivity()).apartmentId);
        apartmentRef = FirebaseDatabase.getInstance().getReference().child("apartments").child(apId).child("partners");

        userIdToApartment.setValue(new UserIdApartment(apId))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });

        apartmentRef.push().setValue(targetUserId).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getActivity().getApplicationContext(), "User added successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
