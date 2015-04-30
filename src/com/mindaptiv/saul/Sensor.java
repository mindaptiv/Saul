//Device.java
//Java container for exporting Android Sensor data to Native Code
//josh@mindaptiv.com

package com.mindaptiv.saul;

public class Sensor 
{
	//Variable Declaration
	Device  superDevice;
	Integer maxRange;
	Integer minDelay;
	Integer type;  //based on Android Sensor values
	Integer version;
	String  name;
	String  vendor;
	float   power;
	float	resolution;
	
	//19+ Data
	Integer fifoMaxEventCount;
	Integer fifoReservedEventCount;
	
	//20+ Data
	String stringType;
	
	//21+ Data
	Integer maxDelay;
	Integer reportingMode;
	Integer isWakeUpSensor;
	//END Variable Declaration
	
	//TODO: add Constructor
}
