package com.alizawren.myparker;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Alisa Ren on 4/6/2019.
 */

public class Util {

    public static final String TAG = "hello";
    public static User currentUser;

    static public Callback<List<ParkingSpot>> getParkingSpots()
    {
        final Callback<List<ParkingSpot>> callback = new Callback<>();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null)
        {
            callback.reject();
            return callback;
        }

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("parkingspots")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        List<ParkingSpot> result = new ArrayList<>();
                        if (task.isSuccessful())
                        {
                            for(QueryDocumentSnapshot document : task.getResult())
                            {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                result.add(document.toObject(ParkingSpot.class));

                            }
                        }
                        else
                        {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                        callback.resolve(result);
                    }
                });

        return callback;
    }

    static public Callback<ParkingSpot> addParkingSpot(User user, final ParkingSpot parkingSpot)
    {
        final Callback<ParkingSpot> callback = new Callback<>();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null)
        {
            callback.reject();
            return callback;
        }

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("parkingspots")
                .document(parkingSpot.getID())
                .set(parkingSpot)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        callback.resolve(parkingSpot);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                        callback.reject();
                    }
                });

        return callback;
    }

    static public User getCurrentUser()
    {
        return currentUser;
    }

    static public Callback<User> getUser(final FirebaseUser firebaseUser)
    {
        System.out.println("Get user is called");
        final Callback<User> callback = new Callback<>();

        if (firebaseUser != null)
        {
            callback.reject();
            return callback;
        }

        final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        final DocumentReference userReference = firestore.collection("users").document(firebaseUser.getUid());

        userReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task)
            {
                if (task.isSuccessful())
                {
                    DocumentSnapshot userDocument = task.getResult();
                    if (!userDocument.exists())
                    {
                        User newUser = new User(firebaseUser.getUid(), firebaseUser.getDisplayName(), firebaseUser.getEmail());
                        userReference.set(newUser);
                        firestore.document("emails/" + newUser.getEmail()).set(newUser);
                        callback.resolve(newUser);

                        //TODO: THIS IS BAD!
                        currentUser = newUser;
                        System.out.println("MADE A USER");
                    }
                    else
                    {
                        User newUser = userDocument.toObject(User.class);
                        callback.resolve(newUser);

                        //TODO: THIS IS BAD!
                        currentUser = newUser;

                        System.out.println("had A USER");
                    }
                }
                else {
                    System.out.println("Task wasn't successful");
                }
            }
        });

        return callback;
    }

    static public String getNewID() {
        return UUID.randomUUID().toString();
    }

}
