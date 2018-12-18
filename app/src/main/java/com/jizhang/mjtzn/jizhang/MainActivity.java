package com.jizhang.mjtzn.jizhang;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity{

    private ScreenShotContentObserver screenShotContentObserver;
    private HistogramView table;
    private LinearLayout Bar_lay;
    private TextView day_exp;
    private TextView month_exp;
    private TextView month_income;

    public static Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Util.getInstance().setContext(getApplicationContext());

        day_exp=findViewById(R.id.day_amount);
        month_exp=findViewById(R.id.month_amount);
        month_income=findViewById(R.id.income_text);
        day_exp.setText(Util.getInstance().getTodayexpense());
        month_exp.setText(Util.getInstance().getMonthexpense());
        month_income.setText(Util.getInstance().getMonthincome());

        Bar_lay = (LinearLayout) findViewById(R.id.chart_ly);

        table = new HistogramView(this);
        Util.getInstance().setTable(table);

        table.init();

        Bar_lay.addView(table);

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AddAct.class);
                startActivity(intent);
            }
        });

        startNotificationListenerService();
        checkandgetPermission();
        screenShotContentObserver = new ScreenShotContentObserver(handler, this);
        getContentResolver().registerContentObserver(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                true,
                screenShotContentObserver
        );
    }



    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void onResume() {
        super.onResume();
        day_exp.setText(Util.getInstance().getTodayexpense());
        month_exp.setText(Util.getInstance().getMonthexpense());
        month_income.setText(Util.getInstance().getMonthincome());
        Util.getInstance().setTable(table);
        table.redraw();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            getContentResolver().unregisterContentObserver(screenShotContentObserver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this,Set_app.class);
            startActivity(intent);
            return true;
        }else if(id == R.id.detail_list){
            Intent intent = new Intent(MainActivity.this,ListAct.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private boolean isNotificationServiceEnable() {
        return NotificationManagerCompat.getEnabledListenerPackages(this).contains(getPackageName());
    }

    private void toggleNotificationListenerService() {
        PackageManager pm = getPackageManager();
        pm.setComponentEnabledSetting(
                new ComponentName(this, com.jizhang.mjtzn.jizhang.myNotificationListenerService.class),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

        pm.setComponentEnabledSetting(
                new ComponentName(this, com.jizhang.mjtzn.jizhang.myNotificationListenerService.class),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    private void startNotificationListenerService(){
        if (!isNotificationServiceEnable()) {
            startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
        }
        if (isNotificationServiceEnable()) {
            Intent intent = new Intent(MainActivity.this, myNotificationListenerService.class);
            startForegroundService(intent);
            toggleNotificationListenerService();
        }else{
            Toast.makeText(MainActivity.this,"没有读取通知权限！",Toast.LENGTH_LONG).show();
        }
    }

    private void checkandgetPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.INTERNET},
                    1000);
        }
    }

}
