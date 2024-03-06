package ca.event.solosphere.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import ca.event.solosphere.R;
import ca.event.solosphere.databinding.ActivityMainBinding;
import ca.event.solosphere.ui.activity.ui.NavigationActivity;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        startActivity(new Intent(this, NavigationActivity.class));
    }
}   