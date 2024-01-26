package ca.event.solosphere.ui.activity;

import static ca.event.solosphere.core.constants.Constants.SPLASH_SCREEN_TIME;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import ca.event.solosphere.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void run() {
                Intent MainIntent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(MainIntent);
                finish();
            }
        }, SPLASH_SCREEN_TIME);

    }
}