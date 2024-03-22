package ca.event.solosphere.ui.fragment;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

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
import ca.event.solosphere.databinding.FragmentEditProfileBinding;
import ca.event.solosphere.ui.utils.AppUtils;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

public class EditProfileFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "EditProfileFragment";
    private FragmentEditProfileBinding binding;
    private Activity context;
    private User user;
    private Bundle bundle;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference mUserRef;
    private StorageReference storageReference;

    String currentUid;
    String currentUserImage = "";
    private Uri filePath;

    private static ActivityResultLauncher<Intent> launcher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarTitle(baseActivity.getResources().getString(R.string.title_edit_profile));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = FragmentEditProfileBinding.inflate(inflater, container, false);

        context = getActivity();

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mUserRef = mFirebaseInstance.getReference(Constants.TBL_USER);
        storageReference = FirebaseStorage.getInstance().getReference();

        bundle = getArguments();
        if (bundle != null) {
            user = (User) bundle.getSerializable(Extras.EXTRA_ATTACHMENT);
            currentUid = user.getUid();
            binding.etName.setText(user.getFullName().trim());
            binding.etPhone.setText(user.getPhone().trim());

            String tempUrl = user.getProfilePicture();

            if (tempUrl != null && !tempUrl.isEmpty()) {
                currentUserImage = tempUrl;
                Glide.with(context).load(tempUrl).placeholder(R.drawable.ic_user_stock).error(R.drawable.ic_user_stock).into(binding.ivProfilePicture);
            }

        }

        binding.btnEditProfile.setOnClickListener(this);
        binding.btnUpdate.setOnClickListener(this);
        return binding.getRoot();
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        launcher =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), (ActivityResult result) -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Uri uri = result.getData().getData();
                        binding.ivProfilePicture.setImageURI(uri);
                        filePath = uri;
                    } else if (result.getResultCode() == ImagePicker.RESULT_ERROR) {
                        Log.e(TAG, "onAttach: " + ImagePicker.Companion.getError(result.getData()));
                    }
                });
    }

    private void openImagePicker() {
        ImagePicker.Companion.with(baseActivity)
                .crop()
                .cropOval()
                .maxResultSize(512, 512, true)
                .provider(ImageProvider.BOTH)
                .createIntentFromDialog(new Function1() {
                    public Object invoke(Object var1) {
                        this.invoke((Intent) var1);
                        return Unit.INSTANCE;
                    }

                    public void invoke(@NotNull Intent it) {
                        Intrinsics.checkNotNullParameter(it, "it");
                        launcher.launch(it);
                    }
                });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnEditProfile:
                openImagePicker();
                break;

            case R.id.btnUpdate:
                if (validateData()) {
                    String name = binding.etName.getText().toString();
                    String phone = "";
                    if (!Objects.requireNonNull(binding.etPhone.getText()).toString().trim().isEmpty()) {
                        phone = binding.etPhone.getText().toString().trim();
                    }
                    uploadImage(name, phone);
                }
                break;

        }

    }

    private void uploadImage(String fullName, String phone) {
        showProgress(binding.loadingView.getRoot());
        if (filePath != null) {
            String filename = UUID.randomUUID().toString();
            StorageReference ref = storageReference.child("images/" + filename);
            ref.putFile(filePath)
                    .addOnSuccessListener(taskSnapshot -> {
                        ref.getDownloadUrl().addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString();
                            updateProfile(fullName, phone, imageUrl);
                        });
                    })
                    .addOnFailureListener(e -> {
                        hideProgress(binding.loadingView.getRoot());
                        Snackbar.make(binding.btnUpdate, baseActivity.getResources().getString(R.string.profile_not_updated), Snackbar.LENGTH_LONG).show();
                    });
        } else {
            updateProfile(fullName, phone, currentUserImage);
        }
    }

    private void updateProfile(String fullName, String phone, String profilePicture) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("fullName", fullName);
        hashMap.put("phone", phone);
        hashMap.put("profilePicture", profilePicture);

        mUserRef.child(currentUid).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });

        mUserRef.child(currentUid).updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        hideProgress(binding.loadingView.getRoot());
                        Snackbar.make(binding.btnUpdate, baseActivity.getResources().getString(R.string.profile_updated), Snackbar.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        hideProgress(binding.loadingView.getRoot());
                        Snackbar.make(binding.btnUpdate, baseActivity.getResources().getString(R.string.profile_not_updated), Snackbar.LENGTH_LONG).show();
                    }
                });
    }


    //Validation for input give by user
    private boolean validateData() {
        if (!AppUtils.validateEditTextIp(binding.etName, RegexTemplate.NOT_EMPTY, getString(R.string.err_name))) {
            return false;
        } else
            return AppUtils.validateEditTextIp(binding.etName, RegexTemplate.NAME_PATTERN, getString(R.string.err_name_length));
    }
}