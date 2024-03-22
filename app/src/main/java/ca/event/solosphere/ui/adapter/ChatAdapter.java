package ca.event.solosphere.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ca.event.solosphere.R;
import ca.event.solosphere.core.model.Chat;
import ca.event.solosphere.databinding.ChatItemLeftBinding;
import ca.event.solosphere.databinding.ChatItemRightBinding;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private Context mContext;
    private ArrayList<Chat> chatArrayList = new ArrayList<>();
    private String senderImage;
    private String currentUserId;


    public ChatAdapter(Context mContext, ArrayList<Chat> chatArrayList, String currentUserId) {
        this.chatArrayList = chatArrayList;
        this.mContext = mContext;
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            ChatItemRightBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.chat_item_right, parent, false);
            return new SenderChatViewHolder(binding);
        } else {
            ChatItemLeftBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.chat_item_left, parent, false);
            return new ReceiverChatViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Chat chat = chatArrayList.get(position);
        if (holder instanceof SenderChatViewHolder) {
            SenderChatViewHolder senderChatViewHolder = (SenderChatViewHolder) holder;
            if (chat.getSender().equals(currentUserId)) {
                Log.e("TAG", "onBindViewHolder: "+ chat );
                senderChatViewHolder.binding.txtMessage.setText(chat.getMessage());
                if (chat.getTime() != null && !chat.getTime().trim().equals("")) {
                    senderChatViewHolder.binding.txtTime.setText(convertTime(chat.getTime()));
                }

                if(chat.getIsSeen().equals("true")){
                    senderChatViewHolder.binding.imgIsSeen.setImageResource(R.drawable.ic_double_check);
                }else {
                    senderChatViewHolder.binding.imgIsSeen.setImageResource(R.drawable.ic_single_check);
                }
            }
        }

        if (holder instanceof ReceiverChatViewHolder) {
            ReceiverChatViewHolder receiverChatViewHolder = (ReceiverChatViewHolder) holder;
            if (chat.getReceiver().equals(currentUserId)) {
                receiverChatViewHolder.binding.txtMessage.setText(chat.getMessage());
                if (chat.getTime() != null && !chat.getTime().trim().equals("")) {
                    receiverChatViewHolder.binding.txtTime.setText(convertTime(chat.getTime()));
                }
            }

        }

    }


    @Override
    public int getItemCount() {
        return chatArrayList.size();
    }

    class SenderChatViewHolder extends RecyclerView.ViewHolder {

        private final ChatItemRightBinding binding;

        SenderChatViewHolder(ChatItemRightBinding chatItemRightBinding) {
            super(chatItemRightBinding.getRoot());
            this.binding = chatItemRightBinding;
        }
    }

    class ReceiverChatViewHolder extends RecyclerView.ViewHolder {

        private final ChatItemLeftBinding binding;

        ReceiverChatViewHolder(ChatItemLeftBinding chatItemLeftBinding) {
            super(chatItemLeftBinding.getRoot());
            this.binding = chatItemLeftBinding;
        }
    }

    public String convertTime(String time) {
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
        String dateString = formatter.format(new Date(Long.parseLong(time)));
        return dateString;
    }


    @Override
    public int getItemViewType(int position) {
        if (chatArrayList.get(position).getSender().equals(currentUserId)) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }
}