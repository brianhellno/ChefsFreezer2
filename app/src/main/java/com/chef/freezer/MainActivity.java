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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chef.freezer.events.DefrostAppEvent;
import com.chef.freezer.events.DialogCancelEvent;
import com.chef.freezer.events.FreezeAppEvent;
import com.chef.freezer.events.UninstallAppCheckEvent;
import com.chef.freezer.events.UninstallAppEvent;
import com.chef.freezer.loader.AppCard;
import com.chef.freezer.loader.AppListLoader;
import com.chef.freezer.ui.AppCardAdapter;
import com.chef.freezer.ui.AppDialogUninstall;
import com.chef.freezer.util.RootUtil;
import com.stericson.RootTools.RootTools;

import java.io.IOException;
import java.util.List;

import de.greenrobot.event.EventBus;
import com.chef.freezer.events.AppDialogEvent;
import com.chef.freezer.ui.AppDialog;
import com.chef.freezer.events.ListUpdateEvent;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<AppCard>> {

    private static final String TAG = "LoaderManager";
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private DrawerLayout drawerLayout;
    private ProgressDialog dialog;
    private AppCardAdapter ca;
    private RecyclerView recList;
	private List<AppCard> mAppList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EventBus.getDefault().register(this);
        RootTools.debugMode = true;
        RootTools.isAccessGiven();

        mDrawerList = (ListView)findViewById(R.id.navList);
        addDrawerItems();
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        recList = (RecyclerView) findViewById(R.id.cardList);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        ca = new AppCardAdapter();

        getSupportLoaderManager().initLoader(0, null, this);
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

    private void addDrawerItems() {
        String[] osArray = { "Android", "iOS", "Windows", "OS X", "Linux", "Boom" };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            Toast.makeText(MainActivity.this, ((TextView)view).getText(), Toast.LENGTH_LONG).show();
			if(((TextView)view).getText().equals("Android")){
				EventBus.getDefault().post(new ListUpdateEvent(mAppList, 0));
			}
			if(((TextView)view).getText().equals("iOS")){
				EventBus.getDefault().post(new ListUpdateEvent(mAppList, 1));
			}
			if(((TextView)view).getText().equals("Windows")){
				EventBus.getDefault().post(new ListUpdateEvent(mAppList, 2));
			}
            drawerLayout.closeDrawer(mDrawerList);
        }
    }

    @Override
    public Loader<List<AppCard>> onCreateLoader(int id, Bundle args) {
        return new AppListLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<AppCard>> loader, List<AppCard> data) {
		this.mAppList = data;
        ca.setapplist(data);
        recList.setAdapter(ca);
    }

    @Override
    public void onLoaderReset(Loader<List<AppCard>> loader) {
    }

    public void onEvent(FreezeAppEvent event) {
        showthed(event.faeae.mLabel);
        RootUtil.rootcommandtest(event.getc());
    }

    public void onEvent(DefrostAppEvent event) {
        showthed(event.daeae.mLabel);
        RootUtil.rootcommandtest(event.getc());
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
        RootUtil.rootcommandtest(event.getc());
    }
	
	public void onEvent(AppDialogEvent event) {
        //showthed(event.ac.mLabel);
		DialogFragment newFragment = AppDialog.newInstance(event.ac);
		newFragment.show(getSupportFragmentManager(), "dialog");
    }
	
	public void onEvent(ListUpdateEvent event) {
		
		switch(event.position){
			case 0:
				ca.setapplist(event.alllist());
				break;
			case 1:
				ca.setapplist(event.userlist());
				break;
			case 2:
				ca.setapplist(event.systemlist());
				break;
			default:
			    break;
		}
    }

    private void showthed(String s) {
        dialog = ProgressDialog.show(this, "Uninstall", s);
        dialog.setCancelable(true);
    }

}
