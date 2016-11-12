package com.sap.josh0207.sap2016;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.facebook.FacebookSdk;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        Timer t = new Timer();
        boolean checkConnection = new ApplicationUtility().checkConnection(this);
        if (checkConnection) {
            t.schedule(new splash(), 3000);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
            builder.setMessage("No Internet Connection. Go to Settings?")
                    .setCancelable(false);
            builder.setNegativeButton("Settings",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                    startActivity(new Intent(Settings.ACTION_SETTINGS));
                }
            });

            builder.setNeutralButton("Retry",new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int id){
                    onRestart();
                }
            });

            builder.setPositiveButton("Exit",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    System.exit(0);
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }
    }
    protected void onRestart() {
        super.onRestart();
        Intent intent = new Intent(SplashActivity.this, SplashActivity.class);
        finish();
        startActivity(intent);
    }
    class splash extends TimerTask {

        @Override
        public void run() {
            Intent i = new Intent(SplashActivity.this, MainActivity.class);
            finish();
            startActivity(i);
        }
    }
}
