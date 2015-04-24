//Storage.java
//Java version of the storageStruct structure from Cylon.h
//josh@mindaptiv.com

package com.mindaptiv.saul;

public class Storage 
{
	//Variable Declaration
	//Related Device object for this Storage object
	Device superDevice;
	
	//Path to access the storage device in the file system
	String path;
	
	//Space
	long bytesAvails;
	long totalBytes;
	//end variable declaration
	
	//Constructor
	public Storage(Device suprah, String location, long available, long total)
	{
		//Set arguments
		this.superDevice = suprah;
		this.path		 = location;
		this.bytesAvails = available;
		this.totalBytes  = total; 
	}
}//end class 
