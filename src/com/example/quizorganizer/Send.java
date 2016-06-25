package com.example.quizorganizer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import Data.QuestionListBundle;
import android.content.Context;
import android.os.AsyncTask;
/*
 * 
 * Send class is an AsyncTask which is designed for sending questions to the participants.
 * 
 * It is executed when the hotspot is scanned for connected users.
 * 
 */
public class Send extends AsyncTask<Void,Void,Void> {
	
	private String ip;
	private final int PORT = 8511;
	
	Context context;
	
	SocketChannel soc;
	QuestionListBundle q;
	
	
	public Send(QuestionListBundle q,String ip,Context context) {
		this.ip = ip;
		this.q = q;
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		System.out.println(ip);
	}
	
	@Override
	protected Void doInBackground(Void... arguments) {
		try {
			
			soc = SocketChannel.open();
			soc.connect(new InetSocketAddress(ip,PORT));
					
					
			ObjectOutputStream oos = new ObjectOutputStream(soc.socket().getOutputStream());
			oos.writeObject(q);
			
			Globals.Globals.sendCount++;
		} catch (IOException e) {
			System.out.println(e.toString() + "error in sending");
		} 
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result) {	
		System.out.println("Done Sending");
	}
}
