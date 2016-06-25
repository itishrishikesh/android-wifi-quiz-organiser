package com.example.quizorganizer;

import Data.Question;
import Data.QuestionDB;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

/*
 * 
 * This Activity will enable user to store questions to the database.
 * Sqlite database is primarily engaged in this Activity.
 * It is used in application for two purposes, either to update a question or add new one.
 * 
 */


public class AddQuestion extends Activity {
	EditText ans,que,opta,optb,optc,optd;
	Button b1;

	Question question;
	public static QuestionDB db;

	SimpleCursorAdapter aa;
	
	int rowIndex;
	Bundle extras;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_main);

			db = new QuestionDB(this);
			db.open();

			ans = (EditText) findViewById(R.id.ans);
			que = (EditText) findViewById(R.id.que);
			opta = (EditText) findViewById(R.id.optA);
			optb = (EditText) findViewById(R.id.optB);
			optc = (EditText) findViewById(R.id.optC);
			optd = (EditText) findViewById(R.id.optD);
			b1 = (Button) findViewById(R.id.set_time);
			
			extras = getIntent().getExtras();
			
			
			//When the activity be called for editing an existin question rowIndex will be useful
			if(extras!=null)
				rowIndex = extras.getInt("rowIndex");
			
			if(rowIndex>0){

				Question question = db.getEntry(rowIndex);

				opta.setText(question.getOptionA());
				optb.setText(question.getOptionB());
				optc.setText(question.getOptionC());
				optd.setText(question.getOptionD());
				que.setText(question.getQuestion());
				ans.setText(question.getAnswer());

				b1.setText("Update");
			}


			b1.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					
					String Ans = ans.getText().toString();

					String Que = que.getText().toString();

					String optA = opta.getText().toString();

					String optB = optb.getText().toString();

					String optC = optc.getText().toString();

					String optD = optd.getText().toString();

					if(!validateInput())
					{
						question = new Question(Que, optA, optB, optC, optD, Ans);
						
						if(extras!=null)
							db.updateEntry(rowIndex,question);
						else
							db.insertEntry(question);
						
						Toast.makeText(getBaseContext(), "Question Stored.", Toast.LENGTH_LONG).show();

						opta.setText("");
						optb.setText("");
						optc.setText("");
						optd.setText("");
						que.setText("");
						ans.setText("");
					}
				}

			});
	}

	public boolean validateInput(){
		boolean error = false;

		if(opta.getText().toString().isEmpty()){
			opta.setError("cannot be empty");
			error = true;
		}
		if(optb.getText().toString().isEmpty()){
			optb.setError("cannot be empty");
			error = true;
		}
		if(que.getText().toString().isEmpty()){
			optc.setError("cannot be empty");
			error = true;
		}
		if(optd.getText().toString().isEmpty()){
			que.setError("cannot be empty");
			error = true;
		}
		if(ans.getText().toString().isEmpty()){
			ans.setError("cannot be empty");
			error = true;
		}
		else{
			String str = ans.getText().toString();
			if(str.equals("A")||str.equals("B")||str.equals("C")||str.equals("D")){	}
			else{
				ans.setError("invalid entry");
			}
		}
		if(optd.getText().toString().isEmpty()){
			optd.setError("cannot be empty");
			error = true;
		}
		return error;
	}
	
	@Override
	public void onPause(){
		super.onPause();
	}
	
	@Override
	public void onDestroy(){
		db.close();
		super.onDestroy();
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
