package ca.event.solosphere.ui.activity;

import static ca.event.solosphere.core.constants.Constants.SPLASH_SCREEN_TIME;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import ca.event.solosphere.R;
import ca.event.solosphere.core.constants.Extras;
import ca.event.solosphere.ui.fragment.BaseFragment;
import ca.event.solosphere.ui.fragment.LoginFragment;
import ca.event.solosphere.ui.utils.AppUtils;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void run() {
                startBusinessIntent(new LoginFragment());
            }
        }, SPLASH_SCREEN_TIME);

    }

    private void startBusinessIntent(BaseFragment baseFragment) {
        Intent intent = new Intent(SplashActivity.this, BaseFragmentActivity.class);
        intent.putExtra(Extras.EXTRA_FRAGMENT_SIGNUP, baseFragment);
        startActivity(intent);
        finish();
    }

    private void startIntent() {
        Intent MainIntent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(MainIntent);
        finish();
    }

}