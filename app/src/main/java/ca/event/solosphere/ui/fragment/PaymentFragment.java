package ca.event.solosphere.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

import ca.event.solosphere.R;
import ca.event.solosphere.core.constants.Constants;
import ca.event.solosphere.core.constants.Extras;
import ca.event.solosphere.core.model.Attendee;
import ca.event.solosphere.core.model.Event;
import ca.event.solosphere.core.model.User;
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
    private int ticketQuantity;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference mAttendeesRef;
    private DatabaseReference mEventRef;
    private Event event;
    private final String DEMO_CREDIT_CARD_NUMBER = "1234 5678 9012 3456";
    private final String DEMO_EXPIRY = "01/25";
    private final String DEMO_SECURITY_CODE = "123";


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
            event = (Event) bundle.getSerializable(Extras.EXTRA_EVENT_DETAIL);
            eventID = bundle.getString(Extras.EXTRA_EVENT_ID);
            ticketQuantity = bundle.getInt(Extras.EXTRA_TICKET_QUANTITY);
        }

        init();

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

    private void init(){
        mAuth = FirebaseAuth.getInstance();
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mAttendeesRef = mFirebaseInstance.getReference(Constants.TBL_ATTENDEES);
        mEventRef = mFirebaseInstance.getReference(Constants.TBL_EVENTS);
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == binding.btnPay.getId()){
            String securityCode = binding.etSecurityCode.getText().toString();
            if(Objects.equals(creditCardNumber, DEMO_CREDIT_CARD_NUMBER) &&
                    Objects.equals(creditCardExpiry, DEMO_EXPIRY) &&
            securityCode.equals(DEMO_SECURITY_CODE)){
                isTicketBooked();
            }
        }
    }

    private void isTicketBooked() {
        showProgress(binding.loadingView.getRoot());
        Query queries = mAttendeesRef.child(eventID).orderByChild(Constants.COLUMN_USER_ID).equalTo(mAuth.getCurrentUser().getUid());
        String key = mAttendeesRef.push().getKey();
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Iterator<DataSnapshot> iterator = snapshot.getChildren().iterator();
                    if (iterator.hasNext()) {
                        DataSnapshot firstElement = iterator.next();
                        Attendee attendee = firstElement.getValue(Attendee.class);
                        uploadUserTicketData(attendee.getTotalTickets()+ticketQuantity,firstElement.getKey());
                    }
                }else{
                    uploadUserTicketData(ticketQuantity,mAttendeesRef.push().getKey());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getMessage());
            }
        };
        queries.addListenerForSingleValueEvent(eventListener);
    }

    private void uploadUserTicketData(int ticketQuantity,String key) {
        Attendee attendee = new Attendee(mAuth.getUid(),eventID,"false",ticketQuantity);
        mAttendeesRef.child(eventID).child(key).setValue(attendee)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        hideProgress(binding.loadingView.getRoot());
                        updateEventData();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to add event data to Firebase
                        hideProgress(binding.loadingView.getRoot());
                        Snackbar.make(context, binding.getRoot(), e.getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                });
    }

    private void updateEventData() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("attendees", event.getAttendees()+ticketQuantity);
        mEventRef.child(eventID).updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        hideProgress(binding.loadingView.getRoot());
                        showPaymentSuccessDialog();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to add event data to Firebase
                        hideProgress(binding.loadingView.getRoot());
                        Snackbar.make(context, binding.getRoot(), e.getMessage(), Snackbar.LENGTH_LONG).show();
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