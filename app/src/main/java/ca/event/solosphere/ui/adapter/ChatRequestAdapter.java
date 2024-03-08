package ca.event.solosphere.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import ca.event.solosphere.R;
import ca.event.solosphere.databinding.LayoutChatRequestBinding;

public class ChatRequestAdapter extends RecyclerView.Adapter<ChatRequestAdapter.ChatRequestViewHolder> {

    private Context mContext;

    public ChatRequestAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ChatRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutChatRequestBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.layout_chat_request, parent, false);
        return new ChatRequestViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatRequestViewHolder holder, @SuppressLint("RecyclerView") int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }


    class ChatRequestViewHolder extends RecyclerView.ViewHolder {

        private final LayoutChatRequestBinding binding;

        ChatRequestViewHolder(LayoutChatRequestBinding layoutChatRequestBinding) {
            super(layoutChatRequestBinding.getRoot());
            this.binding = layoutChatRequestBinding;
        }
    }
}
