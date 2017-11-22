package com.example.sharangirdhani.homework07;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

/**
 * Created by sharangirdhani on 11/21/17.
 */

public class PendingRequestAdapter extends RecyclerView.Adapter<PendingRequestAdapter.PendingRequestRecyclerViewHolder>{
    private Context mContext;
    private List<User> userList;
    private String token;
    private Handler handler;
    private IPendingRequestInterface iPendingRequestInterface;

    public PendingRequestAdapter(Context mContext, List<User> friendsList, IPendingRequestInterface iPendingRequestInterface) {
        this.mContext = mContext;
        this.userList = friendsList;
        this.iPendingRequestInterface = iPendingRequestInterface;
    }



    public static class PendingRequestRecyclerViewHolder extends RecyclerView.ViewHolder{

        TextView txtViewName;
        ImageButton btnAccept;
        ImageButton btnReject;

        public PendingRequestRecyclerViewHolder(View itemView) {
            super(itemView);
            txtViewName = (TextView) itemView.findViewById(R.id.tv_friendName);
            btnAccept = (ImageButton) itemView.findViewById(R.id.imgBtnDelete);
            btnReject = (ImageButton) itemView.findViewById(R.id.imgBtnDecline);
        }
    }


    @Override
    public PendingRequestRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.pending_request_row,parent,false);
        PendingRequestRecyclerViewHolder pendingRequestRecyclerViewHolder = new PendingRequestRecyclerViewHolder(view);
        return pendingRequestRecyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(PendingRequestRecyclerViewHolder holder, final int position) {
        final User user = userList.get(position);

        holder.txtViewName.setText(user.getFirstName()+" "+user.getLastName());
        holder.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iPendingRequestInterface.pendingRequestAccepted(user);
            }
        });

        holder.btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iPendingRequestInterface.pendingRequestRejected(user);
            }
        });
    }


    @Override
    public int getItemCount() {
        return userList.size();
    }

    interface IPendingRequestInterface{
        void pendingRequestAccepted(User user);
        void pendingRequestRejected(User user);
    }
}
