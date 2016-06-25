package com.example.quizorganizer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

import Data.Question;
import Data.QuestionListBundle;
import Data.Result;
import Globals.Globals;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/*
 * 
 * This is where all the code related to participant is situated.
 * 
 * It includes code for receiving questions from the host to sending the final total marks.
 * 
 * It has an AsyncTask class Associated which has a code to receive questions from the host.
 * 
 * It will receive a bundle of question that will be presented to the user 1 by 1.
 * 
 * It will also receive how much time will be given for the quiz.
 *
 *  
 */

public class Participant extends Activity implements OnClickListener{
	
	TextView question,timer;
	Button optA,optB,optC,optD;
	
	QuestionListBundle qlb;
	Question que;
	
	ArrayList<Question> QueList = new ArrayList<Question>();
	
	ProgressDialog pd;
	
	String name;
	
	int marks = 0;
	
	Receive receive;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_participant);
			receive = new Receive();
			
			question = (TextView) findViewById(R.id.question);
			timer = (TextView)  findViewById(R.id.timer);
			optA = (Button) findViewById(R.id.optA);
			optA.setOnClickListener(this);
			optB = (Button) findViewById(R.id.optB);
			optB.setOnClickListener(this);
			optC = (Button) findViewById(R.id.optC);
			optC.setOnClickListener(this);
			optD = (Button) findViewById(R.id.optD);
			optD.setOnClickListener(this);
			
			receive.execute();
			
		} catch (Exception e) {
			
			Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
			
		}
	}
	
	public void onClick(View v) {
		update();
		Toast.makeText(getBaseContext(), que.getAnswer(), Toast.LENGTH_LONG).show();
		switch(v.getId()){
		case R.id.optA:
			if(que.getAnswer().equals("A")){
				marks = marks + 1;
			}
			else{
			}
			break;
		case R.id.optB:
			if(que.getAnswer().equals("B")){
				marks = marks + 1;
			}
			else{
			}
			break;
		case R.id.optC:
			if(que.getAnswer().equals("C")){
			}
			else{
			}
			break;
		case R.id.optD:
			if(que.getAnswer().equals("D")){
				marks = marks + 1;
			}
			else{
			}
			break;
		}
	}
	
	CountDownTimer cdt;
	public void startTimer(int time){
			cdt = new CountDownTimer(time*1000,1000){
			
			@Override
			public void onFinish() {
				timer.setText("Done!");
				receive.send();
			}

			@Override
			public void onTick(long arg0) {
				timer.setText("" + arg0/1000);

			}

		}.start();
	}
	
	class Receive extends AsyncTask<Void,Void,Object>{
		
		private final int PORT = 8511;
		
		ServerSocketChannel ssChannel;
		SocketChannel soc;
		
		@Override
		public void onPreExecute(){
			System.out.println("Reception Started");
		}
		
		@Override
		protected Object doInBackground(Void... params) {
			try {
				ssChannel = ServerSocketChannel.open();
		        ssChannel.configureBlocking(true);
		        ssChannel.socket().bind(new InetSocketAddress(PORT));

		        soc = ssChannel.accept();
				
				ObjectInputStream in = new ObjectInputStream(soc.socket().getInputStream());
				Object obj =  in.readObject();
				
				return obj;
			} catch (StreamCorruptedException e) {
				return e;
			} catch (UnknownHostException e) {
				return e;
			} catch (IOException e) {
				return e;
			} catch (ClassNotFoundException e) {
				return e;
			}
		}
		
		public void send(){
			try{
				new Thread(){
					@Override
					public void run(){
						try{
							ObjectOutputStream oos = new ObjectOutputStream(soc.socket().getOutputStream());
							final Result result = new Result(Globals.userName,marks);
							oos.writeObject(result);
							runOnUiThread(new Runnable(){
								public void run(){
									System.out.println("Sent");
									Toast.makeText(Participant.this, result.getUserName(), Toast.LENGTH_LONG).show();
								}
							});
						}catch(final Exception e){
							runOnUiThread(new Runnable(){
								public void run(){
									e.printStackTrace();
									Toast.makeText(Participant.this, e.toString(), Toast.LENGTH_LONG).show();
								}
							});
						}
					}
				}.start();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		@Override
		public void onPostExecute(Object obj){
			if(obj instanceof QuestionListBundle){
				
				System.out.println("Reception Ended");
				
				qlb = (QuestionListBundle)obj;
				QueList = qlb.getAl();
				startQuiz();
			}
			else{
				//In case if an Exception Raised
				question.setBackgroundColor(Color.RED);
				question.setText("Unable to fetch questions");
				optA.setVisibility(View.GONE);
				optB.setVisibility(View.GONE);
				optC.setVisibility(View.GONE);
				optD.setVisibility(View.GONE);
			}
		}
	}
	
	
	int ListSize,i=0;
	public void startQuiz(){
		if(QueList.isEmpty()){
			Toast.makeText(this, "List Empty", Toast.LENGTH_SHORT).show();
		}
		else{
			Toast.makeText(this, "Quiz Started", Toast.LENGTH_SHORT).show();
			ListSize = QueList.size();
			startTimer(qlb.getTime());
			update();
		}
	}
	public void update(){
		if(i<=ListSize-1){
			que = QueList.get(i);
			optA.setText(que.getOptionA());
			optB.setText(que.getOptionB());
			optC.setText(que.getOptionC());
			optD.setText(que.getOptionD());
			question.setText(que.getQuestion());
			i++;
		}
		else{
			optA.setVisibility(View.GONE);
			optB.setVisibility(View.GONE);
			optC.setVisibility(View.GONE);
			optD.setVisibility(View.GONE);
			question.setVisibility(View.GONE);
			cdt.onFinish();
			System.out.println("Quiz Over");
		}
	}
}
