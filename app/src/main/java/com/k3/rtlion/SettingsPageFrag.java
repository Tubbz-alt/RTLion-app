package com.k3.rtlion;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SettingsPageFrag {

    private Activity activity;
    private Context context;
    private ViewGroup viewGroup;
    private JSInterface jsInterface;

    private TextView txvSettingsWarning;
    private LinearLayout llSettings;

    public SettingsPageFrag(Activity activity, ViewGroup viewGroup, JSInterface jsInterface){
        this.activity = activity;
        this.context = activity.getApplicationContext();
        this.viewGroup = viewGroup;
        this.jsInterface = jsInterface;
    }

    private void initViews(){
        txvSettingsWarning = viewGroup.findViewById(R.id.txvSettingsWarning);
        llSettings = viewGroup.findViewById(R.id.llSettings);
    }
    public void initialize(){
        initViews();
        txvSettingsWarning.setVisibility(View.VISIBLE);
        llSettings.setVisibility(View.GONE);
    }
    public void removeConWarning(){
        txvSettingsWarning.setVisibility(View.GONE);
        llSettings.setVisibility(View.VISIBLE);
    }
}
