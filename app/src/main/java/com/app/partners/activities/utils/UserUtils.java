package com.app.partners.activities.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserUtils {
    static private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public static void signOut() {
        mAuth.signOut();
    }
    public static FirebaseUser getUser() {
        return mAuth.getCurrentUser();
    }
}
