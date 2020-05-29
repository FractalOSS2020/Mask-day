package com.example.maskday;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    public static final String CHANNEL_ID = "10001";
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUEST_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private long backPressedTime = 0;

    private DrawerLayout drawerLayout;
    private View drawerView;
    private WebView webView;
    private String url = "https://fractaloss2020.github.io/";
    private LinearLayout pushAlarmLayout, inputLayout, appInfoLayout, maskCheckLayout;
    private ImageView menu;
    private FloatingActionButton floatingActionButton;
    private Realm realm;
    private RealmResults<UserModel> userData;
    private TextView daily_rule;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!checkLocationServicesStatis()){
            GPSSetting();
        } else {
            checkRunTimePermission();
        }
        Realm.init(this);
        init();
        randomRule();

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClientClass());

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(drawerView);

            }
        });

        drawerLayout.setDrawerListener(listener);
        drawerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        /* 웹뷰 새로고침 */
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.reload();
            }
        });

        /* 출생년도 저장장 */
       inputLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBirthDayPicker();
            }
        });

       /* 어플 정보 */
       appInfoLayout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               //어플 정보 볼 수 있는 Activity 로 이동
               Intent intent = new Intent(MainActivity.this,AppInfoActivity.class);
               startActivity(intent);

           }
       });
    }



    /* 뒤로가기 버튼 설정 */
    @Override
    public void onBackPressed() {
//        super.onBackPressed();

        if(System.currentTimeMillis() > backPressedTime + 2000){
            backPressedTime = System.currentTimeMillis();
            Toast.makeText(this, "뒤로가기 버튼을 한 번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(System.currentTimeMillis() <= backPressedTime + 2000){
            finish();
        }
    }

    /* 기본 레이아웃 초기 설정 */
    private void init() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        menu = (ImageView) findViewById(R.id.menu);
        webView = (WebView) findViewById(R.id.webView);
        drawerView = (View) findViewById(R.id.drawer);
        pushAlarmLayout = (LinearLayout) findViewById(R.id.push_alarm);
        inputLayout = (LinearLayout) findViewById(R.id.input_year);
        appInfoLayout = (LinearLayout) findViewById(R.id.app_info);
        maskCheckLayout = (LinearLayout) findViewById(R.id.mask_checked);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.refresh_btn);
        daily_rule = (TextView)findViewById(R.id.daily_rule_text);
    }

    private void showBirthDayPicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);

        final Dialog birthDayDialog = new Dialog(this);
        birthDayDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        birthDayDialog.setContentView(R.layout.dialog_birthyear);

        Button okBtn = (Button) birthDayDialog.findViewById(R.id.ok_button);
        Button cancelBtn = (Button) birthDayDialog.findViewById(R.id.cancel_button);

        final NumberPicker picker = (NumberPicker) birthDayDialog.findViewById(R.id.number_picker);
        picker.setMinValue(year - 100);
        picker.setMaxValue(year);
        picker.setDescendantFocusability(NumberPicker.FOCUS_AFTER_DESCENDANTS);
        setDividerColor(picker, android.R.color.white);
        picker.setWrapSelectorWheel(false);
        picker.setValue(year - 20);
        picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {

            }
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 출생년도 저장하기
                saveRealm(picker.getValue());
                Toast.makeText(MainActivity.this, "저장되었습니다!", Toast.LENGTH_SHORT).show();
                birthDayDialog.dismiss();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                birthDayDialog.dismiss();
            }
        });

        birthDayDialog.show();
    }

    /* Realm 에 저장하기 */
    private void saveRealm(int birthYear){
        realm = Realm.getDefaultInstance();
        UserModel userModel = new UserModel();

//        realm.beginTransaction();
//        userModel = realm.createObject(UserModel.class);
//        userModel.setBirthYear(birthYear);
//        realm.commitTransaction();
        Log.d("출생년도", birthYear + "");
    }

    /* dialog 구분선 커스텀 */
    private void setDividerColor(NumberPicker picker, int color) {
        java.lang.reflect.Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (java.lang.reflect.Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    ColorDrawable colorDrawable = new ColorDrawable(color);
                    pf.set(picker, colorDrawable);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    /* 위치 권한 설정 */
    private void GPSSetting(){
        ContentResolver res = getContentResolver();
        boolean gpsEnabled = Settings.Secure.isLocationProviderEnabled(res, LocationManager.GPS_PROVIDER);
        if(!gpsEnabled){
            new AlertDialog.Builder(this)
                    .setTitle("GPS를 사용하시겠습니까?")
                    .setMessage("GPS를 사용하면 현재 위치를 통해 약국의 정보를 알 수 있습니다.")
                    .setCancelable(true)
                    .setPositiveButton("사용", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(intent, GPS_ENABLE_REQUEST_CODE);
                        }
                    })
                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(MainActivity.this, "GPS사용을 거절하였습니다.", Toast.LENGTH_SHORT).show();
                            dialogInterface.cancel();
                        }
                    }).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case GPS_ENABLE_REQUEST_CODE:
                if(checkLocationServicesStatis()){
                    Log.d("MainActivity", "onActivityResult : GPS 활성화 되었음");
                    checkRunTimePermission();
                    return;
                }

                break;
        }
    }


    public boolean checkLocationServicesStatis(){
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PERMISSIONS_REQUEST_CODE && grantResults.length == REQUEST_PERMISSIONS.length){
            boolean check_result = true;

            for (int result : grantResults){
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }

            if (check_result){
                // 위치값을 가져올 수 있음
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUEST_PERMISSIONS[0]) || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUEST_PERMISSIONS[1])){
                    Toast.makeText(this, "권한이 거부되었습니다. 앱을 다시 실행하여 권한을 허용해주세요.", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "권한이 거부되었습니다. 설정에서 권한을 허용해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    private void checkRunTimePermission(){
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED && hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
            // 위치값을 가져올 수 있음
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, REQUEST_PERMISSIONS[0])){
                Toast.makeText(this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(MainActivity.this, REQUEST_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, REQUEST_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            }
        }
    }







    public void notification() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 16);
        calendar.set(Calendar.MINUTE, 5);
        calendar.set(Calendar.SECOND, 0);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.applogo)
                .setContentTitle("올 때 마스크")
                .setContentText("마스크 구매일입니다! 잊지말고 마스크를 구매해주세요 :)")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setWhen(calendar.getTimeInMillis())
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setSmallIcon(R.drawable.applogo);
            CharSequence charSequence = "Notification Channel";
            String description = "It's for over Oreo";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, charSequence, importance);
            channel.setDescription(description);

            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        } else builder.setSmallIcon(R.drawable.applogo);

        assert notificationManager != null;
        notificationManager.notify(1234, builder.build());

    }


    private void randomRule(){

        String[] strings ={"증상이 있으면 빨리 코로나19 검사 받기","마스크 착용 생활화","30초 손씻기와 손 소독 자주하기","사람과 사람 사이, 두 팔 간격 건강 거리 두기"
        ,"매일 2번 이상 환기, 주기적 소독","집회/모임/회식 자제하기","거리는 멀어져도 마음은 가까이"};

        int randomNum = (int)(Math.random() * strings.length);
        daily_rule.setText( strings[randomNum]);





    }

    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


    DrawerLayout.DrawerListener listener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    };
}
