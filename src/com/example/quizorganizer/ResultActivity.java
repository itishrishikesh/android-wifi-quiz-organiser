package com.example.quizorganizer;

import Data.Result;
import HotSpot.OnOfHotspot;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/*
 * 
 * This Activity has a code which waits for the results to arrive from all clients.
 * 
 * As the results is recived it will present it in a listview.
 * 
 */

public class ResultActivity extends Activity {
	 
	ListView lv;
	
	ProgressDialog pd;
	
	MyCustomAdapter aa;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		
		lv = (ListView) findViewById(R.id.resultList);
		
		new Thread(){
			@Override
			public void run(){
				while(Globals.Globals.userCount != Globals.Globals.receiveCount){
					System.out.print("Waiting for reception of result");
				}
				runOnUiThread(new Runnable(){

					public void run() {

						aa = new MyCustomAdapter(ResultActivity.this);
						
						lv.setAdapter(aa);
						
					}
					
				});
			}
		}.start();
		
		
		
	}
	
	@Override()
	public void onDestroy(){
		OnOfHotspot.configApState(getApplicationContext(), false);
		super.onDestroy();
	}

	class MyCustomAdapter extends ArrayAdapter<Result>
	{
		TextView username,totalMarks;
		public MyCustomAdapter(Context context)
		{
			super(context, R.layout.result,R.id.username, Globals.Globals.resultList);
		}
		
		@Override
		public View getView(int position,View convertView,ViewGroup parent)
		{
			System.out.println("reached getView()");
			
			View row = super.getView(position, convertView, parent);
			
				username = (TextView) row.findViewById(R.id.username);
				totalMarks = (TextView) row.findViewById(R.id.totalmarks);
				
				username.setText(Globals.Globals.resultList.get(position).getUserName());
				totalMarks.setText(Globals.Globals.resultList.get(position).getTotalMarks()+"");
				
			return row;
		}
		
	}
}
