package com.example.healthybuddy.row;

import com.example.healthybuddy.R;
import com.example.healthybuddy.R.id;
import com.example.healthybuddy.R.layout;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemRow implements Row
{
	private final ApplicationInfo mApp;
	private final LayoutInflater mInflater;
	private final PackageManager mManager;
	
	public ItemRow(LayoutInflater inflater, ApplicationInfo app, PackageManager manager)
	{
		mApp = app;
		mInflater = inflater;
		mManager = manager;
	}
	
	public View getView(View convertView)
	{
		ViewHolder holder;
		View view;
		
		if (convertView == null)
		{
			ViewGroup mViewGroup = (ViewGroup) mInflater.inflate(R.layout.fragment_app_list, null);
			
			holder = new ViewHolder((ImageView) mViewGroup.findViewById(R.id.app_icon),
					(TextView) mViewGroup.findViewById(R.id.app_title),
					(CheckBox) mViewGroup.findViewById(R.id.app_checkbox));
			mViewGroup.setTag(holder);
			
			view = mViewGroup;
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
			view = convertView;
		}
		
		holder.mAppIcon.setImageDrawable((mApp.loadIcon(mManager)));
		holder.mAppTitle.setText(mApp.loadLabel(mManager));
		if (((CompoundButton) holder.mAppCheckbox).isChecked())
			((CompoundButton) holder.mAppCheckbox).setChecked(true);
		
		return view;
	}
	
	private static class ViewHolder {
        final ImageView mAppIcon;
        final TextView mAppTitle;
        final TextView mAppCheckbox;

        private ViewHolder(ImageView imageView, TextView titleView, TextView checkboxView) {
            this.mAppIcon = imageView;
            this.mAppTitle = titleView;
            this.mAppCheckbox = checkboxView;
        }
    }

	@Override
	public int getViewType() {
		return RowType.APP_ROW.ordinal();
	}
}
