package ca.event.solosphere.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
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
import java.util.List;

import ca.event.solosphere.R;
import ca.event.solosphere.core.constants.Constants;
import ca.event.solosphere.core.constants.Extras;
import ca.event.solosphere.core.model.Event;
import ca.event.solosphere.databinding.FragmentEventDetailBinding;
import ca.event.solosphere.databinding.FragmentSearchedEventBinding;
import ca.event.solosphere.ui.adapter.EventAdapter;

public class SearchedEventFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "SearchedEventFragment";
    private FragmentSearchedEventBinding binding;
    private Activity context;
    private Bundle bundle;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseInstance;

    private DatabaseReference mEventRef;
    private List<Event> eventList;
    private String searchedString;

    public SearchedEventFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarTitle(baseActivity.getResources().getString(R.string.title_searched_events));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSearchedEventBinding.inflate(inflater, container, false);
        context = getActivity();
        bundle = getArguments();
        if(bundle != null){
            searchedString = bundle.getString(Extras.EXTRA_SEARCHED_STRING);
            binding.tvSearchResult.setText(context.getString(R.string.search_results)+ " '"+ searchedString + "'");
        }
        init();
        return binding.getRoot();
    }

    public void init(){
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
                        assert event != null;
                        if (event.getName().trim().contains(searchedString)) {
                            eventList.add(event);
                        }
                    }
                    if(eventList.size() > 0){
                        showEventView(true);
                        EventAdapter eventAdapter = new EventAdapter(context,eventList);
                        binding.rvEvent.setAdapter(eventAdapter);
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

    @SuppressLint("SetTextI18n")
    private void showEventView(boolean flag) {
        binding.llEvents.setVisibility(flag ? View.VISIBLE : View.GONE);
        binding.tvNoEvents.setVisibility(!flag ? View.VISIBLE : View.GONE);
        if(!flag)
            binding.tvNoEvents.setText("No result found for '"+searchedString+"'");
    }

    @Override
    public void onClick(View view) {

    }
}