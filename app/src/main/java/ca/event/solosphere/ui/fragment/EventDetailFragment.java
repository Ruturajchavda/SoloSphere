package ca.event.solosphere.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.palette.graphics.Palette;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import ca.event.solosphere.R;
import ca.event.solosphere.core.constants.Constants;
import ca.event.solosphere.core.constants.Extras;
import ca.event.solosphere.core.model.Event;
import ca.event.solosphere.databinding.FragmentEventDetailBinding;
import ca.event.solosphere.ui.activity.BaseFragmentActivity;

public class EventDetailFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "EventDetailFragment";
    private FragmentEventDetailBinding binding;
    private int backgroundColor;
    private Activity context;
    private boolean isEventLiked = false;
    private String eventID;
    private Bundle bundle;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference mEventRef;
    private Event event;

    public EventDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideToolbar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEventDetailBinding.inflate(inflater, container, false);
        context = getActivity();
        bundle = getArguments();
        if(bundle != null){
            eventID = bundle.getString(Extras.EXTRA_EVENT_ID);
        }
        init();

        Bitmap imageBitmap = ((BitmapDrawable)binding.ivEvent.getDrawable()).getBitmap();
        // generate background color from event image
        Palette.generateAsync(imageBitmap, new Palette.PaletteAsyncListener() {
            public void onGenerated(Palette palette) {
                backgroundColor = palette.getDominantColor(context.getColor(R.color.colorPrimary));
                binding.llMain.setBackgroundColor(backgroundColor);

                //set background color to event name
                binding.llEventHighlight.setBackgroundColor(backgroundColor);
                binding.llEventHighlight.getBackground().setAlpha(220);
            }
        });

        binding.btnBuyTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BaseFragmentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Extras.EXTRA_EVENT_DETAIL, event);
                intent.putExtra(Extras.EXTRA_FRAGMENT_BUNDLE, bundle);
                intent.putExtra(Extras.EXTRA_FRAGMENT_SIGNUP, new CheckoutFragment());
                startActivity(intent);
            }
        });

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.onBackPressed();
            }
        });

        binding.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isEventLiked){
                    isEventLiked = false;
                    binding.btnLike.setColorFilter(context.getColor(R.color.color_white));
//                    removeFromLikedEvent();
                }else{
                    isEventLiked = true;
                    binding.btnLike.setColorFilter(context.getColor(R.color.event_like_color));
                }
            }
        });

        binding.btnSeeAllAttendees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BaseFragmentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean(Extras.EXTRA_IS_USER, true);
                bundle.putSerializable(Extras.EXTRA_ATTACHMENT, event);
                intent.putExtra(Extras.EXTRA_FRAGMENT_BUNDLE, bundle);
                intent.putExtra(Extras.EXTRA_FRAGMENT_SIGNUP, new AttendeeFragment());
                startActivity(intent);
            }
        });

        // set event description
        SpannableStringBuilder builder = new SpannableStringBuilder();
        int startBold = builder.length();
        builder.append(getString(R.string.about_event));
        builder.setSpan(new StyleSpan(Typeface.BOLD), startBold, builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new ForegroundColorSpan(context.getColor(R.color.color_white)), startBold, builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(getString(R.string.about_event_details));
        binding.tvEventDescription.setText(builder);

        return binding.getRoot();
    }

    public void init(){
        //Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mEventRef = mFirebaseInstance.getReference(Constants.TBL_EVENTS);
        getEventData();
    }

    private void getEventData() {
        mEventRef.child(eventID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    event = dataSnapshot.getValue(Event.class);
                    if (event != null) {
                        // Set event data to UI elements
                        binding.tvEventName.setText(event.getName());
                        binding.tvEventCategory.setText(event.getCategory());
                        binding.tvEventPrice.setText("$"+event.getPrice());
                        binding.tvEventDate.setText(event.getStartDate().substring(0,2));
                        binding.tvEventMonth.setText("March");
                        binding.tvEventDay.setText("Wednesday");
                        binding.tvEventTime.setText(event.getStartTime()+"-"+event.getEndTime());
                        binding.tvEventDescription.setText(event.getDesc());
                        binding.tvEventLocation.setText(event.getLocation());
                        // Similarly, you can set other event data to corresponding UI elements

                        String eventImageUrl = event.getEventImage();
                        if (eventImageUrl != null && !eventImageUrl.isEmpty()) {
                            Glide.with(context)
                                    .load(eventImageUrl)
                                    .placeholder(R.drawable.demo_event_1) // Placeholder image
                                    .error(R.drawable.demo_event_1) // Error image if loading fails
                                    .into(binding.ivEvent); // ImageView where the event image is displayed
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

    @Override
    public void onClick(View view) {

    }
}