package ca.event.solosphere.ui.fragment.organizer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


import ca.event.solosphere.R;
import ca.event.solosphere.core.constants.Constants;
import ca.event.solosphere.core.constants.Extras;
import ca.event.solosphere.core.model.Attendee;
import ca.event.solosphere.core.model.Event;
import ca.event.solosphere.core.model.User;
import ca.event.solosphere.databinding.FragmentScanQRBinding;
import ca.event.solosphere.ui.fragment.BaseFragment;

public class ScanQRFragment extends BaseFragment {

    private static final String TAG = "ScanQRFragment";
    private FragmentScanQRBinding binding;
    private Context mContext;

    private Event event;
    private Bundle bundle;

    private CodeScanner mCodeScanner;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private boolean isValidTicket = false;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference mAttendeesRef;

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

        mAuth = FirebaseAuth.getInstance();
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mAttendeesRef = mFirebaseInstance.getReference(Constants.TBL_ATTENDEES);

        mCodeScanner = new CodeScanner(mContext, binding.scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                baseActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String[] QRData = result.getText().split(",");

                        String eventId = QRData[0];
                        String attendeeId = QRData[1];

                        if (event.getEventID().equals(eventId)) {
                            validateTicket(true, eventId, attendeeId);
                        } else {
                            Snackbar.make(binding.scannerView, baseActivity.getResources().getString(R.string.not_scanning_right_event), Snackbar.LENGTH_LONG).show();
                        }

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

    private void openCamera() {
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
        if (requestCode == REQUEST_CAMERA_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED)
                Snackbar.make(mContext, binding.scannerView, baseActivity.getResources().getString(R.string.err_permission_denied), Snackbar.LENGTH_LONG).show();
            else
                openCamera();
        } else
            baseActivity.finish();
    }

    private void validateTicket(boolean isShowProgress, String eventId, String attendeeId) {
        if (isShowProgress) {
            showProgress(binding.loadingView.getRoot());
        }
        Query query = mAttendeesRef.child(eventId).orderByChild(Constants.COLUMN_USER_ID).equalTo(attendeeId);
        query.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                hideProgress(binding.loadingView.getRoot());

                if (dataSnapshot != null && dataSnapshot.exists()) {
                    for (DataSnapshot attendeeSnapshot : dataSnapshot.getChildren()) {
                        Attendee attendee = attendeeSnapshot.getValue(Attendee.class);

                        if (attendee != null && attendee.getIsCheckedIn().equals("false")) {
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("isCheckedIn", "true");
                            attendeeSnapshot.getRef().updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Snackbar.make(binding.scannerView, baseActivity.getResources().getString(R.string.successfully_checked_in), Snackbar.LENGTH_LONG).show();
                                }
                            });
                        } else {
                            Snackbar.make(binding.scannerView, baseActivity.getResources().getString(R.string.err_already_checked_in), Snackbar.LENGTH_LONG).show();
                        }

                    }

                } else {
                    Snackbar.make(binding.scannerView, baseActivity.getResources().getString(R.string.err_not_valid_ticket), Snackbar.LENGTH_LONG).show();
                }

                query.removeEventListener(this);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                hideProgress(binding.loadingView.getRoot());

                Activity activity = getActivity();
                if (activity != null && isAdded()) {

                    if (databaseError != null) {
                        Snackbar.make(binding.scannerView, databaseError.getMessage().toString(), Snackbar.LENGTH_LONG).show();
                    }

                }
            }
        });
    }
}