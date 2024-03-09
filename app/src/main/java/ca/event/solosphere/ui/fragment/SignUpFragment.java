package ca.event.solosphere.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
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
import ca.event.solosphere.core.model.User;
import ca.event.solosphere.ui.activity.MainActivity;
import ca.event.solosphere.ui.utils.AppUtils;

public class SignUpFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "SignUpFragment";
    private MaterialTextView txtSignIn;
    private EditText inputName;
    private EditText inputPhone;
    private EditText inputEmail;
    private EditText inputPassword;
    private EditText inputConPassword;
    private MaterialButton btnSignUp;
    private ImageView btnSignUpGoogle;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference mFirebaseDatabase;
    private GoogleSignInClient mGoogleSignInClient;
    private String userKey;
    private View loadingView;
    private static ActivityResultLauncher<Intent> launcher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarTitle("");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        this.txtSignIn = view.findViewById(R.id.txtSignIn);
        this.inputName = view.findViewById(R.id.inputName);
        this.inputPhone = view.findViewById(R.id.inputPhone);
        this.inputEmail = view.findViewById(R.id.inputEmail);
        this.inputPassword = view.findViewById(R.id.inputPassword);
        this.inputConPassword = view.findViewById(R.id.inputConPassword);
        this.btnSignUp = view.findViewById(R.id.btnSignUp);
        this.btnSignUpGoogle = view.findViewById(R.id.btnSignUpGoogle);
        this.loadingView = view.findViewById(R.id.loadingView);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtSignIn.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
        btnSignUpGoogle.setOnClickListener(this);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(Constants.DEFAULT_WEB_CLIENT).requestEmail().build();

        mGoogleSignInClient = GoogleSignIn.getClient(baseActivity, gso);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtSignIn:
                baseActivity.finish();
                break;

            case R.id.btnSignUp:
                if (validateData()) {
                    createAccount(inputEmail.getText().toString().trim(), inputPassword.getText().toString().trim());
                }
                break;

            case R.id.btnSignUpGoogle:
                signUpWithGoogle();
                break;
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // Handle result of Google SignUp Intent
        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();

                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        try {
                            GoogleSignInAccount account = task.getResult(ApiException.class);
                            firebaseAuthWithGoogle(account);
                        } catch (ApiException e) {
                            Snackbar.make(baseActivity, btnSignUp, "Google Sign in failed", Snackbar.LENGTH_LONG).show();
                        }
                    } else {
                        Log.e(TAG, "onAttach: Activity Failed " + result.getResultCode());
                    }
                }
        );
    }

    //Launch google singUp Intent
    private void signUpWithGoogle() {
        Intent signUpIntent = mGoogleSignInClient.getSignInIntent();
        launcher.launch(signUpIntent);
    }

    //Authenticate google signup using firebase
    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        showProgress(loadingView);
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(baseActivity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    uploadUserData(account.getDisplayName(), "", account.getEmail(),account.getPhotoUrl().toString());
                } else {
                    hideProgress(loadingView);
                    Snackbar.make(baseActivity, btnSignUp, "Authentication Failed. " + Objects.requireNonNull(task.getException()).getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    //Register user with email and password
    private void createAccount(String email, String password) {
        showProgress(loadingView);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(baseActivity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    uploadUserData(inputName.getText().toString().trim(), inputPhone.getText().toString().trim(), email, "");
                } else {
                    hideProgress(loadingView);
                    Snackbar.make(baseActivity, btnSignUp, "Authentication Failed. " +
                                    Objects.requireNonNull(task.getException()).getLocalizedMessage(),
                            Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    //Send user data to Database
    private void uploadUserData(String name, String phone, String email, String profilePicture) {
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference(Constants.TBL_USER);

        Query queries = mFirebaseDatabase.orderByChild(Constants.COLUMN_EMAIL).equalTo(email);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    userKey = mFirebaseDatabase.push().getKey();
                    User user = new User(name, phone, email, profilePicture, userKey);
                    mFirebaseDatabase.child(userKey).setValue(user);
                    startMainIntent();
                } else {
                    Snackbar.make(baseActivity, btnSignUpGoogle, baseActivity.getResources().getString(R.string.err_user_exist), Snackbar.LENGTH_LONG).show();
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
        if (!AppUtils.validateEditTextIp(inputName, RegexTemplate.NOT_EMPTY, getString(R.string.err_name))) {
            return false;
        } else if (!AppUtils.validateEditTextIp(inputName, RegexTemplate.NAME_PATTERN, getString(R.string.err_name_length))) {
            return false;
        } else if (!AppUtils.validateEditTextIp(inputPhone, RegexTemplate.MOBILE_NO, getString(R.string.err_not_valid_phone))) {
            return false;
        } else if (!AppUtils.validateEditTextIp(inputEmail, RegexTemplate.NOT_EMPTY, getString(R.string.err_email))) {
            return false;
        } else if (!AppUtils.validateEditTextIp(inputEmail, RegexTemplate.EMAIL, getString(R.string.err_not_valid_email))) {
            return false;
        } else if (!AppUtils.validateEditTextIp(inputPassword, RegexTemplate.PASSWORD_PATTERN_SPECIAL, getString(R.string.err_valid_password))) {
            return false;
        } else if (!AppUtils.validateEditTextIp(inputConPassword, RegexTemplate.PASSWORD_PATTERN_SPECIAL, getString(R.string.err_valid_password))) {
            return false;
        } else if (!inputPassword.getText().toString().equals(inputConPassword.getText().toString())) {
            Snackbar.make(baseActivity, inputConPassword, getString(R.string.err_passwords_do_not_match), Snackbar.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void startMainIntent() {
        Intent intent = new Intent(baseActivity, MainActivity.class);
        baseActivity.startActivity(intent);
        baseActivity.finish();
    }
}