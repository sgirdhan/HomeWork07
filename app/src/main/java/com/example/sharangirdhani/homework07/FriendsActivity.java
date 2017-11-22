package com.example.sharangirdhani.homework07;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class FriendsActivity extends AppCompatActivity implements ManageFriendsFragment.OnFragmentInteractionListener, AddNewFriendFragment.OnFragmentInteractionListener, PendingRequestFragment.OnFragmentInteractionListener{
    ImageButton imgBtnHome;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    ArrayList<String> friendsListAdd;
    ArrayList<String> friendsListPending;
    ArrayList<String> friendsListManage;
    ArrayList<User> userListAdd;
    ArrayList<User> userListPending;
    ArrayList<User> userListManage;
    PagerAdapter pagerAdapter;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(R.layout.custom_actionbar_view);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);

        actionBar.getCustomView().findViewById(R.id.imageViewLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(FriendsActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        imgBtnHome = (ImageButton) findViewById(R.id.imgBtn_home);
        imgBtnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FriendsActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });


        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.pager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    private void setupViewPager(ViewPager viewPager) {
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ManageFriendsFragment(), "Friends");
        adapter.addFragment(new AddNewFriendFragment(), "Add New Friend");
        adapter.addFragment(new PendingRequestFragment(), "Request Pending");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public ArrayList<User> getManageFriendsList() {
        userListManage = new ArrayList<>();
        friendsListManage = new ArrayList<>();

        firebaseDatabase.getReference().child("users").child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User currentUser = dataSnapshot.getValue(User.class);
                if(currentUser.friends.containsKey("accepted")) {
                    friendsListManage.addAll(currentUser.friends.get("accepted").keySet());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        firebaseDatabase.getReference().child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child: dataSnapshot.getChildren()){
                    //fetch all the users
                    User user = child.getValue(User.class);
                    if(friendsListManage.contains(user.getUser_id())) {
                        userListManage.add(user);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return userListManage;
    }

    @Override
    public ArrayList<User> getPendingRequestsUserList() {
        userListPending = new ArrayList<>();
        friendsListPending = new ArrayList<>();

        firebaseDatabase.getReference().child("users").child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User currentUser = dataSnapshot.getValue(User.class);
                if(currentUser.friends.containsKey("pending")) {

                    Iterator it = currentUser.friends.get("pending").entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry pair = (Map.Entry)it.next();
                        if(pair.getValue().equals("d")) {
                            friendsListPending.add((String) pair.getKey());
                        }
                        it.remove(); // avoids a ConcurrentModificationException
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        firebaseDatabase.getReference().child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child: dataSnapshot.getChildren()){
                    //fetch all the users
                    User user = child.getValue(User.class);
                    if(friendsListPending.contains(user.getUser_id())) {
                        userListPending.add(user);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return userListPending;
    }

    @Override
    public ArrayList<User> getAddFriendUserList() {
        userListAdd = new ArrayList<>();
        friendsListAdd = new ArrayList<>();

        firebaseDatabase.getReference().child("users").child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User currentUser = dataSnapshot.getValue(User.class);
                if(currentUser.friends.containsKey("pending")) {
                    friendsListAdd.addAll(currentUser.friends.get("pending").keySet());
                }
                if(currentUser.friends.containsKey("accepted")) {
                    friendsListAdd.addAll(currentUser.friends.get("accepted").keySet());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        firebaseDatabase.getReference().child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child: dataSnapshot.getChildren()){
                    //fetch all the users
                    User user = child.getValue(User.class);
                    if(!friendsListAdd.contains(user.getUser_id()) && !user.getUser_id().equals(firebaseAuth.getCurrentUser().getUid())) {
                        userListAdd.add(user);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return userListAdd;
    }
}
