package com.sap.josh0207.sap2016;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ProposalPostActivity extends AppCompatActivity {
    private String uid="", mid, cid,content1,price1,proposalImageDownload;
    private EditText content,price;
    private ImageButton proposal;
    private Button submit;
    private DatabaseReference mDatabase;
    private StorageReference mImage;
    private ProgressDialog mProgress;
    private FirebaseUser user;
    private static final int REQUEST_CAMERA = 1;
    private static final int GALLERY_REQUEST = 2;

    private Uri Image_proposal_Uri = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proposal_post);
        mid = getIntent().getStringExtra("Mid");
        cid = getIntent().getStringExtra("ID");

        mProgress = new ProgressDialog(this);

        mImage = FirebaseStorage.getInstance().getReference();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Proposal");
        content = (EditText)findViewById(R.id.cont);
        price = (EditText)findViewById(R.id.price);

        proposal= (ImageButton)findViewById(R.id.img_proposal);

        submit = (Button)findViewById(R.id.Submit);

        proposal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPost();
            }
        });
    }
    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ProposalPostActivity.this);
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
    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), GALLERY_REQUEST);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == GALLERY_REQUEST) {
                Image_proposal_Uri = data.getData();
                proposal.setImageURI(Image_proposal_Uri);

            } else if (requestCode == REQUEST_CAMERA) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                    Image_proposal_Uri = Uri.fromFile(destination);
                    proposal.setImageURI(Image_proposal_Uri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void addPost(){
        content1 = content.getText().toString().trim();
        price1 = price.getText().toString().trim();
         if(Image_proposal_Uri == null){
             Toast.makeText(getApplicationContext(), "Please upload Image", Toast.LENGTH_LONG).show();
         }else if(content1==null){
             Toast.makeText(getApplicationContext(), "Please enter content", Toast.LENGTH_LONG).show();
        }else if(price1==null){
             Toast.makeText(getApplicationContext(), "Please enter price", Toast.LENGTH_LONG).show();
         }else{
             mProgress.setMessage("Creating Campaign");
             mProgress.show();
             final StorageReference heroFilePath = mImage.child("Proposal").child(Image_proposal_Uri.getPath());
             final DatabaseReference newProposal = mDatabase.push();
             heroFilePath.putFile(Image_proposal_Uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                 @Override
                 public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                     proposalImageDownload = taskSnapshot.getDownloadUrl().toString();
                     if (proposalImageDownload != null) {
                         Picasso.with(getApplicationContext()).load(proposalImageDownload).into(proposal);
                         newProposal.child("photoURL").setValue(proposalImageDownload);
                     } else if (proposalImageDownload == null) {
                         Toast.makeText(getApplicationContext(), "Please upload Hero Image", Toast.LENGTH_LONG).show();
                     }
                 }
             });
             newProposal.child("campaignID").setValue(cid);
             newProposal.child("merchantID").setValue(mid);
             user = FirebaseAuth.getInstance().getCurrentUser();
             for(UserInfo profile: user.getProviderData()){
                 //check is it the provider id matches "facebook.com"
                 if(profile.getProviderId().equals(getString(R.string.facebook_provider_id))){
                     uid = profile.getUid();
                 }
             }
             newProposal.child("influencerID").setValue(uid);
             newProposal.child("content").setValue(content1);
             newProposal.child("price").setValue(price1);
             newProposal.child("statusCode").setValue("1");
             mProgress.dismiss();
             android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ProposalPostActivity.this);
             builder.setMessage("Successfully Create The Proposal, Merchant Will Give Response in Short Time, Please Check It Later")
                     .setCancelable(false);
             builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                 @Override
                 public void onClick(DialogInterface dialogInterface, int i) {
                     dialogInterface.dismiss();
                     finish();
                 }
             });
             builder.show();
         }
    }
}