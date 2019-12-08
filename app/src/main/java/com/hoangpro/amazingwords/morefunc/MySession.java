package com.hoangpro.amazingwords.morefunc;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hoangpro.amazingwords.model.Account;
import com.hoangpro.amazingwords.sqlite.User;

public class MySession {
    public static Account currentAccount = null;

    public static void updateAccount(Context context, Account account) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("accounts").child(account.idFacebook).setValue(account);
        User.writeUser(context);
    }
}
