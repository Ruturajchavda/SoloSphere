package ca.event.solosphere.ui.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Locale;

import ca.event.solosphere.R;
import ca.event.solosphere.core.model.Event;
import ca.event.solosphere.databinding.LayoutListedEventsBinding;
import ca.event.solosphere.ui.interfaces.RecyclerViewItemInterface;

public class ListedEventsAdapter extends RecyclerView.Adapter<ListedEventsAdapter.ListedEventsAdapterViewHolder> {

    private Activity mContext;
    private ArrayList<Event> eventArrayList = new ArrayList<>();

    private RecyclerViewItemInterface viewItemInterface;

    public ListedEventsAdapter(Activity mContext, ArrayList<Event> eventArrayList) {
        this.mContext = mContext;
        this.eventArrayList = eventArrayList;
    }

    public void setItemClickListener(RecyclerViewItemInterface viewItemInterface) {
        this.viewItemInterface = viewItemInterface;
    }

    @NonNull
    @Override
    public ListedEventsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutListedEventsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.layout_listed_events, parent, false);
        return new ListedEventsAdapterViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ListedEventsAdapterViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Event event = eventArrayList.get(position);

        String tempUrl = event.getEventImage();

        if (tempUrl != null && !tempUrl.isEmpty()) {

            Glide.with(mContext).load(tempUrl).placeholder(R.drawable.image_placeholder).error(R.drawable.image_placeholder).into(holder.binding.imageEvent);
        }

        holder.binding.txtEvent.setText(event.getName());

        holder.binding.txtDateTime.setText(event.getStartDate() + " • " + event.getStartTime());
        holder.binding.txtLocation.setText(event.getLocation());

        holder.binding.editEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (viewItemInterface != null) {
                    viewItemInterface.OnItemClick(holder.getAdapterPosition(), event);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return eventArrayList.size();
    }


    class ListedEventsAdapterViewHolder extends RecyclerView.ViewHolder {

        private final LayoutListedEventsBinding binding;

        ListedEventsAdapterViewHolder(LayoutListedEventsBinding layoutListedEventsBinding) {
            super(layoutListedEventsBinding.getRoot());
            this.binding = layoutListedEventsBinding;
        }
    }

}
