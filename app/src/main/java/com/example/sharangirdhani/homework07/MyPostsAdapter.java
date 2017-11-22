package com.example.sharangirdhani.homework07;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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

public class MyPostsAdapter extends RecyclerView.Adapter<MyPostsAdapter.MyPostRecyclerViewHolder> {
    private Context mContext;
    private List<Post> postList;
    private SharedPreferences sharedPreferences;
    private String token;
    private MyPostsAdapter.IPostHandler postHandler;

    public MyPostsAdapter(Context mContext, List<Post> postList, MyPostsAdapter.IPostHandler postHandler) {
        this.mContext = mContext;
        this.postList = postList;
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        this.token = sharedPreferences.getString("token","");
        this.postHandler = postHandler;
    }

    public static class MyPostRecyclerViewHolder extends RecyclerView.ViewHolder{

        TextView time;
        TextView name;
        TextView post;
        ImageButton delete;

        public MyPostRecyclerViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.tv_friendlabel);
            time = (TextView) itemView.findViewById(R.id.tv_time);
            post = (TextView) itemView.findViewById(R.id.tv_post);
            delete = (ImageButton) itemView.findViewById((R.id.imgBtnDelete));
        }
    }


    @Override
    public MyPostsAdapter.MyPostRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.mypost_row,parent,false);
        MyPostRecyclerViewHolder postRecyclerViewHolder = new MyPostsAdapter.MyPostRecyclerViewHolder(view);
        return postRecyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(MyPostRecyclerViewHolder holder, final int position) {
        final Post post = postList.get(position);

        holder.name.setText(post.getUsername());
        holder.time.setText(post.getPrettyTime());
        holder.post.setText(post.getPostData());

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postHandler.deletePost(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    interface IPostHandler{
        void deletePost(int position);
    }
}
