package ca.event.solosphere.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayoutMediator;

import ca.event.solosphere.R;
import ca.event.solosphere.databinding.FragmentHomeBinding;
import ca.event.solosphere.databinding.FragmentRecentChatsBinding;
import ca.event.solosphere.ui.activity.BaseFragmentActivity;
import ca.event.solosphere.ui.activity.NavigationActivity;
import ca.event.solosphere.ui.adapter.TabsAdapter;

public class RecentChatsFragment extends Fragment {
    private FragmentRecentChatsBinding binding;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRecentChatsBinding.inflate(inflater, container, false);

        context = getActivity();

        //Set Toolbar Title
        binding.toolbar.setTitle(context.getResources().getString(R.string.title_chats));

        // SetUp Tabs
        TabsAdapter tabsAdapter = new TabsAdapter(getActivity());
        binding.viewPager.setAdapter(tabsAdapter);

        // Array of tab titles
        String[] tabTitles = {context.getResources().getString(R.string.title_recent_chats), context.getResources().getString(R.string.title_chat_request)};
        new TabLayoutMediator(binding.tabLayout, binding.viewPager,
                (tab, position) -> tab.setText(tabTitles[position])
        ).attach();

        return binding.getRoot();
    }

}