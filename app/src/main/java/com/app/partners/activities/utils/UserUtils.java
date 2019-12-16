package com.app.partners.activities.utils;

import com.google.firebase.auth.FirebaseAuth;

public class UserUtils {
    static private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public static void signOut() {
        mAuth.signOut();
    }
}
