package com.example.quizorganizer;

import Data.QuestionDB;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

/*
 * 
 * This Activity will provide a list of question present in the database.
 * 
 * It will also allow users to add,delete and modify existing questions.
 *  
 */

public class QuestionManagement extends Activity implements OnClickListener,OnItemClickListener{
	
	ListView lv;
	Button addQuestion;
	
	QuestionDB db;
	Cursor cur;
	
	SimpleCursorAdapter aa;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question_management);
		
		db = new QuestionDB(this);
		db.open();
		
		
		lv = (ListView) findViewById(R.id.lv2);
		addQuestion = (Button) findViewById(R.id.addQ);
		addQuestion.setOnClickListener(this);
		
		cur = db.getAllEntries();
		
		lv.setOnItemClickListener(this);

		aa = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cur, new String[]{QuestionDB.COL6}, new int[]{ android.R.id.text1 }, 0);

		lv.setAdapter(aa);
		

	}
	public void onClick(View arg0) {
		
		switch(arg0.getId()){
		case R.id.addQ:
			Intent i = new Intent(this,AddQuestion.class);
			startActivity(i);
			break;
		}
	}
	
	@Override
	public void onResume(){
		cur = db.getAllEntries();
		aa.notifyDataSetChanged();
		super.onResume();
	}
	@Override
	public void onDestroy(){
		db.close();
		super.onDestroy();
	}
	
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		
		showPopUpDialog(arg3);
		
	}	
	
	AlertDialog.Builder builder;
	
	public void showPopUpDialog(final long indexOfItem){
		builder = new AlertDialog.Builder(this);

		final ListView popUp = new ListView(this);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new String[] {"Delete","Edit"});
		
		popUp.setAdapter(adapter);
		
		popUp.setOnItemClickListener(new OnItemClickListener(){
			
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
						//Toast.makeText(QuestionManagement.this, arg2 , Toast.LENGTH_LONG).show();
				switch(arg2){
				case 0:
					db.removeEntry(indexOfItem);
					Intent ii = new Intent(QuestionManagement.this,QuestionManagement.class);
					startActivity(ii);
					finish();
					break;
				case 1:
					Intent i = new Intent(QuestionManagement.this,AddQuestion.class);
					i.putExtra("rowIndex", (int)indexOfItem);
					Toast.makeText(QuestionManagement.this, ""+(int)indexOfItem , Toast.LENGTH_LONG).show();
					startActivity(i);
					break;
				}
			}
			
		});
		
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		    
		    public void onClick(DialogInterface dialog, int which) {
		        dialog.cancel();
		    }
		    
		});
		
		builder.setView(popUp);

		builder.show();
	}
	
	//Code for Menu
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
