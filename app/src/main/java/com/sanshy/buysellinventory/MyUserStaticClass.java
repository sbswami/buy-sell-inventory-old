package com.sanshy.buysellinventory;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyUserStaticClass {
    public static DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    public static FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public static FirebaseUser userStatic = mAuth.getCurrentUser();

    public static String userIdMainStatic = userStatic.getUid();

    public static String getUserIdMainStatic() {
        return userIdMainStatic;
    }

    public static void setUserIdMainStatic(String userIdMainStatic) {
        MyUserStaticClass.userIdMainStatic = userIdMainStatic;
    }
}
