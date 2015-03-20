//Cylon.java
//Java version of the cylonStruct C++ structure from Cylon.h
//"By your command," - Cylon Centurion
//josh@mindaptiv.com

package com.mindaptiv.saul;

//imports
import java.lang.Integer;

public class Cylon 
{
	//names
	//TODO: add username
	//TODO: add deviceName
	
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
	//TODO: add time zone name
	
	//processor
	//TODO: add architecture
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
	//TODO: add pictureType
	
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
}
