package com.sap.josh0207.sap2016.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sap.josh0207.sap2016.Campaign;
import com.sap.josh0207.sap2016.R;

public class BrandCampaignFragment extends Fragment {

    private RecyclerView mCampaignList;
    private DatabaseReference mDatabase;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_brand_campaign, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Campaign");

        mCampaignList = (RecyclerView)view.findViewById(R.id.campaign_list);
        mCampaignList.setHasFixedSize(true);
        mCampaignList.setLayoutManager(new LinearLayoutManager(this.getActivity()));


        //Load AdMob
        MobileAds.initialize(getActivity(),"ca-app-pub-4516935382926964~7526464337");
        AdView mAdView = (AdView) view.findViewById(R.id.adView1);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        return view;
    }
    @Override
    public void onStart(){
        super.onStart();

        FirebaseRecyclerAdapter<Campaign,CampaignViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Campaign,CampaignViewHolder>(
                Campaign.class,
                R.layout.campaign_row,
                CampaignViewHolder.class,
                mDatabase
        ){
            @Override
            protected void populateViewHolder(CampaignViewHolder viewHolder,Campaign model, int position){
                viewHolder.setCampaignName(model.getCampaignName());
                viewHolder.setDescription(model.getDescription());
            }
        };
        mCampaignList.setAdapter(firebaseRecyclerAdapter);
    }
    public static class CampaignViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public CampaignViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setCampaignName(String campaignName){
            TextView campaign_title = (TextView) mView.findViewById(R.id.campaign_title);
            campaign_title.setText(campaignName);
        }

        public void setDescription (String description){
            TextView campaign_desc = (TextView)mView.findViewById(R.id.campaign_desc);
            campaign_desc.setText(description);
        }


    }
}
