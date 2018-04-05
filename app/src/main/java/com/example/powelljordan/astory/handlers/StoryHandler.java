package com.example.powelljordan.astory.handlers;

import android.support.annotation.NonNull;

import com.example.powelljordan.astory.models.Comment;
import com.example.powelljordan.astory.models.Mention;
import com.example.powelljordan.astory.models.Reaction;
import com.example.powelljordan.astory.models.Story;
import com.example.powelljordan.astory.uitil.Constants;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * Created by jorda on 12/26/2017.
 */

public class StoryHandler {
    private static String TAG = "StoryHandler";
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static CollectionReference StoryCollection = db.collection("stories");

    @NonNull
    public static Task getStory(String id) {
        return StoryCollection.document(id).get();
    }

    @NonNull
    public static Task getStories(){
        return StoryCollection.get();
    }

    @NonNull
    public static void registerStoryListener (EventListener<QuerySnapshot> eventListener) {
        StoryCollection.addSnapshotListener(eventListener);
    }

    @NonNull
    public static Query getStoriesByUser(String userId){
        return StoryCollection.whereEqualTo("author", userId);
    }

    @NonNull
    public static Task getCommentsOnStory(String storyId){
        return StoryCollection.document(storyId).collection(Constants.COMMENTS_SUBCOLLECTION).get();
    }

    @NonNull
    public static Task getMentionsOnStory(String storyId){
        return StoryCollection.document(storyId).collection(Constants.MENTIONS_SUBCOLLECTION).get();
    }

    @NonNull
    public static Task getReactionsOnStory(String storyId){
        return StoryCollection.document(storyId).collection(Constants.REACTIONS_SUBCOLLECTION).get();
    }

    @NonNull
    public static Task addStory(Story story){
        story.id = StoryCollection.document().getId();
        return StoryCollection.document(story.id).set(story);
    }

    @NonNull
    public static Task addCommmentToStory(String storyId, Comment comment){
        CollectionReference commentRef = StoryCollection.document(storyId)
                .collection(Constants.COMMENTS_SUBCOLLECTION);
        String commentId = commentRef.document().getId();
        return commentRef.document(commentId).set(comment);
    }

    @NonNull
    public static Task addReactionToStory(String storyId, Reaction reaction){
        CollectionReference reactionRef = StoryCollection.document(storyId)
                .collection(Constants.REACTIONS_SUBCOLLECTION);
        String reactionId = reactionRef.document().getId();
        return reactionRef.document(reactionId).set(reaction);
    }

    @NonNull
    public static Task addCMentionToStory(String storyId, Mention mention){
        CollectionReference mentionRef = StoryCollection.document(storyId)
                .collection(Constants.MENTIONS_SUBCOLLECTION);
        String mentionId = mentionRef.document().getId();
        return mentionRef.document(mentionId).set(mention);
    }

}
