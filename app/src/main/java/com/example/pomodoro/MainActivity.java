package com.example.pomodoro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Debug;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.security.Permission;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button profile;
    public static Button Start;
    Spinner dropDownTimer;
    Spinner dropdownProfile;

    public static  int timer;
    String[] timerTextArray = {"10 min", "20 min", "30 min", "40 min", "50 min"};
    String[] profiles = {"Profile A", "Profile B", "Profile C"};


    int[] time = {10,20,30,40,50};

    public  static  boolean isTimerRuning;

    public static ProgressBar bar;

    public static TextView timerText;

    public static PackageManager pm;

    //get a list of installed apps.
    public  static  int currentProfileIndex;
    public static ArrayList<AppInfo> currentProfileApps;
    public static ArrayList<ArrayList<AppInfo>> profilelst = new ArrayList<ArrayList<AppInfo>>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pm = getPackageManager();

        profile = findViewById(R.id.profile);
        Start = findViewById(R.id.Start);
        timerText = findViewById(R.id.timer_Counter);

        bar = findViewById(R.id.progressBar);

        setAllProfiles();


        this.setTitle("Title");

        TimerDrop();
        ProfileDropDown();
        Premissions();
    }

    public  void ProfileButton(View view){
        Intent intent = new Intent(this, AppList_Activity.class);

        currentProfileApps = profilelst.get(currentProfileIndex);

        startActivity(intent);
        finish();
    }

    public  void Start(View V){
        timerText.setVisibility(View.VISIBLE);
        dropDownTimer.setVisibility(View.INVISIBLE);

        createService();
    }


    private ArrayList<AppInfo> getInstalledApps() {
        PackageManager pm = getPackageManager();
        ArrayList<AppInfo> apps = new ArrayList<AppInfo>();
        List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);
        //List<PackageInfo> packs = getPackageManager().getInstalledPackages(PackageManager.GET_PERMISSIONS);
        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);
            if ((!isSystemPackage(p))) {
                String appName = p.applicationInfo.loadLabel(getPackageManager()).toString();
                Drawable icon = p.applicationInfo.loadIcon(getPackageManager());
                String packages = p.applicationInfo.packageName;
                apps.add(new AppInfo(appName, icon, packages));
            }
        }
        return apps;
    }

    private boolean isSystemPackage(PackageInfo pkgInfo) {
        return (pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
    }

    public  void createService(){

        Intent intent = new Intent(this,BroadcastService.class);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            System.out.println("Starting foreground");
            startForegroundService(intent);
        }else{
            System.out.println("Starting just Intent");
            startService(intent);
        }

        startService(intent);
    }

    public  void Premissions(){

        AppOpsManager appOps = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow("android:get_usage_stats",
                android.os.Process.myUid(), getPackageName());
        boolean granted = mode == AppOpsManager.MODE_ALLOWED;

        if(granted == false){
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new  Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                startActivityForResult(intent, 12345);
            }
        }
    }

    public void TimerDrop(){

        dropDownTimer = findViewById(R.id.dropdown_Timer);
        dropDownTimer.setVisibility(View.INVISIBLE);
        dropDownTimer.setOnItemSelectedListener(this);

        if(isTimerRuning == false) {
            timerText.setVisibility(View.INVISIBLE);
            dropDownTimer.setVisibility(View.VISIBLE);

            ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, timerTextArray);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dropDownTimer.setAdapter(adapter);

        }
    }

    public  void ProfileDropDown(){

        dropdownProfile = findViewById(R.id.dropdown_profile);
        dropdownProfile.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, profiles);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdownProfile.setAdapter(adapter);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if(parent.getId() == R.id.dropdown_Timer){

            timer = time[position];
        }

        if(parent.getId() == R.id.dropdown_profile){
            currentProfileIndex = position;
        }
    }

    private  void setAllProfiles(){
        if(profilelst.size() != profiles.length) {
            for (int i = 0; i < profiles.length; i++) {

                loadProfile(i);
            }
        }
    }

    public void loadProfile(int profileID){
        profilelst.add(getInstalledApps());
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    @Override
    public void onBackPressed() {

    }
}

