package ca.event.solosphere.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import ca.event.solosphere.R;
import ca.event.solosphere.databinding.LayoutEventBinding;
import ca.event.solosphere.databinding.LayoutEventCategoryBinding;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private Context mContext;

    public EventAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutEventBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.layout_event, parent, false);
        return new EventViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if(position == 1){
            holder.binding.imageViewSmallBanner.setImageResource(R.drawable.demo_event_2);
            holder.binding.tvEventCategoryName.setText("Holi Event - Celebration");
            holder.binding.tvEventCategoryPlace.setText("Waterloo - Ontario, 02:00 PM");
            holder.binding.textViewOffer.setText("March 13");
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }


    class EventViewHolder extends RecyclerView.ViewHolder {

        private final LayoutEventBinding binding;

        EventViewHolder(LayoutEventBinding layoutEventBinding) {
            super(layoutEventBinding.getRoot());
            this.binding = layoutEventBinding;
        }
    }
}
