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
import android.view.WindowManager;
import android.widget.TextView;

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
import ca.event.solosphere.core.constants.Extras;
import ca.event.solosphere.core.model.Chat;
import ca.event.solosphere.core.model.User;
import ca.event.solosphere.databinding.FragmentChatListBinding;
import ca.event.solosphere.ui.activity.BaseFragmentActivity;
import ca.event.solosphere.ui.adapter.ChatListAdapter;
import ca.event.solosphere.ui.adapter.ChatRequestAdapter;
import ca.event.solosphere.ui.interfaces.RecyclerViewItemInterface;

public class ChatListFragment extends Fragment implements View.OnClickListener, RecyclerViewItemInterface {
    private static final String TAG = "ChatListFragment";
    private FragmentChatListBinding binding;
    private Activity context;
    private ArrayList<User> userArrayList = new ArrayList<>();
    private ChatListAdapter chatListAdapter;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference mFirebaseDatabase;
    private DatabaseReference mContactsRef;
    private DatabaseReference mChatRef;
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
        binding = FragmentChatListBinding.inflate(inflater, container, false);

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
        mContactsRef = mFirebaseInstance.getReference(Constants.TBL_CONTACTS);
        mChatRef = mFirebaseInstance.getReference(Constants.TBL_CHATS);

        currentUid = mAuth.getCurrentUser().getUid();

        doChatList(true);

        binding.errorView.getRoot().setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.errorView:
                doChatList(true);
                break;
        }
    }

    private void doChatList(boolean isShowProgress) {
        if (isShowProgress) {
            showProgress(binding.loadingView.getRoot());
        }

        mContactsRef.child(currentUid).addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userArrayList.clear();
                hideProgress(binding.loadingView.getRoot());
                binding.errorView.getRoot().setVisibility(View.GONE);

                if (dataSnapshot != null && dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String userId = userSnapshot.getKey();
                        getUserData(userId);
                    }

                } else {
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
                        Snackbar.make(binding.rvChatList, databaseError.getMessage().toString(), Snackbar.LENGTH_LONG).show();
                    }

                }
            }
        });
    }

    private void getUserData(String uid) {
        final String[] currentState = {Constants.STATE_NEW};
        mFirebaseDatabase.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        lastMessage(user);
                        userArrayList.add(user);
                    }
                    chatListAdapter.notifyDataSetChanged();
                }
                updateUI();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getMessage().toString());
            }
        });

    }

    private void lastMessage(final User user) {
        final String[] theLastMessage = {getActivity().getResources().getString(R.string.start_conversation)};

        mChatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    if (mAuth.getCurrentUser() != null && chat != null) {
                        if (chat.getReceiver().equals(currentUid) && chat.getSender().equals(user.getUid()) ||
                                chat.getReceiver().equals(user.getUid()) && chat.getSender().equals(currentUid)) {
                            theLastMessage[0] = chat.getMessage();
                        }

                    }
                }
                user.setLastMessage(theLastMessage[0]);
                chatListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void populateAdapter() {

        if (getActivity() != null) {
            chatListAdapter = new ChatListAdapter(getActivity(), userArrayList);
        }
        binding.rvChatList.setAdapter(chatListAdapter);
        chatListAdapter.setItemClickListener(this);
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
            Intent intent = new Intent(getActivity(), BaseFragmentActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(Extras.EXTRA_ATTACHMENT, user1);
            bundle.putInt(Extras.EXTRA_SS_ID, position);
            intent.putExtra(Extras.EXTRA_FRAGMENT_SIGNUP, new ChatFragment());
            intent.putExtra(Extras.EXTRA_FRAGMENT_BUNDLE, bundle);
            getActivity().startActivity(intent);
        }

    }

    @Override
    public void OnItemMoved(int position, Object o) {

    }

    @Override
    public void OnItemShare(int position, Object o) {

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