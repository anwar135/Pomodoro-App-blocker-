package com.example.pomodoro;

import android.app.ActivityManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import android.os.Process;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;

public class BroadcastService extends Service {
    private Looper serviceLooper;

    private static Toast toast;

    boolean intentIsRunning;

    BroadcastService BroadcastService = this;

    private TimerClass timer = new TimerClass(BroadcastService);


    @Override
    public void onCreate() {
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

       //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        timer.StartTimer(MainActivity.timer);
        toast = new Toast(this);
        serviceLooper = thread.getLooper();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        toast.makeText(this, "Test service Started", Toast.LENGTH_SHORT).show();

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }

    boolean overally = false;

    public void checkBackgroundApps() {
        //ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        for(AppInfo app : MainActivity.currentProfileApps) {
            if (IsAppRunningForGround(app.getPackages()) == true) {

                Intent intent = new Intent(getBaseContext(), BlockActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplication().startActivity(intent);

                //create();
            }
        }
    }

    private boolean IsAppRunningForGround(String packageN) {
        if (Build.VERSION.SDK_INT >= 21) {
            String currentApp = null;
            UsageStatsManager usm = (UsageStatsManager) this.getSystemService(Context.USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            List<UsageStats> applist = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 1000, time);
            if (applist != null && applist.size() > 0) {
                SortedMap<Long, UsageStats> mySortedMap = new TreeMap<>();
                for (UsageStats usageStats : applist) {
                    mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                }
                if (mySortedMap != null && !mySortedMap.isEmpty()) {
                    currentApp = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                }
            }

            if(packageN.equals(currentApp)){
                return  true;
            }

        }
        return false;
    }


    private  void create(){

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT);

        WindowManager wm = (WindowManager) getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater inflater = (LayoutInflater)getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup mTopView = (ViewGroup) inflater.inflate(R.layout.activity_block, null);
       // getWindow().setAttributes(params);
        wm.addView(mTopView, params);
    }
}
