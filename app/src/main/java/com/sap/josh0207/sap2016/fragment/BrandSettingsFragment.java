package com.sap.josh0207.sap2016.fragment;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sap.josh0207.sap2016.AddCampaignActivity;
import com.sap.josh0207.sap2016.BrandDetailActivity;
import com.sap.josh0207.sap2016.InfLoginActivity;
import com.sap.josh0207.sap2016.R;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import static android.app.Activity.RESULT_OK;

public class BrandSettingsFragment extends Fragment {
    private ImageButton setting_profile_pic;

    private FirebaseAuth mAuth;
    private DatabaseReference mdatabase;
    private StorageReference mStorageImage;
    private Uri mImageUri = null;
    private String uid;

    private static final int GALLERY_REQUEST = 1;
    private static final int REQUEST_CAMERA =2;

    private ArrayAdapter<String> listAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_brand_settings,container,false);


        mAuth = FirebaseAuth.getInstance();

        //Firebase user
        FirebaseUser Merchant = mAuth.getCurrentUser();
        uid = Merchant.getUid();

        //Load database
        mdatabase = FirebaseDatabase.getInstance().getReference().child("Merchant");

        //Load Storage
        mStorageImage = FirebaseStorage.getInstance().getReference().child("Merchant_Profile_Pic");

        //Brand Profile Pic
        setting_profile_pic = (ImageButton)view.findViewById(R.id.setting_merchant_profile_pic);
        DatabaseReference current_merchant_pic = mdatabase.child(uid).child("logo_image");

        current_merchant_pic.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String URL = dataSnapshot.getValue(String.class);
                if(URL!=null) {
                    Picasso.with(getActivity()).load(URL).into(setting_profile_pic);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //ListView
        ListView listView = (ListView)view.findViewById(R.id.setting_listView);

        String[] choice = new String[]{"Set Profile Detail","Add Payment Detail"};

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
                        Intent i = new Intent(getActivity().getApplicationContext(), BrandDetailActivity.class);
                        startActivity(i);
                        break;

                    case 1:

                        break;

                    default:

                        break;
                }
            }
        });

        setting_profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

    return view;
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"),GALLERY_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {

            Uri imageUri = data.getData();

            CropImage.activity(imageUri).setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1,1).start(getContext(),this);

        }else if(requestCode == REQUEST_CAMERA && resultCode == RESULT_OK){
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
            File destination = new File(Environment.getExternalStorageDirectory(),
                    System.currentTimeMillis() + ".jpg");
            Uri uri = Uri.fromFile(destination);
            CropImage.activity(uri).setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1,1).start(getContext(),this);
            FileOutputStream fo;
            try {
                destination.createNewFile();
                fo = new FileOutputStream(destination);
                fo.write(bytes.toByteArray());
                fo.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                mImageUri = result.getUri();

                setting_profile_pic.setImageURI(mImageUri);

                StorageReference filePath = mStorageImage.child(mImageUri.getLastPathSegment());

                filePath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String downloadUrl = taskSnapshot.getDownloadUrl().toString();

                        Picasso.with(getActivity()).load(downloadUrl).into(setting_profile_pic);

                        mAuth = FirebaseAuth.getInstance();
                        DatabaseReference current_user_db = mdatabase.child(uid);

                        current_user_db.child("logo_image").setValue(downloadUrl);


                        Toast.makeText(getActivity().getApplicationContext(),"Upload Successful",Toast.LENGTH_SHORT).show();
                    }
                });



            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
            }
        }
    }
}

