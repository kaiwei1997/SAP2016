package com.sap.josh0207.sap2016.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import com.google.android.gms.vision.text.Text;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sap.josh0207.sap2016.BCampaignDetailActivity;
import com.sap.josh0207.sap2016.Campaign;
import com.sap.josh0207.sap2016.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BrandCampaignFragment extends Fragment {

    private RecyclerView mCampaignList;
    private DatabaseReference mDatabase;
    private String s, Uid,id;
    private Spinner status;
    private FirebaseAuth mAuth;
    List<String> list_status;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_brand_campaign, container, false);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser Merchant = mAuth.getCurrentUser();
        Uid = Merchant.getUid().toString();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Campaign");

        mCampaignList = (RecyclerView)view.findViewById(R.id.campaign_list);

        mCampaignList.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        status = (Spinner)view.findViewById(R.id.spinner_status);

        list_status = new ArrayList<String>();
        list_status.add("All Campaign");
        list_status.add("Expired Campaign");
        list_status.add("Removed Campaign");
        list_status.add("Suspend Campaign");

        ArrayAdapter<String> adp = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, list_status);

        status.setAdapter(adp);

        status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               if(i == 0){
                   onStart();
               }else if(i == 1){
                   FirebaseRecyclerAdapter<Campaign,CampaignViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Campaign,CampaignViewHolder>(
                           Campaign.class,
                           R.layout.campaign_row,
                           CampaignViewHolder.class,
                           mDatabase
                   ){
                       @Override
                       protected void populateViewHolder(CampaignViewHolder viewHolder,Campaign model, int position) {
                           SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                           Calendar c = Calendar.getInstance();
                           String formatDate = df.format(c.getTimeInMillis());
                           id = model.getMerchant_id().toString();
                           s = model.getExpired();
                           if (s.equals(formatDate) && id.equals(Uid)) {
                               viewHolder.setCampaignName(model.getCampaignName());
                               viewHolder.setObjective(model.getObjective());
                               viewHolder.setHeroImage(getActivity().getApplicationContext(), model.getHero_image());
                           }else{
                               viewHolder.mView.setVisibility(View.GONE);
                           }
                       }
                   };
                   mCampaignList.setAdapter(firebaseRecyclerAdapter);
               }else if (i == 2){
                   FirebaseRecyclerAdapter<Campaign,CampaignViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Campaign,CampaignViewHolder>(
                           Campaign.class,
                           R.layout.campaign_row,
                           CampaignViewHolder.class,
                           mDatabase
                   ){
                       @Override
                       protected void populateViewHolder(CampaignViewHolder viewHolder,Campaign model, int position) {
                           s = model.getStatusCode();
                           id = model.getMerchant_id().toString();
                           if (s.equals("2") && id.equals(Uid)) {
                               viewHolder.setCampaignName(model.getCampaignName());
                               viewHolder.setObjective(model.getObjective());
                               viewHolder.setHeroImage(getActivity().getApplicationContext(), model.getHero_image());
                           }else{
                               viewHolder.mView.setVisibility(View.GONE);
                           }
                       }
                   };
                   mCampaignList.setAdapter(firebaseRecyclerAdapter);
               }else  if (i == 3){
                   FirebaseRecyclerAdapter<Campaign,CampaignViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Campaign,CampaignViewHolder>(
                           Campaign.class,
                           R.layout.campaign_row,
                           CampaignViewHolder.class,
                           mDatabase
                   ){
                       @Override
                       protected void populateViewHolder(CampaignViewHolder viewHolder,Campaign model, int position) {
                           s = model.getStatusCode();
                           id = model.getMerchant_id().toString();
                           if (s.equals("3") && id.equals(Uid)) {
                               viewHolder.setCampaignName(model.getCampaignName());
                               viewHolder.setObjective(model.getObjective());
                               viewHolder.setHeroImage(getActivity().getApplicationContext(), model.getHero_image());
                           }else{
                               viewHolder.mView.setVisibility(View.GONE);
                           }
                       }
                   };
                   mCampaignList.setAdapter(firebaseRecyclerAdapter);
               }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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
            protected void populateViewHolder(CampaignViewHolder viewHolder,Campaign model, int position) {
                final String post_id = getRef(position).getKey();
                s = model.getMerchant_id().toString();
                if (s.equals(Uid)) {
                    viewHolder.setCampaignName(model.getCampaignName());
                    viewHolder.setObjective(model.getObjective());
                    viewHolder.setHeroImage(getActivity().getApplicationContext(), model.getHero_image());
                }else{
                    viewHolder.mView.setVisibility(View.GONE);
                }

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(view.getContext(), BCampaignDetailActivity.class);
                        i.putExtra("Id",post_id);
                        view.getContext().startActivity(i);
                    }
                });
            }
        };
        mCampaignList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class CampaignViewHolder extends RecyclerView.ViewHolder{

        View mView;
        TextView campaign_title,campaign_obj;
        ImageView heroImageView;

        public CampaignViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setCampaignName(String campaignName){
            campaign_title= (TextView) mView.findViewById(R.id.campaign_title);
            campaign_title.setText (campaignName);
        }

        public void setObjective (String objective) {
            campaign_obj = (TextView) mView.findViewById(R.id.campaign_desc);
            campaign_obj.setText(objective);
        }

        public void setHeroImage(Context ctx, String heroImage){
            heroImageView = (ImageView)mView.findViewById(R.id.campaign_image);
            Picasso.with(ctx).load(heroImage).into(heroImageView);
            }

        }
    }
