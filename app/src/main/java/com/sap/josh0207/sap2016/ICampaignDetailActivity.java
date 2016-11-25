package com.sap.josh0207.sap2016;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ICampaignDetailActivity extends AppCompatActivity {
    private DatabaseReference mDatabase, mDatabase1,mDatabase2;
    private TextView brandName,campaignName,objective,descrip,tc,getProduct,cont;
    private ImageView brandLogo,m1,m2,m3;
    private Button create,report;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgress;
    private String id,Uid, merchantId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icampaign_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.campaign_detail_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Brief");

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser inf = mAuth.getCurrentUser();
        Uid = inf.getUid().toString();

        mProgress = new ProgressDialog(this);

        brandLogo = (ImageView)findViewById(R.id.IV_BrandLogo);
        brandName = (TextView)findViewById(R.id.tv_brandName);
        campaignName = (TextView)findViewById(R.id.tv_Title);
        objective = (TextView)findViewById(R.id.et_objective);
        descrip = (TextView)findViewById(R.id.Desc);
        tc = (TextView)findViewById(R.id.et_tc);
        cont = (TextView)findViewById(R.id.Content);
        getProduct = (TextView)findViewById(R.id.et_getProduct);
        m1 = (ImageView)findViewById(R.id.mood1);
        m2 = (ImageView)findViewById(R.id.mood2);
        m3 = (ImageView)findViewById(R.id.mood3);
        create = (Button)findViewById(R.id.btn_Create);
        report = (Button)findViewById(R.id.btn_Report);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Campaign");
        mDatabase1 = FirebaseDatabase.getInstance().getReference().child("Merchant");
        mDatabase2 = FirebaseDatabase.getInstance().getReference().child("Report_Campaign");

        id = getIntent().getStringExtra("ID");

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(),ProposalPostActivity.class);
                i.putExtra("ID",id);
                i.putExtra("Uid",Uid);
                i.putExtra("Mid",merchantId);
                view.getContext().startActivity(i);
            }
        });

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReportCampaign();
            }
        });

        mDatabase.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                merchantId = (String)dataSnapshot.child("merchant_id").getValue();
                mDatabase1.child(merchantId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String logo = (String)dataSnapshot.child("logo_image").getValue();
                        Picasso.with(ICampaignDetailActivity.this).load(logo).into(brandLogo);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                String brand = (String)dataSnapshot.child("brandName").getValue();
                String title = (String)dataSnapshot.child("campaignName").getValue();
                String objc = (String)dataSnapshot.child("objective").getValue();
                String description = (String)dataSnapshot.child("description").getValue();
                String Content = (String)dataSnapshot.child("content").getValue();
                String term = (String)dataSnapshot.child("tc").getValue();
                String get_pro = (String)dataSnapshot.child("get_product").getValue();
                String mo1 = (String)dataSnapshot.child("mood1").getValue();
                String mo2 = (String)dataSnapshot.child("mood2").getValue();
                String mo3 = (String)dataSnapshot.child("mood3").getValue();

                if(mo1=="") {
                    m1.setVisibility(View.INVISIBLE);
                }else if(mo1!=""){
                    Picasso.with(ICampaignDetailActivity.this).load(mo1).into(m1);
                }else if(mo2!="") {
                    Picasso.with(ICampaignDetailActivity.this).load(mo2).into(m2);
                }else if(mo3!="") {
                    Picasso.with(ICampaignDetailActivity.this).load(mo3).into(m3);
                }

                brandName.setText(brand);
                campaignName.setText(title);
                objective.setText(objc);
                descrip.setText(description);
                cont.setText(Content);
                tc.setText(term);
                getProduct.setText(get_pro);
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

    public void ReportCampaign(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm").setCancelable(false);
        builder.setMessage("Are you really want to report this campaign?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseReference db = mDatabase2.child("campaignID");
                Toast.makeText(getApplicationContext(),"This Campaign Have Been Report Successfully, Our Team Will review On it in 2 Working Days",Toast.LENGTH_LONG).show();
                db.setValue(id);
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
}
