package com.freelancher.bdtrainstracker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;

public class SendSMSActivity extends AppCompatActivity {
    private InterstitialAd interstitialAd ;
    private final String TAG = MainActivity.class.getSimpleName();

    private long backPressedTime;

    TextView nametext,codetext;
    Button sendsms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sms);
        nametext = findViewById(R.id.finalTrainname);
        codetext = findViewById(R.id.finalTraincode);
        sendsms = findViewById(R.id.btnsend);

        Intent intent = getIntent();

        String massage1 = intent.getStringExtra("name");
        String massage2 = intent.getStringExtra("code");
        nametext.setText(massage1);
        codetext.setText(massage2);

        sendsms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                int checkPermission = ContextCompat.checkSelfPermission(
                        SendSMSActivity.this, Manifest.permission.SEND_SMS);
                if (checkPermission == PackageManager.PERMISSION_GRANTED){

                    mymassage();
                }else{
                    ActivityCompat.requestPermissions(SendSMSActivity.this,
                            new String[]{Manifest.permission.SEND_SMS},0);
                }

            }


        });





        // adview();



    }
    private void mymassage() {

        try{
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("16318", null, codetext.getText().toString(), null, null);
            Toast.makeText(SendSMSActivity.this, "SMS Sent Successfully", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            Toast.makeText(SendSMSActivity.this, "SMS Failed to Send, Please try again", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case 0 :
                if (grantResults.length>=0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    mymassage();
                }else{
                    Toast.makeText(SendSMSActivity.this,"You don't have the permission",Toast.LENGTH_LONG).show();
                }
        }
    }


    @Override
    public void onBackPressed() {

        if (backPressedTime + 1000 >System.currentTimeMillis()){
            super.onBackPressed();
        }else{
            adview();
        }




        backPressedTime = System.currentTimeMillis();
    }

    @Override
    protected void onDestroy() {
        if (interstitialAd != null) {
            interstitialAd.destroy();
        }
        super.onDestroy();
    }



    public void adview(){

        interstitialAd = new InterstitialAd(this, "2273826056191900_2273828169525022");
        // Set listeners for the Interstitial Ad
        interstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
                Log.e(TAG, "Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
                Log.e(TAG, "Interstitial ad dismissed.");
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
                Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
                Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
                // Show the ad
                interstitialAd.show();
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
                Log.d(TAG, "Interstitial ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
                Log.d(TAG, "Interstitial ad impression logged!");
            }
        });

        // For auto play video ads, it's recommended to load the ad
        // at least 30 seconds before it is shown
        interstitialAd.loadAd();
    }

}
