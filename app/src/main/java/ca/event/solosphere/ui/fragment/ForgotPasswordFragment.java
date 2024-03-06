package ca.event.solosphere.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import ca.event.solosphere.R;
import ca.event.solosphere.core.constants.Constants;
import ca.event.solosphere.core.constants.RegexTemplate;
import ca.event.solosphere.ui.utils.AppUtils;

public class ForgotPasswordFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "ForgotPasswordFragment";
    private EditText inputEmail;
    private MaterialButton btnContinue;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference mFirebaseDatabase;
    private View loadingView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarTitle("");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        this.inputEmail = view.findViewById(R.id.inputEmail);
        this.btnContinue = view.findViewById(R.id.btnContinue);
        this.loadingView = view.findViewById(R.id.loadingView);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnContinue.setOnClickListener(this);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnContinue:
                if (validateData()) {
                    sendResetPasswordLink(inputEmail.getText().toString().trim());
                }
                break;
        }

    }

    private void sendResetPasswordLink(String email) {
        showProgress(loadingView);
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference(Constants.TBL_USER);

        Query queries = mFirebaseDatabase.orderByChild(Constants.COLUMN_EMAIL).equalTo(email);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    mAuth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Snackbar.make(baseActivity,btnContinue,baseActivity.getResources().getString(R.string.password_link_sent),Snackbar.LENGTH_LONG).show();
                                    }else {
                                        Snackbar.make(baseActivity, btnContinue, "" + Objects.requireNonNull(task.getException()).getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
                                    }
                                }
                            });
                } else {
                    Snackbar.make(baseActivity, btnContinue, baseActivity.getResources().getString(R.string.err_user_not_exist), Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getMessage());
            }
        };
        queries.addListenerForSingleValueEvent(eventListener);
        hideProgress(loadingView);
    }

    //Validation for input give by user
    private boolean validateData() {
        if (!AppUtils.validateEditTextIp(inputEmail, RegexTemplate.NOT_EMPTY, getString(R.string.err_email))) {
            return false;
        } else if (!AppUtils.validateEditTextIp(inputEmail, RegexTemplate.EMAIL, getString(R.string.err_not_valid_email))) {
            return false;
        }
        return true;
    }

}