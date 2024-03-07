package ca.event.solosphere.ui.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ca.event.solosphere.databinding.FragmentChatListBinding;
import ca.event.solosphere.ui.adapter.ChatListAdapter;

public class ChatListFragment extends Fragment {

    private FragmentChatListBinding binding;
    private Context context;


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

        ChatListAdapter chatListAdapter = new ChatListAdapter(context);
        binding.rvChatList.setAdapter(chatListAdapter);

        return binding.getRoot();
    }
}