package com.example.powelljordan.astory.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.powelljordan.astory.R;
import com.example.powelljordan.astory.models.Story;
import com.example.powelljordan.astory.databinding.FragmentStoryBinding;
import com.example.powelljordan.astory.handlers.StoryHandler;
import com.example.powelljordan.astory.uitil.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jorda on 12/26/2017.
 */

public class StoryFragment extends Fragment {
    private Story story;
    FragmentStoryBinding binding;
    private final String TAG = "StoryFragment";
    public static StoryFragment newInstance(Story story) {
        Log.d("StoryFragment", "newInstance: " + story);
        StoryFragment fragment = new StoryFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable(Constants.STORY_FRAGMENT_ID, story);
        fragment.setArguments(arguments);
        return fragment;
    }

    @BindView(R.id.story_title)
    TextView storyTitle;

    @BindView(R.id.story_text_content)
    TextView storyTextContent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            story = arguments.getParcelable(Constants.STORY_FRAGMENT_ID);
            Log.d(TAG, "story: " + story);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_story, container, false);
        View view = binding.getRoot();
        ButterKnife.bind(this, view);
        binding.setStory(story);
        return view;
    }

    public void updateStory (Story story) {
        this.story = story;
        Log.d(TAG, "updated Story: " + story);
//        binding.notify();
    }
}
