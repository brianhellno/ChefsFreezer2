package com.chef.freezer.ui;

import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chef.freezer.R;
import com.chef.freezer.loader.AppCard;

import java.util.List;

/**
 * Created by Brian on 8/8/2015.
 */
public class AppCardAdapter extends RecyclerView.Adapter<AppCardAdapter.AppViewHolder> {

    public List<AppCard> appList;

    public AppCardAdapter(){
    }

    public void setapplist(List<AppCard> appCardList){
        this.appList = appCardList;
    }

    public List<AppCard> getapplist(){
        return appList;
    }

    @Override
    public int getItemCount() {
        return appList.size();
    }

    @Override
    public void onBindViewHolder(AppViewHolder appViewHolder, int i) {
        AppCard ci = appList.get(i);
        appViewHolder.mIcon.setImageDrawable(ci.getIcon());
        appViewHolder.tName.setText(ci.toString());
        appViewHolder.tVersionName.setText(ci.getversionname());
        appViewHolder.tVersionCode.setText(ci.getversioncode() + "");
    }

    @Override
    public AppViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.app_card, viewGroup, false);
        return new AppViewHolder(itemView);
    }

    public static class AppViewHolder extends RecyclerView.ViewHolder {

        protected ImageView mIcon;
        protected TextView tName;
        protected TextView tVersionName;
        protected TextView tVersionCode;


        public AppViewHolder(View v) {
            super(v);

            mIcon = (ImageView) v.findViewById(R.id.appicon);
            tName = (TextView) v.findViewById(R.id.textviewappname);
            tVersionName = (TextView) v.findViewById(R.id.textviewversionname);
            tVersionCode = (TextView) v.findViewById(R.id.textviewversioncode);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), tName.getText(), Toast.LENGTH_SHORT).show();
                    DialogFragment newFragment = AppDialog.newInstance((AppCard) v.getItemAtPosition(position));
                    newFragment.show(getFragmentManager(), "dialog");
                }
            });

        }
    }

}
