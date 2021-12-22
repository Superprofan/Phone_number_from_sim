package com.superprofan.simtest;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
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
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final int PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private final String CHANNEL_ID = "personal notifications";
    private final int NOTIFICATION_ID = 001;
    public String notificationText;
    public String notificationTitle;
    //String TAG = "PhoneActivityTAG";
    //Activity activity = MainActivity.this;
   // String wantPermission = Manifest.permission.READ_PHONE_STATE;
    //private static final int PERMISSION_REQUEST_CODE = 1;
    private static final String TAG = "myLogs";
    TextView tvPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Log.i(TAG, "NEW START 11111111: ");
        tvPhoneNumber = findViewById(R.id.tv_phonenumber);
        requestContactPermission();
        Log.i(TAG, "STARRRRRRRT222222222222: ");
    }




    private void allSIMContact()
    {

        try
        {
            String m_simPhonename;
            String m_simphoneNo;

            //читаем контакты в симке, считаем количество
            Uri simUri = Uri.parse("content://icc/adn");
            Cursor cursorSim = getContentResolver().query(simUri,null,null,null,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

            Log.i("PhoneContact", "total: "+cursorSim.getCount());

            //читаем контакты, имена и номера, чистим от мусора
            while (cursorSim.moveToNext())
            {
                m_simPhonename =cursorSim.getString(cursorSim.getColumnIndex("name"));
                m_simphoneNo = cursorSim.getString(cursorSim.getColumnIndex("number"));
                m_simphoneNo.replaceAll("\\D","");
                m_simphoneNo.replaceAll("&", "");
                m_simPhonename=m_simPhonename.replace("|","");

            //если найден номер с нужным названием, выводим в лог
                if (cursorSim.getString(cursorSim.getColumnIndex("name")).equals("MyNumber")) {
                    Log.i("PhoneContact", "name: " + m_simPhonename + " phone: " + m_simphoneNo);
                    Log.i("CHECK","something happened here");

            //выводим номер и название в текствью
                    tvPhoneNumber.setText(m_simPhonename+" "+m_simphoneNo);
            //номер и название для вывода в уведомление
                    notificationText = m_simPhonename+" "+m_simphoneNo;
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
    //создаем уведомление
    public void displayNotification (View view){
        createNotificationChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_sms_notification);
        builder.setContentTitle("твой номер");
        builder.setContentText(notificationText);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
    }
    //создаем канал уведомления (в новых версиях андроида иначе не попрет)
    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            CharSequence name = "personal notifications";
            String description = "Include all the personal notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel notificationChannel= new NotificationChannel(CHANNEL_ID, name, importance);
            notificationChannel.setDescription(description);
            NotificationManager notificationManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }


//запрашиваем разрешение на чтение контактов

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