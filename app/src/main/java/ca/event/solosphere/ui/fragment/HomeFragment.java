package ca.event.solosphere.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.sql.Array;
import java.util.ArrayList;

import ca.event.solosphere.R;
import ca.event.solosphere.core.model.EventCategory;
import ca.event.solosphere.databinding.FragmentHomeBinding;
import ca.event.solosphere.ui.adapter.EventAdapter;
import ca.event.solosphere.ui.adapter.EventCategoryAdapter;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private Activity context;
    private ArrayList<EventCategory> eventCategories;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        context = getActivity();
        eventCategories = new ArrayList<>();
        EventCategory eventCategory1 = new EventCategory(R.drawable.category_feed,"My feed");
        EventCategory eventCategory2 = new EventCategory(R.drawable.category_food,"Food");
        EventCategory eventCategory3 = new EventCategory(R.drawable.category_music,"Concerts");
        eventCategories.add(eventCategory1);
        eventCategories.add(eventCategory2);
        eventCategories.add(eventCategory3);
        EventCategoryAdapter eventCategoryAdapter = new EventCategoryAdapter(context,eventCategories);
        binding.rvEventCategory.setAdapter(eventCategoryAdapter);

        EventAdapter eventAdapter = new EventAdapter(context);
        binding.rvEvent.setAdapter(eventAdapter);


        return binding.getRoot();
    }
}