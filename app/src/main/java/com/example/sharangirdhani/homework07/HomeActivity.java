package com.example.sharangirdhani.homework07;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class HomeActivity extends AppCompatActivity implements PostsAdapter.IPostHandler{

    ImageButton imgBtnFriends;
    ImageButton imgBtnEdit;
    ImageButton imgBtnPostSend;

    EditText edtNewPost;

    TextView tvUserName;
    TextView tvPostLabel;

    RecyclerView rvPosts;

    Boolean hideButtons;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference userRef;

    ArrayList<Post> postList;
    PostsAdapter postsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        userRef = firebaseDatabase.getReference("users").child(firebaseAuth.getCurrentUser().getUid());
        postList = new ArrayList<>();

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(R.layout.custom_actionbar_view);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);

        actionBar.getCustomView().findViewById(R.id.imageViewLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        imgBtnFriends = (ImageButton) findViewById(R.id.imgBtn_friends);
        imgBtnEdit = (ImageButton) findViewById(R.id.imgBtnEdit);
        imgBtnPostSend = (ImageButton) findViewById(R.id.imgBtnSendPost);

        edtNewPost = (EditText) findViewById(R.id.edtTxtNewPost);

        tvUserName = (TextView) findViewById(R.id.tv_friendlabel);

        tvPostLabel = (TextView) findViewById(R.id.tvPostLabel);

        imgBtnFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, FriendsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        imgBtnPostSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String post = edtNewPost.getText().toString();
                boolean isValid = true;

                if (post.length() == 0) {
                    edtNewPost.setError("Cannot send empty post.");
                    isValid = false;
                }

                if(isValid) {
                    final DatabaseReference postId = firebaseDatabase.getReference("users").child(firebaseAuth.getCurrentUser().getUid()).child("posts").push();
                    edtNewPost.setText("");
                    String currentTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());

                    Post newPost = new Post(postId.getKey(), firebaseAuth.getCurrentUser().getDisplayName(), currentTime, post, firebaseAuth.getCurrentUser().getUid());
                    postId.setValue(newPost);

                    Toast.makeText(HomeActivity.this,"Post added successfully",Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MyProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });


        rvPosts = (RecyclerView) findViewById(R.id.rvPosts);
        postsAdapter = new PostsAdapter(this, postList, this);

        rvPosts.setAdapter(postsAdapter);
        rvPosts.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User currentUser = dataSnapshot.getValue(User.class);
                tvUserName.setText(currentUser.getFullName());
                if(currentUser.friends.containsKey("accepted")) {
                    for(String friendId : currentUser.friends.get("accepted").keySet()) {
                        firebaseDatabase.getReference().child("users").child(friendId).child("posts").addChildEventListener(new ChildEventListener() {
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
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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

    @Override
    protected void onResume() {
        super.onResume();
        tvUserName.setText(firebaseAuth.getCurrentUser().getDisplayName());
    }

    @Override
    public void goToUserPostActivity(String userId) {
        Intent intent = new Intent(this, MyPostsActivity.class);
        intent.putExtra("USER_ID", userId);
        startActivity(intent);
        finish();
    }
}
