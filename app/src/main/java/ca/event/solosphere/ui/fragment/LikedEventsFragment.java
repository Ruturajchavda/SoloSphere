package ca.event.solosphere.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import ca.event.solosphere.R;
import ca.event.solosphere.core.constants.Constants;
import ca.event.solosphere.core.constants.Extras;
import ca.event.solosphere.core.model.Event;
import ca.event.solosphere.core.model.LikedEvent;
import ca.event.solosphere.databinding.FragmentLikedEventsBinding;
import ca.event.solosphere.databinding.FragmentSearchedEventBinding;
import ca.event.solosphere.ui.adapter.EventAdapter;
import ca.event.solosphere.ui.adapter.EventAdapter1;


public class LikedEventsFragment extends Fragment {

    private FragmentLikedEventsBinding binding;
    private static final String TAG = "LikedEventFragment";
    private Activity context;
    private Bundle bundle;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseInstance;

    private DatabaseReference mLikedEventRef;
    private DatabaseReference mEventRef;
    private List<Event> likedEventList;
    private List<Event> eventList;


    public LikedEventsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentLikedEventsBinding.inflate(inflater, container, false);

        context = getActivity();
        bundle = getArguments();
        init();
        return binding.getRoot();
    }

    public void init(){
        likedEventList = new ArrayList<>();
        eventList = new ArrayList<>();
        //Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mEventRef = mFirebaseInstance.getReference(Constants.TBL_EVENTS);
        mLikedEventRef = mFirebaseInstance.getReference(Constants.TBL_LIKED_EVENTS);
        getAllEventsData();
    }

    private void getLikedEventsData() {
        mLikedEventRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot likedEventSnapshot : dataSnapshot.getChildren()) {
                        // Iterate through each event data
                        LikedEvent likedEvent = likedEventSnapshot.getValue(LikedEvent.class); // Assuming Event is your custom class representing an event
                        assert likedEvent != null;
                        if (Objects.equals(mAuth.getUid(), likedEvent.getUserID())) {
                            for(Event event:eventList){
                                if(Objects.equals(event.getEventID(), likedEvent.getEventID())){
                                    likedEventList.add(event);
                                }
                            }
                        }
                    }
                    if(likedEventList.size() > 0){
                        showEventView(true);
                        EventAdapter eventAdapter = new EventAdapter(context,likedEventList);
                        binding.rvLikedEvent.setAdapter(eventAdapter);
                    }else{
                        showEventView(false);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });
    }

    private void getAllEventsData() {
        mEventRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                        // Iterate through each event data
                        Event event = eventSnapshot.getValue(Event.class); // Assuming Event is your custom class representing an event
                        assert event != null;
                        eventList.add(event);
                    }
                    getLikedEventsData();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void showEventView(boolean flag) {
        binding.llLikedEvents.setVisibility(flag ? View.VISIBLE : View.GONE);
        binding.tvNoLikedEvents.setVisibility(!flag ? View.VISIBLE : View.GONE);
    }
}