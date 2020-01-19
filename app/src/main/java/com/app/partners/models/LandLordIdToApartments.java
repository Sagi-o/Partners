package com.app.partners.models;

import java.util.ArrayList;

public class LandLordIdToApartments {

    ArrayList<String> apartmentsIds;

    public LandLordIdToApartments(ArrayList<String> apartmentsIds) {
        this.apartmentsIds = apartmentsIds;
    }

    public LandLordIdToApartments() {
        apartmentsIds = new ArrayList<>();
    }

}
