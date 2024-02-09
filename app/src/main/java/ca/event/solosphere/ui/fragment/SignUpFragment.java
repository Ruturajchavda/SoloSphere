package ca.event.solosphere.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.textview.MaterialTextView;

import ca.event.solosphere.R;
import ca.event.solosphere.core.constants.Extras;
import ca.event.solosphere.ui.activity.BaseFragmentActivity;

public class SignUpFragment extends BaseFragment implements View.OnClickListener {

    private MaterialTextView txtSignIn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarTitle("");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        this.txtSignIn = view.findViewById(R.id.txtSignIn);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtSignIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.txtSignIn:
                startBusinessIntent(new LoginFragment());
                break;
        }

    }

    private void startBusinessIntent(BaseFragment baseFragment) {
        Intent intent = new Intent(baseActivity, BaseFragmentActivity.class);
        intent.putExtra(Extras.EXTRA_FRAGMENT_SIGNUP, baseFragment);
        startActivity(intent);
        baseActivity.finish();
    }

}