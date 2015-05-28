//Saul.java
//interface for building Cylon with Saul functionality
//"I'll save it for a special occasion," - Saul Tigh
//josh@mindaptiv.com

package com.mindaptiv.saul;

import android.view.KeyEvent;
import android.view.MotionEvent;

public interface Saul 
{
	//Producers
	void produceUsername();
	void produceDeviceName();
	void produceDateTime();
	void produceProcessorInfo();
	void produceMemoryInfo();
	void produceInputDevices();
	void produceDisplayDevices();
	void produceStorageDevices();
	void produceSystemRumble();
	void produceGPS();
	void produceSensors();
	void produceCameras();
	void produceBluetoothDevices();
	void produceDevices();
	void produceAvatar();
	void produceUsbDevices();
	
	//Handler
	boolean handleKeyEvent(KeyEvent event);
	boolean handleMotionEvent(MotionEvent event);
	
}
