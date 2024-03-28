package ca.event.solosphere.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ca.event.solosphere.R;
import ca.event.solosphere.core.constants.Extras;
import ca.event.solosphere.databinding.FragmentBookedEventsBinding;
import ca.event.solosphere.ui.activity.BaseFragmentActivity;
import ca.event.solosphere.databinding.FragmentBookedEventsBinding;
import ca.event.solosphere.databinding.FragmentLikedEventsBinding;
import ca.event.solosphere.ui.adapter.EventAdapter;
import ca.event.solosphere.ui.adapter.EventAdapter1;

public class BookedEventsFragment extends Fragment {

    private FragmentBookedEventsBinding binding;
    private Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBookedEventsBinding.inflate(inflater, container, false);
        mContext = getActivity();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        Intent intent = new Intent(getActivity(), BaseFragmentActivity.class);
//        intent.putExtra(Extras.EXTRA_FRAGMENT_SIGNUP, new TicketFragment());
//        getActivity().startActivity(intent);

        EventAdapter1 eventAdapter = new EventAdapter1(getActivity());
        binding.rvEvent.setAdapter(eventAdapter);

    }
}