/*
    Salman Mujtaba 800969897
    Sharan Girdhani 800960333
    My Social App
    Homework 7
 */

package com.example.sharangirdhani.homework07;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by sharangirdhani on 11/21/17.
 */

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostRecyclerViewHolder> {
    private Context mContext;
    private List<Post> postList;
    private SharedPreferences sharedPreferences;
    private String token;
    private IPostHandler postHandler;

    public PostsAdapter(Context mContext, List<Post> postList, IPostHandler postHandler) {
        this.mContext = mContext;
        this.postList = postList;
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        this.token = sharedPreferences.getString("token","");
        this.postHandler = postHandler;
    }

    public static class PostRecyclerViewHolder extends RecyclerView.ViewHolder{

        TextView time;
        TextView name;
        TextView post;

        public PostRecyclerViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.tv_friendlabel);
            time = (TextView) itemView.findViewById(R.id.tv_time);
            post = (TextView) itemView.findViewById(R.id.tv_post);
        }
    }

    @Override
    public PostRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.post_row,parent,false);
        PostRecyclerViewHolder postRecyclerViewHolder = new PostRecyclerViewHolder(view);
        return postRecyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(PostRecyclerViewHolder holder, final int position) {
        final Post post = postList.get(position);

        holder.name.setText(post.getUsername());
        holder.time.setText(post.getPrettyTime());
        holder.post.setText(post.getPostData());

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postHandler.goToUserPostActivity(post.getUserId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    interface IPostHandler{
        void goToUserPostActivity(String userId);
    }
}
