package ca.event.solosphere.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ca.event.solosphere.R;
import ca.event.solosphere.core.constants.Constants;
import ca.event.solosphere.core.constants.Extras;
import ca.event.solosphere.core.model.Chat;
import ca.event.solosphere.core.model.User;
import ca.event.solosphere.databinding.FragmentChatBinding;
import ca.event.solosphere.ui.adapter.ChatAdapter;
import ca.event.solosphere.ui.adapter.ChatListAdapter;

public class ChatFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = "ChatFragment";
    private FragmentChatBinding binding;
    private Context context;

    private User user;
    private Bundle bundle;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference mUserRef;
    private DatabaseReference mChatRef;

    ChatAdapter chatAdapter;
    ArrayList<Chat> chatArrayList = new ArrayList<>();

    ValueEventListener seenListener;

    String currentUid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideToolbar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChatBinding.inflate(inflater, container, false);

        context = getActivity();
        baseActivity.setSupportActionBar(binding.viewToolbar.toolbarChat);

        binding.viewToolbar.toolbarChat.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseActivity.finish();
            }
        });

        if (baseActivity.getSupportActionBar() != null) {
            baseActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            baseActivity.getSupportActionBar().setDisplayShowTitleEnabled(true); //To hide text from toolbar
            baseActivity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);

        }

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mUserRef = mFirebaseInstance.getReference(Constants.TBL_USER);
        mChatRef = mFirebaseInstance.getReference(Constants.TBL_CHATS);

        bundle = getArguments();
        if (bundle != null) {
            user = (User) bundle.getSerializable(Extras.EXTRA_ATTACHMENT);
            binding.viewToolbar.txtName.setText(user.getFullName().toString().trim());

            String tempUrl = user.getProfilePicture();

            if (tempUrl != null && !tempUrl.isEmpty()) {

                Glide.with(context).load(tempUrl).placeholder(R.drawable.ic_user_stock).error(R.drawable.ic_user_stock).into(binding.viewToolbar.imageUser);
            }

        } else {
            user = new User();
        }

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getStatus();

        binding.emptyView.textEmpty.setText(baseActivity.getResources().getString(R.string.err_no_chat));

        currentUid = mAuth.getCurrentUser().getUid();

        binding.rvChatList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(baseActivity);
        linearLayoutManager.setStackFromEnd(true);
        binding.rvChatList.setLayoutManager(linearLayoutManager);

        readMessages(currentUid, user.getUid());
        //seenMessage(user.getUid());

        binding.btnSend.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSend:
                String message = binding.inputMessage.getText().toString().trim();
                String time = String.valueOf(System.currentTimeMillis());
                if (!message.equals("")) {
                    sendMessage(currentUid, user.getUid(), message, time);
                } else {
                    Toast.makeText(baseActivity, "You can't send empty message", Toast.LENGTH_SHORT).show();
                }
                binding.inputMessage.setText("");
                break;
        }

    }

    private void sendMessage(String currentUid, final String recipientId, String message, String time) {


        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", currentUid);
        hashMap.put("receiver", recipientId);
        hashMap.put("message", message);
        hashMap.put("messageID", "");
        hashMap.put("isSeen", "false");
        hashMap.put("time", time);

        mChatRef.push().setValue(hashMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    String messageID = databaseReference.getKey();
                    databaseReference.child("messageID").setValue(messageID);
                }
            }
        });
    }


    private void readMessages(final String currentUid, final String recipientId) {
        chatArrayList = new ArrayList<>();
        mChatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatArrayList.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot chatSnapshot : dataSnapshot.getChildren()) {
                        Chat chat = chatSnapshot.getValue(Chat.class);
                        if (chat.getReceiver().equals(currentUid) && chat.getSender().equals(recipientId) ||
                                chat.getReceiver().equals(recipientId) && chat.getSender().equals(currentUid)) {
                            chatArrayList.add(chat);
                        }
                        seenMessage(recipientId);
                        populateAdapter();
                    }
                }
                updateUI();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled: " + error.getMessage());
            }
        });
    }


    private void seenMessage(final String recipientId) {
        seenListener = mChatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(currentUid) && chat.getSender().equals(recipientId)) {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("isSeen", "true");
                        snapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });
    }

    private void populateAdapter() {

        if (getActivity() != null) {
            chatAdapter = new ChatAdapter(baseActivity, chatArrayList, currentUid);
        }
        binding.rvChatList.setAdapter(chatAdapter);
    }

    public void updateUI() {
        if (chatArrayList.size() == 0) {
            binding.emptyView.getRoot().setVisibility(View.VISIBLE);
        } else {
            binding.emptyView.getRoot().setVisibility(View.GONE);
        }
    }

    private void status(String status) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);

        mUserRef.child(currentUid).updateChildren(hashMap);
    }

    private void getStatus() {
        mUserRef.child(user.getUid()).child("status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String status = dataSnapshot.getValue(String.class);
                if (status != null && !status.isEmpty()) {
                    binding.viewToolbar.txtStatus.setText(status);
                    binding.viewToolbar.txtStatus.setVisibility(View.VISIBLE);
                    Log.e(TAG, "onDataChange: "+status );
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        status("Online");
    }

    @Override
    public void onStop() {
        status("Offline");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        status("Offline");
        super.onDestroy();
    }
}