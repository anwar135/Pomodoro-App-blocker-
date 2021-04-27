package com.example.pomodoro;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import android.widget.CompoundButton.OnCheckedChangeListener;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<String> {

    Context context;
    public static ArrayList<AppInfo> Apps;

    public ListAdapter(@NonNull Context context, ArrayList<String> appNames, ArrayList<AppInfo> apps) {
        super(context, R.layout.apps_list,R.id.AppName, appNames);


        this.context = context;
        this.Apps = apps;

    }


    @NonNull
    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {

        View singleItem = convertView;

        ProgramViewHolder holder = null;

        if(singleItem == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            singleItem = layoutInflater.inflate(R.layout.apps_list,parent,false);

            holder = new ProgramViewHolder(singleItem);

            singleItem.setTag(holder);
        }else{
            holder = (ProgramViewHolder) singleItem.getTag();
        }

        holder.appName.setText(Apps.get(position).getName());
        holder.locked.setChecked(Apps.get(position).isBlocked);
        holder.image.setImageDrawable(Apps.get(position).icon);

        holder.locked.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                Apps.get(position).isBlocked =  isChecked;
            }
        });

        return singleItem;
    }
}

