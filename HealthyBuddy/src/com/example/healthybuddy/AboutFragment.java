package com.example.healthybuddy;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AboutFragment extends Fragment
{
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fragment_about, container, false);
		
		return rootView;
	}

}
