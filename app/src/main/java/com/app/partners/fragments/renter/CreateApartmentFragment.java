package com.app.partners.fragments.renter;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.partners.R;
import com.app.partners.activities.main.Renter;
import com.app.partners.activities.utils.NameValue;
import com.app.partners.activities.utils.UserUtils;
import com.app.partners.models.Apartment;
import com.app.partners.models.ApartmentExpenses;
import com.app.partners.models.UserIdApartment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateApartmentFragment extends Fragment {
    Button create;
    EditText name, address;

    public CreateApartmentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_create_apartment, container, false);
        create = v.findViewById(R.id.create_button);
        name = v.findViewById(R.id.apartment_name);
        address = v.findViewById(R.id.address);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createApartment();
            }
        });

        return v;
    }

    private void createApartment() {
        String name_st = name.getText().toString();
        String address_st = address.getText().toString();

        if (name_st.isEmpty() || address_st.isEmpty()) {
            Toast.makeText(getActivity().getApplicationContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        final String uid = UserUtils.getUser().getUid();

        // Apartment Key
        final DatabaseReference apartmentsRef = FirebaseDatabase.getInstance().getReference().child("apartments").push();

        final String apartmentId = apartmentsRef.getKey();

        Apartment newApartment = new Apartment(name_st, address_st);

        apartmentsRef.setValue(newApartment).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                apartmentsRef.child("partners").push().setValue(uid).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        createUserIdToApartment(apartmentId, uid);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }

    private void createUserIdToApartment(final String apartmentId, final String uid) {
        // User to Apartment
        DatabaseReference userIdApartmentRef = FirebaseDatabase.getInstance().getReference().child("userId_to_apartment").child(uid);

        userIdApartmentRef.setValue(new UserIdApartment(apartmentId)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                ((Renter)getActivity()).apartmentId = apartmentId;
                Toast.makeText(getActivity().getApplicationContext(), "Apartment created successfully!", Toast.LENGTH_SHORT).show();
                createApartmentExpenses(apartmentId, uid);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void createApartmentExpenses(final String apartmentId, final String uid) {
        // User to Apartment
        final DatabaseReference apartmentExpensesRef = FirebaseDatabase.getInstance().getReference().child("apartmentExpenses").child(apartmentId).child(uid);

        apartmentExpensesRef.setValue(new NameValue(((Renter)getActivity()).userName, 0)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                navigateToMyApartment();
            }
        });

//        ApartmentExpenses apartmentExpenses = new ApartmentExpenses();
//
////        apartmentExpenses.expenses.put(uid, 0);
//
//        apartmentExpensesRef.setValue(apartmentExpenses).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                apartmentExpensesRef.child("expenses").child(uid).setValue(new NameValue(((Renter)getActivity()).userName, 0)).addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        navigateToMyApartment();
//                    }
//                });
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//            }
//        });
    }

    private void navigateToMyApartment() {
        ((Renter)getActivity()).viewPager.setCurrentItem(2);
    }

}
