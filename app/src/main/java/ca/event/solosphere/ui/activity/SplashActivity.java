package ca.event.solosphere.ui.activity;

import static ca.event.solosphere.core.constants.Constants.SPLASH_SCREEN_TIME;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ca.event.solosphere.R;
import ca.event.solosphere.core.constants.Extras;
import ca.event.solosphere.core.session.SessionManager;
import ca.event.solosphere.ui.fragment.BaseFragment;
import ca.event.solosphere.ui.fragment.LoginFragment;
import ca.event.solosphere.ui.fragment.organizer.OrgHomeFragment;
import ca.event.solosphere.ui.fragment.organizer.OrgLoginFragment;
import ca.event.solosphere.ui.utils.AppUtils;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (SessionManager.getInstance().isShowIntro()) {
                    startActivity(new IntroScreenActivity());
                } else {
                    if (mAuth.getCurrentUser() != null) {
                        startActivity(new NavigationActivity());
                    } else {
                        startBusinessIntent(new LoginFragment());
                    }
                }
                /*if (mAuth.getCurrentUser() != null) {
                    startBusinessIntent(new OrgHomeFragment());
                } else {
                    startBusinessIntent(new OrgLoginFragment());
                }*/
            }
        }, SPLASH_SCREEN_TIME);

    }

    private void startBusinessIntent(BaseFragment baseFragment) {
        Intent intent = new Intent(SplashActivity.this, BaseFragmentActivity.class);
        intent.putExtra(Extras.EXTRA_FRAGMENT_SIGNUP, baseFragment);
        startActivity(intent);
        finish();
    }

    private void startActivity(Activity activity) {
        Intent intent = new Intent(SplashActivity.this, activity.getClass());
        startActivity(intent);
        finish();
    }

}