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
import com.sap.josh0207.sap2016.BProposalDetailActivity;
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
        list_status.add("Reject Proposal");
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
            protected void populateViewHolder(BrandProposalListFragment.ProposalViewHolder viewHolder, final Proposal model, final int position) {
                final String proposal_id = getRef(position).getKey();
                s = model.getMerchantID().toString();
                if (s.equals(Uid)) {
                    viewHolder.setStatus(model.getStatusCode());
                    viewHolder.setFBImage(getActivity().getApplicationContext(),model.getInfluencerID());
                    viewHolder.setFBName(model.getFB_name());
                    viewHolder.setPrice(model.getPrice());
                    viewHolder.setContent(model.getContent());
                    viewHolder.setProposalImage(getActivity().getApplicationContext(),model.getPhotoURL());
                }else{
                    viewHolder.mView.setVisibility(View.GONE);
                }

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(view.getContext(),BProposalDetailActivity.class);
                        i.putExtra("Proposal_ID",proposal_id);
                        i.putExtra("FbId",model.getInfluencerID());
                        view.getContext().startActivity(i);
                    }
                });

            }
        };
        mCampaignList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class ProposalViewHolder extends RecyclerView.ViewHolder{

        View mView;
        TextView tvstatus,proposal_Price,cont,FBName;
        ImageView proposalImageView, FBImage;

        public ProposalViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setFBImage(Context ctx, String Url){
            FBImage = (ImageView)mView.findViewById(R.id.fb_image);
            Picasso.with(ctx).load("https://graph.facebook.com/" + Url + "/picture?height=800").into(FBImage);
        }

        public void setFBName(String name){
            FBName = (TextView)mView.findViewById(R.id.name);
            FBName.setText(name);
        }

        public void setPrice(String Price){
            proposal_Price= (TextView) mView.findViewById(R.id.proposal_price);
            proposal_Price.setText ("RM "+Price);
        }

        public void setContent (String content) {
            cont = (TextView) mView.findViewById(R.id.proposal_content);
            cont.setText(content);
        }

        public void setProposalImage(Context ctx, String proposalImage){
            proposalImageView = (ImageView)mView.findViewById(R.id.proposal_image);
            Picasso.with(ctx).load(proposalImage).into(proposalImageView);
        }

        public void setStatus(String status){
            tvstatus = (TextView)mView.findViewById(R.id.tv_status);
            if(status.equals("1")) {
                tvstatus.setText("Pending");
            }else if(status.equals("2")){
                tvstatus.setText("Approve");
            }else if(status.equals("3")) {
                tvstatus.setText("Reject");
            }
        }

    }
}
