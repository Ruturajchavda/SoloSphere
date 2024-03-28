package ca.event.solosphere.ui.fragment;

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

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import ca.event.solosphere.R;
import ca.event.solosphere.databinding.FragmentTicketBinding;

public class TicketFragment extends BaseFragment {
    private static final String TAG = "TicketFragment";

    private FragmentTicketBinding binding;
    private Context mContext;

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
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        generateQR();
    }

    private void generateQR() {
        QRGEncoder qrgEncoder = new QRGEncoder("This is booked Event", null, QRGContents.Type.TEXT, 800);
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