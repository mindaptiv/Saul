//Cylon.java
//Java version of the cylonStruct C++ structure from Cylon.h
//"By your command," - Cylon Centurion
//josh@mindaptiv.com

package com.mindaptiv.saul;

//imports
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.Integer;
import java.lang.String;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.regex.Pattern;
import java.util.TimeZone;





//import android.app.ActivityManager;
//import android.app.ActivityManager.MemoryInfo;
//import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;


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
	public String architecture;
	public Integer pageSize;
	public Integer processorCount;
	public Integer allocationGranularity;
	//TODO: add minAppAddress
	//TODO: add maxAppAddress
	public Float hertz;
	
	//memory
	public Integer memoryBytes;
	public Integer osArchitecture;
	public String bitStringTest;
	
	//avatar
	//TODO: add pictureLocation
	String pictureType;
	
	//devices
	Integer installedDeviceCount;
	public Integer detectedDeviceCount;
	Integer portableStorageCount;
	Integer videoCount;
	Integer micCount;
	Integer speakerCount;
	Integer locationCount;
	Integer scannerCount;
	public LinkedList<Device> detectedDevices;
	public LinkedList<Controller> controllers;
	public Integer keycode;
	//TODO: add displayDevices
	//TODO: add mice
	
	//error
	public Integer error;
	//END variable declaration
	
	//Constructor
	public Cylon()
	{
		//producers
		//TODO: add username
		this.produceDeviceName();
		this.produceDateTime();
		this.produceProcessorInfo();
		this.produceMemoryInfo();
		this.produceDevices();
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
	
	public void produceProcessorInfo()
	{
		//Grab and set OS Architecture
		String osArch 		= System.getProperty("os.arch", "0");
		this.architecture 	= osArch;
		
		//Set unobtainables to default error values
		this.pageSize 				= 0;
		this.allocationGranularity  = 0;
		//TODO: add min/max app addresses?
		
		//grab and set hertz
		try 
		{
			String cpuMaxFreq = "";
			RandomAccessFile file = new RandomAccessFile("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq", "r");
			cpuMaxFreq = file.readLine();
			file.close();
			Float hertz = Float.parseFloat(cpuMaxFreq);
			this.hertz = 1000 * hertz;
		} 
		catch (FileNotFoundException e) 
		{
			this.hertz = 0f;
		} 
		catch (IOException e) 
		{
			this.hertz = 0f;
		}
		catch (NumberFormatException e)
		{
			this.hertz = 0f;
		}
		
		//grab and set processor count
		//CpuFilter implementation credit to David @ stackoverflow
		class CpuFilter implements FileFilter
		{
			@Override
			public boolean accept(File pathname)
			{
				//Check if filename is cpu, followed by single digit number
				if(Pattern.matches("cpu[0-9]+", pathname.getName()))
				{
					return true;
				}
				return false;
			}//end accept()
		}//end class CpuFilter
		
		try
		{
			//Get CPU info directory
			File dir = new File("/sys/devices/system/cpu/");
			
			//Filter to only list the devices we care about
			File[] files = dir.listFiles(new CpuFilter());
			
			//Set core count to list length
			this.processorCount = files.length;
		} //end try
		catch(Exception e)
		{
			//set default value of 1 core (can't have 0 cores...can you? :O)
			this.processorCount = 1;
		}
		
	}
	
	public void produceMemoryInfo()
	{
		/*
		//grab and set total memory
		//credit to Badal @ stackoverflow
		MemoryInfo mi = new MemoryInfo();
		MockContext mock = new MockContext();
		try
		{
			//NEED CONTEXT CLASS :(
			ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
			activityManager.getMemoryInfo(mi);
			this.memoryBytes = mi.totalMem;
		}
		catch (Exception e)
		{
			//if this fails set the default
			this.memoryBytes = 0;
		}
		*/
		
		//grab and set os bit-level architecture type
		//Android uses VM's but can still determine JVM's architecture for native code to use
		String osArch = System.getProperty("sun.arch.data.model");
		
		//error/unknown/invalid/missing case
		this.osArchitecture = 0;
		
		//otherwise
		if(osArch == "32" || osArch == "64")
		{
			this.osArchitecture = Integer.parseInt(osArch);
		}
	}
	
	public void produceInputDevices()
	{
		//Retrieve ids of available input devices
		int[] ids = InputDevice.getDeviceIds();
		InputDevice[] devices = new InputDevice[ids.length];
		
		//iterate through the ids and populate the devices array with new InputDevice objects
		for (int i = 0; i < ids.length; i++)
		{
			//ith device is retrieved from ith id
			devices[i] = InputDevice.getDevice(ids[i]);
			Device device = new Device(InputDevice.getDevice(ids[i]));
			this.detectedDevices.addLast(device);
			
			//build controller (if appropriate)
			if (device.deviceType == 11)
			{
				Controller controller = new Controller(device, devices[i]);
				
				//insert into controllers
				this.controllers.addLast(controller);
				
				//set the controller index of the corresponding device
				device.controllerIndex = this.controllers.size();
			}
		}//end for
		
	}//end produceInputDevices()

	public void produceDevices()
	{
		//create lists
		this.detectedDevices = new LinkedList<Device>();
		this.controllers	 = new LinkedList<Controller>();
		
		//wrap all other device producers
		produceInputDevices();
		
		//TODO: grab vibration sensor
		
		
		//set count
		this.detectedDeviceCount = this.detectedDevices.size();
	}
	//END producers
	
	//controller.buttons mapping
	/*
	 * 1st Byte: (Right)(Left)(Down)(Up)
	 * 2nd Byte: (Right Thumbstick)(Left Thumbstick)(Back)(Start)
	 * 3rd Byte: (Don't Care)(Don't Care)(R1)(L1)
	 * 4th Byte: (Y)(X)(B)(A)
	 */
	
	//key event handler
	public boolean handleKeyEvent(KeyEvent event)
	{
		Log.i("Saul", "Fired handler.");
		this.keycode = event.getKeyCode();
		
		//Verify ID of source
		for (int i = 0; i < this.controllers.size(); i++)
		{
			
			//check if any controller ID matches the source of the event
			if(Integer.parseInt(controllers.get(i).superDevice.id) == event.getDeviceId())
			{
				//test
				Log.i("Saul", "Controller bitmask before:" + controllers.get(i).buttons);
				
				//if true, then parse code of event
				//Variable declaration
				int key = event.getKeyCode();
				
				//test
				controllers.get(i).keycode = key;
				
				//parse event code
				//parse keycode
				if (key == KeyEvent.KEYCODE_BUTTON_A)
				{
				 
				}
				else if (key == KeyEvent.KEYCODE_BUTTON_B)
				{
					
				}
				else if (key == KeyEvent.KEYCODE_BUTTON_X)
				{
					
				}
				else if (key == KeyEvent.KEYCODE_BUTTON_Y)
				{
					
				}
				else if (key == KeyEvent.KEYCODE_BUTTON_B)
				{
					
				}
				else if (key == KeyEvent.KEYCODE_BUTTON_L1)
				{
					
				}
				else if (key == KeyEvent.KEYCODE_BUTTON_R1)
				{
					
				}
				else if (key == KeyEvent.KEYCODE_BUTTON_START)
				{
					
				}
				else if (key == KeyEvent.KEYCODE_BUTTON_SELECT)
				{
					
				}
				else if (key == KeyEvent.KEYCODE_BUTTON_THUMBL)
				{
					
				}
				else if (key == KeyEvent.KEYCODE_BUTTON_THUMBR)
				{
					
				}
				else if (key == KeyEvent.KEYCODE_DPAD_UP)
				{
					
				}
				else if (key == KeyEvent.KEYCODE_DPAD_DOWN)
				{
					
				}
				else if (key == KeyEvent.KEYCODE_DPAD_LEFT)
				{
					
				}
				else if (key == KeyEvent.KEYCODE_DPAD_RIGHT)
				{
					
				}
				
				//test
				Log.i("Saul", "Controller bitmask after:" + controllers.get(i).buttons);
				
				//return
				return true;
			}
		}//END for
		
		//test
		Log.i("Saul", "No controller located.");
		
		//if no controller matched, return false
		return false;
	}//END handler
	
	public void testLog()
	{
        //Logging
        Log.i("Saul", "REPORT:\n");
        Log.i("Saul", "Cylon: " + this.toString() + "\n");
        Log.i("Saul", "Username: " + this.username + "\n");
		Log.i("Saul", "Device Name: " + this.deviceName + "\n");
		Log.i("Saul", "Date: " + this.day + " " + this.month + "/" + this.date + "/" + this.year + "\n");
		Log.i("Saul", "Time: " + this.hours + ":" + this.minutes + ":" + this.seconds + ":" + this.milliseconds + "\n");
		Log.i("Saul", "Time Zone: " + this.timeZoneName + "=" + this.timeZone + ", dst = " + this.dst + "\n");
		Log.i("Saul", "System Architecture: " + this.architecture + "\n");
		Log.i("Saul", "Page Size: " + this.pageSize + "\n");
		Log.i("Saul", "Allocation Granularity: " + this.allocationGranularity + "\n");
		Log.i("Saul", "CPU Speed: " + this.hertz + "\n");
		Log.i("Saul", "CPU Core Count: " + this.processorCount + "\n");
		//Log.i("Saul", "Installed Memory: " + this.memoryBytes + "\n");
		Log.i("Saul", "Bit Architecture: " + this.osArchitecture + "\n");
		Log.i("Saul", "Detected Device Count: " + this.detectedDeviceCount + "\n");
		
		for(int i = 0; i < this.detectedDevices.size(); i++)
		{
			Log.i("Saul", "     Device #" + i + ": " + "\n" + "          Name = " + this.detectedDevices.get(i).name + 
					"\n" + "          ID = " +Integer.toHexString(Integer.parseInt(this.detectedDevices.get(i).id)) + 
					"\n" + "          Vendor ID = " + Integer.toHexString(this.detectedDevices.get(i).vendorID) + 
					"\n" + "          Bitmask = " + Integer.toHexString(this.detectedDevices.get(i).testMask) + "\n"
 				  + "\n" + "          Type = " + this.detectedDevices.get(i).deviceType + "\n");
		}
		for(int i =0; i < this.controllers.size(); i++)
		{
			Log.i("Saul", "     Controller #" + i + ": " + "\n" + "          Key Code = " + this.controllers.get(i).keycode + 
					"\n" + "          ID = " +Integer.toHexString(Integer.parseInt(this.detectedDevices.get(i).id)) + 
					"\n" + "          Vendor ID = " + Integer.toHexString(this.detectedDevices.get(i).vendorID) + 
					"\n" + "          Bitmask = " + Integer.toHexString(this.detectedDevices.get(i).testMask) + "\n"
 				  + "\n" + "          Type = " + this.detectedDevices.get(i).deviceType + "\n");
		}
	
		Log.i("Saul", "Error Code: " + this.error + "\n");
	}//end testLog
}//END class
