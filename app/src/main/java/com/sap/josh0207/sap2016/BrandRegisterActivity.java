package com.sap.josh0207.sap2016;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class BrandRegisterActivity extends Activity{
    private EditText first_name,last_name,company_name,brand_email,contactNo,password,pass_confirm;
    private Button btn_register;
    private TextView link_to_login;
    private CheckBox checkBox;
    private ImageButton logo;

    private FirebaseAuth mAuth;

    private DatabaseReference mdatabase;

    private ProgressDialog mProgress;

    private static final int REQUEST_CAMERA = 1;
    private static final int GALLERY_REQUEST = 2;

    private Uri Image_logo_Uri = null;

    private StorageReference mImage;

    private String logoDownloadUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_brand_register);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        mProgress = new ProgressDialog(this);

        mdatabase = FirebaseDatabase.getInstance().getReference().child("Merchant");

        mImage = FirebaseStorage.getInstance().getReference();

        first_name = (EditText) findViewById(R.id.et_firstname);
        last_name = (EditText) findViewById(R.id.et_lastname);
        company_name = (EditText) findViewById(R.id.et_companyname);
        brand_email = (EditText) findViewById(R.id.et_email);
        contactNo = (EditText) findViewById(R.id.et_contactNo);
        password = (EditText) findViewById(R.id.et_pass);
        pass_confirm = (EditText) findViewById(R.id.et_passConfirm);
        checkBox = (CheckBox)findViewById(R.id.checkbox1);
        btn_register = (Button) findViewById(R.id.btnRegister);
        link_to_login = (TextView) findViewById(R.id.link_to_login);

        logo = (ImageButton)findViewById(R.id.merchant_logo);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startBrandRegister();
            }
        });

        // Link to login click event
        link_to_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
                Intent i = new Intent(BrandRegisterActivity.this,BrandLoginActivity.class);
                startActivity(i);
            }
        });

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(BrandRegisterActivity.this);
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
                Image_logo_Uri = data.getData();
                logo.setImageURI(Image_logo_Uri);

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
                    Image_logo_Uri = Uri.fromFile(destination);
                    logo.setImageURI(Image_logo_Uri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void startBrandRegister() {
        final String f_name = first_name.getText().toString().trim();
        final String l_name = last_name.getText().toString().trim();
        final String c_name = company_name.getText().toString().trim();
        String email = brand_email.getText().toString().trim();
        final String contact = contactNo.getText().toString().trim();
        String pass = password.getText().toString().trim();

        if(Image_logo_Uri==null){
            Toast.makeText(getApplicationContext(), "Please insert logo image", Toast.LENGTH_LONG).show();
        }
        else if (TextUtils.isEmpty(f_name)) {
            Toast.makeText(getApplicationContext(), "Please enter your first name", Toast.LENGTH_LONG).show();
            first_name.setFocusable(true);
        } else if (TextUtils.isEmpty(l_name)) {
            Toast.makeText(getApplicationContext(), "Please enter your last name", Toast.LENGTH_LONG).show();
            last_name.setFocusable(true);
        } else if (TextUtils.isEmpty(c_name)) {
            Toast.makeText(getApplicationContext(), "Please enter your company name", Toast.LENGTH_LONG).show();
            company_name.setFocusable(true);
        } else if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Please enter your email address", Toast.LENGTH_LONG).show();
            brand_email.setFocusable(true);
        } else if (!email.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")) {
            Toast.makeText(getApplicationContext(), "Please enter valid email address", Toast.LENGTH_LONG).show();
            brand_email.setFocusable(true);
        } else if (TextUtils.isEmpty(contact)) {
            Toast.makeText(getApplicationContext(), "Please enter your contact number", Toast.LENGTH_LONG).show();
            contactNo.setFocusable(true);
        } else if (contact.length() > 11 || contact.length() < 10) {
            Toast.makeText(getApplicationContext(), "Please enter valid contact number", Toast.LENGTH_LONG).show();
            contactNo.setFocusable(true);
        } else if (TextUtils.isEmpty(pass)) {
            Toast.makeText(getApplicationContext(), "Please enter your password", Toast.LENGTH_LONG).show();
            password.setFocusable(true);
        } else if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), "Password at least with 6 character", Toast.LENGTH_LONG).show();
            password.setFocusable(true);
        } else if (!password.getText().toString().equals(pass_confirm.getText().toString())) {
            Toast.makeText(getApplicationContext(), "The confirmation password does not match", Toast.LENGTH_LONG).show();
            pass_confirm.setText("");
            pass_confirm.setFocusable(true);
        } else if (!checkBox.isChecked()) {
            Toast.makeText(getApplicationContext(), "You must agree to the Terms of Use and Privacy Statement", Toast.LENGTH_LONG).show();
            checkBox.setFocusable(true);
        } else {
            mProgress.setMessage("Signing Up...");
            mProgress.show();
            mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Toast.makeText(BrandRegisterActivity.this, "Merchant Signup:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                    if (!task.isSuccessful()) {
                        mProgress.dismiss();
                        Toast.makeText(BrandRegisterActivity.this, "Register failed." + task.getException(),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        String userId = mAuth.getCurrentUser().getUid();

                        final DatabaseReference current_user_db = mdatabase.child(userId);

                        final StorageReference logoFilePath = mImage.child("Merchant_Logo").child(Image_logo_Uri.getPath());

                        logoFilePath.putFile(Image_logo_Uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                logoDownloadUri = taskSnapshot.getDownloadUrl().toString();
                                if (logoDownloadUri != null) {
                                    Picasso.with(getApplicationContext()).load(logoDownloadUri).into(logo);
                                    current_user_db.child("logo_image").setValue(logoDownloadUri);
                                } else if (logoDownloadUri == null) {
                                    Toast.makeText(getApplicationContext(), "Please upload Hero Image", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                        current_user_db.child("first_name").setValue(f_name);
                        current_user_db.child("last_name").setValue(l_name);
                        current_user_db.child("company_name").setValue(c_name);
                        current_user_db.child("contact_no").setValue(contact);

                        mAuth.getCurrentUser().sendEmailVerification();
                        mProgress.dismiss();
                        Toast.makeText(getApplicationContext(), "Register Successful,verify email have sent to " + mAuth.getCurrentUser().getEmail(), Toast.LENGTH_LONG).show();
                        Intent loginIntent = new Intent(BrandRegisterActivity.this, BrandLoginActivity.class);
                        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(loginIntent);
                        finish();
                    }

                }
            });
        }
    }
}
