package ca.event.solosphere.ui.fragment.organizer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Objects;

import ca.event.solosphere.R;
import ca.event.solosphere.core.constants.Constants;
import ca.event.solosphere.core.constants.Extras;
import ca.event.solosphere.core.constants.RegexTemplate;
import ca.event.solosphere.core.model.User;
import ca.event.solosphere.databinding.FragmentOrgLoginBinding;
import ca.event.solosphere.ui.activity.BaseFragmentActivity;
import ca.event.solosphere.ui.fragment.BaseFragment;
import ca.event.solosphere.ui.fragment.ForgotPasswordFragment;
import ca.event.solosphere.ui.fragment.SignUpFragment;
import ca.event.solosphere.ui.utils.AppUtils;

public class OrgLoginFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = "OrgLoginFragment";

    private FragmentOrgLoginBinding binding;
    private Context context;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference mFirebaseDatabase;
    private String deviceToken = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideToolbar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentOrgLoginBinding.inflate(inflater, container, false);

        context = getActivity();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference(Constants.TBL_USER);

        binding.btnLogin.setOnClickListener(this);
        binding.txtForgotPass.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                if (validateData()) {
                    signInUser(binding.inputEmail.getText().toString().trim(), binding.inputPassword.getText().toString().trim());
                }
                break;

            case R.id.txtForgotPass:
                startBusinessIntent(new ForgotPasswordFragment());
                break;
        }

    }

    private void signInUser(String email, String password) {
        showProgress(binding.loadingView.getRoot());
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(baseActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            getUserData(mAuth.getCurrentUser().getUid());
                        } else {
                            hideProgress(binding.loadingView.getRoot());
                            Snackbar.make(baseActivity, binding.btnLogin, "Authentication Failed. " +
                                            Objects.requireNonNull(task.getException()).getLocalizedMessage(),
                                    Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void getUserData(String uid) {
        mFirebaseDatabase.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        if (user.getUserType().equals(Constants.TYPE_ORG)) {
                            updateToken();
                        } else {
                            mAuth.signOut();
                            hideProgress(binding.loadingView.getRoot());
                            Snackbar.make(context, binding.btnLogin, baseActivity.getResources().getString(R.string.err_not_org), Snackbar.LENGTH_LONG).show();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getMessage().toString());
            }
        });

    }


    private void updateToken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    return;
                }
                // Get new FCM registration token
                deviceToken = task.getResult();
                mFirebaseDatabase.child(mAuth.getCurrentUser().getUid()).child("deviceToken").setValue(deviceToken)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    startBusinessIntent(new OrgHomeFragment());
                                }

                            }
                        });
            }
        });

    }


    //Validation for input give by user
    private boolean validateData() {
        if (!AppUtils.validateEditTextIp(binding.inputEmail, RegexTemplate.NOT_EMPTY, getString(R.string.err_email))) {
            return false;
        } else if (!AppUtils.validateEditTextIp(binding.inputEmail, RegexTemplate.EMAIL, getString(R.string.err_not_valid_email))) {
            return false;
        } else if (!AppUtils.validateEditTextIp(binding.inputPassword, RegexTemplate.PASSWORD_PATTERN_SPECIAL, getString(R.string.err_valid_password))) {
            return false;
        }
        return true;
    }

    private void startBusinessIntent(BaseFragment baseFragment) {
        Intent intent = new Intent(baseActivity, BaseFragmentActivity.class);
        intent.putExtra(Extras.EXTRA_FRAGMENT_SIGNUP, baseFragment);
        baseActivity.startActivity(intent);
        baseActivity.finish();
    }


}