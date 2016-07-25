package com.capstone.cudaf.ultratravel;

import android.app.Application;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;


/**
 * Customized Application class
 */

public class UltratravelApplication extends Application {

    private FirebaseStorage mStorage;
    private DatabaseReference mDatabase;

    synchronized public FirebaseStorage getStorage(){
        if (mStorage == null){
            mStorage = FirebaseStorage.getInstance();
        }
        return mStorage;
    }

    synchronized public DatabaseReference getDatabaseReference(){
        if (mDatabase == null){
            mDatabase = FirebaseDatabase.getInstance().getReference();
        }
        return mDatabase;
    }


}
