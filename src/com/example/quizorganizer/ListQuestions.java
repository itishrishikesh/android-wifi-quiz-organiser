package com.example.quizorganizer;

import java.util.ArrayList;

import Data.Question;
import Data.QuestionDB;
import Data.QuestionListBundle;
import HotSpot.OnOfHotspot;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ListQuestions extends Activity {
	
	ListView lv;
	Button proceed;
	EditText hmtEt;
	
	ProgressDialog pd;
	
	QuestionDB db;
	
	ArrayList<Question> al = new ArrayList<Question>();
	
	QuestionListBundle qlb;
	
	SimpleCursorAdapter aa;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
	
		db = new QuestionDB(this);
		db.open();
		
		try {
			proceed = (Button) findViewById(R.id.proceed);
			lv = (ListView) findViewById(R.id.lv);
			hmtEt = (EditText) findViewById(R.id.hmtET);
			
			Cursor cur = db.getAllEntries();
			
			if(cur.getCount()<=0){
				Toast.makeText(getApplicationContext(), "The List is Empty please Add Questions!", Toast.LENGTH_LONG).show();
				finish();
			}
			else{
				Toast.makeText(getApplicationContext(), "Select Questions to be Sent", Toast.LENGTH_LONG).show();
			}
			
			aa = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_checked, cur, new String[]{QuestionDB.COL6}, new int[]{ android.R.id.text1 }, 0);
			
			lv.setAdapter(aa);
			
			lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

			proceed.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View arg0) {
					try {
						Integer.parseInt(hmtEt.getText().toString());
						
						SparseBooleanArray sparseBooleanArray = lv.getCheckedItemPositions();
						int count = lv.getCount();
						
						for(int i=0; i<count; i++){
							if(sparseBooleanArray.get(i)){
								long id = aa.getItemId(i);
								al.add(db.getEntry(id));
							}
						}
						AlertDialog();
					}catch(NumberFormatException e){
						hmtEt.setError("please enter valid time!");
					}catch (Exception e) {
						Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
					}
					
				}
			});
		} catch (Exception e) {
		Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
		}
		
	}
	
	AlertDialog.Builder builder;
	public void AlertDialog(){
		builder = new AlertDialog.Builder(this);
		builder.setTitle("Please, Confirm!");
		
		final TextView tv = new TextView(this);
		tv.setTextSize(25);
		tv.setText("Please, make Sure all the devices are connected to your wifi network.");

		builder.setView(tv);
		// Set up the buttons
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface arg0, int arg1) {
				try {
					
					qlb = new QuestionListBundle(al,Integer.parseInt(hmtEt.getText().toString()));
					new ScanHotspot(qlb,getApplicationContext()).start();
					
					Intent i = new Intent(ListQuestions.this,ResultActivity.class);
					startActivity(i);
					finish();
				}catch(NumberFormatException e){
					hmtEt.setError("only number please!");	
				} 
				catch (Exception e) {
					Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
				}
				
			} 
		   
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface arg0, int arg1) {
				arg0.cancel();
				
			}
		});
		builder.show();

	}
	
	@Override
	public void onDestroy(){
		db.close();
		super.onDestroy();
	}
	
	//Menu Code
	private static final int MenuHelp = Menu.FIRST;
	private static final int MenuAbout = Menu.FIRST + 1;
	private static final int MenuDeleteAll = Menu.FIRST + 3;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		super.onCreateOptionsMenu(menu);
		
		MenuItem m1 = menu.add(0,MenuHelp,Menu.NONE,"Help");
		MenuItem m3 = menu.add(0,MenuAbout,Menu.NONE,"About");
		MenuItem m4 = menu.add(0,MenuDeleteAll,Menu.NONE,"Delete All");
		
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
		case MenuDeleteAll:
			db.deleteAll();
			break;
		}
		
		return false;
	}

}
