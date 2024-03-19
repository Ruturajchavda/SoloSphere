package ca.event.solosphere.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import ca.event.solosphere.R;
import ca.event.solosphere.core.constants.Constants;
import ca.event.solosphere.core.model.Attendees;
import ca.event.solosphere.core.model.User;
import ca.event.solosphere.databinding.FragmentAttendeeBinding;
import ca.event.solosphere.ui.adapter.AttendeeAdapter;
import ca.event.solosphere.ui.interfaces.RecyclerViewItemInterface;

public class AttendeeFragment extends BaseFragment implements View.OnClickListener, RecyclerViewItemInterface {
    private static final String TAG = "AttendeeFragment";
    private FragmentAttendeeBinding binding;
    private Activity context;
    private ArrayList<Attendees> attendeesArrayList = new ArrayList<>();
    private AttendeeAdapter attendeeAdapter;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference mFirebaseDatabase;
    private DatabaseReference mChatRequestRef;
    private DatabaseReference mContactsRef;
    private String currentUid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarTitle(baseActivity.getResources().getString(R.string.title_attendee));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAttendeeBinding.inflate(inflater, container, false);

        context = getActivity();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference(Constants.TBL_USER);
        mChatRequestRef = mFirebaseInstance.getReference(Constants.TBL_CHAT_REQUEST);
        mContactsRef = mFirebaseInstance.getReference(Constants.TBL_CONTACTS);

        currentUid = mAuth.getCurrentUser().getUid();

        doAttendeesList(true);

        binding.errorView.getRoot().setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.errorView:
                doAttendeesList(true);
                break;

        }
    }

    private void doAttendeesList(boolean isShowProgress) {
        if (isShowProgress) {
            showProgress(binding.loadingView.getRoot());
        }

        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                attendeesArrayList.clear();
                hideProgress(binding.loadingView.getRoot());
                binding.errorView.getRoot().setVisibility(View.GONE);

                if (dataSnapshot != null && dataSnapshot.exists()) {
                    for (DataSnapshot attendeesSnapshot : dataSnapshot.getChildren()) {
                        Attendees attendees = attendeesSnapshot.getValue(Attendees.class);

                        if (attendees != null && !Objects.equals(attendees.getUid(), mAuth.getCurrentUser().getUid())) {
                            Log.e(TAG, "onDataChange: " + attendees);
                            manageChatRequest(attendees);
                        }
                    }


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
                        Snackbar.make(binding.rvAttendee, databaseError.getMessage().toString(), Snackbar.LENGTH_LONG).show();
                    }

                }
            }
        });


    }

    private void populateAdapter() {

        if (getActivity() != null) {
            attendeeAdapter = new AttendeeAdapter(baseActivity, attendeesArrayList);
        }
        binding.rvAttendee.setAdapter(attendeeAdapter);
        attendeeAdapter.setItemClickListener(this);
    }

    public void updateUI() {
        if (attendeesArrayList.size() == 0) {
            binding.emptyView.getRoot().setVisibility(View.VISIBLE);
        } else {
            binding.emptyView.getRoot().setVisibility(View.GONE);
        }
    }

    private void manageChatRequest(Attendees attendees) {
        attendeesArrayList.add(attendees);
        final String[] currentState = {Constants.STATE_NEW};
        mChatRequestRef.child(currentUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.hasChild(attendees.getUid())) {
                        String requestType = dataSnapshot.child(attendees.getUid()).child(Constants.COLUMN_REQ_TYPE).getValue().toString();
                        if (requestType.equals(Constants.STATE_REQ_SENT)) {
                            currentState[0] = Constants.STATE_REQ_SENT;
                        } else {
                            currentState[0] = Constants.STATE_REQ_RECEIVED;
                        }
                    } else {
                        mContactsRef.child(currentUid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChild(attendees.getUid())) {
                                    currentState[0] = Constants.STATE_FRIENDS;
                                    attendees.setCurrentState(currentState[0]);
                                    attendeeAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.e(TAG, "onCancelled: " + databaseError.getMessage());
                            }
                        });
                    }
                } else {
                    currentState[0] = Constants.STATE_NEW;
                    mContactsRef.child(currentUid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                if (dataSnapshot.hasChild(attendees.getUid())) {
                                    currentState[0] = Constants.STATE_FRIENDS;
                                    attendees.setCurrentState(currentState[0]);
                                    attendeeAdapter.notifyDataSetChanged();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e(TAG, "onCancelled: " + databaseError.getMessage());
                        }
                    });

                }
                attendees.setCurrentState(currentState[0]);
                populateAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getMessage().toString());
            }
        });

    }


    @Override
    public void OnItemClick(int position) {

    }

    @Override
    public void OnItemClick(int position, Object o) {
        if (o != null && o instanceof Attendees) {
            Attendees attendees = (Attendees) o;
            sendChatRequest(attendees);
        }

    }

    @Override
    public void OnItemMoved(int position, Object o) {
        if (o != null && o instanceof Attendees) {
            Attendees attendees = (Attendees) o;
            cancelChatRequest(attendees);
        }

    }

    @Override
    public void OnItemShare(int position, Object o) {

    }

    private void cancelChatRequest(Attendees attendees) {

        mChatRequestRef.child(currentUid).child(attendees.getUid())
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mChatRequestRef.child(attendees.getUid()).child(currentUid)
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                attendees.setCurrentState(Constants.STATE_NEW);
                                                attendeeAdapter.notifyDataSetChanged();
                                            }
                                        }
                                    });
                        }
                    }
                });


    }

    private void sendChatRequest(Attendees attendees) {

        mChatRequestRef.child(currentUid).child(attendees.getUid()).child(Constants.COLUMN_REQ_TYPE)
                .setValue(Constants.STATE_REQ_SENT)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mChatRequestRef.child(attendees.getUid()).child(currentUid)
                                    .child(Constants.COLUMN_REQ_TYPE).setValue(Constants.STATE_REQ_RECEIVED)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                attendees.setCurrentState(Constants.STATE_REQ_SENT);
                                                attendeeAdapter.notifyDataSetChanged();
                                            }
                                        }
                                    });
                        }
                    }
                });
    }
}