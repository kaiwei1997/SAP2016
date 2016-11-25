package com.sap.josh0207.sap2016.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sap.josh0207.sap2016.BCampaignDetailActivity;
import com.sap.josh0207.sap2016.Campaign;
import com.sap.josh0207.sap2016.ICampaignDetailActivity;
import com.sap.josh0207.sap2016.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class InfCampaignFragment extends Fragment {
    private RecyclerView mCampaignList;
    private DatabaseReference mDatabase;
    private String id, categoryS;
    private Spinner category;
    private FirebaseAuth mAuth;
    List<String> list_category;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inf_campaign, container, false);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Campaign");
        mCampaignList = (RecyclerView)view.findViewById(R.id.icampaign_list);
        mCampaignList.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        category = (Spinner)view.findViewById(R.id.spinner_category);

        list_category = new ArrayList<String>();
        list_category.add("All");
        list_category.add("Animals");
        list_category.add("Automotive");
        list_category.add("Beauty & Personal Care");
        list_category.add("Business,Finance & Insurance");
        list_category.add("Children & Family");
        list_category.add("Education & Book");
        list_category.add("Entertainment & Events");
        list_category.add("Fashion");
        list_category.add("Food & Drinks");
        list_category.add("Health, Fitness & Sport");
        list_category.add("Home & Garden");
        list_category.add("Photography, Arts & Design");
        list_category.add("Restaurant, Bars & Hotel");
        list_category.add("Social Enterprise & Not-for-Profit");
        list_category.add("Social Media, Web, Tech");
        list_category.add("Travel & Destinations");

        ArrayAdapter<String> adp = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, list_category);

        category.setAdapter(adp);

        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, final View view, int i, long l) {
                if(i==0){
                    onStart();
                }
                else if(i ==1){
                    FirebaseRecyclerAdapter<Campaign,InfCampaignFragment.CampaignViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Campaign,InfCampaignFragment.CampaignViewHolder>(
                            Campaign.class,
                            R.layout.campaign_row,
                            InfCampaignFragment.CampaignViewHolder.class,
                            mDatabase
                    ){
                        @Override
                        protected void populateViewHolder(InfCampaignFragment.CampaignViewHolder viewHolder, Campaign model, int position) {
                            categoryS = model.getCategory().toString();
                            if(categoryS.equals("Animals")) {
                                viewHolder.setCampaignName(model.getCampaignName());
                                viewHolder.setObjective(model.getObjective());
                                viewHolder.setHeroImage(getActivity().getApplicationContext(), model.getHero_image());
                            }else{
                                viewHolder.setCampaignName("NA");
                                viewHolder.setObjective("NA");
                                viewHolder.setHeroImage(getActivity().getApplicationContext(),"null");
                            }
                        }
                    };
                    mCampaignList.setAdapter(firebaseRecyclerAdapter);
                }else if(i ==2){
                    FirebaseRecyclerAdapter<Campaign,InfCampaignFragment.CampaignViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Campaign,InfCampaignFragment.CampaignViewHolder>(
                            Campaign.class,
                            R.layout.campaign_row,
                            InfCampaignFragment.CampaignViewHolder.class,
                            mDatabase
                    ){
                        @Override
                        protected void populateViewHolder(InfCampaignFragment.CampaignViewHolder viewHolder, Campaign model, int position) {
                            categoryS = model.getCategory().toString();
                            if(categoryS.equals("Automotive")) {
                                viewHolder.setCampaignName(model.getCampaignName());
                                viewHolder.setObjective(model.getObjective());
                                viewHolder.setHeroImage(getActivity().getApplicationContext(), model.getHero_image());
                            }else{
                                viewHolder.setCampaignName("NA");
                                viewHolder.setObjective("NA");
                                viewHolder.setHeroImage(getActivity().getApplicationContext(),"null");
                            }
                        }

                    };
                    mCampaignList.setAdapter(firebaseRecyclerAdapter);
                }else if(i ==3){
                    FirebaseRecyclerAdapter<Campaign,InfCampaignFragment.CampaignViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Campaign,InfCampaignFragment.CampaignViewHolder>(
                            Campaign.class,
                            R.layout.campaign_row,
                            InfCampaignFragment.CampaignViewHolder.class,
                            mDatabase
                    ){
                        @Override
                        protected void populateViewHolder(InfCampaignFragment.CampaignViewHolder viewHolder, Campaign model, int position) {
                            categoryS = model.getCategory().toString();
                            if(categoryS.equals("Beauty & Personal Care")) {
                                viewHolder.setCampaignName(model.getCampaignName());
                                viewHolder.setObjective(model.getObjective());
                                viewHolder.setHeroImage(getActivity().getApplicationContext(), model.getHero_image());
                            }else{
                                viewHolder.setCampaignName("NA");
                                viewHolder.setObjective("NA");
                                viewHolder.setHeroImage(getActivity().getApplicationContext(),"null");
                            }
                        }
                    };
                    mCampaignList.setAdapter(firebaseRecyclerAdapter);
                }else if(i ==4) {
                    FirebaseRecyclerAdapter<Campaign, InfCampaignFragment.CampaignViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Campaign, InfCampaignFragment.CampaignViewHolder>(
                            Campaign.class,
                            R.layout.campaign_row,
                            InfCampaignFragment.CampaignViewHolder.class,
                            mDatabase
                    ) {
                        @Override
                        protected void populateViewHolder(InfCampaignFragment.CampaignViewHolder viewHolder, Campaign model, int position) {
                            categoryS = model.getCategory().toString();
                            if (categoryS.equals("Business,Finance & Insurance")) {
                                viewHolder.setCampaignName(model.getCampaignName());
                                viewHolder.setObjective(model.getObjective());
                                viewHolder.setHeroImage(getActivity().getApplicationContext(), model.getHero_image());
                            } else {
                                viewHolder.setCampaignName("NA");
                                viewHolder.setObjective("NA");
                                viewHolder.setHeroImage(getActivity().getApplicationContext(), "null");
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

        onStart();

        return view;
    }
    @Override
    public void onStart(){
        super.onStart();

        FirebaseRecyclerAdapter<Campaign,InfCampaignFragment.CampaignViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Campaign,InfCampaignFragment.CampaignViewHolder>(
                Campaign.class,
                R.layout.campaign_row,
                InfCampaignFragment.CampaignViewHolder.class,
                mDatabase
        ){
            @Override
            protected void populateViewHolder(InfCampaignFragment.CampaignViewHolder viewHolder, Campaign model,int position) {
                final String post_id = getRef(position).getKey();
                final String stat = model.getStatusCode();
                if(stat.equals("1")) {
                    viewHolder.setCampaignName(model.getCampaignName());
                    viewHolder.setObjective(model.getObjective());
                    viewHolder.setHeroImage(getActivity().getApplicationContext(), model.getHero_image());
                }

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(view.getContext(), ICampaignDetailActivity.class);
                        i.putExtra("ID",post_id);
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

            if(campaign_title.getText()=="NA"){
                campaign_title.setVisibility(View.GONE);
            }
        }

        public void setObjective (String objective) {
            campaign_obj = (TextView) mView.findViewById(R.id.campaign_desc);
            campaign_obj.setText(objective);

            if(campaign_obj.getText()=="NA"){
                campaign_obj.setVisibility(View.GONE);
            }
        }

        public void setHeroImage(Context ctx, String heroImage){
            heroImageView = (ImageView)mView.findViewById(R.id.campaign_image);
            Picasso.with(ctx).load(heroImage).into(heroImageView);

            if(heroImage.equals("Null")){
                heroImageView.setVisibility(View.GONE);
            }
        }

    }
}

