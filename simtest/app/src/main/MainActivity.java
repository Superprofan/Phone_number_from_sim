package com.superprofan.simtest;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final int PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    //String TAG = "PhoneActivityTAG";
    Activity activity = MainActivity.this;
    String wantPermission = Manifest.permission.READ_PHONE_STATE;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final String TAG = "myLogs";
    TextView tvPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Log.i(TAG, "NEW START 11111111: ");



        tvPhoneNumber = findViewById(R.id.tv_phonenumber);
/*
        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,null,null, null);
        while (phones.moveToNext())
        {
            String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }
        phones.close();
*/
        requestContactPermission();


        Log.i(TAG, "STARRRRRRRT222222222222: ");


    }




    private void allSIMContact()
    {

        try
        {
            String m_simPhonename;
            String m_simphoneNo;



            Uri simUri = Uri.parse("content://icc/adn");

            Cursor cursorSim = getContentResolver().query(simUri,null,null,null,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
            //Log.i(TAG, "STARRRRRRRT33333333333333333: ");
            Log.i("PhoneContact", "total: "+cursorSim.getCount());


            while (cursorSim.moveToNext())
            {
                m_simPhonename =cursorSim.getString(cursorSim.getColumnIndex("name"));
                m_simphoneNo = cursorSim.getString(cursorSim.getColumnIndex("number"));
                m_simphoneNo.replaceAll("\\D","");
                m_simphoneNo.replaceAll("&", "");
                m_simPhonename=m_simPhonename.replace("|","");

               // Log.i("PhoneContact", "name: " + m_simPhonename + " phone: " + m_simphoneNo);
               // Log.i("CHECK","something happened here");

                //if m_simPhonename =="myNumber" - > Log.i
                if (cursorSim.getString(cursorSim.getColumnIndex("name")).equals("MyNumber")) {
                    Log.i("PhoneContact", "name: " + m_simPhonename + " phone: " + m_simphoneNo);
                    Log.i("CHECK","something happened here");

                    tvPhoneNumber.setText(m_simPhonename+" "+m_simphoneNo);
                    break;
                }
            }
            cursorSim.close();

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }




    public void requestContactPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        android.Manifest.permission.READ_CONTACTS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Read contacts access needed");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage("Please enable access to contacts.");
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            requestPermissions(
                                    new String[]
                                            {android.Manifest.permission.READ_CONTACTS}
                                    , PERMISSIONS_REQUEST_READ_CONTACTS);
                        }
                    });
                    builder.show();
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{android.Manifest.permission.READ_CONTACTS},
                            PERMISSIONS_REQUEST_READ_CONTACTS);
                }
            } else {
                allSIMContact();
            }
        } else {
            allSIMContact();
        }
    }


}