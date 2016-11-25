package com.sap.josh0207.sap2016;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

public class IProposalDetailActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private TextView fbLink,Price,content,stat;
    private ImageView img,FbImg;
    private String pId, fbid;
    private Button sub, rej;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iproposal_detail);
    }
}
