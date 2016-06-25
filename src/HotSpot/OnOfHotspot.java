package HotSpot;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

/*
 * 
 * This class helps creating the hotspot and configuring it.
 * 
 */

public class OnOfHotspot {
	
	public static WifiConfiguration config;
	
	//check whether wifi hotspot on or off
	
	public static boolean isApOn(Context context) {
	    WifiManager wifimanager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);     
	    try {
	        Method method = wifimanager.getClass().getDeclaredMethod("isWifiApEnabled");
	        method.setAccessible(true);
	        return (Boolean) method.invoke(wifimanager);
	    }
	    catch (Throwable ignored) {}
	    return false;
	}

	// toggle wifi hotspot on or off
	public static boolean configApState(Context context,boolean apState) {
	    WifiManager wifimanager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
	    wifimanager.setWifiEnabled(false);
	    try {  
	        if(apState==true){
	        	
	        	System.out.println(config.SSID);
	        	
	        	WifiConfiguration netConfig = new WifiConfiguration();
	    	    netConfig.SSID = "\""+Globals.Globals.userName +"'s Quiz "+"\"";
	    		
	    		netConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
	        	
	        	Method method = wifimanager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);                   
	        	method.invoke(wifimanager, netConfig, apState);
	        	return true;
	        }
	        else{
	        	System.out.println(config.SSID);
	        	
	        	Method method = wifimanager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
	        	method.invoke(wifimanager, config, false);
	        	method.invoke(wifimanager, config, true);
	        	method.invoke(wifimanager, config, false);
	        	return true;
	        }
	    } 
	    catch (Exception e) {
	        e.printStackTrace();
	    }       
	    return false;
	}

	
	public static WifiConfiguration getApConfiguration(Context context){
		
		WifiConfiguration config = null;
		
		WifiManager wifimanager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		Method[] methods = wifimanager.getClass().getDeclaredMethods();
		for (Method m: methods) {           
		    if (m.getName().equals("getWifiApConfiguration")) {
		        try {
					config = (WifiConfiguration)m.invoke(wifimanager);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
		    }
		}
		
		OnOfHotspot.config = config;
		
		return config;

	}
}