package com.chef.freezer;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.chef.freezer.events.AppDialogEvent;
import com.chef.freezer.events.DefrostAppEvent;
import com.chef.freezer.events.DialogCancelEvent;
import com.chef.freezer.events.FreezeAppEvent;
import com.chef.freezer.events.ListUpdateEvent;
import com.chef.freezer.events.UninstallAppCheckEvent;
import com.chef.freezer.events.UninstallAppEvent;
import com.chef.freezer.loader.AppCard;
import com.chef.freezer.loader.AppListLoader;
import com.chef.freezer.ui.AppCardAdapter;
import com.chef.freezer.ui.AppDialog;
import com.chef.freezer.ui.AppDialogUninstall;
import com.chef.freezer.ui.DrawerItemCustomAdapter;
import com.chef.freezer.ui.ObjectDrawerItem;
import com.chef.freezer.util.Logger;
import com.chef.freezer.util.RootUtil;
import com.stericson.RootTools.RootTools;

import java.io.IOException;
import java.util.List;

import de.greenrobot.event.EventBus;
import com.chef.freezer.util.ListHandler;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<AppCard>> {

    private static final String TAG = "MainActivity";
    private List<AppCard> mAppList;
    private ListView mDrawerList;
	DrawerItemCustomAdapter dicaadapter;
    private AppCardAdapter ca;
    private DrawerLayout drawerLayout;
    private ProgressDialog dialog;
    private RecyclerView recList;
    //private int mDrawerSelect = 0;
    private boolean Root = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EventBus.getDefault().register(this);
        RootTools.debugMode = true;
        Root = RootTools.isAccessGiven();
        Logger.logv(TAG, "Checking for Root! " + Root);

        mDrawerList = (ListView) findViewById(R.id.navList);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        recList = (RecyclerView) findViewById(R.id.cardList);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        ca = new AppCardAdapter();

        setupdraweritems();

		
        getSupportLoaderManager().initLoader(0, null, this);
		//getSupportLoaderManager().getLoader(0).;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        try {
            RootTools.closeAllShells();
        } catch (IOException e) {
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
        if (id == R.id.action_settings) {
            Toast.makeText(MainActivity.this, R.string.action_settings, Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupdraweritems() {
        ObjectDrawerItem[] drawerItem = new ObjectDrawerItem[4];
        drawerItem[0] = new ObjectDrawerItem(R.mipmap.ic_launcher, "All");
        drawerItem[1] = new ObjectDrawerItem(R.mipmap.ic_launcher, "User");
        drawerItem[2] = new ObjectDrawerItem(R.mipmap.ic_launcher, "System");
        drawerItem[3] = new ObjectDrawerItem(R.mipmap.ic_launcher, "Frozen");
        dicaadapter = new DrawerItemCustomAdapter(this, R.layout.drawer_list_item, drawerItem);
		dicaadapter.setpos(0);
        mDrawerList.setAdapter(dicaadapter);
        mDrawerList.setItemChecked(0, true);
        Logger.logv(TAG, "Setup the drawer.");
    }

    private void setmainappslist(List<AppCard> lac) {
        this.mAppList = lac;
        if (ca.getapplist() == null) {
            ca.setapplist(lac);
            recList.setAdapter(ca);
        }
		applistupdatetest(lac, dicaadapter.getpos());
		//Log.v("setmainappslist", "selected position" + mDrawerList.getSelectedItemPosition());
		Toast.makeText(this, "Drawer position: " + dicaadapter.getpos(), Toast.LENGTH_LONG).show();
        //EventBus.getDefault().post(new ListUpdateEvent(mAppList, mDrawerSelect));
    }

    @Override
    public Loader<List<AppCard>> onCreateLoader(int id, Bundle args) {
        return new AppListLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<AppCard>> loader, List<AppCard> data) {
        setmainappslist(data);
    }

    @Override
    public void onLoaderReset(Loader<List<AppCard>> loader) {
    }

    public void onEvent(AppDialogEvent event) {
        DialogFragment newFragment = AppDialog.newInstance(event.ac);
        newFragment.show(getSupportFragmentManager(), "dialog");
    }

    public void onEvent(ListUpdateEvent event) {
        switch (event.position) {
            case 0:
                ca.setapplist(event.alllist());
                break;
            case 1:
                ca.setapplist(event.userlist());
                break;
            case 2:
                ca.setapplist(event.systemlist());
                break;
            case 3:
                ca.setapplist(event.frozenlist());
                break;
            default:
                break;
        }
    }

    public void onEvent(FreezeAppEvent event) {
        showthed(event.faeae.mLabel);
        RootUtil.rootcommand(event.getc());
    }

    public void onEvent(DefrostAppEvent event) {
        showthed(event.daeae.mLabel);
        RootUtil.rootcommand(event.getc());
    }

    public void onEvent(DialogCancelEvent event) {
        dialog.cancel();
    }

    public void onEvent(UninstallAppCheckEvent event) {
        DialogFragment newFragment = AppDialogUninstall.newInstance(event.uaceae);
        newFragment.show(getSupportFragmentManager(), "uninstalldialog");
    }

    public void onEvent(UninstallAppEvent event) {
        showthed(event.uaeae.mLabel);
        RootUtil.rootcommand(event.getc());
    }

    private void showthed(String s) {
        dialog = ProgressDialog.show(this, "Testing", s);
        dialog.setCancelable(true);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            //mDrawerSelect = position;
			dicaadapter.setpos(position);
            //EventBus.getDefault().post(new ListUpdateEvent(mAppList, position));
			setmainappslist(mAppList);
			//applistupdatetest(mAppList, position);
            drawerLayout.closeDrawer(mDrawerList);
        }
    }
	
	public void applistupdatetest(List<AppCard> lac, int position) {
        switch (position) {
            case 0:
                ca.setapplist(lac);
                break;
            case 1:
                ca.setapplist(ListHandler.mbuser(lac));
                break;
            case 2:
                ca.setapplist(ListHandler.mbsystem(lac));
                break;
            case 3:
                ca.setapplist(ListHandler.mbdisabled(lac));
                break;
            default:
                break;
        }
    }

}
