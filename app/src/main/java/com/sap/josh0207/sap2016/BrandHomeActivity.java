package com.sap.josh0207.sap2016;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sap.josh0207.sap2016.fragment.BrandCampaignFragment;
import com.sap.josh0207.sap2016.fragment.BrandNotificationsFragment;
import com.sap.josh0207.sap2016.fragment.BrandSettingsFragment;
import com.squareup.picasso.Picasso;

import java.net.URL;

import static com.facebook.FacebookSdk.getApplicationContext;

public class BrandHomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mdatabase;
    private View nvHeader;
    private TextView merchantEmail, merchantName;
    private ImageView profile;
    private NavigationView navigationView;
    private DrawerLayout drawer ;
    private Toolbar toolbar;

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_CAMPAIGN = "campaigns";
    private static final String TAG_NOTIFICATIONS = "notifications";
    private static final String TAG_SETTINGS = "settings";
    public static String CURRENT_TAG = TAG_CAMPAIGN;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private boolean LoadHomeFragOnBackPress = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_brand__home);
        setTitle("My Campaign");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BrandCampaignFragment campaign = new BrandCampaignFragment();
        FragmentManager manager_campaign = getSupportFragmentManager();
        manager_campaign.beginTransaction().setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out).
                replace(R.id.content_brand__home,campaign,CURRENT_TAG).commit();


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);


        //Firebase start checking user login
        mAuth = FirebaseAuth.getInstance();

        mdatabase = FirebaseDatabase.getInstance().getReference().child("Merchant");

        if (mAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(getApplicationContext(), BrandLoginActivity.class));
        }

        //Navigation Header
        nvHeader = navigationView.getHeaderView(0);
        profile = (ImageView) nvHeader.findViewById(R.id.img_profile);
        merchantEmail = (TextView) nvHeader.findViewById(R.id.tv_MerchantEmail);
        merchantName = (TextView) nvHeader.findViewById(R.id.tv_MerchantName);

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        //Set menu dot
        navigationView.getMenu().getItem(1).setActionView(R.layout.menu_dot);

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        //Firebase get current user
        FirebaseUser merchant = mAuth.getCurrentUser();
        String uid = merchant.getUid();

        //Load merchant profile
        DatabaseReference current_merchant_pic = mdatabase.child(uid).child("image");

        current_merchant_pic.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String URL = dataSnapshot.getValue(String.class);
                if(URL!=null) {
                    Picasso.with(getApplicationContext()).load(URL).into(profile);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Load merchant first name
        DatabaseReference current_user_first_name = mdatabase.child(uid).child("first_name");

        current_user_first_name.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String first_name = dataSnapshot.getValue(String.class);
                merchantName.setText("Welcome, " + first_name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //set Navigation header email

        merchantEmail.setText(merchant.getEmail());

        //Initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_CAMPAIGN;
            loadHomeFragment();
        }
    }

    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();
            return;
        }

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }


    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView(){
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_BrandMyCampaign:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_CAMPAIGN;
                        BrandCampaignFragment campaign = new BrandCampaignFragment();
                        FragmentManager manager_campaign = getSupportFragmentManager();
                        manager_campaign.beginTransaction().setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out).
                                replace(R.id.content_brand__home,campaign,CURRENT_TAG).commit();
                        break;
                    case R.id.nav_BrandNotifications:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_NOTIFICATIONS;
                        BrandNotificationsFragment notifications = new BrandNotificationsFragment();
                        FragmentManager manager_notifications = getSupportFragmentManager();
                        manager_notifications.beginTransaction().setCustomAnimations(android.R.anim.fade_in,
                                android.R.anim.fade_out).replace(R.id.content_brand__home,notifications,CURRENT_TAG).commit();
                        break;
                    case R.id.nav_BrandSettings:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_SETTINGS;
                        BrandSettingsFragment settings = new BrandSettingsFragment();
                        FragmentManager manager_settings = getSupportFragmentManager();
                        manager_settings.beginTransaction().replace(R.id.content_brand__home,settings,CURRENT_TAG).commit();
                        break;
                    case R.id.nav_AboutUs:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(BrandHomeActivity.this, AboutUsActivity.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_PrivacyPolicy:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(BrandHomeActivity.this, PrivacyPolicyActivity.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_FAQ:
                        startActivity(new Intent(BrandHomeActivity.this,FaqActivity.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_ContactUs:
                        startActivity(new Intent(BrandHomeActivity.this,ContactUsActivity.class));
                        drawer.closeDrawers();
                        return true;
                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        if (LoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_CAMPAIGN;
                loadHomeFragment();
                return;
            }
        }
            super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.brand__home_, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Logout");
            builder.setMessage("Are You Sure Want To Logout?");

            builder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                    mAuth.signOut();
                    LoginManager.getInstance().logOut();
                    finish();
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                }
            });

            builder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            builder.show();
            return true;
        }

        if(id == R.id.nav_BrandAddCampaign){
            Intent i = new Intent(getApplicationContext(),AddCampaignActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}




