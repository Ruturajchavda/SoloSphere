package ca.event.solosphere.ui.fragment;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.github.drjacky.imagepicker.ImagePicker;
import com.github.drjacky.imagepicker.constant.ImageProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

import ca.event.solosphere.R;
import ca.event.solosphere.core.constants.Constants;
import ca.event.solosphere.core.constants.Extras;
import ca.event.solosphere.core.constants.RegexTemplate;
import ca.event.solosphere.core.model.User;
import ca.event.solosphere.databinding.FragmentPaymentBinding;
import ca.event.solosphere.ui.activity.NavigationActivity;
import ca.event.solosphere.ui.utils.AppUtils;
import cn.pedant.SweetAlert.SweetAlertDialog;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

public class PaymentFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "PaymentFragment";
    private FragmentPaymentBinding binding;
    private Activity context;
    private Bundle bundle;
    private String creditCardNumber = "";
    private String creditCardExpiry = "";
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
            String securityCode = binding.etSecurityCode.getText().toString();
            if(Objects.equals(creditCardNumber, DEMO_CREDIT_CARD_NUMBER) &&
                    Objects.equals(creditCardExpiry, DEMO_EXPIRY) &&
            securityCode.equals(DEMO_SECURITY_CODE)){
                showPaymentSuccessDialog();
            }
        }

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
//        Button button = alert.findViewById(R.id.confirm_button);
//        button.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
    }

}