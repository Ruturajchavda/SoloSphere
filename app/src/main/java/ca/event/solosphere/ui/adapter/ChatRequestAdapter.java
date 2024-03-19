package ca.event.solosphere.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import ca.event.solosphere.R;
import ca.event.solosphere.core.constants.Constants;
import ca.event.solosphere.core.model.Attendees;
import ca.event.solosphere.core.model.User;
import ca.event.solosphere.databinding.LayoutChatRequestBinding;
import ca.event.solosphere.ui.interfaces.RecyclerViewItemInterface;

public class ChatRequestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity mContext;
    private static final int VIEW_TYPE_CHAT_REQ = 0;
    private static final int VIEW_TYPE_EMPTY = 1;

    private ArrayList<User> userArrayList = new ArrayList<>();
    private RecyclerViewItemInterface viewItemInterface;

    public ChatRequestAdapter(Activity mContext, ArrayList<User> userArrayList) {
        this.mContext = mContext;
        this.userArrayList = userArrayList;
    }

    public void setItemClickListener(RecyclerViewItemInterface viewItemInterface) {
        this.viewItemInterface = viewItemInterface;
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

            User user = userArrayList.get(position);

            String tempUrl = user.getProfilePicture();

            if (tempUrl != null && !tempUrl.isEmpty()) {

                Glide.with(mContext).load(tempUrl).placeholder(R.drawable.ic_user_stock).error(R.drawable.ic_user_stock).into(chatRequestViewHolder.binding.imageUser);
            }

            chatRequestViewHolder.binding.txtName.setText(user.getFullName());

            if (user.getCurrentState().equals(Constants.STATE_REQ_SENT)) {
                chatRequestViewHolder.binding.layoutAcceptReq.setVisibility(View.GONE);
                chatRequestViewHolder.binding.txtStatus.setText(R.string.status_sent_req);
                chatRequestViewHolder.binding.txtStatus.setVisibility(View.VISIBLE);

                chatRequestViewHolder.binding.btnSendReq.setText(R.string.title_cancel_request);

                chatRequestViewHolder.binding.btnSendReq.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (viewItemInterface != null) {
                            viewItemInterface.OnItemClick(chatRequestViewHolder.getAdapterPosition(), user);
                        }

                    }
                });

            } else if (user.getCurrentState().equals(Constants.STATE_REQ_RECEIVED)) {
                chatRequestViewHolder.binding.layoutSendReq.setVisibility(View.GONE);
                chatRequestViewHolder.binding.txtStatus.setText(R.string.status_received_req);
                chatRequestViewHolder.binding.txtStatus.setVisibility(View.VISIBLE);

                chatRequestViewHolder.binding.btnAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (viewItemInterface != null) {
                            viewItemInterface.OnItemMoved(chatRequestViewHolder.getAdapterPosition(), user);
                        }

                    }
                });

                chatRequestViewHolder.binding.btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (viewItemInterface != null) {
                            viewItemInterface.OnItemShare(chatRequestViewHolder.getAdapterPosition(), user);
                        }

                    }
                });

            }
        }
    }

    @Override
    public int getItemCount() {
        return userArrayList.size() + 1;
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
