package com.sap.josh0207.sap2016.fragment;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sap.josh0207.sap2016.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import static android.app.Activity.RESULT_OK;

public class BrandSettingsFragment extends Fragment {
    private ImageButton setting_profile_pic;

    private FirebaseAuth mAuth;
    private DatabaseReference mdatabase;

    private static final int GALLERY_REQUEST = 1;

    private ArrayAdapter<String> listAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_brand_settings,container,false);

        setting_profile_pic = (ImageButton)view.findViewById(R.id.setting_merchant_profile_pic);

        //ListView
        ListView listView = (ListView)view.findViewById(R.id.setting_listView);

        String[] choice = new String[]{"Set Profile","Add Payment Detail"};

        ArrayList<String> choiceList = new ArrayList<String>();
        choiceList.addAll(Arrays.asList(choice));

        listAdapter = new ArrayAdapter<String>(getActivity(),R.layout.simple_row,choiceList);

        listView.setAdapter(listAdapter);

        //Merchant
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser merchant = mAuth.getCurrentUser();

        String uid = merchant.getUid();

        setting_profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);
            }
        });

    return view;
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {

            Uri imageUri = data.getData();

            CropImage.activity(imageUri).start(getContext(),this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                Uri resultUri = result.getUri();

                setting_profile_pic.setImageURI(resultUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
            }
        }
    }
}
