package com.example.sharangirdhani.homework07;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class AddNewFriendFragment extends Fragment implements AddNewFriendsAdapter.IFriendAddInterface {

    private OnFragmentInteractionListener mListener;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private List<User> userList;
    private List<String> friendsList;
    private User currentUser;
    AddNewFriendsAdapter addNewFriendsAdapter;
    LinearLayoutManager layoutManager;

    public AddNewFriendFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        Toast.makeText(this.getContext(), "Add New Friend Fragment called", Toast.LENGTH_LONG).show();
        View view = inflater.inflate(R.layout.fragment_add_new_friend, container, false);

        userList = mListener.getAddFriendUserList();

        addNewFriendsAdapter = new AddNewFriendsAdapter(getActivity(), userList, this);

        RecyclerView rvAddFriends = ((RecyclerView)view.findViewById(R.id.rv_addNewFriend));
        rvAddFriends.setAdapter(addNewFriendsAdapter);
        layoutManager = new LinearLayoutManager(getActivity());
        rvAddFriends.setLayoutManager(layoutManager);
        addNewFriendsAdapter.notifyDataSetChanged();
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void friendRequestSent(User user) {
        String myId = firebaseAuth.getCurrentUser().getUid();
        String userId = user.getUser_id();
        firebaseDatabase.getReference().child("users").child(myId).child("friends").child("pending").child(userId).setValue("s");
        firebaseDatabase.getReference().child("users").child(userId).child("friends").child("pending").child(myId).setValue("d");
        userList.remove(user);
        addNewFriendsAdapter.notifyDataSetChanged();
        Toast.makeText(getContext(), "Friend request sent", Toast.LENGTH_LONG).show();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
        ArrayList<User> getAddFriendUserList();
    }

    interface IActivity {
        ArrayList<User> getUserList();
    }
}
