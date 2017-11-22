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
 * Created by sharangirdhani on 11/22/17.
 */

public class ManageFriendsAdapter extends RecyclerView.Adapter<ManageFriendsAdapter.ManageFriendsRecyclerViewHolder> {
    private Context mContext;
    private List<User> userList;
    private String token;
    private Handler handler;
    private IManageFriendsInterface iManageFriendsInterface;

    public ManageFriendsAdapter(Context mContext, List<User> friendsList, IManageFriendsInterface iManageFriendsInterface) {
        this.mContext = mContext;
        this.userList = friendsList;
        this.iManageFriendsInterface = iManageFriendsInterface;
    }


    public static class ManageFriendsRecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView txtViewName;
        ImageButton btnDelete;

        public ManageFriendsRecyclerViewHolder(View itemView) {
            super(itemView);
            txtViewName = (TextView) itemView.findViewById(R.id.tv_friendName);
            btnDelete = (ImageButton) itemView.findViewById(R.id.imgBtnDelete);
        }
    }


    @Override
    public ManageFriendsRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.manage_friend_row, parent, false);
        ManageFriendsRecyclerViewHolder manageFriendsRecyclerViewHolder = new ManageFriendsRecyclerViewHolder(view);
        return manageFriendsRecyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(ManageFriendsRecyclerViewHolder holder, final int position) {
        final User user = userList.get(position);

        holder.txtViewName.setText(user.getFirstName() + " " + user.getLastName());
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iManageFriendsInterface.friendDeleted(user);
            }
        });
    }


    @Override
    public int getItemCount() {
        return userList.size();
    }

    interface IManageFriendsInterface {
        void friendDeleted(User user);
    }
}