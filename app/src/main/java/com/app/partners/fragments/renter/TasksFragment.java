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
import com.app.partners.adapters.TasksAdapter;
import com.app.partners.models.Expense;
import com.app.partners.models.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TasksFragment extends Fragment {

    TasksAdapter tasksAdapter;
    RecyclerView recyclerView;


    public TasksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tasks, container, false);

        recyclerView = v.findViewById(R.id.tasks);

        initTasksList();

        return v;
    }

    private void initTasksList() {
         String apId = (((Renter)getActivity()).apartmentId);

            DatabaseReference taskRef = FirebaseDatabase.getInstance().getReference().child("apartments").child(apId).child("tasks");

            ValueEventListener listener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        final ArrayList<Task> tasks = new ArrayList<>();

                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            Task task = ds.getValue(Task.class);
                            tasks.add(task);
                        }
                        tasksAdapter = new TasksAdapter(tasks);
                        recyclerView.setAdapter(tasksAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };

            taskRef.addValueEventListener(listener);
        }


}
