package ca.event.solosphere.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import ca.event.solosphere.core.constants.Extras;
import ca.event.solosphere.databinding.FragmentProfileBinding;
import ca.event.solosphere.ui.activity.BaseFragmentActivity;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private Activity context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        context = getActivity();

        Intent intent = new Intent(getActivity(), BaseFragmentActivity.class);
        intent.putExtra(Extras.EXTRA_FRAGMENT_SIGNUP, new AttendeeFragment());
        getActivity().startActivity(intent);

        return binding.getRoot();
    }
}