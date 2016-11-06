package com.sap.josh0207.sap2016;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BrandLoginActivity extends Activity {

    private TextView linkToRegister,ForgetPass,linkToHome;
    private Button btn_login;
    private EditText email, password;

    private FirebaseAuth mAuth;

    private ProgressDialog mProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_brand_login);

        mAuth = FirebaseAuth.getInstance();

        mProgress = new ProgressDialog(this);


        linkToRegister = (TextView) findViewById(R.id.link_to_register);
        linkToHome = (TextView)findViewById(R.id.link_to_home);
        btn_login = (Button) findViewById(R.id.btn_Login);
        email = (EditText) findViewById(R.id.et_email);
        password = (EditText) findViewById(R.id.et_pass);
        ForgetPass=(TextView)findViewById(R.id.tv_BrandForgetPass);


        //check user already login
        if (mAuth.getCurrentUser() != null && mAuth.getCurrentUser().isEmailVerified()==true) {
            startActivity(new Intent(getApplicationContext(), BrandHomeActivity.class));
            finish();
        }


        // Link to Register Screen
        linkToRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), BrandRegisterActivity.class);
                startActivity(i);
                finish();
            }
        });

        //Link to Homepage
        linkToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                finish();;
            }
        });

        // Login Button Event
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    checkLogin();
            }
        });

        //Foret Pass
        ForgetPass.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(getApplicationContext(),BrandResetPasswordActivity.class));
                finish();
            }
        });
    }

    private void checkLogin() {
        final String brand_email = email.getText().toString().trim();
        String pass = password.getText().toString().trim();

        if (TextUtils.isEmpty(brand_email)) {
            Toast.makeText(getApplicationContext(), "Please enter email", Toast.LENGTH_LONG).show();
        } else if (!brand_email.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")) {
            Toast.makeText(getApplicationContext(), "Please enter valid email address", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(pass)) {
            Toast.makeText(getApplicationContext(), "Please enter password", Toast.LENGTH_LONG).show();
        } else if (!TextUtils.isEmpty(brand_email) && !TextUtils.isEmpty(pass)) {
            mProgress.setMessage("Signing in...");
            mProgress.show();
            mAuth.signInWithEmailAndPassword(brand_email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        mProgress.dismiss();
                        Toast.makeText(BrandLoginActivity.this, "Invalid Email or Password", Toast.LENGTH_LONG).show();
                        password.setText("");
                    }
                    else if(!mAuth.getCurrentUser().isEmailVerified()) {
                        mProgress.dismiss();
                        Toast.makeText(BrandLoginActivity.this, "Email unverified, verify email have sent to your email " + mAuth.getCurrentUser().getEmail(), Toast.LENGTH_LONG).show();
                        mAuth.getCurrentUser().sendEmailVerification();
                    }
                    else if (task.isSuccessful()) {
                        mProgress.dismiss();
                        finish();
                        startActivity(new Intent(getApplicationContext(), BrandHomeActivity.class));
                    }
                }
            });
        }
    }
}



