package com.app.partners.fragments.renter;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.partners.R;
import com.app.partners.activities.main.Renter;
import com.app.partners.activities.utils.IdValue;
import com.app.partners.models.Apartment;
import com.app.partners.models.Expense;
import com.app.partners.models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;

import static com.app.partners.activities.utils.UserUtils.getUser;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddExpenseFragment extends Fragment {

    EditText description, price;
    Button add;
    Apartment myApartment = new Apartment();
    String description_st;
    int price_int;

    public AddExpenseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_expense, container, false);

        description = v.findViewById(R.id.description);
        price = v.findViewById(R.id.price);
        add = v.findViewById(R.id.add);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (description.getText().toString().isEmpty() || price.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity().getApplicationContext(), "Fill all details", Toast.LENGTH_SHORT).show();
                    return;
                }
                addExpense();
            }
        });


        return v;
    }

    public void addExpense() {
        price_int = Integer.parseInt(price.getText().toString());
        description_st = description.getText().toString();

        long timestamp = new Date().getTime();
        String uid = ((Renter)getActivity()).userId;
        final String apId = (((Renter)getActivity()).apartmentId);
        getUser(price_int,uid);
        getApartment(apId,uid);
        String userName = (((Renter)getActivity()).userName);

        final Expense newExpense = new Expense(description_st, price_int, uid, "", timestamp, userName);
        DatabaseReference expenseRef = FirebaseDatabase.getInstance().getReference().child("apartments").child(apId).child("expenses").push();
        String expenseKey = expenseRef.getKey();

        final DatabaseReference userExpenseRef = FirebaseDatabase.getInstance().getReference().child("userExpenses").child(uid).child(expenseKey);

        expenseRef.setValue(newExpense).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getActivity().getApplicationContext(), "Expense added successfully", Toast.LENGTH_SHORT).show();
                userExpenseRef.setValue(newExpense);
            }
        });

        myApartment.sumOfShopping+=price_int;
        myApartment.list.put(uid,price_int);
        calculateStats();



    }

    private void getUser(final int price,String uid) {


        final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(uid);
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final User user = dataSnapshot.getValue(User.class);
                user.sumOfShopping+=price;
                userRef.setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        userRef.setValue(user);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }; userRef.addListenerForSingleValueEvent(listener);



    }
    private void calculateStats() {
        int numOfPartners = myApartment.list.size();
        final int average = myApartment.sumOfShopping/numOfPartners;
        DatabaseReference partnersRef = FirebaseDatabase.getInstance().getReference("apartments").child("partners");
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    int debt = average  - myApartment.list.get(ds.getValue().toString());
                    updateUSer(ds.getValue().toString(),debt);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }; partnersRef.addListenerForSingleValueEvent(listener);


    }

    private void updateUSer(String uid, final int debt) {

        final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final User user = dataSnapshot.getValue(User.class);
                user.debtToApartment = debt;
                userRef.setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        userRef.setValue(user);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }; userRef.addListenerForSingleValueEvent(listener);

    }

    private void getApartment(String apId, final String uid) {
        final DatabaseReference apartmentRef = FirebaseDatabase.getInstance().getReference().child("apartments").child(apId);
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myApartment = dataSnapshot.getValue(Apartment.class);
                myApartment.sumOfShopping+=price_int;
                myApartment.list.put(uid, price_int);
                apartmentRef.setValue(myApartment).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        apartmentRef.setValue(myApartment);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        apartmentRef.addListenerForSingleValueEvent(listener);
    }

}
