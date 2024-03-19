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
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import ca.event.solosphere.R;
import ca.event.solosphere.core.constants.Constants;
import ca.event.solosphere.core.model.Attendees;
import ca.event.solosphere.core.model.User;
import ca.event.solosphere.databinding.LayoutChatRequestBinding;
import ca.event.solosphere.ui.interfaces.RecyclerViewItemInterface;

public class AttendeeAdapter extends RecyclerView.Adapter<AttendeeAdapter.AttendeeAdapterViewHolder> {

    private Activity mContext;
    private ArrayList<Attendees> attendeesArrayList = new ArrayList<>();

    private RecyclerViewItemInterface viewItemInterface;

    public AttendeeAdapter(Activity mContext, ArrayList<Attendees> attendeesArrayList) {
        this.mContext = mContext;
        this.attendeesArrayList = attendeesArrayList;
    }

    public void setItemClickListener(RecyclerViewItemInterface viewItemInterface) {
        this.viewItemInterface = viewItemInterface;
    }

    @NonNull
    @Override
    public AttendeeAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutChatRequestBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.layout_chat_request, parent, false);
        return new AttendeeAdapterViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AttendeeAdapterViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Attendees attendees = attendeesArrayList.get(position);

        String tempUrl = attendees.getProfilePicture();

        if (tempUrl != null && !tempUrl.isEmpty()) {

            Glide.with(mContext).load(tempUrl).placeholder(R.drawable.ic_user_stock).error(R.drawable.ic_user_stock).into(holder.binding.imageUser);
        }

        holder.binding.txtName.setText(attendees.getFullName());
        if (attendees.getCurrentState() != null) {
            if (attendees.getCurrentState().equals(Constants.STATE_NEW)) {
                holder.binding.btnSendReq.setText(R.string.title_send_request);

                holder.binding.btnSendReq.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (viewItemInterface != null) {
                            viewItemInterface.OnItemClick(holder.getAdapterPosition(), attendees);
                        }

                    }
                });

            } else if (attendees.getCurrentState().equals(Constants.STATE_REQ_SENT)) {
                holder.binding.btnSendReq.setText(R.string.title_cancel_request);

                holder.binding.btnSendReq.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (viewItemInterface != null) {
                            viewItemInterface.OnItemMoved(holder.getAdapterPosition(), attendees);
                        }

                    }
                });
            } else if (attendees.getCurrentState().equals(Constants.STATE_REQ_RECEIVED)) {
                holder.binding.btnSendReq.setVisibility(View.GONE);
                holder.binding.txtStatus.setText(R.string.status_already_received_req);
                holder.binding.txtStatus.setVisibility(View.VISIBLE);
            } else {
                holder.binding.btnSendReq.setVisibility(View.GONE);
                holder.binding.txtStatus.setText(R.string.status_friends);
                holder.binding.txtStatus.setVisibility(View.VISIBLE);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (viewItemInterface != null) {
                            viewItemInterface.OnItemShare(holder.getAdapterPosition(), attendees);
                        }

                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return attendeesArrayList.size();
    }


    class AttendeeAdapterViewHolder extends RecyclerView.ViewHolder {

        private final LayoutChatRequestBinding binding;

        AttendeeAdapterViewHolder(LayoutChatRequestBinding layoutChatRequestBinding) {
            super(layoutChatRequestBinding.getRoot());
            this.binding = layoutChatRequestBinding;

            binding.layoutAcceptReq.setVisibility(View.GONE);
        }
    }
}