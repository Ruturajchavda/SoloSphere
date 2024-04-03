package ca.event.solosphere.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ca.event.solosphere.R;
import ca.event.solosphere.core.constants.Constants;
import ca.event.solosphere.core.constants.Extras;
import ca.event.solosphere.core.model.Attendee;
import ca.event.solosphere.core.model.Event;
import ca.event.solosphere.databinding.FragmentCheckoutBinding;
import ca.event.solosphere.databinding.FragmentPaymentBinding;
import ca.event.solosphere.ui.activity.NavigationActivity;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class CheckoutFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "CheckoutFragment";
    private FragmentCheckoutBinding binding;
    private Activity context;
    private Bundle bundle;
    private Event event;
    private int ticketQuantity = 1;
    private double totalTicketPrice;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarTitle(baseActivity.getResources().getString(R.string.title_checkout));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCheckoutBinding.inflate(inflater, container, false);
        context = getActivity();
        bundle = getArguments();
        init();
        if(bundle != null){
            event = (Event) bundle.getSerializable(Extras.EXTRA_EVENT_DETAIL);
            fillEventData();
        }
        return binding.getRoot();
    }

    private void init(){
        binding.btnCheckout.setOnClickListener(this);
        binding.plusMinusLayout.setOnClickListener(this);
        binding.btnMinus.setOnClickListener(this);
    }

    private void fillEventData() {
        totalTicketPrice = event.getPrice();
        binding.tvEventName.setText(event.getName());
        binding.tvEventCategory.setText(event.getCategory());
        binding.tvEventPrice.setText("$"+event.getPrice());
        binding.tvTicketQuantity.setText(String.valueOf(ticketQuantity));
        binding.tvTotalTicket.setText(ticketQuantity+" Tickets");
        binding.tvTotalPrice.setText("$"+event.getPrice());
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View view) {
        if(view.getId() == binding.btnCheckout.getId()){

        }else if(view.getId() == binding.btnPlus.getId()){
            ticketQuantity++;
            binding.tvTicketQuantity.setText(String.valueOf(ticketQuantity));
            binding.tvTotalTicket.setText(ticketQuantity+" Tickets");
            totalTicketPrice = event.getPrice() * ticketQuantity;
            binding.tvTotalPrice.setText("$"+totalTicketPrice);
        }else if(view.getId() == binding.btnMinus.getId()){
            if(ticketQuantity >= 1){
                ticketQuantity--;
                binding.tvTicketQuantity.setText(String.valueOf(ticketQuantity));
                binding.tvTotalTicket.setText(ticketQuantity+" Tickets");
                totalTicketPrice = event.getPrice() * ticketQuantity;
                binding.tvTotalPrice.setText("$"+totalTicketPrice);
            }
        }
    }
}