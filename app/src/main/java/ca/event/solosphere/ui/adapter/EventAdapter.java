package ca.event.solosphere.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import ca.event.solosphere.R;
import ca.event.solosphere.databinding.LayoutEventBinding;
import ca.event.solosphere.ui.activity.EventDetailActivity;
import eightbitlab.com.blurview.RenderScriptBlur;

public class EventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity mContext;
    private static final int VIEW_TYPE_EVENT = 0;
    private static final int VIEW_TYPE_EMPTY = 1;

    public EventAdapter(Activity mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_EVENT) {
            LayoutEventBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.layout_event, parent, false);
            return new EventViewHolder(binding);
        } else {
            // Inflate empty view layout
            View emptyView = new View(mContext);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 250);
            emptyView.setLayoutParams(layoutParams);
            return new EmptyViewHolder(emptyView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof EventViewHolder) {
            // Bind data for event item
            EventViewHolder eventViewHolder = (EventViewHolder) holder;
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mContext.startActivity(new Intent(mContext, EventDetailActivity.class));
                }
            });
            if (position == 2) {
                eventViewHolder.binding.imageViewSmallBanner.setImageResource(R.drawable.demo_event_2);
                eventViewHolder.binding.tvEventCategoryName.setText("Holi Event - Celebration");
                eventViewHolder.binding.tvEventCategoryPlace.setText("Waterloo - Ontario, 02:00 PM");
                eventViewHolder.binding.textViewOffer.setText("March 13");
            }
        }
    }

    @Override
    public int getItemCount() {
        return 4 + 1;
    }

    @Override
    public int getItemViewType(int position) {
        // Check if it's the last position, then return blank item view type
        return (position == getItemCount() - 1) ? VIEW_TYPE_EMPTY : VIEW_TYPE_EVENT;
    }

    class EventViewHolder extends RecyclerView.ViewHolder {

        private final LayoutEventBinding binding;

        EventViewHolder(LayoutEventBinding layoutEventBinding) {
            super(layoutEventBinding.getRoot());
            this.binding = layoutEventBinding;

            float radius = 3f;

            View decorView = mContext.getWindow().getDecorView();
            ViewGroup rootView = (ViewGroup) decorView.findViewById(android.R.id.content);
            Drawable windowBackground = decorView.getBackground();
            binding.blurView.setupWith(rootView, new RenderScriptBlur(mContext)) // or RenderEffectBlur
                    .setFrameClearDrawable(windowBackground) // Optional
                    .setBlurRadius(radius);
        }
    }

    // ViewHolder for the empty item
    class EmptyViewHolder extends RecyclerView.ViewHolder {
        EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }
}
