package ca.event.solosphere.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

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

import ca.event.solosphere.R;
import ca.event.solosphere.core.constants.Constants;
import ca.event.solosphere.core.model.User;
import ca.event.solosphere.databinding.FragmentChatRequestBinding;
import ca.event.solosphere.ui.adapter.ChatRequestAdapter;
import ca.event.solosphere.ui.interfaces.RecyclerViewItemInterface;

public class ChatRequestFragment extends Fragment implements View.OnClickListener, RecyclerViewItemInterface {
    private static final String TAG = "ChatRequestFragment";
    private FragmentChatRequestBinding binding;
    private Activity context;

    private ArrayList<User> userArrayList = new ArrayList<>();
    private ChatRequestAdapter chatRequestAdapter;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference mFirebaseDatabase;
    private DatabaseReference mChatRequestRef;
    private DatabaseReference mContactsRef;
    private String currentUid;
    private User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChatRequestBinding.inflate(inflater, container, false);

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

        doRequestList(true);

        binding.errorView.getRoot().setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.errorView:
                doRequestList(true);
                break;

        }
    }

    private void doRequestList(boolean isShowProgress) {
        if (isShowProgress) {
            showProgress(binding.loadingView.getRoot());
        }

        mChatRequestRef.child(currentUid).addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userArrayList.clear();
                hideProgress(binding.loadingView.getRoot());
                binding.errorView.getRoot().setVisibility(View.GONE);

                if (dataSnapshot != null && dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String userId = userSnapshot.getKey();
                        String requestType = userSnapshot.child(Constants.COLUMN_REQ_TYPE).getValue().toString();
                        getUserData(userId, requestType);
                    }

                }else {
                    updateUI();
                }

                populateAdapter();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                hideProgress(binding.loadingView.getRoot());

                Activity activity = getActivity();
                if (activity != null && isAdded()) {

                    binding.errorView.getRoot().setVisibility(View.VISIBLE);
                    if (databaseError != null) {
                        Log.e(TAG, "onCancelled: " + databaseError.getMessage().toString());
                        Snackbar.make(binding.rvChatRequest, databaseError.getMessage().toString(), Snackbar.LENGTH_LONG).show();
                    }

                }
            }
        });
    }

    private void getUserData(String uid, String requestType) {
        final String[] currentState = {Constants.STATE_NEW};
        mFirebaseDatabase.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        userArrayList.add(user);

                        if (requestType.equals(Constants.STATE_REQ_SENT)) {
                            currentState[0] = Constants.STATE_REQ_SENT;
                        } else {
                            currentState[0] = Constants.STATE_REQ_RECEIVED;
                        }

                        user.setCurrentState(currentState[0]);
                    }
                    chatRequestAdapter.notifyDataSetChanged();
                }
                updateUI();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getMessage().toString());
            }
        });


    }

    private void populateAdapter() {

        if (getActivity() != null) {
            chatRequestAdapter = new ChatRequestAdapter(getActivity(), userArrayList);
        }
        binding.rvChatRequest.setAdapter(chatRequestAdapter);
        chatRequestAdapter.setItemClickListener(this);
    }

    public void updateUI() {
        if (userArrayList.size() == 0) {
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
        if (o != null && o instanceof User) {
            User user1 = (User) o;
            cancelChatRequest(user1,position);
        }

    }

    @Override
    public void OnItemMoved(int position, Object o) {
        if (o != null && o instanceof User) {
            User user1 = (User) o;
            acceptChatRequest(user1,position);
        }

    }

    @Override
    public void OnItemShare(int position, Object o) {
        if (o != null && o instanceof User) {
            User user1 = (User) o;
            cancelChatRequest(user1,position);
        }

    }

    private void cancelChatRequest(User user, int position) {
        mChatRequestRef.child(currentUid).child(user.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    mChatRequestRef.child(user.getUid()).child(currentUid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                chatRequestAdapter.notifyItemRemoved(position);
                            }
                        }
                    });
                }
            }
        });
    }

    private void acceptChatRequest(User user, int position) {
        mContactsRef.child(currentUid).child(user.getUid()).child(Constants.TBL_CONTACTS)
                .setValue(Constants.STATE_FRIENDS)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mContactsRef.child(user.getUid()).child(currentUid).child(Constants.TBL_CONTACTS)
                                    .setValue(Constants.STATE_FRIENDS)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                mChatRequestRef.child(currentUid).child(user.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            mChatRequestRef.child(user.getUid()).child(currentUid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        chatRequestAdapter.notifyItemRemoved(position);
                                                                        Snackbar.make(getActivity(), binding.rvChatRequest, "Hurrah! You're now Friends!", Snackbar.LENGTH_LONG).show();
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    });
                        }
                    }
                });

    }

    public void showProgress(View view) {
        view.setVisibility(View.VISIBLE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void hideProgress(View view) {
        view.setVisibility(View.GONE);
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

    }
}