//Cylon.java
//Java version of the cylonStruct C++ structure from Cylon.h
//"By your command," - Cylon Centurion
//josh@mindaptiv.com

package com.mindaptiv.saul;

//imports
import java.lang.Integer;
import java.lang.String;

import android.os.Build;


public class Cylon implements Saul
{
	//Variable Declaration:
	//names
	String username;
	public String deviceName;
	
	//time
	Integer milliseconds;
	Integer seconds;
	Integer minutes;
	Integer hours;
	
	//date
	Integer day;
	Integer date;
	Integer month;
	Integer year;
	
	//time zone
	Integer dst;
	Integer timeZone;
	String timeZoneName;
	
	//processor
	String architecture;
	Integer pageSize;
	Integer processorCount;
	Integer allocationGranularity;
	//TODO: add minAppAddress
	//TODO: add maxAppAddress
	Integer hertz;
	
	//memory
	Integer memoryBytes;
	Integer osArchitecture;
	
	//avatar
	//TODO: add pictureLocation
	String pictureType;
	
	//devices
	Integer installedDeviceCount;
	Integer detectedDeviceCount;
	Integer portableStorageCount;
	Integer videoCount;
	Integer micCount;
	Integer speakerCount;
	Integer locationCount;
	Integer scannerCount;
	//TODO: add detectedDevices
	//TODO: add displayDevices
	//TODO: add controllers
	//TODO: add mice
	
	//error
	Integer error;
	//END variable declaration
	
	//Constructor
	public Cylon()
	{
		//producers
		this.produceDeviceName();
	}
	
	//Saul Methods
	//Producers
	public void produceUsername(Cylon saul)
	{
		//TODO: Grab content URI
		
	}
	
	public void produceDeviceName()
	{	
		//Grab model name
		this.deviceName = Build.MODEL;
	}
	
	public void produceDateTime(Cylon saul)
	{
		
	}
	
	public void produceProcessorInfo(Cylon saul)
	{
		
	}
	
	public void produceMemoryInfo(Cylon saul)
	{
		
	}
	
	public void produceInputDevices(Cylon saul)
	{
		
	}
	
	//wraps all other producers
	public void produceSaul(Cylon saul)
	{
		produceUsername(saul);
		produceDeviceName();
		produceDateTime(saul);
		produceMemoryInfo(saul);
		produceInputDevices(saul);
	}
	//END producers
}
