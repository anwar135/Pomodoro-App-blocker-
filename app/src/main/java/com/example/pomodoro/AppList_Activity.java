package com.example.pomodoro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class AppList_Activity extends AppCompatActivity {

    Button Save;
    Button Close;
    public ArrayList<AppInfo> beforeModificationlst;

    ListView listView;
    ArrayList<String> appNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list_);


        Save = findViewById(R.id.Save);
        Close = findViewById(R.id.Close);

        listView = findViewById(R.id.listView);
        set();

        beforeModificationlst = MainActivity.currentProfileApps;

        ListAdapter listAdapter = new ListAdapter(this,appNames ,MainActivity.currentProfileApps);

        listView.setAdapter(listAdapter);

    }

   private  void set(){
        for(AppInfo app : MainActivity.currentProfileApps){
            appNames.add(app.getName());
        }
   }

    public void SaveMethod(View view){

        Intent intent = new Intent(this, MainActivity.class);
        MainActivity.currentProfileApps = ListAdapter.Apps;

        //Save();

        startActivity(intent);

        finish();
    }

    public  void Save(){

        SharedPreferences sharedpreference = getSharedPreferences("shared preference",MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedpreference.edit();

        Gson gson = new Gson();
        String json = gson.toJson(MainActivity.currentProfileApps);
        editor.putString("Event",json);

        editor.apply();

    }


    public void CloseMethod(View view){

        Intent intent = new Intent(this, MainActivity.class);
        MainActivity.currentProfileApps = beforeModificationlst;

        Log.d("TAG", "CloseMethod: " + MainActivity.currentProfileApps.get(0).isBlocked);
        startActivity(intent);
        finish();
    }
}