package ca.event.solosphere.ui.fragment;

import android.app.Activity;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import ca.event.solosphere.R;
import ca.event.solosphere.core.constants.Extras;
import ca.event.solosphere.core.model.User;
import ca.event.solosphere.databinding.FragmentPrivacyTOCBinding;

public class PrivacyTOCFragment extends BaseFragment {
    private static final String TAG = "PrivacyTOCFragment";
    private FragmentPrivacyTOCBinding binding;
    private Activity context;
    private Bundle bundle;
    private String privacyUrl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPrivacyTOCBinding.inflate(inflater, container, false);

        context = getActivity();

        bundle = getArguments();
        if (bundle != null) {
            setToolbarTitle(bundle.getString(Extras.EXTRA_TITLE));
            privacyUrl = bundle.getString(Extras.EXTRA_URL);
        }
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.wvPrivacy.loadUrl(privacyUrl);
    }
}