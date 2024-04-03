package ca.event.solosphere.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import ca.event.solosphere.R;
import ca.event.solosphere.core.constants.Extras;
import ca.event.solosphere.core.model.Event;
import ca.event.solosphere.databinding.LayoutEventBinding;
import ca.event.solosphere.ui.activity.BaseFragmentActivity;
import ca.event.solosphere.ui.activity.EventDetailActivity;
import ca.event.solosphere.ui.fragment.EventDetailFragment;
import ca.event.solosphere.ui.fragment.PaymentFragment;
import eightbitlab.com.blurview.RenderScriptBlur;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private Activity context;
    private List<Event> eventList;

    public EventAdapter(Activity context, List<Event> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutEventBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.layout_event, parent, false);
        return new EventViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event currentEvent = eventList.get(position);
        holder.binding.tvEventName.setText(currentEvent.getName());
        Glide.with(context).load(currentEvent.getEventImage()).placeholder(R.drawable.solosphere_events_banner).error(R.drawable.solosphere_events_banner).into(holder.binding.ivEventImage);
        holder.binding.tvEventDate.setText(currentEvent.getStartDate());
        String eventPlaceTime = currentEvent.getCity()+" - "+currentEvent.getState()+", "+
                currentEvent.getStartTime();
        holder.binding.tvEventPlaceTime.setText(eventPlaceTime);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BaseFragmentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(Extras.EXTRA_EVENT_ID, currentEvent.getEventID());
                intent.putExtra(Extras.EXTRA_FRAGMENT_SIGNUP, new EventDetailFragment());
                intent.putExtra(Extras.EXTRA_FRAGMENT_BUNDLE, bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }


    class EventViewHolder extends RecyclerView.ViewHolder {
        private final LayoutEventBinding binding;

        EventViewHolder(LayoutEventBinding layoutEventBinding) {
            super(layoutEventBinding.getRoot());
            this.binding = layoutEventBinding;

            float radius = 10f;

            View decorView = context.getWindow().getDecorView();
            ViewGroup rootView = decorView.findViewById(android.R.id.content);
            Drawable windowBackground = decorView.getBackground();
            binding.blurView.setupWith(rootView, new RenderScriptBlur(context)) // or RenderEffectBlur
                    .setFrameClearDrawable(windowBackground) // Optional
                    .setBlurRadius(radius);

        }
    }
}
