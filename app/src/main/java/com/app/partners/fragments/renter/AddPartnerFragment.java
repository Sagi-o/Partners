package com.app.partners.fragments.renter;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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
                getUserIdFromPhone(phone.getText().toString());
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

    private void addPartner(String targetUserId) {
        String apId = (((Renter)getActivity()).apartmentId);

        apartmentRef = FirebaseDatabase.getInstance().getReference().child("apartments").child(apId).child("partners");
        userIdToApartment = FirebaseDatabase.getInstance().getReference().child("userId_to_apartment").child(targetUserId);

        userIdToApartment.setValue(new UserIdApartment(apId))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });

        apartmentRef.push().setValue(UserUtils.getUser().getUid()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });

    }

}
