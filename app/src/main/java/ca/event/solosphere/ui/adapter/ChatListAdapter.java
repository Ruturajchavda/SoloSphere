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
import ca.event.solosphere.databinding.LayoutChatListBinding;
import ca.event.solosphere.ui.interfaces.RecyclerViewItemInterface;

public class ChatListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity mContext;

    private static final int VIEW_TYPE_CHAT_LIST = 0;
    private static final int VIEW_TYPE_EMPTY = 1;
    private RecyclerViewItemInterface viewItemInterface;

    public ChatListAdapter(Activity mContext) {
        this.mContext = mContext;
    }

    public void setItemClickListener(RecyclerViewItemInterface viewItemInterface) {
        this.viewItemInterface = viewItemInterface;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_CHAT_LIST) {
            LayoutChatListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.layout_chat_list, parent, false);
            return new ChatListViewHolder(binding);
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
        if (holder instanceof ChatListViewHolder) {
            // Bind data for event item
            ChatListViewHolder chatListViewHolder = (ChatListViewHolder) holder;
            chatListViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (viewItemInterface != null) {
                        viewItemInterface.OnItemClick(chatListViewHolder.getAdapterPosition());
                    }

                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return 10 + 1;
    }

    @Override
    public int getItemViewType(int position) {
        // Check if it's the last position, then return blank item view type
        return (position == getItemCount() - 1) ? VIEW_TYPE_EMPTY : VIEW_TYPE_CHAT_LIST;
    }

    class ChatListViewHolder extends RecyclerView.ViewHolder {

        private final LayoutChatListBinding binding;

        ChatListViewHolder(LayoutChatListBinding layoutChatListBinding) {
            super(layoutChatListBinding.getRoot());
            this.binding = layoutChatListBinding;
        }
    }

    class EmptyViewHolder extends RecyclerView.ViewHolder {
        EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }
}

