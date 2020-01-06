package com.app.partners.fragments.renter;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.partners.R;
import com.app.partners.activities.main.Renter;
import com.app.partners.models.Expense;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddExpenseFragment extends Fragment {

    EditText description, price;
    Button add;

    String description_st;
    int price_int;

    public AddExpenseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_add_expense, container, false);

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
        String apId = (((Renter)getActivity()).apartmentId);
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


    }

}
