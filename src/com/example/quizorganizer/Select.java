package com.example.quizorganizer;


import java.util.ArrayList;

import HotSpot.OnOfHotspot;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/* 
 * 
 * This is the first Activity which will be presented to the user.
 *  
 * It includes three buttons function of each is described as belows:
 * 
 * Host -> As user will click these button a hotspot will be created and further will be redirected to ListQuestion Activity.
 * Participate -> This will ask user to connect to a proper quiz network.
 * Question Store -> Will open QuestionManagement Activity which deals how question are stored in the system.
 * 
 * 
 */

public class Select extends Activity implements OnClickListener{
	Button host,participant,addQ;
	ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select);
		
		host = (Button) findViewById(R.id.host);
		host.setOnClickListener(this);
		participant = (Button) findViewById(R.id.participant);
		participant.setOnClickListener(this);
		addQ = (Button) findViewById(R.id.addQue);
		addQ.setOnClickListener(this);

	}

	public void onClick(View arg0) {
		Intent i;
		switch(arg0.getId()){
		case R.id.host:
			try{
				OnOfHotspot.getApConfiguration(this);
				OnOfHotspot.configApState(this, true);
				
				i = new Intent(this,ListQuestions.class);
				startActivity(i);
				
			}catch(Exception e){
				System.out.println(e.toString());
			}
			break;
		case R.id.participant:
			
			String msg = "Please, make sure you're connected to a proper quiz host before proceeding.";
			
			showDialog(msg);
			
			break;
		case R.id.addQue:
			i = new Intent(this,QuestionManagement.class);
			startActivity(i);
			break;
		}
	}
	ArrayList<String> str ;
	public void showDialog(String message){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Please, make sure.");
		
		TextView tv = new TextView(this);
		
		tv.setText(message);
		
		builder.setView(tv);
		
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface arg0, int arg1) {
				WifiManager wm = (WifiManager) getBaseContext().getSystemService(Context.WIFI_SERVICE);
				wm.setWifiEnabled(true);
				
				if(isConnected()){
					startActivity(new Intent(Select.this,Participant.class));
					finish();
				}
				else{
					startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
				}
			}
		});
		
		builder.show();
	}

	public boolean isConnected(){
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		Boolean is3g = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
		Boolean isWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
		if(is3g)
			return false;
		else if(isWifi)
			return true;
		else
			return false;
	}
	
	
	private static final int MenuHelp = Menu.FIRST;
	private static final int MenuAbout = Menu.FIRST + 1;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		super.onCreateOptionsMenu(menu);
		
		MenuItem m1 = menu.add(0,MenuHelp,Menu.NONE,"Help");
		MenuItem m3 = menu.add(0,MenuAbout,Menu.NONE,"About");
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem){
		
		switch(menuItem.getItemId()){
		case MenuHelp:
			startActivity(new Intent(this,HelpScreen.class));
			break;
		case MenuAbout:
			startActivity(new Intent(this,About.class));
			break;
		
		}
		
		return false;
	}
}
