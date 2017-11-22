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
 * {@link PendingRequestFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class PendingRequestFragment extends Fragment implements PendingRequestAdapter.IPendingRequestInterface{

    private OnFragmentInteractionListener mListener;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private List<User> userList;
    private User currentUser;
    PendingRequestAdapter pendingRequestAdapter;
    LinearLayoutManager layoutManager;

    public PendingRequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        Toast.makeText(this.getContext(), "Pending Request Fragment called", Toast.LENGTH_LONG).show();
        View view = inflater.inflate(R.layout.fragment_pending_request, container, false);

        userList = mListener.getPendingRequestsUserList();

        pendingRequestAdapter = new PendingRequestAdapter(getActivity(), userList, this);

        RecyclerView rvPendingRequests = ((RecyclerView)view.findViewById(R.id.rv_pendingRequests));
        rvPendingRequests.setAdapter(pendingRequestAdapter);
        layoutManager = new LinearLayoutManager(getActivity());
        rvPendingRequests.setLayoutManager(layoutManager);
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
    public void pendingRequestAccepted(User user) {
        String myId = firebaseAuth.getCurrentUser().getUid();
        String userId = user.getUser_id();
        firebaseDatabase.getReference().child("users").child(myId).child("friends").child("accepted").child(userId).setValue(userId);
        firebaseDatabase.getReference().child("users").child(userId).child("friends").child("accepted").child(myId).setValue(myId);
        firebaseDatabase.getReference().child("users").child(myId).child("friends").child("pending").child(userId).setValue(null);
        firebaseDatabase.getReference().child("users").child(userId).child("friends").child("pending").child(myId).setValue(null);
        userList.remove(user);
        pendingRequestAdapter.notifyDataSetChanged();
        Toast.makeText(getContext(), "Request Accepted", Toast.LENGTH_LONG).show();
    }

    @Override
    public void pendingRequestRejected(User user) {
        String myId = firebaseAuth.getCurrentUser().getUid();
        String userId = user.getUser_id();
        firebaseDatabase.getReference().child("users").child(myId).child("friends").child("pending").child(userId).setValue(null);
        firebaseDatabase.getReference().child("users").child(userId).child("friends").child("pending").child(myId).setValue(null);
        userList.remove(user);
        pendingRequestAdapter.notifyDataSetChanged();
        Toast.makeText(getContext(), "Request Declined", Toast.LENGTH_LONG).show();
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
        ArrayList<User> getPendingRequestsUserList();
    }
}
