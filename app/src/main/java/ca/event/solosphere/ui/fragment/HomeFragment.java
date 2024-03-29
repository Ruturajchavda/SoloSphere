package ca.event.solosphere.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import ca.event.solosphere.R;
import ca.event.solosphere.core.constants.Constants;
import ca.event.solosphere.core.model.Event;
import ca.event.solosphere.core.model.EventCategory;
import ca.event.solosphere.databinding.FragmentHomeBinding;
import ca.event.solosphere.ui.adapter.EventAdapter;
import ca.event.solosphere.ui.adapter.EventCategoryAdapter;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private FragmentHomeBinding binding;
    private Activity context;
    private ArrayList<EventCategory> eventCategories;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference mEventRef;
    private List<Event> eventList;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        context = getActivity();

        init();
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

    void init(){
        eventList = new ArrayList<>();
        //Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mEventRef = mFirebaseInstance.getReference(Constants.TBL_EVENTS);
        getEventsData();
    }

    private void getEventsData() {
        mEventRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                        // Iterate through each event data
                        Event event = eventSnapshot.getValue(Event.class); // Assuming Event is your custom class representing an event
                        if (event != null) {
                            eventList.add(event);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getMessage().toString());
            }
        });
    }
}