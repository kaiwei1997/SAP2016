package com.sap.josh0207.sap2016;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.facebook.internal.Utility;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageOptions;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.R.attr.onClick;
import static java.security.AccessController.getContext;

public class AddCampaignActivity extends AppCompatActivity {

    private ImageButton HeroImageBtn;

    private static final int REQUEST_CAMERA = 1;
    private static final int GALLERY_REQUEST = 2;

    private Uri Image_hero_Uri = null;

    private Spinner category;

    private EditText brandName, campaignName, desc, link, getProduct, content, action, tc;

    private Button submit;

    private FirebaseAuth mAuth;

    private DatabaseReference mdatabase;

    private StorageReference mImage;

    private ProgressDialog mProgress;

    private String heroDownloadUri;

    private String uid, category_selected,post_id;

    List<String> list_category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_campaign);

        Toolbar toolbar = (Toolbar) findViewById(R.id.add_campaign_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Add Campaign");

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser Merchant = mAuth.getCurrentUser();
        uid = Merchant.getUid();

        mProgress = new ProgressDialog(this);

        mdatabase = FirebaseDatabase.getInstance().getReference().child("Campaign");

        mImage = FirebaseStorage.getInstance().getReference();

        HeroImageBtn = (ImageButton) findViewById(R.id.btn_hero_image);

        submit = (Button) findViewById(R.id.btn_submit);

        category = (Spinner) findViewById(R.id.category_spinner);

        list_category = new ArrayList<String>();
        list_category.add("");
        list_category.add("Animals");
        list_category.add("Automotive");
        list_category.add("Beauty & Personal Care");
        list_category.add("Business,Finance & Insurance");
        list_category.add("Children & Family");
        list_category.add("Education & Book");
        list_category.add("Entertainment & Events");
        list_category.add("Fashion");
        list_category.add("Food & Drinks");
        list_category.add("Health, Fitness & Sport");
        list_category.add("Home & Garden");
        list_category.add("Photography, Arts & Design");
        list_category.add("Restaurant, Bars & Hotel");
        list_category.add("Social Enterprise & Not-for-Profit");
        list_category.add("Social Media, Web, Tech");
        list_category.add("Travel & Destinations");

        ArrayAdapter<String> adp = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, list_category);

        category.setAdapter(adp);

        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category_selected = category.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {


            }
        });


        HeroImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgress.setMessage("Creating Campaign");
                mProgress.show();
                addCampaign();
            }
        });
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(AddCampaignActivity.this);
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
                Image_hero_Uri = data.getData();
                HeroImageBtn.setImageURI(Image_hero_Uri);

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
                    Image_hero_Uri = Uri.fromFile(destination);
                    HeroImageBtn.setImageURI(Image_hero_Uri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    public void addCampaign() {
        brandName = (EditText) findViewById(R.id.et_brand_name);
        campaignName = (EditText) findViewById(R.id.et_campaign_name);
        desc = (EditText) findViewById(R.id.et_product_desc);
        link = (EditText) findViewById(R.id.et_product_link);
        getProduct = (EditText) findViewById(R.id.et_get_product);
        content = (EditText) findViewById(R.id.et_content);
        action = (EditText) findViewById(R.id.et_action);
        tc = (EditText) findViewById(R.id.et_tC);

        final String brand_name = brandName.getText().toString().trim();
        final String campaign_name = campaignName.getText().toString().trim();
        final String description = desc.getText().toString().trim();
        final String product_link = link.getText().toString().trim();
        final String get_Product = getProduct.getText().toString().trim();
        final String get_content = content.getText().toString().trim();
        final String get_action = action.getText().toString().trim();
        final String term = tc.getText().toString().trim();

        if (Image_hero_Uri == null) {
            Toast.makeText(getApplicationContext(), "Please upload Hero Image", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(brand_name)) {
            Toast.makeText(getApplicationContext(), "Please enter Brand Name", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(campaign_name)) {
            Toast.makeText(getApplicationContext(), "Please enter campaign or product name", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(description)) {
            Toast.makeText(getApplicationContext(), "Please enter product description", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(product_link)) {
            Toast.makeText(getApplicationContext(), "Please enter product link", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(get_Product)) {
            Toast.makeText(getApplicationContext(), "Please enter how to get your pruduct", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(get_content)) {
            Toast.makeText(getApplicationContext(), "Please enter how you want the influencer content look like", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(get_action)) {
            Toast.makeText(getApplicationContext(), "Please enter what is the purpose of posts", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(term)) {
            Toast.makeText(getApplicationContext(), "Please enter term and condition for posts", Toast.LENGTH_LONG).show();
        } else {
            mProgress.setMessage("Creating Campaign");
            mProgress.show();
            final DatabaseReference newCampaign = mdatabase.push();
            final StorageReference heroFilePath = mImage.child("Campaign").child("HeroImage").child(Image_hero_Uri.getPath());

            heroFilePath.putFile(Image_hero_Uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    heroDownloadUri = taskSnapshot.getDownloadUrl().toString();
                    if (heroDownloadUri != null) {
                        Picasso.with(getApplicationContext()).load(heroDownloadUri).into(HeroImageBtn);
                        newCampaign.child("hero_image").setValue(heroDownloadUri);
                    } else if (heroDownloadUri == null) {
                        Toast.makeText(getApplicationContext(), "Please upload Hero Image", Toast.LENGTH_LONG).show();
                    }
                }
            });
            newCampaign.child("brandName").setValue(brand_name);
            newCampaign.child("campaignName").setValue(campaign_name);
            newCampaign.child("description").setValue(description);
            newCampaign.child("category").setValue(category_selected);
            newCampaign.child("link").setValue(product_link);
            newCampaign.child("get_product").setValue(get_Product);
            newCampaign.child("content").setValue(get_content);
            newCampaign.child("action").setValue(get_action);
            newCampaign.child("tc").setValue(term);
            newCampaign.child("merchant_id").setValue(uid);
            newCampaign.child("statusCode").setValue("1");

            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DATE,30);
            String formatDate = df.format(c.getTimeInMillis());
            newCampaign.child("expired").setValue(formatDate);

            post_id = newCampaign.getKey();
        }
        mProgress.dismiss();
        if (post_id != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirm").setCancelable(false);
            builder.setMessage("Your application have successfully send to our team, we will respond on it within 24 hours.\n" +
                    "Add more photo about your campaign?");

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                    Intent i = new Intent(getApplicationContext(), CampaignAddPhotoActivity.class);
                    i.putExtra("post_id", post_id);
                    startActivity(i);
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    dialog.dismiss();
                    Intent intent = new Intent(getApplicationContext(), BrandHomeActivity.class);
                    startActivity(intent);
                }
            });

            builder.show();
        }
    }
}


