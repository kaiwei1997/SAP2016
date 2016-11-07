package com.sap.josh0207.sap2016;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sap.josh0207.sap2016.fragment.InfCampaignFragment;
import com.sap.josh0207.sap2016.fragment.InfNotificationsFragment;
import com.sap.josh0207.sap2016.fragment.InfSettingsFragment;

public class InfHomeActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mdatabase;
    private View nvHeader;
    private TextView infEmail, infName;
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
        setContentView(R.layout.activity_inf_home);
        setTitle("My Campaign");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);


        //Firebase start checking user login
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(getApplicationContext(), InfLoginActivity.class));
        }


        //Navigation Header
        nvHeader = navigationView.getHeaderView(0);
        profile = (ImageView) nvHeader.findViewById(R.id.inf_img_profile);
        infEmail = (TextView) nvHeader.findViewById(R.id.tv_InfEmail);
        infName = (TextView) nvHeader.findViewById(R.id.tv_InfName);

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        //Set menu dot
        navigationView.getMenu().getItem(1).setActionView(R.layout.menu_dot);

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        infName.setText("Welcome, " + mAuth.getCurrentUser().getDisplayName());

        //set Navigation header email

        infEmail.setText(mAuth.getCurrentUser().getEmail());

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
                    case R.id.nav_InfMyCampaign:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_CAMPAIGN;
                        InfCampaignFragment campaign = new InfCampaignFragment();
                        FragmentManager manager_campaign = getSupportFragmentManager();
                        manager_campaign.beginTransaction().setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out).
                                replace(R.id.content_inf_home,campaign,CURRENT_TAG).commit();
                        break;
                    case R.id.nav_InfNotifications:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_NOTIFICATIONS;
                        InfNotificationsFragment notifications = new InfNotificationsFragment();
                        FragmentManager manager_notifications = getSupportFragmentManager();
                        manager_notifications.beginTransaction().setCustomAnimations(android.R.anim.fade_in,
                                android.R.anim.fade_out).replace(R.id.content_inf_home,notifications,CURRENT_TAG).commit();
                        break;
                    case R.id.nav_InfSettings:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_SETTINGS;
                        InfSettingsFragment settings = new InfSettingsFragment();
                        FragmentManager manager_settings = getSupportFragmentManager();
                        manager_settings.beginTransaction().replace(R.id.content_inf_home,settings,CURRENT_TAG).commit();
                        break;
                    case R.id.nav_AboutUs:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(InfHomeActivity.this, AboutUsActivity.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_PrivacyPolicy:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(InfHomeActivity.this, PrivacyPolicyActivity.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_FAQ:
                        startActivity(new Intent(InfHomeActivity.this,FaqActivity.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_ContactUs:
                        startActivity(new Intent(InfHomeActivity.this,ContactUsActivity.class));
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
            AlertDialog.Builder builder = new AlertDialog.Builder(InfHomeActivity.this);
            builder.setTitle("Logout");
            builder.setMessage("Are You Sure Want To Logout?")
                    .setCancelable(false);
            builder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            builder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                    mAuth.signOut();
                    LoginManager.getInstance().logOut();
                    Toast.makeText(getApplicationContext(),"User Logout",Toast.LENGTH_LONG).show();
                    finish();
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                }
            });

            builder.show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
