//Device.java
//Java container for exporting Android Sensor data to Native Code
//josh@mindaptiv.com

package com.mindaptiv.saul;

import android.os.Build;

public class Sensor 
{
	//Variable Declaration
	Device  superDevice;
	Integer minDelay;
	Integer type;  //based on Android Sensor values
	Integer version;
	String  name;
	String  vendor;
	float   power;
	float	resolution;
	float maxRange;
	
	//19+ Data
	Integer fifoMaxEventCount;
	Integer fifoReservedEventCount;
	
	//20+ Data
	String stringType; //based on Android Sensor values
	
	//21+ Data
	Integer maxDelay;
	Integer reportingMode; //based on Android Sensor values
	Integer isWakeUpSensor;
	//END Variable Declaration
	
	//TODO: add Constructor
	public Sensor(android.hardware.Sensor sensor, Device device)
	{
		//Grab data
		this.superDevice 	= device;
		this.maxRange 		= sensor.getMaximumRange();
		this.minDelay 		= sensor.getMinDelay();
		this.type			= sensor.getType();
		this.version		= sensor.getVersion();
		this.name			= sensor.getName();
		this.vendor			= sensor.getVendor();
		this.power			= sensor.getPower();
		this.resolution		= sensor.getResolution();
		
		//if we're on version 19 or later
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
		{
			this.fifoMaxEventCount 		= sensor.getFifoMaxEventCount();
			this.fifoReservedEventCount = sensor.getFifoReservedEventCount();
		}
		else
		{
			//default values for unavailable fields
			this.fifoMaxEventCount 		= 0;
			this.fifoReservedEventCount = 0;
		}
		
		//if we're on version 20 or later
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH)
		{
			this.stringType = sensor.getStringType();
		}
		else
		{
			//default values for unavailable fields	
			this.stringType = "0";
		}
		
		//if we're on version 21 or later
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
		{
			this.maxDelay = sensor.getMaxDelay();
			this.reportingMode = sensor.getReportingMode();
			this.isWakeUpSensor = 0;
			if (sensor.isWakeUpSensor())
			{
				this.isWakeUpSensor = 1;
			}//end wake up
		}
		else
		{
			//default values for unavailable fields
			this.maxDelay = 0;
			this.reportingMode = 0;
			this.isWakeUpSensor = 0;
		}
	}
}
