package com.sap.josh0207.sap2016.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.sap.josh0207.sap2016.BrandDetailActivity;
import com.sap.josh0207.sap2016.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import static android.R.attr.data;

public class InfSettingsFragment extends Fragment {

    private String facebookUserId = "";
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private TextView f_total;
    private ArrayAdapter<String> listAdapter;


    private ImageView profilePicture;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inf_settings, container, false);

        //ListView
        ListView listView = (ListView)view.findViewById(R.id.inf_setting_listView);
        String[] choice = new String[]{"Rate Card","My Profile","Payment Detail","Select my interests"};

        ArrayList<String> choiceList = new ArrayList<String>();
        choiceList.addAll(Arrays.asList(choice));

        listAdapter = new ArrayAdapter<String>(getActivity(),R.layout.simple_row,choiceList);

        listView.setAdapter(listAdapter);


        //set onClickListener for list view
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:

                        break;

                    case 1:

                        break;

                    case 2:

                        break;

                    case 3:

                        break;

                    default:

                        break;
                }
            }
        });

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

        //construct the URL to the friend list
        new GraphRequest(AccessToken.getCurrentAccessToken(),"/"+facebookUserId+"/friends",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        String data = response.getJSONObject().toString();

                    }
                }
        ).executeAsync();

        return view;
    }
}
