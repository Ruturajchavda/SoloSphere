package ca.event.solosphere.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ca.event.solosphere.R;
import ca.event.solosphere.core.model.EventCategory;
import ca.event.solosphere.databinding.LayoutEventCategoryBinding;

public class EventCategoryAdapter extends RecyclerView.Adapter<EventCategoryAdapter.EventCategoryViewHolder> {

    private Context mContext;
    private int selectedEventCategoryPosition = 0;
    private ArrayList<EventCategory> eventCategories;

    public EventCategoryAdapter(Context mContext, ArrayList<EventCategory> eventCategories) {
        this.mContext = mContext;
        this.eventCategories = eventCategories;
    }

    @NonNull
    @Override
    public EventCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutEventCategoryBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.layout_event_category, parent, false);
        return new EventCategoryViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull EventCategoryViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.binding.ivEventCategory.setImageResource(eventCategories.get(position).getCategoryImage());
        holder.binding.tvEventCategory.setText(eventCategories.get(position).getCategoryTitle());
        if(position == selectedEventCategoryPosition){
            holder.binding.llEventCategory.setBackground(mContext.getDrawable(R.drawable.event_category_background_selected));
            holder.binding.ivEventCategory.setColorFilter(ContextCompat.getColor(mContext, R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);
        }else{
            holder.binding.llEventCategory.setBackground(mContext.getDrawable(R.drawable.event_category_background_unselected));
            holder.binding.ivEventCategory.setColorFilter(ContextCompat.getColor(mContext, R.color.color_white), android.graphics.PorterDuff.Mode.MULTIPLY);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                notifyItemChanged(selectedEventCategoryPosition);
                selectedEventCategoryPosition = holder.getAdapterPosition();
                notifyItemChanged(selectedEventCategoryPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventCategories.size();
    }


    class EventCategoryViewHolder extends RecyclerView.ViewHolder {

        private final LayoutEventCategoryBinding binding;

        EventCategoryViewHolder(LayoutEventCategoryBinding workoutRowBinding) {
            super(workoutRowBinding.getRoot());
            this.binding = workoutRowBinding;
        }
    }
}
