package com.example.powelljordan.astory.handlers;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.powelljordan.astory.MainActivity;
import com.example.powelljordan.astory.models.Notification;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.powelljordan.astory.models.User;
import com.google.firebase.firestore.SetOptions;

/**
 * Created by jorda on 12/23/2017.
 */

public class UserHandler {
    private static String TAG = "UserHandler";
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static CollectionReference UserCollection = db.collection("users");

    public static void findOrCreate(final String id, final String username, final Uri profilePictureURL){
        Log.d(TAG, "id: " + id + " username: " + username);
        UserCollection.document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    final DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        Log.d(TAG, "User data: " + document.getData());
                        MainActivity.currentUserObj = document.toObject(User.class);
                    } else{
                        Log.d(TAG, "No user entry found. Creating a new one");
                        User user = new User(id, username, profilePictureURL);
                        UserCollection.document(id)
                                .set(user)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "User successfully created");
                                        MainActivity.currentUserObj = document.toObject(User.class);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "Error creating user", e);
                                    }
                                });

                    }
                } else {
                    Log.d(TAG, "Get failed with ", task.getException());
                }
            }
        });
    }

    public static Task getUser(String userId) {
        return UserCollection.document(userId).get();
    }

    public static Task getUsers(){
        return UserCollection.get();
    }

    public static Task createUser(User user) {
        return UserCollection.document(user.id).set(user);
    }

    public static Task addNotification(String userId, Notification notification) {
        return UserCollection.document(userId).set(notification.data, SetOptions.merge());
    }
}
