package ca.event.solosphere.ui.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ca.event.solosphere.R;
import ca.event.solosphere.core.constants.Constants;
import ca.event.solosphere.core.constants.Extras;
import ca.event.solosphere.core.model.User;
import ca.event.solosphere.databinding.FragmentProfileBinding;
import ca.event.solosphere.ui.activity.BaseFragmentActivity;
import ca.event.solosphere.ui.activity.SplashActivity;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "ProfileFragment";
    private FragmentProfileBinding binding;
    private Activity context;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference mUserRef;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        context = getActivity();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
    }


    private void init() {
        //Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mUserRef = mFirebaseInstance.getReference(Constants.TBL_USER);


        binding.tvEditProfile.setOnClickListener(this);
        binding.tvPrivacy.setOnClickListener(this);
        binding.tvTOS.setOnClickListener(this);
        binding.tvSupport.setOnClickListener(this);
        binding.tvLogout.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getUserData(mAuth.getCurrentUser().getUid());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == binding.tvEditProfile.getId()) {
            Intent intent = new Intent(getActivity(), BaseFragmentActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(Extras.EXTRA_ATTACHMENT, user);
            intent.putExtra(Extras.EXTRA_FRAGMENT_SIGNUP, new EditProfileFragment());
            intent.putExtra(Extras.EXTRA_FRAGMENT_BUNDLE, bundle);
            getActivity().startActivity(intent);

        } else if (v.getId() == binding.tvPrivacy.getId()) {
            Intent intent = new Intent(getActivity(), BaseFragmentActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(Extras.EXTRA_URL, Constants.privacyUrl);
            bundle.putString(Extras.EXTRA_TITLE, getActivity().getResources().getString(R.string.privacy_policy_text));
            intent.putExtra(Extras.EXTRA_FRAGMENT_SIGNUP, new PrivacyTOCFragment());
            intent.putExtra(Extras.EXTRA_FRAGMENT_BUNDLE, bundle);
            getActivity().startActivity(intent);

        } else if (v.getId() == binding.tvTOS.getId()) {
            Intent intent = new Intent(getActivity(), BaseFragmentActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(Extras.EXTRA_URL, Constants.TOSUrl);
            bundle.putString(Extras.EXTRA_TITLE, getActivity().getResources().getString(R.string.terms_of_service));
            intent.putExtra(Extras.EXTRA_FRAGMENT_SIGNUP, new PrivacyTOCFragment());
            intent.putExtra(Extras.EXTRA_FRAGMENT_BUNDLE, bundle);
            getActivity().startActivity(intent);

        } else if (v.getId() == binding.tvSupport.getId()) {
            Snackbar.make(binding.layoutProfile, getResources().getString(R.string.support_text), Snackbar.LENGTH_SHORT).show();
        } else if (v.getId() == binding.tvLogout.getId()) {
            doLogout();
        }
    }

    private void getUserData(String uid) {
        mUserRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        binding.tvUsername.setText(user.getFullName().trim());
                        binding.tvEmail.setText(user.getEmail().trim());
                        String tempUrl = user.getProfilePicture();

                        if (tempUrl != null && !tempUrl.isEmpty()) {

                            Glide.with(context).load(tempUrl).placeholder(R.drawable.ic_user_stock).error(R.drawable.ic_user_stock).into(binding.ivProfilePicture);
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

    //Logout
    private void doLogout() {
        new MaterialAlertDialogBuilder(getActivity())
                .setTitle(getString(R.string.title_log_out))
                .setMessage(getString(R.string.log_out_question))
                .setPositiveButton("Log out", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAuth.signOut();
                        Intent intent = new Intent(getActivity(), BaseFragmentActivity.class);
                        intent.putExtra(Extras.EXTRA_FRAGMENT_SIGNUP, new LoginFragment());
                        startActivity(intent);
                        getActivity().finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).
                show();
    }
}