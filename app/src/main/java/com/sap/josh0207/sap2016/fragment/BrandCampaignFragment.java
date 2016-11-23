package com.sap.josh0207.sap2016.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.nearby.internal.connection.dev.SendPayloadParams;
import com.google.android.gms.vision.text.Text;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sap.josh0207.sap2016.Campaign;
import com.sap.josh0207.sap2016.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class BrandCampaignFragment extends Fragment {

    private RecyclerView mCampaignList;
    private DatabaseReference mDatabase;
    private String s;
    private Spinner status;

    List<String> list_status;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_brand_campaign, container, false);



        mDatabase = FirebaseDatabase.getInstance().getReference().child("Campaign");

        mCampaignList = (RecyclerView)view.findViewById(R.id.campaign_list);

        mCampaignList.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        status = (Spinner)view.findViewById(R.id.spinner_status);

        list_status = new ArrayList<String>();
        list_status.add("All Campaign");
        list_status.add("Expired Campaign");
        list_status.add("Removed Campaign");

        ArrayAdapter<String> adp = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, list_status);

        status.setAdapter(adp);

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
                    viewHolder.setHeroImage(getActivity().getApplicationContext(), model.getHero_image());
                }
        };
        mCampaignList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class CampaignViewHolder extends RecyclerView.ViewHolder{

        View mView;
        TextView campaign_title,campaign_desc;
        ImageView heroImageView;

        public CampaignViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setCampaignName(String campaignName){
            campaign_title= (TextView) mView.findViewById(R.id.campaign_title);
            campaign_title.setText (campaignName);
        }

        public void setDescription (String description) {
            campaign_desc = (TextView) mView.findViewById(R.id.campaign_desc);
            campaign_desc.setText(description);
        }

        public void setHeroImage(Context ctx, String heroImage){
            heroImageView = (ImageView)mView.findViewById(R.id.campaign_image);
            Picasso.with(ctx).load(heroImage).into(heroImageView);
            }

        }

    }
