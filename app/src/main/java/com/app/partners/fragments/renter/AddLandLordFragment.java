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

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddLandLordFragment extends Fragment {

    EditText phone;
    Button enter;
    DatabaseReference phoneUserIdRef;
    DatabaseReference apartmentRef;
    DatabaseReference userIdToApartment;
    String apId;

    public AddLandLordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_land_lord, container, false);

        apId = ((Renter)getActivity()).apartmentId;

        phone = v.findViewById(R.id.phone);
        enter = v.findViewById(R.id.enter);

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(getContext())
                        .setTitle("Add New landlord")
                        .setMessage("Are you sure you want to add landlord" + phone.getText().toString() + " ?")
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

                addLandLord(targetUserId);
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

    private void addLandLord(final String targetUserId) {


        DatabaseReference landLordRef = FirebaseDatabase.getInstance().getReference().child("apartments").
                child(apId).child("landlord");

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    //Add LandLord to apartment
                    addLandLordToApartment(targetUserId);

                    DatabaseReference landlordToApartmentsRef = FirebaseDatabase.getInstance().getReference("land_lord_to_apartments").child(targetUserId);
                    UserIdApartment userIdApartment = new UserIdApartment(apId);
                    landlordToApartmentsRef.push().setValue(userIdApartment);


                }
                else {
                    Toast.makeText(getActivity().getApplicationContext(), "The apartment already has a landlord", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }; landLordRef.addListenerForSingleValueEvent(listener);

        // 1.
        // Add to "landloard_to_apartments/targetUserId/push()/{ apartmentId: apartmentId }"
        // New UserIdApartment

        // 2.
        // Fetch "apartments/apId/landloardId"
        // If null: do him
        // else stop process

        // If want apas data: Fetch to this ref, and get apartments


//        userIdToApartment = FirebaseDatabase.getInstance().getReference().child("userId_to_apartment").child(targetUserId);
//
//        // Check if already have an apartment
//        ValueEventListener listener = new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    addPartnerToApartment(targetUserId);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        };
//        userIdToApartment.addListenerForSingleValueEvent(listener);
    }


    public void addLandLordToApartment(String targetUserId) {
        String apId = (((Renter)getActivity()).apartmentId);
        apartmentRef = FirebaseDatabase.getInstance().getReference().child("apartments").child(apId).child("landlord");
        apartmentRef.setValue(targetUserId);
    }

}
