package com.example.pomodoro;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class ProgramViewHolder {

    TextView appName;
    CheckBox locked;
    ImageView image;

    ProgramViewHolder(View view){

        appName = view.findViewById(R.id.AppName);
        locked = view.findViewById(R.id.Lock);
        image = view.findViewById(R.id.appImage);


    }

}
