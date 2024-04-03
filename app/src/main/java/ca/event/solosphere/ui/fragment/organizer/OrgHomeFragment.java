package ca.event.solosphere.ui.fragment.organizer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import ca.event.solosphere.R;
import ca.event.solosphere.core.constants.Constants;
import ca.event.solosphere.core.constants.Extras;
import ca.event.solosphere.core.model.Event;
import ca.event.solosphere.databinding.FragmentOrgHomeBinding;
import ca.event.solosphere.ui.activity.BaseFragmentActivity;
import ca.event.solosphere.ui.adapter.ListedEventsAdapter;
import ca.event.solosphere.ui.fragment.AttendeeFragment;
import ca.event.solosphere.ui.fragment.BaseFragment;
import ca.event.solosphere.ui.interfaces.RecyclerViewItemInterface;

public class OrgHomeFragment extends BaseFragment implements View.OnClickListener, RecyclerViewItemInterface {
    private static final String TAG = "OrgHomeFragment";
    private FragmentOrgHomeBinding binding;
    private Activity context;

    private ArrayList<Event> eventArrayList = new ArrayList<>();
    private ListedEventsAdapter listedEventsAdapter;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference mEventRef;

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarTitle(baseActivity.getResources().getString(R.string.title_event_list));
        hideToolbarBack();
        BaseFragmentActivity.isAddEventEnable = true;
        BaseFragmentActivity.isLogoutEnable = true;
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentOrgHomeBinding.inflate(inflater, container, false);
        context = getActivity();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
    }

    private void init() {
        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mEventRef = mFirebaseInstance.getReference(Constants.TBL_EVENTS);

        //doEventList(true);

        binding.errorView.getRoot().setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        doEventList(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.errorView:
                doEventList(true);
                break;
        }
    }

    private void doEventList(boolean isShowProgress) {
        if (isShowProgress) {
            showProgress(binding.loadingView.getRoot());
        }

        Query queries = mEventRef.orderByChild(Constants.COLUMN_ORG_ID).equalTo(mAuth.getCurrentUser().getUid());
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                eventArrayList.clear();
                hideProgress(binding.loadingView.getRoot());
                binding.errorView.getRoot().setVisibility(View.GONE);

                if (dataSnapshot != null && dataSnapshot.exists()) {
                    for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                        Event event = eventSnapshot.getValue(Event.class);
                        eventArrayList.add(event);
                    }
                    Collections.reverse(eventArrayList);
                    populateAdapter();

                }
                updateUI();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                hideProgress(binding.loadingView.getRoot());

                Activity activity = getActivity();
                if (activity != null && isAdded()) {

                    binding.errorView.getRoot().setVisibility(View.VISIBLE);
                    if (databaseError != null) {
                        Log.e(TAG, "onCancelled: " + databaseError.getMessage().toString());
                        Snackbar.make(binding.rvEventList, databaseError.getMessage().toString(), Snackbar.LENGTH_LONG).show();
                    }

                }
            }
        };
        queries.addListenerForSingleValueEvent(eventListener);
    }

    private void populateAdapter() {

        if (getActivity() != null) {
            listedEventsAdapter = new ListedEventsAdapter(baseActivity, eventArrayList);
        }
        binding.rvEventList.setAdapter(listedEventsAdapter);
        listedEventsAdapter.setItemClickListener(this);
    }

    public void updateUI() {
        if (eventArrayList.size() == 0) {
            binding.emptyView.getRoot().setVisibility(View.VISIBLE);
        } else {
            binding.emptyView.getRoot().setVisibility(View.GONE);
        }
    }

    @Override
    public void OnItemClick(int position) {

    }

    @Override
    public void OnItemClick(int position, Object o) {
        if (o != null && o instanceof Event) {
            Event event = (Event) o;
            Intent intent = new Intent(getActivity(), BaseFragmentActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(Extras.EXTRA_ATTACHMENT, event);
            intent.putExtra(Extras.EXTRA_FRAGMENT_SIGNUP, new OrgEditEventFragment());
            intent.putExtra(Extras.EXTRA_FRAGMENT_BUNDLE, bundle);
            baseActivity.startActivity(intent);
        }

    }

    @Override
    public void OnItemMoved(int position, Object o) {
        if (o != null && o instanceof Event) {
            Event event = (Event) o;
            Intent intent = new Intent(getActivity(), BaseFragmentActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(Extras.EXTRA_ATTACHMENT, event);
            intent.putExtra(Extras.EXTRA_FRAGMENT_SIGNUP, new ScanQRFragment());
            intent.putExtra(Extras.EXTRA_FRAGMENT_BUNDLE, bundle);
            startActivity(intent);
        }

    }

    @Override
    public void OnItemShare(int position, Object o) {
        if (o != null && o instanceof Event) {
            Event event = (Event) o;
            Intent intent = new Intent(getActivity(), BaseFragmentActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(Extras.EXTRA_ATTACHMENT, event);
            bundle.putBoolean(Extras.EXTRA_IS_USER,false);
            intent.putExtra(Extras.EXTRA_FRAGMENT_SIGNUP, new AttendeeFragment());
            intent.putExtra(Extras.EXTRA_FRAGMENT_BUNDLE, bundle);
            startActivity(intent);
        }

    }
}