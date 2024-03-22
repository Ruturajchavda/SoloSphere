package ca.event.solosphere.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.palette.graphics.Palette;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import ca.event.solosphere.R;
import ca.event.solosphere.databinding.ActivityEventDetailBinding;
import ca.event.solosphere.databinding.ActivityIntroScreenBinding;

public class EventDetailActivity extends AppCompatActivity {

    private ActivityEventDetailBinding binding;
    private int backgroundColor;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // assign view to binding
        binding = ActivityEventDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bitmap imageBitmap = ((BitmapDrawable)binding.ivEvent.getDrawable()).getBitmap();
        // generate background color from event image
        Palette.generateAsync(imageBitmap, new Palette.PaletteAsyncListener() {
            public void onGenerated(Palette palette) {
                backgroundColor = palette.getDominantColor(getColor(R.color.colorPrimary));
                binding.llMain.setBackgroundColor(backgroundColor);

                //set background color to event name
                binding.llEventHighlight.setBackgroundColor(backgroundColor);
                binding.llEventHighlight.getBackground().setAlpha(220);
            }
        });

    }

}