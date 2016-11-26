package com.sap.josh0207.sap2016;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sap.josh0207.sap2016.fragment.BrandSettingsFragment;
import com.squareup.picasso.Picasso;

public class BProposalDetailActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private TextView fbLink,Price,content,stat;
    private ImageView img,FbImg;
    private String pId, fbid;
    private Button sub, rej;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bproposal_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.proposal_detail_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Proposal Detail");

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Proposal");

        pId = getIntent().getStringExtra("Proposal_ID");
        fbid = getIntent().getStringExtra("FbId");

        fbLink = (TextView)findViewById(R.id.inf_fb_id);
        Price =(TextView)findViewById(R.id.proposal_price);
        content = (TextView)findViewById(R.id.proposal_content);
        img = (ImageView)findViewById(R.id.proposal_image);
        FbImg = (ImageView)findViewById(R.id.FB_image);
        stat = (TextView)findViewById(R.id.tv_status);
        sub= (Button)findViewById(R.id.approve);
        rej = (Button)findViewById(R.id.reject);

        FbImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent("android.intent.action.VIEW", Uri.parse("https://facebook.com/"+fbid));
                startActivity(i);
            }
        });

        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Approve();
            }
        });

        rej.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Reject();
            }
        });

        mDatabase.child(pId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String fbI = (String)dataSnapshot.child("influencerID").getValue();
                Picasso.with(BProposalDetailActivity.this).load("https://graph.facebook.com/" + fbI + "/picture?height=800").into(FbImg);

                fbLink.setText("Click On Image To Redirect To Influencer Profile");

                String pr = (String)dataSnapshot.child("price").getValue();
                Price.setText("RM "+ pr);

                String c = (String)dataSnapshot.child("content").getValue();
                content.setText(c);

                String PImg = (String)dataSnapshot.child("photoURL").getValue();
                Picasso.with(BProposalDetailActivity.this).load(PImg).into(img);

                String st = (String)dataSnapshot.child("statusCode").getValue();
                if(st.equals("1")) {
                    stat.setText("Pending");
                }else if(st.equals("2")){
                    stat.setText("Approve");
                    sub.setEnabled(false);
                    rej.setEnabled(false);
                }else if(st.equals("3")) {
                    stat.setText("Reject");
                    sub.setEnabled(false);
                    rej.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void Approve(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm").setCancelable(false);
        builder.setMessage("Are you Sure To Approve This Campaign?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseReference db = mDatabase.child(pId);
                db.child("statusCode").setValue("2");
                Toast.makeText(getApplicationContext(),"This proposal have been Approve, Influencer will Post On facebook As Soon As Possible",Toast.LENGTH_LONG).show();
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    public void Reject(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm").setCancelable(false);
        builder.setMessage("Are you Sure To Approve This Campaign?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseReference db = mDatabase.child(pId);
                db.child("statusCode").setValue("3");
                Toast.makeText(getApplicationContext(),"This proposal have been rejected",Toast.LENGTH_LONG).show();
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}
