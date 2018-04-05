package com.example.powelljordan.astory.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.powelljordan.astory.MainActivity;
import com.example.powelljordan.astory.R;
import com.example.powelljordan.astory.models.User;
import com.example.powelljordan.astory.databinding.FragmentUserBinding;
import com.example.powelljordan.astory.events.TransitionEvent;
import com.example.powelljordan.astory.handlers.UserHandler;
import com.example.powelljordan.astory.uitil.Constants;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by jorda on 12/26/2017.
 */

public class UserFragment extends Fragment {
    private final String TAG = "UserFragment";

    private View.OnClickListener userOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v){
            onLogout();
        }
    };

    @BindView (R.id.logout_button)
    Button logoutButton;

    public static UserFragment newInstance (User user) {
        UserFragment userFragment = new UserFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable(Constants.USER_FRAGMENT_ID, user);
        userFragment.setArguments(arguments);
        return userFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);
        final FragmentUserBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user, container, false);
        View view = binding.getRoot();
        ButterKnife.bind(this, view);
        Bundle arguments = getArguments();
        User user = arguments.getParcelable(Constants.USER_FRAGMENT_ID);
        binding.setUser(user);
//        String id = MainActivity.currentUser.getUid();
//        UserHandler.getUser(id).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot userDoc = task.getResult();
//                    if (userDoc != null && userDoc.exists()) {
//                        User user = userDoc.toObject(User.class);
//                        binding.setUser(user);
//
//                    }
//                }
//            }
//        });
        return view;
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState){
        logoutButton.setOnClickListener(userOnClickListener);
    }

    public void onLogout(){
        AuthUI.getInstance()
                .signOut(getActivity())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        EventBus.getDefault().post(new TransitionEvent(Constants.LOGOUT_TRANSITION));
                        Log.d(TAG, "User signed out");
                    }
                });
    }
}
