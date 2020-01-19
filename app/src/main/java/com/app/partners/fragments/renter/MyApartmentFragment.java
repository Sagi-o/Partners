package com.app.partners.fragments.renter;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.app.partners.R;
import com.app.partners.activities.main.Renter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyApartmentFragment extends Fragment {

    TextView name_text, address_text;
    FloatingActionButton plusButton;
    Button addPartner, addLandLord;
    Button addTask;

    public MyApartmentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_apartment, container, false);

        name_text = v.findViewById(R.id.name);
        address_text = v.findViewById(R.id.address);
        plusButton = v.findViewById(R.id.plusButton);
        addPartner = v.findViewById(R.id.addPartner);
        addLandLord = v.findViewById(R.id.addLandLord);
        addTask = v.findViewById(R.id.addTask);

        String name = ((Renter)getActivity()).myApartment.name;
        String address = ((Renter)getActivity()).myApartment.address;

        name_text.setText(name);
        address_text.setText(address);

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Renter)getActivity()).viewPager.setCurrentItem(4);
            }
        });

        addPartner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Renter)getActivity()).viewPager.setCurrentItem(3);
            }
        });

        addLandLord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Renter)getActivity()).viewPager.setCurrentItem(8);
            }
        });

        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Renter)getActivity()).viewPager.setCurrentItem(6);
            }
        });




        return v;
    }
}
