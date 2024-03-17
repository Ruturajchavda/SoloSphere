package ca.event.solosphere.ui.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import ca.event.solosphere.R;
import ca.event.solosphere.databinding.LayoutChatRequestBinding;
import ca.event.solosphere.databinding.LayoutEventBinding;

public class ChatRequestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private Activity mContext;
    private static final int VIEW_TYPE_CHAT_REQ = 0;
    private static final int VIEW_TYPE_EMPTY = 1;
    public ChatRequestAdapter(Activity mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_CHAT_REQ) {
            LayoutChatRequestBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.layout_chat_request, parent, false);
            return new ChatRequestViewHolder(binding);
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
        if (holder instanceof ChatRequestViewHolder) {
            // Bind data for event item
            ChatRequestViewHolder chatRequestViewHolder = (ChatRequestViewHolder) holder;

        }
    }

    @Override
    public int getItemCount() {
        return 10 + 1;
    }

    @Override
    public int getItemViewType(int position) {
        // Check if it's the last position, then return blank item view type
        return (position == getItemCount() - 1) ? VIEW_TYPE_EMPTY : VIEW_TYPE_CHAT_REQ;
    }

    class ChatRequestViewHolder extends RecyclerView.ViewHolder {

        private final LayoutChatRequestBinding binding;

        ChatRequestViewHolder(LayoutChatRequestBinding layoutChatRequestBinding) {
            super(layoutChatRequestBinding.getRoot());
            this.binding = layoutChatRequestBinding;
        }
    }

    // ViewHolder for the empty item
    class EmptyViewHolder extends RecyclerView.ViewHolder {
        EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }
}
