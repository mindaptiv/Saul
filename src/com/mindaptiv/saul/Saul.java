//Saul.java
//interface for building Cylon with Saul functionality
//"I'll save it for a special occasion," - Saul Tigh
//josh@mindaptiv.com

package com.mindaptiv.saul;

public interface Saul 
{
	//Producers
	void produceUsername(Cylon saul);
	void produceDeviceName(Cylon saul);
	void produceDateTime(Cylon saul);
	void produceProcessorInfo(Cylon saul);
	void produceMemoryInfo(Cylon saul);
	void produceInputDevices(Cylon saul);
	
	void produceSaul(Cylon saul);
}
