package com.example.healthybuddy;

public class AppItem {

	private final String mTitle;
	private final int mID;
	private boolean isChecked;
	
	public AppItem(String name, Integer itemID, boolean check)
	{
		mID = itemID;
		mTitle = name;
		isChecked = false;
	}
	
	public Integer getItemID()
	{
		return mID;
	}
	
	public String getName()
	{
		return mTitle;
	}
	
	public void checkItem()
	{
		isChecked = true;
	}
	
	public boolean checkChecked()
	{
		return isChecked;
	}
}