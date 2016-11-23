package com.sap.josh0207.sap2016;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class BCampaignDetailActivity extends AppCompatActivity {

    private DatabaseReference mDatabase, mDatabase1;
    private TextView brandName,campaignName;
    private EditText objective,description,term,getProduct;
    private ImageView brandLogo,m1,m2,m3;
    private Button edit,close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bcampaign_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.campaign_detail_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Brief");

        brandLogo = (ImageView)findViewById(R.id.IV_BrandLogo);
        brandName = (TextView)findViewById(R.id.tv_brandName);
        campaignName = (TextView)findViewById(R.id.tv_Title);
        objective = (EditText)findViewById(R.id.et_objective);
        description = (EditText)findViewById(R.id.Desc);
        term = (EditText)findViewById(R.id.et_tc);
        getProduct = (EditText)findViewById(R.id.et_getProduct);
        m1 = (ImageView)findViewById(R.id.mood1);
        m2 = (ImageView)findViewById(R.id.mood2);
        m3 = (ImageView)findViewById(R.id.mood3);
        edit = (Button)findViewById(R.id.btn_Edit);
        close = (Button)findViewById(R.id.btn_Close);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Campaign");
        mDatabase1 = FirebaseDatabase.getInstance().getReference().child("Merchant");

        String id = getIntent().getStringExtra("Id");

        mDatabase.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String merchantId = (String)dataSnapshot.child("merchant_id").getValue();
                mDatabase1.child(merchantId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String logo = (String)dataSnapshot.child("logo_image").getValue();
                        Picasso.with(BCampaignDetailActivity.this).load(logo).into(brandLogo);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                String brand = (String)dataSnapshot.child("brandName").getValue();
                String title = (String)dataSnapshot.child("campaignName").getValue();
                String objc = (String)dataSnapshot.child("objective").getValue();
                String description = (String)dataSnapshot.child("description").getValue();
                String term = (String)dataSnapshot.child("tc").getValue();
                String get_pro = (String)dataSnapshot.child("get_product").getValue();
                String mo1 = (String)dataSnapshot.child("mood1").getValue();
                if(mo1.equals(null)){
                    m1.setVisibility(View.INVISIBLE);
                }else{
                    Picasso.with(BCampaignDetailActivity.this).load(mo1).into(m1);
                    m1.setVisibility(View.VISIBLE);
                }

                brandName.setText(brand);
                campaignName.setText(title);
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
