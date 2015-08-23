package com.chef.freezer.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chef.freezer.R;
import com.chef.freezer.events.AppDialogEvent;
import com.chef.freezer.loader.AppCard;
import com.chef.freezer.util.Logger;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Brian on 8/8/2015.
 * <p/>
 * Adapter for implementing the card view.
 */
public class AppCardAdapter extends RecyclerView.Adapter<AppCardAdapter.AppViewHolder> {

    private static final String TAG = "AppCardAdapter";
    public List<AppCard> appList;

    public AppCardAdapter() {
        Logger.logv(TAG, "Starting Adapter...");
    }

    public void setapplist(List<AppCard> appCardList) {
        this.appList = appCardList;
        this.notifyDataSetChanged();
    }

    public List<AppCard> getapplist() {
        return appList;
    }

    @Override
    public int getItemCount() {
        return appList.size();
    }

    @Override
    public void onBindViewHolder(AppViewHolder appViewHolder, int i) {
        AppCard ci = appList.get(i);

        String s = "";
        if (ci.isappfrozen()) {
            s = "Frozen";
        }

        appViewHolder.mIcon.setImageDrawable(ci.getIcon());
        appViewHolder.tName.setText(ci.toString());
        appViewHolder.tVersionName.setText(ci.getversionname());
        appViewHolder.tVersionCode.setText(ci.getversioncode() + "");
        appViewHolder.tFrozen.setText(s);
        appViewHolder.ac = ci;
    }

    @Override
    public AppViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.app_card, viewGroup, false);
        return new AppViewHolder(itemView);
    }

    public static class AppViewHolder extends RecyclerView.ViewHolder {

        protected ImageView mIcon;
        protected TextView tName;
        protected TextView tVersionName;
        protected TextView tVersionCode;
        protected TextView tFrozen;
        protected AppCard ac;

        public AppViewHolder(View v) {
            super(v);
            mIcon = (ImageView) v.findViewById(R.id.appicon);
            tName = (TextView) v.findViewById(R.id.textviewappname);
            tVersionName = (TextView) v.findViewById(R.id.textviewversionname);
            tVersionCode = (TextView) v.findViewById(R.id.textviewversioncode);
            tFrozen = (TextView) v.findViewById(R.id.tvfrozen);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Logger.logv(TAG, "Clicked on: " + ac.toString());
                    EventBus.getDefault().post(new AppDialogEvent(ac));
                }
            });
        }
    }

}
