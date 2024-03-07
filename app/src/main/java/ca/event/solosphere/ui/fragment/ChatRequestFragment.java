package ca.event.solosphere.ui.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.event.solosphere.databinding.FragmentChatRequestBinding;
import ca.event.solosphere.ui.adapter.ChatRequestAdapter;

public class ChatRequestFragment extends Fragment {

    private FragmentChatRequestBinding binding;
    private Context context;

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

        ChatRequestAdapter chatRequestAdapter = new ChatRequestAdapter(context);
        binding.rvChatRequest.setAdapter(chatRequestAdapter);

        return binding.getRoot();
    }
}