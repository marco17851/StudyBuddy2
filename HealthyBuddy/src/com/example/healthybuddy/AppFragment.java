package com.example.healthybuddy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.example.healthybuddy.row.ItemRow;
import com.example.healthybuddy.row.Row;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

public class AppFragment extends Fragment
{
	private ListView mListView;
	private ArrayList mAppList;
	private HashMap mToBeLocked;
	private ArrayList<String> mLockList;
	private Bundle extras;
	private Button mButton;
	private CheckBox mCheckBox;
	private TextView mAppName;
	private PackageManager mManager;
	private Context mContext;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fragment_apps, container, false);
		
		mContext = this.getActivity();
		mListView = (ListView) rootView.findViewById(R.id.appListView);
		mButton = (Button) rootView.findViewById(R.id.appStartButton);
		mLockList = new ArrayList<String>();
		mToBeLocked = new HashMap();
		
		mManager = mContext.getPackageManager();
		extras = new Bundle();
		
		//The following copied and modified from StackOverflow user kcoppock
		ArrayList<ApplicationInfo> apps = (ArrayList<ApplicationInfo>) mManager.getInstalledApplications(0);
		final ArrayList<ApplicationInfo> installedApps = new ArrayList<ApplicationInfo>();
		
		int id = 0;
		for(ApplicationInfo app : apps) {
		    //checks for flags; if flagged, check if updated system app
		    if((app.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 1) {
		    	extras.putInt((String) app.loadLabel(mManager), id);
		        installedApps.add(app);
		        id += 1;
		    //it's a system app, not interested
		    } else if ((app.flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
		        //Discard this one
		    //in this case, it should be a user-installed app
		    } else {
		    	extras.putInt((String) app.loadLabel(mManager), id);
		        installedApps.add(app);
		        id += 1;
		    }
		}
		//End
		
		Collections.sort(installedApps, new ApplicationInfo.DisplayNameComparator(mManager)); 
		
		mListView.setAdapter(new AppListAdapter(installedApps, extras));
		mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		
		mListView.setOnItemClickListener(new OnItemClickListener() {
		    @Override
		    public void onItemClick(AdapterView<?> arg0, View view, int position, long id) 
		    {
		    	mCheckBox = (CheckBox) view.findViewById(R.id.app_checkbox);
		    	mCheckBox.toggle();
		    	if (mCheckBox.isChecked())
		    		mToBeLocked.put(position, "hi");
		    	else
		    		mToBeLocked.remove(position);
		    	
		    	for (int x = 0; x < mListView.getChildCount(); x++)
				{
					View childView = mListView.getChildAt(x);
					mCheckBox = (CheckBox) childView.findViewById(R.id.app_checkbox);
					
					if (mCheckBox.isChecked())
					{
						mAppName = (TextView) childView.findViewById(R.id.app_title);
						if (!mLockList.contains((String) mAppName.getText()))
							mLockList.add((String) mAppName.getText());
					}
				}
		    }
		});
		
		mButton.setHapticFeedbackEnabled(true);
		mButton.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v) 
			{
				mButton.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);

				for (int x = 0; x < mListView.getChildCount(); x++)
				{
					View view = mListView.getChildAt(x);
					mCheckBox = (CheckBox) view.findViewById(R.id.app_checkbox);
					
					if (mCheckBox.isChecked())
					{
						mAppName = (TextView) view.findViewById(R.id.app_title);
						if (!mLockList.contains((String) mAppName.getText()))
							mLockList.add((String) mAppName.getText());
					}
				}
				
				Log.d(Globals.TAG, mLockList.toString());
			}
		});
		
		return rootView;
	}
	
	public class AppListAdapter extends BaseAdapter
	{
		final ArrayList<Row> rows;
		final Bundle extras;
		int id;
		
		AppListAdapter(ArrayList<ApplicationInfo> items, Bundle bundle)
		{
			rows = new ArrayList<Row>();
			extras = bundle;
			
			for (ApplicationInfo app: items)
			{
				id = extras.getInt((String) app.loadLabel(mManager));
				rows.add(new ItemRow(LayoutInflater.from(getActivity()), app, id, mManager));
			}
		}
		
		@Override
		public int getCount() {
			return rows.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) 
		{		
			View view = rows.get(position).getView(convertView);
			CheckBox box = (CheckBox) view.findViewById(R.id.app_checkbox);
			if (mToBeLocked.get(position) == ("hi"))
				box.setChecked(true);
			else
				box.setChecked(false);
			
			return rows.get(position).getView(convertView);
		}
	}
	
}
