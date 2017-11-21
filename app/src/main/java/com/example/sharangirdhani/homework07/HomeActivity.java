package com.example.sharangirdhani.homework07;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.v7.app.ActionBar;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {

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
    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        userRef = firebaseDatabase.getReference("users").child(firebaseAuth.getCurrentUser().getUid());

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

        rvPosts = (RecyclerView) findViewById(R.id.rvPosts);

        imgBtnFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, FriendsActivity.class);
                startActivity(intent);
                finish();
            }
        });
        updateInitialPageLayout();
    }

    public void updateInitialPageLayout() {
        imgBtnEdit.setVisibility(View.INVISIBLE);
        edtNewPost.setVisibility(View.VISIBLE);
        imgBtnPostSend.setVisibility(View.VISIBLE);
        tvPostLabel.setText("Posts");
        hideButtons = true;

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentUser = dataSnapshot.getValue(User.class);
                tvUserName.setText(currentUser.getFirstName() + " " + currentUser.getLastName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
