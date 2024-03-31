package ca.event.solosphere.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import ca.event.solosphere.R;
import ca.event.solosphere.core.constants.Extras;
import ca.event.solosphere.core.model.Event;
import ca.event.solosphere.databinding.FragmentTicketBinding;

public class TicketFragment extends BaseFragment {
    private static final String TAG = "TicketFragment";

    private FragmentTicketBinding binding;
    private Context mContext;
    private FirebaseAuth mAuth;
    private Bundle bundle;
    private Event event;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarTitle(baseActivity.getResources().getString(R.string.title_ticket));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTicketBinding.inflate(inflater, container, false);
        mContext = getActivity();

        bundle = getArguments();
        if (bundle != null) {
            event = (Event) bundle.getSerializable(Extras.EXTRA_ATTACHMENT);
        } else {
            event = new Event();
        }

        mAuth = FirebaseAuth.getInstance();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    @SuppressLint("SetTextI18n")
    private void init() {
        String tempUrl = event.getEventImage();

        if (tempUrl != null && !tempUrl.isEmpty()) {

            Glide.with(mContext).load(tempUrl).placeholder(R.drawable.image_placeholder).error(R.drawable.image_placeholder).into(binding.imageEvent);
        }

        binding.txtEventName.setText(event.getName());
        binding.txtLocation.setText(event.getLocation());
        binding.txtDateTime.setText(event.getStartDate() + "\n" + event.getStartTime());
        binding.txtAttendee.setText(String.valueOf(event.getAttendees()));
        generateQR();
    }

    private void generateQR() {
        String qrData = event.getEventID() + "," + mAuth.getCurrentUser().getUid() + "," + event.getStartDate() + ","
                + event.getStartTime() + "," + event.getEndDate() + "," + event.getEndTime();
        QRGEncoder qrgEncoder = new QRGEncoder(qrData, null, QRGContents.Type.TEXT, 800);
        qrgEncoder.setColorBlack(baseActivity.getResources().getColor(R.color.color_white));
        qrgEncoder.setColorWhite(baseActivity.getResources().getColor(R.color.colorPrimary));
        try {
            Bitmap bitmap = qrgEncoder.getBitmap();
            binding.imageQRCode.setImageBitmap(bitmap);
        } catch (Exception e) {
            Log.e(TAG, "onViewCreated: " + e.getMessage());
        }
    }
}