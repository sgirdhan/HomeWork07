/*
    Salman Mujtaba 800969897
    Sharan Girdhani 800960333
    My Social App
    Homework 7
 */

package com.example.sharangirdhani.homework07;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MyPostsActivity extends AppCompatActivity implements MyPostsAdapter.IPostHandler, PostsAdapter.IPostHandler {
    ImageButton imgBtnFriends;
    ImageButton imgBtnEdit;

    TextView tvUserName;
    TextView tvPostLabel;

    RecyclerView rvPosts;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;

    ArrayList<Post> postList;
    MyPostsAdapter postsAdapter;
    PostsAdapter userPostsAdapter;
    String currentUserId;
    DatabaseReference dbRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_posts);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        postList = new ArrayList<>();
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(R.layout.custom_actionbar_view);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);

        actionBar.getCustomView().findViewById(R.id.imageViewLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MyPostsActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        imgBtnFriends = (ImageButton) findViewById(R.id.imgBtn_friends);

        tvUserName = (TextView) findViewById(R.id.tv_friendlabel);
        tvPostLabel = (TextView) findViewById(R.id.tvPostLabel);
        imgBtnEdit = (ImageButton) findViewById(R.id.imgBtnEdit);
        imgBtnFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPostsActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        rvPosts = (RecyclerView) findViewById(R.id.rvPosts);
        if(getIntent().hasExtra("USER_ID")) {
            currentUserId = getIntent().getExtras().getString("USER_ID");

            if(firebaseAuth.getCurrentUser().getUid().equals(currentUserId)) {
                imgBtnEdit = (ImageButton) findViewById(R.id.imgBtnEdit);
                imgBtnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MyPostsActivity.this, MyProfileActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

                tvPostLabel.setText("My Posts");
                firebaseDatabase.getReference().child("users").child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User currentUser = dataSnapshot.getValue(User.class);
                        tvUserName.setText(currentUser.getFullName());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                postsAdapter = new MyPostsAdapter(this, postList, this);

                rvPosts.setAdapter(postsAdapter);
                rvPosts.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));


                firebaseDatabase.getReference().child("users").child(firebaseAuth.getCurrentUser().getUid()).child("posts").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Post post = dataSnapshot.getValue(Post.class);
                        postList.add(post);
                        Collections.sort(postList, new Comparator<Post>() {
                            @Override
                            public int compare(Post o1, Post o2) {
                                return o2.getCreated().compareTo(o1.getCreated());
                            }
                        });
                        postsAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        postsAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        postsAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
            else {
                dbRef = firebaseDatabase.getReference().child("users").child(currentUserId);
                updateUserBasicValues();

                userPostsAdapter = new PostsAdapter(this, postList, this);

                rvPosts.setAdapter(userPostsAdapter);
                rvPosts.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));


                firebaseDatabase.getReference().child("users").child(currentUserId).child("posts").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Post post = dataSnapshot.getValue(Post.class);
                        postList.add(post);
                        Collections.sort(postList, new Comparator<Post>() {
                            @Override
                            public int compare(Post o1, Post o2) {
                                return o2.getCreated().compareTo(o1.getCreated());
                            }
                        });
                        userPostsAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        userPostsAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        userPostsAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        }
        else {
            Toast.makeText(this, "USER_ID not sent", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(firebaseAuth.getCurrentUser().getUid().equals(currentUserId)) {
            tvUserName.setText(firebaseAuth.getCurrentUser().getDisplayName());
        }
        else {
            updateUserBasicValues();
        }

    }

    @Override
    public void deletePost(int position) {
        Post deletePost = postList.get(position);
        firebaseDatabase.getReference().child("users").child(firebaseAuth.getCurrentUser().getUid()).child("posts").child(deletePost.getPostId()).setValue(null);
        postList.remove(position);
    }

    @Override
    public void goToUserPostActivity(String userId) {

    }

    public void updateUserBasicValues() {
        imgBtnEdit.setVisibility(View.INVISIBLE);
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User currentUser = dataSnapshot.getValue(User.class);
                tvUserName.setText(currentUser.getFullName());
                tvPostLabel.setText(currentUser.getFullName() + "'s Posts");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
