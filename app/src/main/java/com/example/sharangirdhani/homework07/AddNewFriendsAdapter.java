/*
    Salman Mujtaba 800969897
    Sharan Girdhani 800960333
    My Social App
    Homework 7
 */

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
 * Created by KashishSyeda on 11/21/2017.
 */

public class AddNewFriendsAdapter extends RecyclerView.Adapter<AddNewFriendsAdapter.AddNewFriendsRecyclerViewHolder>{

    private Context mContext;
    private List<User> userList;
    private String token;
    private Handler handler;
    private IFriendAddInterface iFriendAddInterface;

    public AddNewFriendsAdapter(Context mContext, List<User> userList, IFriendAddInterface iFriendAddInterface) {
        this.mContext = mContext;
        this.userList = userList;
        this.iFriendAddInterface = iFriendAddInterface;
    }



    public static class AddNewFriendsRecyclerViewHolder extends RecyclerView.ViewHolder{

        TextView txtViewName;
        ImageButton btnAdd;

        public AddNewFriendsRecyclerViewHolder(View itemView) {
            super(itemView);
            txtViewName = (TextView) itemView.findViewById(R.id.tv_friendName);
            btnAdd = (ImageButton) itemView.findViewById(R.id.imgBtnAddFriend);
        }
    }


    @Override
    public AddNewFriendsRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.add_friend_row,parent,false);
        AddNewFriendsRecyclerViewHolder addNewFriendsRecyclerViewHolder = new AddNewFriendsRecyclerViewHolder(view);
        return addNewFriendsRecyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(AddNewFriendsRecyclerViewHolder holder, final int position) {
        final User user = userList.get(position);

        holder.txtViewName.setText(user.getFirstName()+" "+user.getLastName());
        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iFriendAddInterface.friendRequestSent(user);
            }
        });
    }


    @Override
    public int getItemCount() {
        return userList.size();
    }

    interface IFriendAddInterface{
        void friendRequestSent(User user);
    }
}
