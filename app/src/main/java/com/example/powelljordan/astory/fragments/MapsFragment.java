package com.example.powelljordan.astory.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.powelljordan.astory.MainActivity;
import com.example.powelljordan.astory.R;
import com.example.powelljordan.astory.databinding.FragmentMapsBinding;
import com.example.powelljordan.astory.events.LocationEvent;
import com.example.powelljordan.astory.events.TransitionEvent;
import com.example.powelljordan.astory.handlers.StoryHandler;
import com.example.powelljordan.astory.models.Story;
import com.example.powelljordan.astory.uitil.Constants;
import com.example.powelljordan.astory.uitil.Util;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jorda on 12/25/2017.
 */

public class MapsFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private Location currentLocation;
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private final String TAG = "MapsFragment";
    private FragmentMapsBinding fragmentMapsBinding;
    private HashMap<String, Story> storyMap = new HashMap<>();
    private FusedLocationProviderClient mFusedLocationClient;
    private Bitmap profileBitmap;

    @BindView(R.id.profile_button)
    CircularImageView profileButton;

    @BindView(R.id.create_story_button)
    FloatingActionButton createStoryButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            mapFragment.getMapAsync(this);
        }
        fragmentMapsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_maps, container, false);
        View view = fragmentMapsBinding.getRoot();
        getChildFragmentManager().beginTransaction().replace(R.id.map, mapFragment).commit();
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        registerForLocationUpdates();
        getLastLocation();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        new Util.DownloadImageTask(profileButton)
                .execute(MainActivity.currentUserObj.profilePictureURL);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "selected user profile");
                EventBus.getDefault().post(new TransitionEvent(Constants.USER_TRANSITION, MainActivity.currentUserObj));
            }
        });
        createStoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "create story!");
                EventBus.getDefault().post(new TransitionEvent(Constants.EDIT_STORY_TRANSITION, new Story(), MainActivity.currentUserObj, currentLocation));
            }
        });
    }

    @Override
    @SuppressWarnings({"MissingPermission"})
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (isLocationEnabled()){
            mMap.setMyLocationEnabled(true);
        }
        mMap.getUiSettings().setMapToolbarEnabled(false);
        LatLng mit = new LatLng(42.3600949, -71.0960487);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mit));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
        mMap.setOnMarkerClickListener(this);
        loadStories();
    }

    @Subscribe
    public void onLocationEvent(LocationEvent event) {
        currentLocation = event.location;
    }

    @Override
    public void onStop() {
        unregisterForLocationUpdates();
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        String storyId = (String) marker.getTag();
        Log.d(TAG, "Story id " + storyId);
        EventBus.getDefault().post(new TransitionEvent(Constants.STORY_TRANSITION, storyMap.get(storyId)));
        return false;
    }

    public void loadStories() {
        Log.d(TAG, "loading Stories");
        StoryHandler.registerStoryListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot query, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                storyMap = new HashMap<>();
                mMap.clear();
                for (DocumentSnapshot document : query.getDocuments()) {
                    Log.d(TAG, "document: " + document.getData());
                    Story story = document.toObject(Story.class);
                    storyMap.put(story.id, story);
                    Marker marker = mMap
                            .addMarker(new MarkerOptions()
                                    .position(Util.geoPointToLatLng(story.location))
                                    .title(story.title));
                    Log.d(TAG, "marker: " + marker);
                    marker.setTag(story.id);
                }

            }
        });
    }

    @NonNull
    private FusedLocationProviderClient getFusedLocationProviderClient() {
        if (mFusedLocationClient == null) {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        }
        return mFusedLocationClient;
    }

    @SuppressLint("MissingPermission")
    void registerForLocationUpdates() {
        FusedLocationProviderClient locationProviderClient = getFusedLocationProviderClient();
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        Looper looper = Looper.myLooper();
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "App permissions were revoked");
            return;
        }
        locationProviderClient.requestLocationUpdates(locationRequest, locationCallback, looper);
        Log.d(TAG, "Registered Location Updates");
        //Checked in MainActivity
    }

    void  unregisterForLocationUpdates() {
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(locationCallback);
        }
        Log.d(TAG, "Unregistered Location Updates");
    }

    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            Location lastLocation = locationResult.getLastLocation();
            EventBus.getDefault().post(new LocationEvent(lastLocation));
            Toast.makeText(getContext(), "Location update", Toast.LENGTH_LONG);
            //Do Something location
        }
    };

    @SuppressWarnings({"MissingPermission"})
    public void getLastLocation() {
        // Get last known recent location using new Google Play Services SDK (v11+)
        if (isLocationEnabled()) {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // GPS location can be null if GPS is switched off
                            if (location != null) {
                                Toast.makeText(getContext(), "location: " + location, Toast.LENGTH_LONG).show();
                                EventBus.getDefault().post(new LocationEvent(location));
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("MapDemoActivity", "Error trying to get last GPS location");
                            e.printStackTrace();
                        }
                    });
        }
    }

    public Boolean isLocationEnabled(){
        return (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }
}
