package com.app.partners.fragments.renter;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.app.partners.R;
import com.app.partners.activities.main.Renter;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoApartmentFragment extends Fragment {

    Button create;

    public NoApartmentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_no_apartment, container, false);

        create = v.findViewById(R.id.create);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToCreateApartment();
            }
        });

        return v;
    }

    private void navigateToCreateApartment() {
        ((Renter)getActivity()).viewPager.setCurrentItem(1);
    }
}
