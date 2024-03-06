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

import ca.event.solosphere.R;
import ca.event.solosphere.databinding.LayoutEventCategoryBinding;

public class EventCategoryAdapter extends RecyclerView.Adapter<EventCategoryAdapter.EventCategoryViewHolder> {

    private Context mContext;

    public EventCategoryAdapter(Context mContext) {
        this.mContext = mContext;
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
//            holder.binding.llEventCategory.setBackground(mContext.getDrawable(R.drawable.event_category_background_selected));
//            holder.binding.ivEventCategory.setColorFilter(ContextCompat.getColor(mContext, R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);
        if(position == 0){
            holder.binding.ivEventCategory.setImageResource(R.drawable.category_feed);
            holder.binding.tvEventCategory.setText("My feed");
        }else if(position == 1){
            holder.binding.ivEventCategory.setImageResource(R.drawable.category_food);
            holder.binding.tvEventCategory.setText("Food");
        }else if(position == 2){
            holder.binding.ivEventCategory.setImageResource(R.drawable.category_music);
            holder.binding.tvEventCategory.setText("Concerts");
        }{

        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }


    class EventCategoryViewHolder extends RecyclerView.ViewHolder {

        private final LayoutEventCategoryBinding binding;

        EventCategoryViewHolder(LayoutEventCategoryBinding workoutRowBinding) {
            super(workoutRowBinding.getRoot());
            this.binding = workoutRowBinding;
        }
    }
}
