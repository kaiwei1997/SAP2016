package com.sap.josh0207.sap2016.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.sap.josh0207.sap2016.R;
import com.squareup.picasso.Picasso;

public class InfSettingsFragment extends Fragment {

    private String facebookUserId = "";
    private FirebaseUser user;
    private FirebaseAuth mAuth;

    private ImageView profilePicture;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inf_settings, container, false);

        profilePicture = (ImageView)view.findViewById(R.id.setting_inf_profile_pic);

        user = FirebaseAuth.getInstance().getCurrentUser();
        for(UserInfo profile: user.getProviderData()){
            //check is it the provider id matches "facebok.com"
            if(profile.getProviderId().equals(getString(R.string.facebook_provider_id))){
                facebookUserId = profile.getUid();
            }
        }

        //construct the URL to the profile picture, with a custom height
        String photoUrl = "https://graph.facebook.com/" + facebookUserId + "/picture?height=800";

        //Picasso download and show to image
        Picasso.with(getActivity()).load(photoUrl).into(profilePicture);

        return view;
    }
}
