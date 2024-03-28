package ca.event.solosphere.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

import ca.event.solosphere.R;
import ca.event.solosphere.core.constants.Extras;
import ca.event.solosphere.ui.fragment.BaseFragment;
import ca.event.solosphere.ui.fragment.ChatFragment;
import ca.event.solosphere.ui.fragment.organizer.OrgAddEventFragment;
import ca.event.solosphere.ui.utils.AppUtils;

public class BaseFragmentActivity extends AppCompatActivity {
    private static final String TAG = "BaseFragmentActivity";
    private boolean isStateSaved = false;

    public static boolean isAddEventEnable = false;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        View decorView = this.getWindow().getDecorView();
        int systemUiVisibilityFlags = decorView.getSystemUiVisibility();
        systemUiVisibilityFlags &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        decorView.setSystemUiVisibility(systemUiVisibilityFlags);

        isAddEventEnable = false;

        setContentView(R.layout.activity_base_fragment);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if (savedInstanceState != null) {
            isStateSaved = savedInstanceState.getBoolean("isStateSaved", false);
        } else {
            Log.e(TAG, "** savedInstanceState is null **");
        }

        //  getWindow().setStatusBarColor(ContextCompat.getColor(BaseFragmentActivity.this,R.color.color_black));// set status background white

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mToolbar.setTitleTextColor(getResources().getColor(R.color.color_white, this.getTheme()));
        mToolbar.setTitleTextAppearance(BaseFragmentActivity.this, R.style.text_label_title_toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true); //To hide text from toolbar
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);

        }

        BaseFragment fragment = (BaseFragment) getIntent().getExtras().getSerializable(Extras.EXTRA_FRAGMENT_SIGNUP);

        if (fragment != null) {

            Bundle bundle = getIntent().getBundleExtra(Extras.EXTRA_FRAGMENT_BUNDLE);
            fragment.setArguments(bundle);
            //Check if instance of activity is restored or it is new instance.
            Log.i(TAG, "current fragment: " + fragment.getClass().getName());

               /* if (fragment.getClass().getName().equals(Fragment.class.getName())) {
                    isBackEnable = false;
                }*/

            if (!isStateSaved) {
                ChangeCurrentFragment(fragment);
            }

            invalidateOptionsMenu();

        } else {
            Log.e(TAG, "fragment is null");
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_base, menu);
        Log.e(TAG, "onCreateOptionsMenu()");
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        menu.findItem(R.id.menu_add).setVisible(isAddEventEnable);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                addEvent();
                return true;
        }

        return super.onOptionsItemSelected(item);

    }

    private void addEvent() {
        Intent intent = new Intent(this, BaseFragmentActivity.class);
        intent.putExtra(Extras.EXTRA_FRAGMENT_SIGNUP, new OrgAddEventFragment());
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Save the state of activity
        outState.putBoolean("isStateSaved", true);
        super.onSaveInstanceState(outState);
        Log.e(TAG, "onSaveInstanceState");
    }

    public void setToolbarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    public void setToolbarTitle(int id) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(id);
        }
    }

    public void setToolbarIcon(int icon) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setIcon(icon);
        }
    }

    public void hideToolbar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    public void hideToolbarBack() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    private void ChangeCurrentFragment(Fragment fragment) {
        Log.e(TAG, "ChangeCurrentFragment");
        FragmentManager fManager = getSupportFragmentManager();
        FragmentTransaction fTransaction = fManager.beginTransaction();
        fTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        fTransaction.replace(R.id.layoutFHostFragment, fragment, fragment.getClass().getName());
        fTransaction.commit();
    }
}