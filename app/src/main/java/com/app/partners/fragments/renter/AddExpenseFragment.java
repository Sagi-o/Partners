package com.app.partners.fragments.renter;


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
import com.app.partners.activities.main.Renter;
import com.app.partners.activities.utils.NameValue;
import com.app.partners.models.Apartment;
import com.app.partners.models.ApartmentExpenses;
import com.app.partners.models.Expense;
import com.app.partners.models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddExpenseFragment extends Fragment {

    EditText description, price;
    Button add;
//    Apartment myApartment = new Apartment();
    String description_st;
    int price_int;

    String uid, apId;

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

        uid = ((Renter)getActivity()).userId;
        apId = (((Renter)getActivity()).apartmentId);

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

        //
        updateUserSumOfShopping(price_int);
        //

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

        //
//        myApartment.sumOfShopping += price_int;
//        myApartment.list.put(uid, price_int);
//        calculateStats();
        //
    }

    private void updateUserSumOfShopping(final int price) {
        final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(uid);
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final User user = dataSnapshot.getValue(User.class);
                user.sumOfShopping += price;

                HashMap<String, Object> map = new HashMap<>();
                map.put("sumOfShopping", user.sumOfShopping);

                userRef.updateChildren(map);

                updateApartmentExpense(uid);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        userRef.addListenerForSingleValueEvent(listener);
    }

//    private void calculateStats() {
//        int numOfPartners = myApartment.list.size();
//        final int average = myApartment.sumOfShopping/numOfPartners;
//        DatabaseReference partnersRef = FirebaseDatabase.getInstance().getReference("apartments").child("partners");
//        ValueEventListener listener = new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot ds: dataSnapshot.getChildren()){
//                    int debt = average  - myApartment.list.get(ds.getValue().toString());
//                    updateUSer(ds.getValue().toString(),debt);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        }; partnersRef.addListenerForSingleValueEvent(listener);
//
//
//    }

//    private void updateUSer(String uid, final int debt) {
//
//        final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
//        ValueEventListener listener = new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                final User user = dataSnapshot.getValue(User.class);
//                user.debtToApartment = debt;
//                userRef.setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        userRef.setValue(user);
//                    }
//                });
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        }; userRef.addListenerForSingleValueEvent(listener);
//
//    }

    private void updateApartmentExpense(final String uid) {
        final DatabaseReference apartmentExpensesRef = FirebaseDatabase.getInstance().getReference().child("apartmentExpenses").child(apId);

        final DatabaseReference userExpenseRef = apartmentExpensesRef.child(uid);

        ValueEventListener listener2 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    NameValue nv = dataSnapshot.getValue(NameValue.class);

                    userExpenseRef.setValue(new NameValue(((Renter)getActivity()).userName, nv.value + price_int)).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
//                      navigateToMyApartment();
                        }
                    });
                } else {
                    userExpenseRef.setValue(new NameValue(((Renter)getActivity()).userName, price_int)).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
//                      navigateToMyApartment();
                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        userExpenseRef.addListenerForSingleValueEvent(listener2);

//        ValueEventListener listener = new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                final ApartmentExpenses apartmentExpenses = dataSnapshot.getValue(ApartmentExpenses.class);
//
////                apartmentExpenses.sum += price_int;
//
//                // TODO: On adding partner, update to zero
////                apartmentExpenses.expenses.put(uid, apartmentExpenses.expenses.get(uid) + price_int);
//
//                apartmentExpensesRef.child("sum").setValue(apartmentExpenses.sum).addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//
//                        final DatabaseReference userExpenseRef = apartmentExpensesRef.child("expenses").child(uid);
//
//                        ValueEventListener listener2 = new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                                if (dataSnapshot.exists()) {
//                                    NameValue nv = dataSnapshot.getValue(NameValue.class);
//
//                                    userExpenseRef.setValue(new NameValue(((Renter)getActivity()).userName, nv.value + price_int)).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                        @Override
//                                        public void onSuccess(Void aVoid) {
////                                            navigateToMyApartment();
//                                        }
//                                    });
//                                }
//
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                            }
//                        };
//
//                        userExpenseRef.addListenerForSingleValueEvent(listener2);
//
//
//                    }
//                });
//
//
////                apartmentExpensesRef.setValue(apartmentExpenses).addOnSuccessListener();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        };
//        apartmentExpensesRef.addListenerForSingleValueEvent(listener);
    }

}
