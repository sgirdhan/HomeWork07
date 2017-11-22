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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ManageFriendsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ManageFriendsFragment extends Fragment implements ManageFriendsAdapter.IManageFriendsInterface{

    private OnFragmentInteractionListener mListener;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private List<User> userList;
    private User currentUser;
    ManageFriendsAdapter manageFriendsAdapter;
    LinearLayoutManager layoutManager;

    public ManageFriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_manage_friends, container, false);

        userList = mListener.getManageFriendsList();

        manageFriendsAdapter = new ManageFriendsAdapter(getActivity(), userList, this);

        RecyclerView rvManageFriends = ((RecyclerView)view.findViewById(R.id.rv_friends));
        rvManageFriends.setAdapter(manageFriendsAdapter);
        layoutManager = new LinearLayoutManager(getActivity());
        rvManageFriends.setLayoutManager(layoutManager);
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
    public void friendDeleted(User user) {
        String myId = firebaseAuth.getCurrentUser().getUid();
        String userId = user.getUser_id();
        firebaseDatabase.getReference().child("users").child(myId).child("friends").child("accepted").child(userId).setValue(null);
        firebaseDatabase.getReference().child("users").child(userId).child("friends").child("accepted").child(myId).setValue(null);
        userList.remove(user);
        manageFriendsAdapter.notifyDataSetChanged();
        Toast.makeText(getContext(), "Friend removed", Toast.LENGTH_LONG).show();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
        ArrayList<User> getManageFriendsList();
    }
}
