package ca.event.solosphere.ui.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.event.solosphere.R;
import ca.event.solosphere.databinding.FragmentChatBinding;

public class ChatFragment extends BaseFragment {

    private FragmentChatBinding binding;
    private Context context;

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
        Toolbar mToolbar = (Toolbar) binding.getRoot().findViewById(R.id.toolbarChat);
        baseActivity.setSupportActionBar(mToolbar);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
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
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
}