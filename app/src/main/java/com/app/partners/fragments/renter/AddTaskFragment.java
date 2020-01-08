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
import com.app.partners.models.Task;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddTaskFragment extends Fragment {

    EditText description;
    Button add;

    String description_st;

    public AddTaskFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_add_task, container, false);

        description = v.findViewById(R.id.description);
        add = v.findViewById(R.id.add);


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (description.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity().getApplicationContext(), "Fill all details", Toast.LENGTH_SHORT).show();
                    return;
                }
                addTask();
            }
        });


        return v;
    }

    public void addTask() {
        description_st = description.getText().toString();

        long timestamp = new Date().getTime();
        String uid = ((Renter)getActivity()).userId;
        String apId = (((Renter)getActivity()).apartmentId);
        String userName = (((Renter)getActivity()).userName);

        final Task newTask = new Task(description_st,uid,"","",timestamp,userName);

        DatabaseReference taskRef = FirebaseDatabase.getInstance().getReference().child("apartments").child(apId).child("tasks").push();
        String taskKey = taskRef.getKey();
        newTask.taskId = taskKey;

        final DatabaseReference userTaskRef = FirebaseDatabase.getInstance().getReference().child("userTasks").child(uid).child(taskKey);

        taskRef.setValue(newTask).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getActivity().getApplicationContext(), "Task added successfully", Toast.LENGTH_SHORT).show();
                userTaskRef.setValue(newTask);
            }
        });


    }

}
