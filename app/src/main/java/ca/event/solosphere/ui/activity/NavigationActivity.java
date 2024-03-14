package ca.event.solosphere.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ca.event.solosphere.R;
import ca.event.solosphere.databinding.ActivityNavigationBinding;
import ca.event.solosphere.ui.fragment.BookedEventsFragment;
import ca.event.solosphere.ui.fragment.HomeFragment;
import ca.event.solosphere.ui.fragment.LikedEventsFragment;
import ca.event.solosphere.ui.fragment.ProfileFragment;
import ca.event.solosphere.ui.fragment.RecentChatsFragment;
import eightbitlab.com.blurview.RenderScriptBlur;

public class NavigationActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private ActivityNavigationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        float radius = 3f;

        View decorView = getWindow().getDecorView();
        ViewGroup rootView = (ViewGroup) decorView.findViewById(android.R.id.content);
        Drawable windowBackground = decorView.getBackground();
        binding.blurView.setupWith(rootView, new RenderScriptBlur(this)) // or RenderEffectBlur
                .setFrameClearDrawable(windowBackground) // Optional
                .setBlurRadius(radius);

        init();

    }

    private void init()
    {
        changeCurrentFragmentTo(new HomeFragment());
        binding.navigationBar.setOnNavigationItemSelectedListener(this);
    }

    public void changeCurrentFragmentTo(Fragment fragment) {
        invalidateOptionsMenu();
        FragmentManager fManager = getSupportFragmentManager();
        FragmentTransaction fTransaction = fManager.beginTransaction();
        fTransaction.replace(R.id.fragment_container, fragment, fragment.getClass().getName());
        fTransaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        FragmentManager manager = getSupportFragmentManager();
        if(item.getItemId() == R.id.nav_home){
            HomeFragment homeFragment = (HomeFragment) manager.findFragmentByTag(HomeFragment.class.getName());
            if (homeFragment == null) {
                fragment = new HomeFragment();
            }
        } else if(item.getItemId() == R.id.nav_liked_event){
            LikedEventsFragment likedEventFragment = (LikedEventsFragment) manager.findFragmentByTag(LikedEventsFragment.class.getName());
            if (likedEventFragment == null) {
                fragment = new LikedEventsFragment();
            }
        }  else if(item.getItemId() == R.id.nav_chat){
            RecentChatsFragment recentChatsFragment = (RecentChatsFragment) manager.findFragmentByTag(RecentChatsFragment.class.getName());
            if (recentChatsFragment == null) {
                fragment = new RecentChatsFragment();
            }
        } else if(item.getItemId() == R.id.nav_booked_events){
            BookedEventsFragment bookedEventsFragment = (BookedEventsFragment) manager.findFragmentByTag(BookedEventsFragment.class.getName());
            if (bookedEventsFragment == null) {
                fragment = new BookedEventsFragment();
            }
        } else if(item.getItemId() == R.id.nav_profile){
            ProfileFragment profileFragment = (ProfileFragment) manager.findFragmentByTag(ProfileFragment.class.getName());
            if (profileFragment == null) {
                fragment = new ProfileFragment();
            }
        }
        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        invalidateOptionsMenu();
        if (fragment != null) {
            FragmentManager fManager = getSupportFragmentManager();
            FragmentTransaction fTransaction = fManager.beginTransaction();
            fTransaction.replace(R.id.fragment_container, fragment, fragment.getClass().getName());
            fTransaction.commit();
            return true;
        }
        return false;
    }
}