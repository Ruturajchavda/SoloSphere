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

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import ca.event.solosphere.R;
import ca.event.solosphere.core.model.Category;
import ca.event.solosphere.core.model.EventCategory;
import ca.event.solosphere.databinding.LayoutEventCategoryBinding;

public class EventCategoryAdapter extends RecyclerView.Adapter<EventCategoryAdapter.EventCategoryViewHolder> {

    private Context context;
    private int selectedEventCategoryPosition = 0;
    private List<Category> eventCategories;

    public EventCategoryAdapter(Context context, List<Category> eventCategories) {
        this.context = context;
        this.eventCategories = eventCategories;
    }

    @NonNull
    @Override
    public EventCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutEventCategoryBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_event_category, parent, false);
        return new EventCategoryViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull EventCategoryViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Category currentCategory = eventCategories.get(position);
        Glide.with(context).load(currentCategory.getIcon()).placeholder(R.drawable.solosphere_events_banner).error(R.drawable.solosphere_events_banner).into(holder.binding.ivEventCategory);
        holder.binding.tvEventCategory.setText(currentCategory.getName());
        if(position == selectedEventCategoryPosition){
            holder.binding.llEventCategory.setBackground(context.getDrawable(R.drawable.event_category_background_selected));
            holder.binding.ivEventCategory.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);
        }else{
            holder.binding.llEventCategory.setBackground(context.getDrawable(R.drawable.event_category_background_unselected));
            holder.binding.ivEventCategory.setColorFilter(ContextCompat.getColor(context, R.color.color_white), android.graphics.PorterDuff.Mode.MULTIPLY);
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
