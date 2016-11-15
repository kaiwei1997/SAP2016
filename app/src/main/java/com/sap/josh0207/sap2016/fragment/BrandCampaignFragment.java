package com.sap.josh0207.sap2016.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.sap.josh0207.sap2016.R;

public class BrandCampaignFragment extends Fragment {

    private RecyclerView mCampaignList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_brand_campaign, container, false);

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
    }
    public static class CampaignViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public CampaignViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setTitle(String title){
            TextView campaign_title = (TextView) mView.findViewById(R.id.campaign_title);

        }
    }

}
