package ca.event.solosphere.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import ca.event.solosphere.R;
import ca.event.solosphere.core.constants.Constants;
import ca.event.solosphere.core.constants.Extras;
import ca.event.solosphere.core.model.Attendee;
import ca.event.solosphere.core.model.Event;
import ca.event.solosphere.databinding.FragmentPaymentBinding;
import ca.event.solosphere.ui.activity.NavigationActivity;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class PaymentFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "PaymentFragment";
    private FragmentPaymentBinding binding;
    private Activity context;
    private Bundle bundle;
    private String creditCardNumber = "";
    private String creditCardExpiry = "";
    private String eventID;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference mAttendeesRef;
    private final String DEMO_CREDIT_CARD_NUMBER = "1234 5678 9012 3456";
    private final String DEMO_EXPIRY = "01/25";
    private final String DEMO_SECURITY_CODE = "123";

    private static ActivityResultLauncher<Intent> launcher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarTitle(baseActivity.getResources().getString(R.string.title_payment));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPaymentBinding.inflate(inflater, container, false);
        context = getActivity();
        bundle = getArguments();
        if(bundle != null){
            eventID = bundle.getString(Extras.EXTRA_EVENT_ID);
        }

        binding.btnPay.setOnClickListener(this);

        binding.etCardNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                creditCardNumber = charSequence.toString();
                if(charSequence.length() == 4 || charSequence.length() == 9 || charSequence.length() == 14){
                    creditCardNumber = creditCardNumber + " ";
                    binding.etCardNumber.setText(creditCardNumber);
                    binding.etCardNumber.setSelection(creditCardNumber.length());
                }
                binding.tvCardNumber.setText(creditCardNumber);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
        binding.etCardHolderName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.tvMemberName.setText(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
        binding.etExpiryDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                creditCardExpiry = charSequence.toString();
                if(charSequence.length() == 2){
                    creditCardExpiry = creditCardExpiry + "/";
                    binding.etExpiryDate.setText(creditCardExpiry);
                    binding.etExpiryDate.setSelection(creditCardExpiry.length());
                }
                binding.tvValidity.setText(creditCardExpiry);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        return binding.getRoot();
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == binding.btnPay.getId()){
//            showPaymentSuccessDialog();
            saveUserTicketData();
//            String securityCode = binding.etSecurityCode.getText().toString();
//            if(Objects.equals(creditCardNumber, DEMO_CREDIT_CARD_NUMBER) &&
//                    Objects.equals(creditCardExpiry, DEMO_EXPIRY) &&
//            securityCode.equals(DEMO_SECURITY_CODE)){
//                saveUserTicketData();
//            }
        }

    }

    private void saveUserTicketData() {
        mAuth = FirebaseAuth.getInstance();
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mAttendeesRef = mFirebaseInstance.getReference(Constants.TBL_ATTENDEES);
        uploadUserTicketData();
    }

    private void uploadUserTicketData() {
        Attendee attendee = new Attendee(mAuth.getUid(),eventID,false,1);
        String key = mAttendeesRef.push().getKey();
        mAttendeesRef.child(eventID).child(key).setValue(attendee)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showPaymentSuccessDialog();
//                        hideProgress(binding.loadingView.getRoot());
//                        Snackbar.make(context, binding.btnSelectEventImage, baseActivity.getResources().getString(R.string.event_added), Snackbar.LENGTH_LONG).show();
//                        baseActivity.finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to add event data to Firebase
//                        hideProgress(binding.loadingView.getRoot());
//                        Snackbar.make(context, binding.btnSelectEventImage, baseActivity.getResources().getString(R.string.err_event_not_added) + e.getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                });
    }

    private void showPaymentSuccessDialog() {
        String title = "Success !!!";
        String contentText = "Your payment received successfully !";
        SweetAlertDialog alert = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(title)
                .setContentText(contentText)
                .setConfirmText("Ok")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        Intent intent = new Intent(baseActivity, NavigationActivity.class);
                        baseActivity.startActivity(intent);
                        baseActivity.finish();
                    }
                });
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }

}