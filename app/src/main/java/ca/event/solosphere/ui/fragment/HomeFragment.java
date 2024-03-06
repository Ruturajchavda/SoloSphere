package ca.event.solosphere.ui.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.event.solosphere.R;
import ca.event.solosphere.databinding.FragmentHomeBinding;
import ca.event.solosphere.ui.adapter.EventAdapter;
import ca.event.solosphere.ui.adapter.EventCategoryAdapter;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private Context context;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        context = getActivity();

        EventCategoryAdapter eventCategoryAdapter = new EventCategoryAdapter(context);
        binding.rvEventCategory.setAdapter(eventCategoryAdapter);

        EventAdapter eventAdapter = new EventAdapter(context);
        binding.rvEvent.setAdapter(eventAdapter);


        return binding.getRoot();
    }
}