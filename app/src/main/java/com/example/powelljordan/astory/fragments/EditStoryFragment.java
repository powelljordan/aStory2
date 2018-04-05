package com.example.powelljordan.astory.fragments;

import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.powelljordan.astory.R;
import com.example.powelljordan.astory.events.LocationEvent;
import com.example.powelljordan.astory.events.TransitionEvent;
import com.example.powelljordan.astory.handlers.StoryHandler;
import com.example.powelljordan.astory.models.Story;
import com.example.powelljordan.astory.models.User;
import com.example.powelljordan.astory.databinding.FragmentEditStoryBinding;
import com.example.powelljordan.astory.uitil.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.GeoPoint;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jorda on 12/28/2017.
 */

public class EditStoryFragment extends Fragment {
    private final String TAG = "EditStoryFragment";
    private Location storyLocation;
    private Location currentLocation;
    private View view;
    private Story story;
    private User user;

    @BindView(R.id.save_story_button)
    Button saveButton;

    @BindView(R.id.story_title)
    EditText titleEdit;

    @BindView(R.id.story_text_content)
    EditText textEdit;

    public static EditStoryFragment newInstance(Story story, User user, Location location) {
        EditStoryFragment fragment = new EditStoryFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable(Constants.EDIT_STORY_FRAGMENT_ID, story);
        arguments.putParcelable(Constants.EDIT_STORY_USER_FRAGMENT_ID, user);
        arguments.putParcelable(Constants.EDIT_STORY_FRAGMENT_LOCATION_ID, location);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
            user = arguments.getParcelable(Constants.EDIT_STORY_USER_FRAGMENT_ID);
            story = arguments.getParcelable(Constants.EDIT_STORY_FRAGMENT_ID);
            storyLocation = arguments.getParcelable(Constants.EDIT_STORY_FRAGMENT_LOCATION_ID);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final FragmentEditStoryBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_story, container, false);
        view = binding.getRoot();
        ButterKnife.bind(this, view);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveStory();
            }
        });
        return view;
    }

    public void saveStory() {
        Log.d(TAG, "Story to be saved: " + story);
        story.title = titleEdit.getText().toString();
        story.content.text = textEdit.getText().toString();
        story.author = user.username;
        story.audience = user.audience;
        story.location = new GeoPoint(storyLocation.getLatitude(), storyLocation.getLongitude());
        story.date = new Date();

        StoryHandler.addStory(story).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                showSnackbar(R.string.successful_save_response);
                EventBus.getDefault().post(new TransitionEvent(Constants.STORY_SAVED));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showSnackbar(R.string.failure_save_response);
            }
        });
    }

    private void showSnackbar(@StringRes int errorMessageRes) {
        Log.d(TAG, "view: " + view);
        Snackbar.make(view, errorMessageRes, Snackbar.LENGTH_LONG).show();
    }

    @Subscribe
    public void onLocationEvent(LocationEvent event){
        currentLocation = event.location;
    }
}
