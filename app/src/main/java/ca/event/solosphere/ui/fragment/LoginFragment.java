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
import ca.event.solosphere.core.constants.Extras;
import ca.event.solosphere.core.constants.RegexTemplate;
import ca.event.solosphere.ui.activity.BaseFragmentActivity;
import ca.event.solosphere.ui.activity.MainActivity;
import ca.event.solosphere.ui.utils.AppUtils;

public class LoginFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "LoginFragment";
    private MaterialTextView txtSignUp;
    private MaterialTextView txtForgotPass;
    private EditText inputEmail;
    private EditText inputPassword;
    private MaterialButton btnLogin;
    private ImageView btnLoginGoogle;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference mFirebaseDatabase;
    private GoogleSignInClient mGoogleSignInClient;
    private View loadingView;
    private static ActivityResultLauncher<Intent> launcher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideToolbar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);
        this.txtSignUp = view.findViewById(R.id.txtSignUp);
        this.txtForgotPass = view.findViewById(R.id.txtForgotPass);
        this.inputEmail = view.findViewById(R.id.inputEmail);
        this.inputPassword = view.findViewById(R.id.inputPassword);
        this.btnLogin = view.findViewById(R.id.btnLogin);
        this.btnLoginGoogle = view.findViewById(R.id.btnLoginGoogle);
        this.loadingView = view.findViewById(R.id.loadingView);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtSignUp.setOnClickListener(this);
        txtForgotPass.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnLoginGoogle.setOnClickListener(this);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(Constants.DEFAULT_WEB_CLIENT).requestEmail().build();

        mGoogleSignInClient = GoogleSignIn.getClient(baseActivity, gso);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtSignUp:
                startBusinessIntent(new SignUpFragment());
                break;

            case R.id.btnLogin:
                if (validateData()) {
                    signInUser(inputEmail.getText().toString().trim(), inputPassword.getText().toString().trim());
                }
                break;

            case R.id.btnLoginGoogle:
                signInWithGoogle();
                break;

            case R.id.txtForgotPass:
                startBusinessIntent(new ForgotPasswordFragment());
                break;
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // Handle result of Google SignIn Intent
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
                            Snackbar.make(baseActivity, btnLogin, "Google Sign in failed", Snackbar.LENGTH_LONG).show();
                        }
                    } else {
                        Log.e(TAG, "onAttach: Activity Failed " + result.getResultCode());
                    }
                }
        );
    }

    //Launch google singIn Intent
    private void signInWithGoogle() {
        mGoogleSignInClient.signOut().addOnCompleteListener(baseActivity,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                        launcher.launch(signInIntent);
                    }
                });
    }

    //Authenticate google signIn using firebase
    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        showProgress(loadingView);
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference(Constants.TBL_USER);

        Query queries = mFirebaseDatabase.orderByChild(Constants.COLUMN_EMAIL).equalTo(account.getEmail());
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                    mAuth.signInWithCredential(credential).addOnCompleteListener(baseActivity, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                startMainIntent();
                            } else {
                                Snackbar.make(baseActivity, btnLogin, "Authentication Failed. " + Objects.requireNonNull(task.getException()).getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    Snackbar.make(baseActivity, btnLoginGoogle, baseActivity.getResources().getString(R.string.err_user_not_exist), Snackbar.LENGTH_LONG).show();
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

    //Login user with email and password
    private void signInUser(String email, String password) {
        showProgress(loadingView);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(baseActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startMainIntent();
                        } else {
                            hideProgress(loadingView);
                            Snackbar.make(baseActivity, btnLogin, "Authentication Failed. " +
                                            Objects.requireNonNull(task.getException()).getLocalizedMessage(),
                                    Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
    }

    //Validation for input give by user
    private boolean validateData() {
        if (!AppUtils.validateEditTextIp(inputEmail, RegexTemplate.NOT_EMPTY, getString(R.string.err_email))) {
            return false;
        } else if (!AppUtils.validateEditTextIp(inputEmail, RegexTemplate.EMAIL, getString(R.string.err_not_valid_email))) {
            return false;
        } else if (!AppUtils.validateEditTextIp(inputPassword, RegexTemplate.PASSWORD_PATTERN_SPECIAL, getString(R.string.err_valid_password))) {
            return false;
        }
        return true;
    }

    private void startBusinessIntent(BaseFragment baseFragment) {
        Intent intent = new Intent(baseActivity, BaseFragmentActivity.class);
        intent.putExtra(Extras.EXTRA_FRAGMENT_SIGNUP, baseFragment);
        baseActivity.startActivity(intent);
    }

    private void startMainIntent() {
        Intent intent = new Intent(baseActivity, MainActivity.class);
        baseActivity.startActivity(intent);
        baseActivity.finish();
    }

}