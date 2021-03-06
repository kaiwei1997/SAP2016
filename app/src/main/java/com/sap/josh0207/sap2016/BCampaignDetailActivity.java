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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class BCampaignDetailActivity extends AppCompatActivity {

    private DatabaseReference mDatabase, mDatabase1;
    private TextView brandName,campaignName;
    private EditText objective,descrip,tc,getProduct,cont;
    private ImageView brandLogo,m1,m2,m3;
    private Button edit,close;
    private ProgressDialog mProgress;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bcampaign_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.campaign_detail_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Brief");

        mProgress = new ProgressDialog(this);

        brandLogo = (ImageView)findViewById(R.id.IV_BrandLogo);
        brandName = (TextView)findViewById(R.id.tv_brandName);
        campaignName = (TextView)findViewById(R.id.tv_Title);
        objective = (EditText)findViewById(R.id.et_objective);
        descrip = (EditText)findViewById(R.id.Desc);
        tc = (EditText)findViewById(R.id.et_tc);
        cont = (EditText)findViewById(R.id.Content);
        getProduct = (EditText)findViewById(R.id.et_getProduct);
        m1 = (ImageView)findViewById(R.id.mood1);
        m2 = (ImageView)findViewById(R.id.mood2);
        m3 = (ImageView)findViewById(R.id.mood3);
        edit = (Button)findViewById(R.id.btn_Edit);
        close = (Button)findViewById(R.id.btn_Close);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Campaign");
        mDatabase1 = FirebaseDatabase.getInstance().getReference().child("Merchant");

        id = getIntent().getStringExtra("Id");

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveEdit();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CloseCampaign();
            }
        });

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
                String Content = (String)dataSnapshot.child("content").getValue();
                String term = (String)dataSnapshot.child("tc").getValue();
                String get_pro = (String)dataSnapshot.child("get_product").getValue();
                String mo1 = (String)dataSnapshot.child("mood1").getValue();
                String mo2 = (String)dataSnapshot.child("mood2").getValue();
                String mo3 = (String)dataSnapshot.child("mood3").getValue();

                if(mo1=="") {
                    m1.setVisibility(View.INVISIBLE);
                }else if(mo1!=""){
                    Picasso.with(BCampaignDetailActivity.this).load(mo1).into(m1);
                }else if(mo2!="") {
                    Picasso.with(BCampaignDetailActivity.this).load(mo2).into(m2);
                }else if(mo3!="") {
                    Picasso.with(BCampaignDetailActivity.this).load(mo3).into(m3);
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

    public void SaveEdit(){
        String obj = objective.getText().toString().trim();
        String desc = descrip.getText().toString().trim();
        String con = cont.getText().toString().trim();
        String getPro = getProduct.getText().toString().trim();
        String tac = tc.getText().toString().trim();

        if(TextUtils.isEmpty(obj)){
            Toast.makeText(getApplicationContext(), "Objective Cannot Be Blank", Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(desc)){
            Toast.makeText(getApplicationContext(), "Description Cannot Be Blank", Toast.LENGTH_LONG).show();
        }else if(TextUtils.isEmpty(con)){
            Toast.makeText(getApplicationContext(), "Content Cannot Be Blank", Toast.LENGTH_LONG).show();
        }else if(TextUtils.isEmpty(getPro)){
            Toast.makeText(getApplicationContext(), "Get Product Cannot Be Blank", Toast.LENGTH_LONG).show();
        }else if(TextUtils.isEmpty(tac)){
            Toast.makeText(getApplicationContext(), "Term & Condition for post Cannot Be Blank", Toast.LENGTH_LONG).show();
        }else{
            mProgress.setMessage("Saving...");
            mProgress.show();
            DatabaseReference db = mDatabase.child(id);
            db.child("objective").setValue(obj);
            db.child("description").setValue(desc);
            db.child("content").setValue(con);
            db.child("get_product").setValue(getPro);
            db.child("tc").setValue(tac);
            Toast.makeText(getApplicationContext(),"Update Sucessfully",Toast.LENGTH_LONG).show();
        }
        mProgress.dismiss();
    }

    public void CloseCampaign(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm").setCancelable(false);
        builder.setMessage("Are you really want to close this campaign?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseReference db = mDatabase.child(id);
                db.child("statusCode").setValue("3");
                Toast.makeText(getApplicationContext(),"This campaign have been suspeend",Toast.LENGTH_LONG).show();
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
}
