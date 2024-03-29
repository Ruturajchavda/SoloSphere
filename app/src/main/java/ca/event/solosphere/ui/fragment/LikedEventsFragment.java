package ca.event.solosphere.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.event.solosphere.R;
import ca.event.solosphere.databinding.FragmentHomeBinding;
import ca.event.solosphere.databinding.FragmentLikedEventsBinding;
import ca.event.solosphere.ui.adapter.EventAdapter;
import ca.event.solosphere.ui.adapter.EventCategoryAdapter;


public class LikedEventsFragment extends Fragment {

    private FragmentLikedEventsBinding binding;

    public LikedEventsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentLikedEventsBinding.inflate(inflater, container, false);

        EventAdapter eventAdapter = new EventAdapter(getActivity());
        binding.rvEvent.setAdapter(eventAdapter);

        return binding.getRoot();
    }
}