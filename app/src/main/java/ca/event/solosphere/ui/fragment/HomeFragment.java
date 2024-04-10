package ca.event.solosphere.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.BuildConfig;
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
import ca.event.solosphere.core.constants.Extras;
import ca.event.solosphere.core.model.Category;
import ca.event.solosphere.core.model.Event;
import ca.event.solosphere.core.model.EventCategory;
import ca.event.solosphere.databinding.FragmentHomeBinding;
import ca.event.solosphere.ui.activity.BaseFragmentActivity;
import ca.event.solosphere.ui.adapter.EventAdapter;
import ca.event.solosphere.ui.adapter.EventCategoryAdapter;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private FragmentHomeBinding binding;
    private Activity context;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference mEventRef;
    private DatabaseReference mCategoryRef;
    private List<Event> eventList;
    private List<Category> categoryList;

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
        configureSearchView();
        return binding.getRoot();
    }

    private void configureSearchView() {
        binding.etSearchEvents.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    binding.etSearchEvents.clearFocus();
                    // go to searched event fragment
                    if(binding.etSearchEvents.getText().toString().trim() != ""){
                        String searchedString = binding.etSearchEvents.getText().toString().trim();
                        Intent intent = new Intent(context, BaseFragmentActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString(Extras.EXTRA_SEARCHED_STRING, searchedString);
                        intent.putExtra(Extras.EXTRA_FRAGMENT_SIGNUP, new SearchedEventFragment());
                        intent.putExtra(Extras.EXTRA_FRAGMENT_BUNDLE, bundle);
                        context.startActivity(intent);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    void init(){
        eventList = new ArrayList<>();
        categoryList = new ArrayList<>();
        //Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mEventRef = mFirebaseInstance.getReference(Constants.TBL_EVENTS);
        mCategoryRef = mFirebaseInstance.getReference(Constants.TBL_CATEGORIES);
        getCategoryData();
        getEventsData();

    }

    private void getCategoryData() {
        mCategoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                        // Iterate through each event data
                        Category category = eventSnapshot.getValue(Category.class); // Assuming Event is your custom class representing an event
                        if (category != null) {
                            showCategories(true);
                            categoryList.add(category);
                        }else{
                            showCategories(false);
                        }
                    }
                    EventCategoryAdapter eventCategoryAdapter = new EventCategoryAdapter(context,categoryList);
                    binding.rvEventCategory.setAdapter(eventCategoryAdapter);
                }else{
                    showCategories(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });
    }

    private void showCategories(boolean flag) {
        binding.llEventCategory.setVisibility(flag ? View.VISIBLE : View.GONE);
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
                            showEventView(true);
                            eventList.add(event);
                        }else{
                            showEventView(false);
                        }
                    }
                    EventAdapter eventAdapter = new EventAdapter(context,eventList,false);
                    binding.rvEvent.setAdapter(eventAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });
    }

    private void showEventView(boolean flag) {
        binding.llEvents.setVisibility(flag ? View.VISIBLE : View.GONE);
        binding.tvNoEvents.setVisibility(!flag ? View.VISIBLE : View.GONE);
    }

}