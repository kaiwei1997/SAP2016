package com.sap.josh0207.sap2016;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class CampaignAddPhotoActivity extends AppCompatActivity {

    private ImageButton m1,m2,m3;

    private static final int GALLERY_REQUEST_M1 = 1;
    private static final int GALLERY_REQUEST_M2 = 2;
    private static final int GALLERY_REQUEST_M3 = 3;

    private FirebaseAuth mAuth;

    private Uri m1Uri = null;
    private Uri m2Uri = null;
    private Uri m3Uri = null;

    private DatabaseReference mDatabase, mood;

    private StorageReference mStrorage;

    private Button upload;

    private ProgressDialog mProgress;

    private String m1DownloadUri = null;
    private String m2DownloadUri = null;
    private String m3DownloadUri = null;

    private String post_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign_add_photo);

        Toolbar toolbar = (Toolbar) findViewById(R.id.add_photo_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Add Photo for Campaign Mood Board");

        Intent i = getIntent();

        post_id= i.getStringExtra("post_id");

        mAuth = FirebaseAuth.getInstance();

        mProgress = new ProgressDialog(this);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Campaign");

        mStrorage = FirebaseStorage.getInstance().getReference();


        m1 = (ImageButton)findViewById(R.id.btn_m1);
        m2 = (ImageButton)findViewById(R.id.btn_m2);
        m3 = (ImageButton)findViewById(R.id.btn_m3);

        upload = (Button)findViewById(R.id.btn_upload);

        m1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                galleryIntentm1();
            }
        });

        m2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                galleryIntentm2();
            }
        });

        m3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                galleryIntentm3();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadMood();
            }
        });
    }
    private void galleryIntentm1() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), GALLERY_REQUEST_M1);
    }

    private void galleryIntentm2() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), GALLERY_REQUEST_M2);
    }

    private void galleryIntentm3() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), GALLERY_REQUEST_M3);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == GALLERY_REQUEST_M1) {
                m1Uri = data.getData();
                m1.setImageURI(m1Uri);
            } else if (requestCode == GALLERY_REQUEST_M2) {
                m2Uri = data.getData();
                m2.setImageURI(m2Uri);
            } else if (requestCode == GALLERY_REQUEST_M3) {
                m3Uri = data.getData();
                m3.setImageURI(m3Uri);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    public void uploadMood(){
        mood = mDatabase.child(post_id);

        if(m1Uri == null & m2Uri==null & m3Uri == null){
            Toast.makeText(getApplicationContext(),"Must upload at least one picture",Toast.LENGTH_LONG).show();
        }else if(m1Uri == null){
            Toast.makeText(getApplicationContext(),"Must upload at least one picture",Toast.LENGTH_LONG).show();
        }
        if(m1Uri != null) {
            StorageReference moodFilePath1 = mStrorage.child("Campaign").child("Mood").child(m1Uri.getPath());
            moodFilePath1.putFile(m1Uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    m1DownloadUri = taskSnapshot.getDownloadUrl().toString();
                    if(m1DownloadUri!=null){
                        Picasso.with(getApplicationContext()).load(m1DownloadUri).into(m1);
                        mood.child("mood1").setValue(m1DownloadUri);
                    }
                }
            });
        }

        if(m2Uri != null) {
            StorageReference moodFilePath2 = mStrorage.child("Campaign").child("Mood").child(m2Uri.getPath());
            moodFilePath2.putFile(m2Uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    m2DownloadUri = taskSnapshot.getDownloadUrl().toString();
                    if (m2DownloadUri != null) {
                        Picasso.with(getApplicationContext()).load(m2DownloadUri).into(m2);
                        mood.child("mood2").setValue(m2DownloadUri);
                    }
                }
            });
        }

        if(m3Uri != null){
            StorageReference moodFilePath3 = mStrorage.child("Campaign").child("Mood").child(m3Uri.getPath());
            moodFilePath3.putFile(m3Uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    m3DownloadUri = taskSnapshot.getDownloadUrl().toString();
                    if(m3DownloadUri!=null){
                        Picasso.with(getApplicationContext()).load(m3DownloadUri).into(m3);
                        mood.child("mood3").setValue(m3DownloadUri);
                    }
                }
            });
        }
        if(m1Uri!=null || m2Uri!=null ||m3Uri!=null){
                mProgress.setMessage("Creating Campaign");
                mProgress.show();
                mProgress.dismiss();
                Toast.makeText(getApplicationContext(), "Upload Successful", Toast.LENGTH_LONG).show();
                finish();
                Intent i = new Intent(getApplicationContext(), BrandHomeActivity.class);
                startActivity(i);
            }
        }


    }

