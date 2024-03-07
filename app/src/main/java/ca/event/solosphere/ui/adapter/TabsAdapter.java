package ca.event.solosphere.ui.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import ca.event.solosphere.ui.fragment.ChatListFragment;
import ca.event.solosphere.ui.fragment.ChatRequestFragment;

public class TabsAdapter extends FragmentStateAdapter {

    public TabsAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ChatListFragment();
            case 1:
                return new ChatRequestFragment();
            default:
                return null;
        }

    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
