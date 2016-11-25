package com.sap.josh0207.sap2016.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sap.josh0207.sap2016.BCampaignDetailActivity;
import com.sap.josh0207.sap2016.Campaign;
import com.sap.josh0207.sap2016.Proposal;
import com.sap.josh0207.sap2016.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class BrandProposalListFragment extends Fragment {
    private RecyclerView mCampaignList;
    private DatabaseReference mDatabase;
    private String s, Uid;
    private Spinner status;
    private FirebaseAuth mAuth;
    List<String> list_status;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_brand_proposal_list, container, false);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser Merchant = mAuth.getCurrentUser();
        Uid = Merchant.getUid().toString();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Proposal");

        mCampaignList = (RecyclerView) view.findViewById(R.id.bProposal_list);

        mCampaignList.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        status = (Spinner) view.findViewById(R.id.spinner_category);

        list_status = new ArrayList<String>();
        list_status.add("All Proposal");
        list_status.add("Pending Proposal");
        list_status.add("Approved Proposal");
        list_status.add("Published Proposal");

        ArrayAdapter<String> adp1 = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, list_status);

        status.setAdapter(adp1);

        return view;
    }
    @Override
    public void onStart(){
        super.onStart();

        FirebaseRecyclerAdapter<Proposal,BrandProposalListFragment.ProposalViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Proposal,ProposalViewHolder>(
                Proposal.class,
                R.layout.proposal_row,
                BrandProposalListFragment.ProposalViewHolder.class,
                mDatabase
        ){
            @Override
            protected void populateViewHolder(BrandProposalListFragment.ProposalViewHolder viewHolder, Proposal model, int position) {
                s = model.getMerchantID().toString();
                if (s.equals(Uid)) {
                    model.getInfluencerID();
                    viewHolder.setPrice(model.getPrice());
                    viewHolder.setContent(model.getContent());
                    viewHolder.setProposalImage(getActivity().getApplicationContext(),model.getPhotoURL());
                }else{
                    viewHolder.mView.setVisibility(View.GONE);
                }

            }
        };
        mCampaignList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class ProposalViewHolder extends RecyclerView.ViewHolder{

        View mView;
        TextView link,proposal_Price,cont;
        ImageView proposalImageView;

        public ProposalViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setPrice(String campaignName){
            proposal_Price= (TextView) mView.findViewById(R.id.proposal_price);
            proposal_Price.setText (campaignName);
        }

        public void setContent (String content) {
            cont = (TextView) mView.findViewById(R.id.proposal_content);
            cont.setText(content);
        }

        public void setProposalImage(Context ctx, String proposalImage){
            proposalImageView = (ImageView)mView.findViewById(R.id.proposal_image);
            Picasso.with(ctx).load(proposalImage).into(proposalImageView);
        }

    }
}
