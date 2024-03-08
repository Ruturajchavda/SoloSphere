package ca.event.solosphere.ui.adapter;

import android.annotation.SuppressLint;
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

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder> {

    private Context mContext;
    private RecyclerViewItemInterface viewItemInterface;
    public ChatListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setItemClickListener(RecyclerViewItemInterface viewItemInterface) {
        this.viewItemInterface = viewItemInterface;
    }
    @NonNull
    @Override
    public ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutChatListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.layout_chat_list, parent, false);
        return new ChatListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (viewItemInterface != null) {
                    viewItemInterface.OnItemClick(holder.getAdapterPosition());
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return 10;
    }


    class ChatListViewHolder extends RecyclerView.ViewHolder {

        private final LayoutChatListBinding binding;

        ChatListViewHolder(LayoutChatListBinding layoutChatListBinding) {
            super(layoutChatListBinding.getRoot());
            this.binding = layoutChatListBinding;
        }
    }
}

