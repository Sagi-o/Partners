package com.app.partners.fragments.renter;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.partners.R;
import com.app.partners.activities.main.Renter;
import com.app.partners.adapters.ExpensesAdapter;
import com.app.partners.models.Expense;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExpensesFragment extends Fragment {
    RecyclerView recyclerView;
    ExpensesAdapter expensesAdapter;


    public ExpensesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_expenses, container, false);

        recyclerView = v.findViewById(R.id.expenses);

        initExpensesList();

        return v;
    }

    public void initExpensesList() {
        String apId = (((Renter)getActivity()).apartmentId);

        DatabaseReference expenseRef = FirebaseDatabase.getInstance().getReference().child("apartments").child(apId).child("expenses");

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    final ArrayList<Expense> expenses = new ArrayList<>();

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Expense expense = ds.getValue(Expense.class);
                        expenses.add(expense);
                    }
                    expensesAdapter = new ExpensesAdapter(expenses);
                    recyclerView.setAdapter(expensesAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        expenseRef.addValueEventListener(listener);
    }

}
