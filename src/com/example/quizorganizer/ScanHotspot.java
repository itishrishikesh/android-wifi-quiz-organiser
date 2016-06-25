package com.example.quizorganizer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

import android.content.Context;

import Data.QuestionListBundle;
import Data.Result;

/*
 * 
 * Now, this is really messed thing that I have created.
 * 
 * The ScanHotspot is a helper class which includes code for scanning the hotspot
 * to get the IP Addresses of the connected clients and to initiate a socket 
 * connection subsequently.
 * 
 * The ScanHotspot class also starts sending the questions immediately after its 
 * own initialisation.
 * 
 * Now, (One more thing that is messed up) 
 * It also includes a function, which contains a thread which in turn waits for 
 * results.
 * 
 */

public class ScanHotspot extends Thread{

	private ArrayList<String> IpAddr = new ArrayList<String>();
	private QuestionListBundle qlb;
	
	Context context;
	
	public ScanHotspot(QuestionListBundle qlb,Context context){
		this.qlb = qlb;
		this.context = context;
	}
	
	public ScanHotspot(){}
	
	public ArrayList<String> getIpAddr() {
		return IpAddr;
	}
	Send send;
	public static ArrayList<Send> SendList = new ArrayList<Send>();
	
	@Override
	public void run(){
		
		getClientList();
		for(String a : IpAddr){
			System.out.println(qlb.toString()+"ScanHotSpot");
			send = new Send(qlb,a,context);
			send.execute();
			SendList.add(send);
		}
		
		System.out.println("UserCount:"+Globals.Globals.userCount);
		
		startReceiveThread();
	}
	
	int userCount;
	
	public void getClientList() {
		userCount = 0;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("/proc/net/arp"));
			String line;
			while ((line = br.readLine()) != null) {
				String[] splitted = line.split(" +");
				if (splitted != null ) {
					String mac = splitted[3];
					System.out.println("Mac : Outside If "+ mac );
					if (mac.matches("..:..:..:..:..:..")) {
						userCount++;
						IpAddr.add(splitted[0]);
					}
					Globals.Globals.userCount = userCount;
				}
			}
		} catch(Exception e) {
			System.out.println(e.toString());
		}               
	}
	
	public void startReceiveThread(){
		new Thread(){
			@Override
			public void run(){
				try {
					while(Globals.Globals.sendCount != Globals.Globals.userCount){
						System.out.print("Sending.....");
					}
					for(Send send:SendList){
						ObjectInputStream oin = new ObjectInputStream(send.soc.socket().getInputStream());
						Result result = (Result)oin.readObject();

						synchronized(Globals.Globals.resultList){
							Globals.Globals.resultList.add(result);
						}

						System.out.println(Globals.Globals.receiveCount++);
					}
				} catch (StreamCorruptedException e) {
					e.printStackTrace();
				} catch (OptionalDataException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
}
