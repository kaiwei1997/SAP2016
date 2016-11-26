package com.sap.josh0207.sap2016;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.facebook.FacebookSdk.*;

public class IProposalDetailActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private TextView Price,content,stat;
    private ImageView img;
    private String pId,c,PImg,st,fbid;
    private Button post;
    private CallbackManager callbackManager;
    private ShareDialog shareDialog;

    private static final String FACEBOOK_APPID = "677246852429690";
    private static final String FACEBOOK_PERMISSION = "publish_stream";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iproposal_detail);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.proposal_detail_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Proposal Detail");


        mDatabase = FirebaseDatabase.getInstance().getReference().child("Proposal");

        pId = getIntent().getStringExtra("Proposal_ID");
        fbid = getIntent().getStringExtra("FbId");

        Price =(TextView)findViewById(R.id.proposal_price);
        content = (TextView)findViewById(R.id.proposal_content);
        img = (ImageView)findViewById(R.id.proposal_image);
        stat = (TextView)findViewById(R.id.tv_status);
        post= (Button)findViewById(R.id.post);

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareLinkContent content = new ShareLinkContent.Builder()
                        .setImageUrl(Uri.parse(PImg))
                        .setContentDescription(c)
                        .setContentTitle("Social Advertisement Platform")
                        .setQuote(c)
                        .build();
                shareDialog.show(content);
            }
        });

        mDatabase.child(pId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String pr = (String)dataSnapshot.child("price").getValue();
                Price.setText("RM "+ pr);

                c = (String)dataSnapshot.child("content").getValue();
                content.setText(c);

                PImg = (String)dataSnapshot.child("photoURL").getValue();
                Picasso.with(IProposalDetailActivity.this).load(PImg).into(img);

                st = (String)dataSnapshot.child("statusCode").getValue();
                if(st.equals("1")) {
                    stat.setText("Pending");
                    post.setEnabled(false);
                }else if(st.equals("2")){
                    stat.setText("Approve");
                    post.setEnabled(true);
                }else if(st.equals("3")) {
                    stat.setText("Reject");
                    post.setEnabled(false);
                }else if(st.equals("4")){
                    stat.setText("Published");
                    post.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            DatabaseReference db = mDatabase.child(pId);
            db.child("statusCode").setValue("4");
            Toast.makeText(getApplicationContext(),"Successfully Share",Toast.LENGTH_LONG).show();
        }
    }

}
