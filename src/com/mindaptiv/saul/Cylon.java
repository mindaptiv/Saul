//Cylon.java
//Java version of the cylonStruct C++ structure from Cylon.h
//"By your command," - Cylon Centurion
//josh@mindaptiv.com

package com.mindaptiv.saul;

//imports
import java.lang.Integer;
import java.lang.String;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import android.os.Build;


public class Cylon implements Saul
{
	//Variable Declaration:
	//names
	public String username;
	public String deviceName;
	
	//time
	public Integer milliseconds;
	public Integer seconds;
	public Integer minutes;
	public Integer hours;
	
	//date
	public Integer day;
	public Integer date;
	public Integer month;
	public Integer year;
	
	//time zone
	public Integer dst;
	public Integer timeZone;
	public String timeZoneName;
	
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
		this.produceDateTime();
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
	
	public void produceDateTime()
	{
		//Grab the current date/time
		Date now = new Date();
		
		//Grab the calendar and time zone
		Calendar calendar = new GregorianCalendar();
		TimeZone timeZone = calendar.getTimeZone();
		
		//Set dst boolean
		this.dst = 0;
		if(timeZone.inDaylightTime(now) == true)
		{
			this.dst = 1;
		}
		
		//Calculate offset in minutes +/- UTC, then set timeZone value
		Integer offset 	= timeZone.getRawOffset();
		offset 			= offset/60000;
		this.timeZone 	= offset;
		
		//Retrieve the display name of the time zone
		//NOTE: presently uses default locale, if issues persist this may need to change
		this.timeZoneName = timeZone.getDisplayName(timeZone.inDaylightTime(now), TimeZone.LONG);
		
		//Grab and set Date
		int date = calendar.get(Calendar.DATE);
		
		this.date = 0;
		if(date >= 1 || date <= 31)
		{
			this.date = date;
		}
		
		//Grab and set Day
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		
		this.day = 0;
		if(day >= 1 || day <= 7)
		{
			this.day = day;
		}
		
		//Grab and set Month
		int month = calendar.get(Calendar.MONTH);
		month	  = month + 1; //java starts counting months at 0
		
		this.month = 0;
		if(month >= 1 || month <= 12)
		{
			this.month = month;
		}
		
		//Grab and set Year
		int year = calendar.get(Calendar.YEAR);
		
		this.year = 0;
		if(year > 0)
		{
			this.year = year;
		}
		
		//Grab and set milliseconds
		int milliseconds = calendar.get(Calendar.MILLISECOND);
		
		this.milliseconds = 0;
		if (milliseconds >= 0 || milliseconds <= 999)
		{
			this.milliseconds = milliseconds;
		}
		
		//Grab and set seconds
		int seconds = calendar.get(Calendar.SECOND);
		
		this.seconds = 0;
		if(seconds >= 0 || seconds <= 59)
		{
			this.seconds = seconds;
		}
		
		//Grab and set minutes
		int minutes = calendar.get(Calendar.MINUTE);
		
		this.minutes = 0;
		if(minutes >= 0 || minutes <= 59)
		{
			this.minutes = minutes;
		}
		
		//Grab and set hours
		int hours = calendar.get(Calendar.HOUR_OF_DAY);
		
		this.hours = 0;
		if(hours >= 1 || hours <= 23)
		{
			this.hours = hours;
		}
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
	

	//END producers
}
