package com.sap.josh0207.sap2016;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class BProposalDetailActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private TextView fbLink,Price,content,stat;
    private ImageView img,FbImg;
    private String pId;
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

        fbLink = (TextView)findViewById(R.id.inf_fb_id);
        Price =(TextView)findViewById(R.id.proposal_price);
        content = (TextView)findViewById(R.id.proposal_content);
        img = (ImageView)findViewById(R.id.proposal_image);
        FbImg = (ImageView)findViewById(R.id.FB_image);
        stat = (TextView)findViewById(R.id.tv_status);

        mDatabase.child(pId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String fbI = (String)dataSnapshot.child("influencerID").getValue();
                Picasso.with(BProposalDetailActivity.this).load("https://graph.facebook.com/" + fbI + "/picture?height=800").into(FbImg);

                String fbL = (String)dataSnapshot.child("influencerID").getValue();
                fbLink.setText("https://facebook.com/"+fbL);

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
                }else if(st.equals("3")) {
                    stat.setText("Reject");
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
}
