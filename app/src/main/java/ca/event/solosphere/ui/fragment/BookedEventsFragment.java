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
import ca.event.solosphere.core.model.BookedEvent;
import ca.event.solosphere.databinding.FragmentBookedEventsBinding;
import ca.event.solosphere.databinding.FragmentSearchedEventBinding;
import ca.event.solosphere.ui.adapter.EventAdapter;
import ca.event.solosphere.ui.adapter.EventAdapter1;


public class BookedEventsFragment extends Fragment {

    private FragmentBookedEventsBinding binding;
    private static final String TAG = "BookedEventFragment";
    private Activity context;
    private Bundle bundle;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseInstance;

    private DatabaseReference mBookedEventRef;
    private DatabaseReference mEventRef;
    private List<Event> bookedEventList;
    private List<Event> eventList;


    public BookedEventsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentBookedEventsBinding.inflate(inflater, container, false);

        context = getActivity();
        bundle = getArguments();
        init();
        return binding.getRoot();
    }

    public void init(){
        bookedEventList = new ArrayList<>();
        eventList = new ArrayList<>();
        //Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mEventRef = mFirebaseInstance.getReference(Constants.TBL_EVENTS);
        mBookedEventRef = mFirebaseInstance.getReference(Constants.TBL_BOOKED_EVENTS);
        getAllEventsData();
    }
    private void getBookedEventsData() {
        mBookedEventRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot bookedEventSnapshot : dataSnapshot.getChildren()) {
                        // Iterate through each event data
                        BookedEvent bookedEvent = bookedEventSnapshot.getValue(BookedEvent.class); // Assuming Event is your custom class representing an event
                        assert bookedEvent != null;
                        if (Objects.equals(mAuth.getUid(), bookedEvent.getUserID())) {
                            for(Event event:eventList){
                                if(Objects.equals(event.getEventID(), bookedEvent.getEventID())){
                                    bookedEventList.add(event);
                                }
                            }
                        }
                    }
                    if(bookedEventList.size() > 0){
                        showEventView(true);
                        EventAdapter eventAdapter = new EventAdapter(context,bookedEventList);
                        binding.rvBookedEvent.setAdapter(eventAdapter);
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
                    getBookedEventsData();
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
        binding.llBookedEvents.setVisibility(flag ? View.VISIBLE : View.GONE);
        binding.tvNoBookedEvents.setVisibility(!flag ? View.VISIBLE : View.GONE);
    }
}