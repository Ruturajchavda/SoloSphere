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

import java.util.ArrayList;

import ca.event.solosphere.R;
import ca.event.solosphere.core.constants.Constants;
import ca.event.solosphere.core.model.Attendee;
import ca.event.solosphere.core.model.User;
import ca.event.solosphere.databinding.LayoutChatRequestBinding;
import ca.event.solosphere.ui.interfaces.RecyclerViewItemInterface;

public class AttendeeAdapter extends RecyclerView.Adapter<AttendeeAdapter.AttendeeAdapterViewHolder> {

    private Activity mContext;
    private ArrayList<User> userArrayList = new ArrayList<>();
    private ArrayList<Attendee> attendeeArrayList = new ArrayList<>();
    private String currentUid;
    private boolean is_user;
    private RecyclerViewItemInterface viewItemInterface;

    public AttendeeAdapter(Activity mContext, ArrayList<User> userArrayList, ArrayList<Attendee> attendeeArrayList, String currentUid, boolean is_user) {
        this.mContext = mContext;
        this.userArrayList = userArrayList;
        this.attendeeArrayList = attendeeArrayList;
        this.currentUid = currentUid;
        this.is_user = is_user;
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
        User attendees = userArrayList.get(position);
        Attendee attendee = attendeeArrayList.get(position);

        String tempUrl = attendees.getProfilePicture();

        if (tempUrl != null && !tempUrl.isEmpty()) {

            Glide.with(mContext).load(tempUrl).placeholder(R.drawable.ic_user_stock).error(R.drawable.ic_user_stock).into(holder.binding.imageUser);
        }

        if (attendee.getTotalTickets() != 0) {
            holder.binding.txtTravelling.setVisibility(View.VISIBLE);
            if (attendee.getTotalTickets() == 1) {
                holder.binding.txtTravelling.setText(mContext.getString(R.string.title_travelling_solo));
            } else {
                holder.binding.txtTravelling.setText("(" + mContext.getString(R.string.title_travelling_with) + " " + (attendee.getTotalTickets() - 1) + " other) ");
            }
        }

        if (!is_user) {
            holder.binding.txtName.setText(attendees.getFullName());
            holder.binding.layoutSendReq.setVisibility(View.GONE);
            holder.binding.layoutAcceptReq.setVisibility(View.GONE);
        } else {
            if (!attendees.getUid().equals(currentUid)) {
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
            } else {
                holder.binding.txtName.setText(R.string.str_you);
                holder.binding.layoutSendReq.setVisibility(View.GONE);
                holder.binding.layoutAcceptReq.setVisibility(View.GONE);
            }
        }


    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
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