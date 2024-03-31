package ca.event.solosphere.ui.fragment.organizer;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.Result;

import java.io.IOException;


import ca.event.solosphere.R;
import ca.event.solosphere.core.constants.Extras;
import ca.event.solosphere.core.model.Event;
import ca.event.solosphere.databinding.FragmentScanQRBinding;
import ca.event.solosphere.ui.fragment.BaseFragment;

public class ScanQRFragment extends BaseFragment {

    private FragmentScanQRBinding binding;
    private Context mContext;

    private Event event;
    private Bundle bundle;

    private CodeScanner mCodeScanner;
    private static final int REQUEST_CAMERA_PERMISSION = 201;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarTitle(baseActivity.getResources().getString(R.string.title_validate_ticket));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentScanQRBinding.inflate(inflater, container, false);
        mContext = getActivity();

        bundle = getArguments();
        if (bundle != null) {
            event = (Event) bundle.getSerializable(Extras.EXTRA_ATTACHMENT);
        } else {
            event = new Event();
        }

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
    }

    private void init() {
        mCodeScanner = new CodeScanner(mContext, binding.scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                baseActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(baseActivity, result.getText(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        binding.scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCamera();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        openCamera();
    }

    @Override
    public void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    private void openCamera(){
        if (ActivityCompat.checkSelfPermission(baseActivity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            mCodeScanner.startPreview();
        } else {
            ActivityCompat.requestPermissions(baseActivity, new
                    String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CAMERA_PERMISSION && grantResults.length>0){
            if (grantResults[0] == PackageManager.PERMISSION_DENIED)
                Snackbar.make(mContext,binding.scannerView,baseActivity.getResources().getString(R.string.err_permission_denied),Snackbar.LENGTH_LONG).show();
            else
                openCamera();
        }else
            baseActivity.finish();
    }

}