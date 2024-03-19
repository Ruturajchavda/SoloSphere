package ca.event.solosphere.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.event.solosphere.core.constants.Extras;
import ca.event.solosphere.databinding.FragmentChatListBinding;
import ca.event.solosphere.ui.activity.BaseFragmentActivity;
import ca.event.solosphere.ui.adapter.ChatListAdapter;
import ca.event.solosphere.ui.interfaces.RecyclerViewItemInterface;

public class ChatListFragment extends Fragment implements RecyclerViewItemInterface {

    private FragmentChatListBinding binding;
    private Activity context;


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
        chatListAdapter.setItemClickListener(this);
        return binding.getRoot();
    }

    @Override
    public void OnItemClick(int position) {
        Intent intent = new Intent(getActivity(), BaseFragmentActivity.class);
        intent.putExtra(Extras.EXTRA_FRAGMENT_SIGNUP, new ChatFragment());
        getActivity().startActivity(intent);
    }

    @Override
    public void OnItemClick(int position, Object o) {

    }

    @Override
    public void OnItemMoved(int position, Object o) {

    }

    @Override
    public void OnItemShare(int position, Object o) {

    }
}