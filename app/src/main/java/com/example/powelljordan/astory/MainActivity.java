package com.example.powelljordan.astory;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.powelljordan.astory.events.LocationEvent;
import com.example.powelljordan.astory.models.Story;
import com.example.powelljordan.astory.models.User;
import com.example.powelljordan.astory.events.TransitionEvent;
import com.example.powelljordan.astory.fragments.EditStoryFragment;
import com.example.powelljordan.astory.fragments.MapsFragment;
import com.example.powelljordan.astory.fragments.StoryFragment;
import com.example.powelljordan.astory.fragments.UserFragment;
import com.example.powelljordan.astory.uitil.Constants;
import com.example.powelljordan.astory.uitil.Util;
import com.google.firebase.auth.FirebaseUser;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {
    public static FirebaseUser currentUser;
    public static User currentUserObj;
    public static Location currentLocation;
    private final String TAG = "MainActivity";
    private UserFragment userFragment;
    private StoryFragment storyFragment;
    private EditStoryFragment editStoryFragment;
    private MapsFragment mapsFragment;

    @BindView(android.R.id.content)
    View mRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_main);
        ButterKnife.bind(this);
        checkLocationPermissions();
        mapsFragment = new MapsFragment();
        if (savedInstanceState == null) {
            loadMapsFragment();
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop(){
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onTransitionEvent(TransitionEvent event) {
        Log.d(TAG, "Event type: " + event.transitionType);
        if (event.transitionType == Constants.USER_TRANSITION) {
            loadUserFragment(event.user);
        }
        if (event.transitionType == Constants.STORY_TRANSITION) {
            loadStoryFragment(event.story);
        }
        if (event.transitionType == Constants.EDIT_STORY_TRANSITION) {
            loadEditStoryFragment(event.story, event.user, event.location);
        }
        if (event.transitionType == Constants.STORY_SAVED) {
            reloadMapsFragment();
        }
        if (event.transitionType == Constants.LOGOUT_TRANSITION) {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }
    }

    @Subscribe
    public void onLocationEvent(LocationEvent event){
        currentLocation = event.location;
    }

    public void loadMapsFragment(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction
                .add(R.id.fragment_container, mapsFragment)
                .commit();
    }

    public void reloadMapsFragment(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, mapsFragment);
        transaction.commit();
    }


    public void loadUserFragment(User user){
//        if (userFragment == null) {
            userFragment = UserFragment.newInstance(user);
//        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, userFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void loadStoryFragment(Story story){
        Log.d(TAG, "loadedStory: "+story);
//        if (storyFragment == null) {
            storyFragment = StoryFragment.newInstance(story);
//        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, storyFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void loadEditStoryFragment(Story story, User user, Location location){
        if (story.id != null && (!story.author.equals(user.id))) {
            Log.e(TAG, user.username + " tried to edit story " + story.title + " but isn't the author");
            return;
        }
//        if (editStoryFragment == null) {
            editStoryFragment = EditStoryFragment.newInstance(story, user, location);
//        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, editStoryFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }



    public void checkLocationPermissions() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {
                            android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION },
                    Constants.PERMISSION_ACCESS_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == Constants.PERMISSION_ACCESS_LOCATION) {
            Log.d(TAG, "grantResults: " + grantResults + " PackageManager: " + PackageManager.PERMISSION_GRANTED);
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // All good!
            } else {
                showSnackbar(R.string.no_location_permission);
            }
        }
    }

    private void showSnackbar(@StringRes int errorMessageRes) {
        Log.d(TAG, "mRootView: " + mRootView);
        Snackbar.make(mRootView, errorMessageRes, Snackbar.LENGTH_LONG).show();
    }
}
