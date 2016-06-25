package com.example.quizorganizer;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/*
 * 
 * This Activity will be the first Activity to launch.
 * 
 * It asks user for username.
 * 
 * I created a database so that it can be stored on to the system.
 * 
 * If the username is already stored the activity will redirect to Select Activity.
 * 
 */

public class UserName extends Activity {
	
	EditText et;
	Button b1;
	SQLiteDatabase myDatabase;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_username);
		
		et = (EditText)findViewById(R.id.editText1);
		b1 = (Button) findViewById(R.id.button1);
		
		myDatabase = openOrCreateDatabase("myDB.db", Context.MODE_PRIVATE, null);
		
		myDatabase.execSQL("create table if not exists myTB(name TEXT)");
			
		Cursor cur = myDatabase.rawQuery("select * from myTB", null);
		
		final Intent i = new Intent(this,Select.class);
		
		if(cur.getCount()>0){
			cur.moveToFirst();
			Globals.Globals.userName = cur.getString(0);
			startActivity(i);
			finish();
			
		}
		
		b1.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				
				if(et.getText().toString().isEmpty()){
					et.setError("Please, enter username!");
				}
				else{
					myDatabase.execSQL("insert into myTB values('"+ et.getText().toString() +"')");
					startActivity(i);
					finish();
				}
			}
		});
		
	}
}
