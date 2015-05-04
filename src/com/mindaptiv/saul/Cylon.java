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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.display.*; //some contents that we want to access are only available in later versions, hence ".*" (no ifdef in Java)
import android.location.LocationManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.text.TextUtils;
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
	public long memoryBytes;
	public long threshold;
	public long bytesAvails;
	public Integer lowMemory;
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
	public LinkedList<Display> displays;
	public LinkedList<Storage> storages;
	public LinkedList<Sensor> sensors;
	public Integer keycode;
	String[] cameras; //For API 21+
	
	//Android
	Context context;
	Application app;
	
	//error
	public Integer error;
	//END variable declaration
	
	
	//Constructor
	public Cylon(Context context, Application app)
	{
		//Context
		this.context = context;
		this.app     = app;
				
		//producers
		this.produceUsername();
		this.produceDeviceName();
		this.produceDateTime();
		this.produceProcessorInfo();
		this.produceMemoryInfo();
		this.produceDevices();
	}
	
	//Saul Methods
	//Producers
	public void produceUsername()
	{
		//Credit to JoelFernandes @ stack overflow for partial display_name code
		Cursor c = this.app.getContentResolver().query(ContactsContract.Profile.CONTENT_URI, null, null, null, null);
		if(!(c.getCount() <= 0))
		{
			c.moveToFirst();
			
			//set username
			this.username = c.getString(c.getColumnIndex(ContactsContract.Profile.DISPLAY_NAME_PRIMARY));
		}
		else
		{
			//username unavailable, set to missing value to prevent exception
			this.username = "0";
		}
			
		//Close the cursor
		c.close();
	}//END produce username
	
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
		//grab and set total memory
		//credit to Badal @ stackoverflow
		MemoryInfo mi = new MemoryInfo();
		
		try
		{
			//retrieve memory metrics
			ActivityManager activityManager = (ActivityManager) this.context.getSystemService(Context.ACTIVITY_SERVICE);
			activityManager.getMemoryInfo(mi);
			this.memoryBytes = mi.totalMem;
			this.bytesAvails = mi.availMem;
			this.threshold   = mi.threshold;
			this.lowMemory   = 0;
			if(mi.lowMemory)
			{
				this.lowMemory = 1;
			}
			
		}
		catch (Exception e)
		{
			//if this fails set the default
			this.memoryBytes = 0;
		}
		
		
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
				device.controllerIndex = this.controllers.size() - 1;
			}
		}//end for
		
	}//end produceInputDevices()
	
	@SuppressLint("InlinedApi")
	public void produceDisplayDevices()
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
		{	
			//Variable Declaration
			//NOTE: API 17+ only
			DisplayManager displayManager = (DisplayManager) context.getSystemService(Context.DISPLAY_SERVICE);
			android.view.Display[] displaysies = displayManager.getDisplays();
			
			
			//create Device and  Display objects for each item in displaysies
			for (int i = 0; i < displaysies.length; i++)
			{	
				//Create display-based context for each Display
				//NOTE: API 17+ only
				//partial code credit to Tommy Visic @ stackoverflow
				Context displayContext = this.context.createDisplayContext(displaysies[i]);
				
				//Create new device using Display
				Device device = new Device(displaysies[i]);
				
				//add to devices list
				this.detectedDevices.addLast(device);
				
				//Create display device
				com.mindaptiv.saul.Display display = new Display(displaysies[i], displayContext, device);
				
				//Add to list of displays
				this.displays.addLast(display);
				device.displayIndex = this.displays.size() - 1;
			}//END FOR
		}//END if
		else
		{
			return;
		}//END else
		
	}//END produce display devices
	
	/*
	 * TODO: eventually get a working printer function in place
	public void producePrintingDevices()
	{
		//only run on 19+
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
		{	
			//internal service class
			class SaulPrintService extends android.printservice.PrintService
			{
				//
				private final String TAG = SaulPrintService.class.getSimpleName();
				
				private Handler mHandler = new Handler();
				
				
				@Override
				protected PrinterDiscoverySession onCreatePrinterDiscoverySession() 
				{
					return null;
				}

				@Override
				protected void onRequestCancelPrintJob(PrintJob printJob) 
				{
				
				}

				@Override
				protected void onPrintJobQueued(PrintJob printJob) 
				{
					
				}
			}
			
			//internal discovery class
			//credit to yagi on HDfpga for partial discovery code and the chromium open source project for partial discovery code
			class SaulPrintDiscovery extends android.printservice.PrinterDiscoverySession
			{
				//Variable Declarations
				private android.printservice.PrintService printService;
				
				private final static String PRINTER_ID = "Saul.Print.Service";
				
				//Constructor
				public SaulPrintDiscovery(android.printservice.PrintService service)
				{
					this.printService = service;
				}
				
				
				@SuppressLint("InlinedApi")
				@Override
				public void onStartPrinterDiscovery(List<PrinterId> priorityList) 
				{	
					//Variable Declaration
					PrinterId id = this.printService.generatePrinterId(PRINTER_ID);
					PrinterInfo.Builder builder = new PrinterInfo.Builder(id, PRINTER_ID, PrinterInfo.STATUS_IDLE);
					PrinterInfo info = builder.build();
					List<PrinterInfo> infos = new ArrayList<PrinterInfo>();
					infos.add(info);
					addPrinters(infos);
				}

				@Override
				public void onStopPrinterDiscovery() 
				{
					//meh
				}

				@Override
				public void onValidatePrinters(List<PrinterId> printerIds) 
				{
					//meh
				}

				@Override
				public void onStartPrinterStateTracking(PrinterId printerId) 
				{
					//meh?
				}

				@Override
				public void onStopPrinterStateTracking(PrinterId printerId) 
				{
					//meh
				}

				@Override
				public void onDestroy() 
				{
					//meh
				}	
			}//end class
			
			
			//List for handling retrieved Printer info
			List<android.print.PrinterId> printers = new ArrayList<android.print.PrinterId>();
			List<android.print.PrinterInfo> printerInfo = new ArrayList<android.print.PrinterInfo>();
			
			//Variable declaration
			SaulPrintService service = new SaulPrintService();
			SaulPrintDiscovery session = new SaulPrintDiscovery(service);
			
			//start session
			session.onStartPrinterDiscovery(printers);
	
			//get printers
			printerInfo = session.getPrinters();
			session.addPrinters(printerInfo);
			
			//stop session
			session.onStopPrinterDiscovery();
			
			Log.i("Saul", "Printer Count: " + printerInfo.size()); 
		}//end if
		else
		{
			return;
		}
	}//end printers
	*/
	
	@SuppressWarnings("deprecation")
	public void produceStorageDevices()
	{	
		//Credit to Yaroslav Boichuk @ stackoverflow for partial storage size code
		//Credit to Dmitriy Lozenko @ Stackoverflow for partial storage directories code
		
		//Variable declaration
		long bytesAvails = 0;
		long totalBytes = 0;
		
		//directory separator
		final Pattern DIR_SEPORATOR = Pattern.compile("/");
		
		//Final set of paths
		final Set<String> rv = new HashSet<String>();
		
		//Primary External Storage (may be internal flash on some devices)
		final String rawExternalStorage = System.getenv("EXTERNAL_STORAGE");
		
		//Second SD-cards separated by ":"
		final String rawSecondaryStoragesStr = System.getenv("SECONDARY_STORAGE");
		
		//Primary emulated SD-Card
		final String rawEmulatedStorageTarget = System.getenv("EMULATED_STORAGE_TARGET");
		
		//NOTE: in cases where an SD card may not be present, etc. multiple paths located may be referring to the same physical storage (with one path emulated, and one path physical)
		if(TextUtils.isEmpty(rawEmulatedStorageTarget))
		{
			//Device has "physical external storage", use plain paths
			if(TextUtils.isEmpty(rawExternalStorage))
			{
				//EXTERNAL_STORAGE undefined; use default sdcard path
				rv.add("/storage/sdcard0");
			}//end if external undef
			else
			{
				rv.add(rawExternalStorage);
			}//END else
		}
		else
		{
			//Device has emulated storage; external storage paths should have userIdburned into them
			final String rawUserId;
			if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1)
			{
				//Default to empty for older platforms
				rawUserId = "";
			}
			else
			{
				//Variable Declaration
				final String path = Environment.getExternalStorageDirectory().getAbsolutePath();
				final String[] folders = DIR_SEPORATOR.split(path);
				final String lastFolder = folders[folders.length - 1];
				boolean isDigit = false;
				
				try
				{
					Integer.valueOf(lastFolder);
					isDigit = true;
				}
				catch(NumberFormatException ignored)
				{
					//empty
				}
				
				rawUserId = isDigit ? lastFolder :"";
			}//END else
			
			// /storage/emulation/n
			if(TextUtils.isEmpty(rawUserId))
			{
				rv.add(rawEmulatedStorageTarget);
			}
			else
			{
				rv.add(rawEmulatedStorageTarget + File.separator + rawUserId);
			}
		}//END outer else
		
		//Add all secondary storages
		if(!TextUtils.isEmpty(rawSecondaryStoragesStr))
		{
			//All secondary SD-Cards split into array
			final String[] rawSecondaryStorages = rawSecondaryStoragesStr.split(File.pathSeparator);
			Collections.addAll(rv, rawSecondaryStorages);
		}
		
		//Cast to array of paths (of type String)
		String[] paths = rv.toArray(new String[rv.size()]);
		
		for (int i = 0; i < paths.length; i++ )
		{	
			//Create new File system stats
			StatFs stats = new StatFs(paths[i]);
			
			//Check if default
			boolean isDefault = false;
			if (paths[i].equals(Environment.getExternalStorageDirectory().getPath()))
			{
				//if path is the same as the environment's primary external storage directory, treat as default storage location
				isDefault = true;
			}
			
			//check if emulated
			//credit to Matt Quail @ stackoverflow for pattern checking code
			boolean isEmulated = false;
			if(Pattern.compile(Pattern.quote(rawEmulatedStorageTarget), Pattern.CASE_INSENSITIVE).matcher(paths[i]).find())
			{
				isEmulated = true;
			}
			
			//get size in bytes
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2)
			{
				//code added in API 18
				totalBytes = stats.getBlockSizeLong() * stats.getBlockCountLong();
				bytesAvails = stats.getBlockSizeLong() * stats.getAvailableBlocksLong();
			}
			else
			{
				//code deprecated in API 18
				totalBytes = (long)stats.getBlockSize() * (long)stats.getBlockCount();
				bytesAvails = (long)stats.getBlockSize() * (long)stats.getAvailableBlocks();
			}
			
			//Create new Device object
			Device device = new Device(paths[i], isDefault);
			this.detectedDevices.addLast(device);
			
			//Create new Storage object
			Storage storage = new Storage(device, paths[i], bytesAvails, totalBytes, isEmulated);
			this.storages.addLast(storage);
			this.detectedDevices.getLast().storageIndex = this.storages.size() - 1;
		}//END for
		
		//TODO: handle OTG storage
	
	}//end produce storage
	
	//produce rumble device info
	public void produceSystemRumble()
	{
		//Credit to 
		//Retrieve the service
		Vibrator rumble = (Vibrator) this.context.getSystemService(Context.VIBRATOR_SERVICE);
		
		//Check if a rumble device is present onboard
		if(rumble.hasVibrator())
		{
			//Credit to Conroy Whitney @ android.konreu.com for SOS code
			int dot = 200;      // Length of a Morse Code "dot" in milliseconds
			int dash = 500;     // Length of a Morse Code "dash" in milliseconds
			int short_gap = 200;    // Length of Gap Between dots/dashes
			int medium_gap = 500;   // Length of Gap Between Letters
			int long_gap = 100;    // Length of Gap Between Words
			@SuppressWarnings("unused")
			long[] sos = {
			    0,  // Start immediately
			    dot, short_gap, dot, short_gap, dot,    // s
			    medium_gap,
			    dash, short_gap, dash, short_gap, dash, // o
			    medium_gap,
			    dot, short_gap, dot, short_gap, dot,    // s
			    long_gap
			};
			
			//Credit to gearside.com for the vibration patterns
			@SuppressWarnings("unused")
			long[] mario = {0, 125,75,125,275,200,275,125,75,125,275,200,600,200,600};
			@SuppressWarnings("unused")
			long[] empire = {0, 500,110,500,110,450,110,200,110,170,40,450,110,200,110,170,40,500};
			@SuppressWarnings("unused")
			long[] turtles = {0, 75,75,75,75,75,75,75,75,150,150,150,450,75,75,75,75,75,525};
			@SuppressWarnings("unused")
			long [] mj = {0, 0,300,100,50,100,50,100,50,100,50,100,50,100,50,150,150,150,450,100,50,100,50,150,150,150,450,100,50,100,50,150,150,150,450,150,150};
			@SuppressWarnings("unused")
			long[] powerRangers = {0, 150,150,150,150,75,75,150,150,150,150,450};
			//rumble.vibrate(empire, -1);
			
			//Create new device
			Device device = new Device();
			this.detectedDevices.addLast(device);
		}
	} //END rumble producer
	

	public void produceGPS()
	{
		//Grab location manager
		LocationManager manager = (LocationManager)this.context.getSystemService(Context.LOCATION_SERVICE);
		
		//Determine if GPS is listed as a provider for location data
		ArrayList<String> providers = (ArrayList<String>) manager.getAllProviders();
		boolean flag = false;
		for (int i = 0; i < providers.size(); i++)
		{
			if(providers.get(i).equals(LocationManager.GPS_PROVIDER))
			{
				flag = true;
			}
		}
		
		if(flag)
		{
			//determine if enabled
			boolean enabled = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
			
			//Create device
			Device device = new Device(enabled);
			this.detectedDevices.addLast(device);
		}//end if detected
	}//END produce GPS
	
	public void produceSensors()
	{
		//grab manager
		SensorManager manager = (SensorManager) this.context.getSystemService(Context.SENSOR_SERVICE);
		
		//Grab sensors
		List<android.hardware.Sensor> sensorList = manager.getSensorList(android.hardware.Sensor.TYPE_ALL);
		
		for(int i = 0; i < sensorList.size(); i++)
		{
			//Create objects, add to appropriate lists, and map to each other
			Integer defaulty = 0;
			
			//determine if the sensors is the default for its type
			if(sensorList.get(i) == manager.getDefaultSensor(sensorList.get(i).getType()))
			{
				defaulty = 1;
			}//end IF
			
			Device device = new Device(sensorList.get(i), defaulty);
			Sensor sensor = new Sensor(sensorList.get(i), device);
			sensors.addLast(sensor);
			device.sensorsIndex = sensors.size() - 1;
			this.detectedDevices.addLast(device);
		}
	}
	
	//produce info on cameras
	@SuppressLint("InlinedApi")
	@SuppressWarnings("deprecation")
	public void produceCameras()
	{
		//Use old deprecated Camera class
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
		{
			int numCams = Camera.getNumberOfCameras();
			
			for(int i = 0; i < numCams; i++)
			{
				//Grab info for the camera
				CameraInfo info = new CameraInfo();
				Camera.getCameraInfo(i, info);
				
				//Create and store Device object
				Device device = new Device(i, info);
				this.detectedDevices.addLast(device);
			}//END for
		}//END if
		//use camera2 package
		else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
		{
			//Grab manager
			CameraManager manager = (CameraManager) this.context.getSystemService(Context.CAMERA_SERVICE);

			try 
			{
				//Grab camera ID's
				String[] cameras = manager.getCameraIdList();
				this.cameras 	 = cameras;
				
				//Open Cameras
				for (int i = 0; i < cameras.length; i++)
				{
					//Grab camera characteristics
					CameraCharacteristics stats = manager.getCameraCharacteristics(cameras[i]);
					
					//Create Device and store
					Device device = new Device(i, stats, cameras[i]);
					this.detectedDevices.addLast(device);
				}
				
			} 
			catch (CameraAccessException e) 
			{
				//Bail!
				return;
			}
			
		}
	}//END produceCameras
	
	public void produceDevices()
	{
		//create lists
		this.detectedDevices = new LinkedList<Device>();
		this.controllers	 = new LinkedList<Controller>();
		this.displays		 = new LinkedList<Display>();
		this.storages		 = new LinkedList<Storage>();
		this.sensors		 = new LinkedList<Sensor>();
		
		//wrap all other device producers
		produceInputDevices();
		produceStorageDevices();
		produceSystemRumble();
		produceGPS();
		produceSensors();
		produceCameras();
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
		{	
			produceDisplayDevices();
		}
			
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
				//Log.i("Saul", "Controller bitmask before:" + Integer.toHexString(controllers.get(i).buttons));
				//Log.i("Saul", "Action: " + event.getAction());
				
				//if true, then parse code of event
				//Variable declaration
				int key = event.getKeyCode();
				
				//test
				controllers.get(i).keycode = key;
				
				//parse event code
				//parse keycode
				if (key == KeyEvent.KEYCODE_BUTTON_A)
				{
					if(event.getAction() == KeyEvent.ACTION_DOWN || event.getAction() == KeyEvent.ACTION_MULTIPLE)
					{
						controllers.get(i).buttons = controllers.get(i).buttons | Controller.A_BUTTON;
					}
					else if (event.getAction() == KeyEvent.ACTION_UP && ((controllers.get(i).buttons & Controller.A_BUTTON) == Controller.A_BUTTON) )
					{
						controllers.get(i).buttons = controllers.get(i).buttons - Controller.A_BUTTON;
					}
				}
				else if (key == KeyEvent.KEYCODE_BUTTON_B)
				{
					if(event.getAction() == KeyEvent.ACTION_DOWN || event.getAction() == KeyEvent.ACTION_MULTIPLE)
					{
						controllers.get(i).buttons = controllers.get(i).buttons | Controller.B_BUTTON;
					}
					else if (event.getAction() == KeyEvent.ACTION_UP && ((controllers.get(i).buttons & Controller.B_BUTTON) == Controller.B_BUTTON) )
					{
						controllers.get(i).buttons = controllers.get(i).buttons - Controller.B_BUTTON;
					}
				}
				else if (key == KeyEvent.KEYCODE_BUTTON_X)
				{
					if(event.getAction() == KeyEvent.ACTION_DOWN || event.getAction() == KeyEvent.ACTION_MULTIPLE)
					{
						controllers.get(i).buttons = controllers.get(i).buttons | Controller.X_BUTTON;
					}
					else if (event.getAction() == KeyEvent.ACTION_UP && ((controllers.get(i).buttons & Controller.X_BUTTON) == Controller.X_BUTTON) )
					{
						controllers.get(i).buttons = controllers.get(i).buttons - Controller.X_BUTTON;
					}
				}
				else if (key == KeyEvent.KEYCODE_BUTTON_Y)
				{
					if(event.getAction() == KeyEvent.ACTION_DOWN || event.getAction() == KeyEvent.ACTION_MULTIPLE)
					{
						controllers.get(i).buttons = controllers.get(i).buttons | Controller.Y_BUTTON;
					}
					else if (event.getAction() == KeyEvent.ACTION_UP && ((controllers.get(i).buttons & Controller.Y_BUTTON) == Controller.Y_BUTTON) )
					{
						controllers.get(i).buttons = controllers.get(i).buttons - Controller.Y_BUTTON;
					}
				}
				else if (key == KeyEvent.KEYCODE_BUTTON_B)
				{
					if(event.getAction() == KeyEvent.ACTION_DOWN || event.getAction() == KeyEvent.ACTION_MULTIPLE)
					{
						controllers.get(i).buttons = controllers.get(i).buttons | Controller.B_BUTTON;
					}
					else if (event.getAction() == KeyEvent.ACTION_UP && ((controllers.get(i).buttons & Controller.B_BUTTON) == Controller.B_BUTTON) )
					{
						controllers.get(i).buttons = controllers.get(i).buttons - Controller.B_BUTTON;
					}
				}
				else if (key == KeyEvent.KEYCODE_BUTTON_L1)
				{
					if(event.getAction() == KeyEvent.ACTION_DOWN || event.getAction() == KeyEvent.ACTION_MULTIPLE)
					{
						controllers.get(i).buttons = controllers.get(i).buttons | Controller.LEFT_SHOULDER;
					}
					else if (event.getAction() == KeyEvent.ACTION_UP && ((controllers.get(i).buttons & Controller.LEFT_SHOULDER) == Controller.LEFT_SHOULDER) )
					{
						controllers.get(i).buttons = controllers.get(i).buttons - Controller.LEFT_SHOULDER;
					}
				}
				else if (key == KeyEvent.KEYCODE_BUTTON_R1)
				{
					if(event.getAction() == KeyEvent.ACTION_DOWN || event.getAction() == KeyEvent.ACTION_MULTIPLE)
					{
						controllers.get(i).buttons = controllers.get(i).buttons | Controller.RIGHT_SHOULDER;
					}
					else if (event.getAction() == KeyEvent.ACTION_UP && ((controllers.get(i).buttons & Controller.RIGHT_SHOULDER) == Controller.RIGHT_SHOULDER) )
					{
						controllers.get(i).buttons = controllers.get(i).buttons - Controller.RIGHT_SHOULDER;
					}
				}
				else if (key == KeyEvent.KEYCODE_BUTTON_START)
				{
					if(event.getAction() == KeyEvent.ACTION_DOWN || event.getAction() == KeyEvent.ACTION_MULTIPLE)
					{
						controllers.get(i).buttons = controllers.get(i).buttons | Controller.START_BUTTON;
					}
					else if (event.getAction() == KeyEvent.ACTION_UP && ((controllers.get(i).buttons & Controller.START_BUTTON) == Controller.START_BUTTON) )
					{
						controllers.get(i).buttons = controllers.get(i).buttons - Controller.START_BUTTON;
					}
				}
				else if (key == KeyEvent.KEYCODE_BUTTON_SELECT)
				{
					if(event.getAction() == KeyEvent.ACTION_DOWN || event.getAction() == KeyEvent.ACTION_MULTIPLE)
					{
						controllers.get(i).buttons = controllers.get(i).buttons | Controller.SELECT_BUTTON;
					}
					else if (event.getAction() == KeyEvent.ACTION_UP && ((controllers.get(i).buttons & Controller.SELECT_BUTTON) == Controller.SELECT_BUTTON) )
					{
						controllers.get(i).buttons = controllers.get(i).buttons - Controller.SELECT_BUTTON;
					}
				}
				else if (key == KeyEvent.KEYCODE_BUTTON_THUMBL)
				{
					if(event.getAction() == KeyEvent.ACTION_DOWN || event.getAction() == KeyEvent.ACTION_MULTIPLE)
					{
						controllers.get(i).buttons = controllers.get(i).buttons | Controller.LEFT_THUMB;
					}
					else if (event.getAction() == KeyEvent.ACTION_UP && ((controllers.get(i).buttons & Controller.LEFT_THUMB) == Controller.LEFT_THUMB) )
					{
						controllers.get(i).buttons = controllers.get(i).buttons - Controller.LEFT_THUMB;
					}
				}
				else if (key == KeyEvent.KEYCODE_BUTTON_THUMBR)
				{
					if(event.getAction() == KeyEvent.ACTION_DOWN || event.getAction() == KeyEvent.ACTION_MULTIPLE)
					{
						controllers.get(i).buttons = controllers.get(i).buttons | Controller.RIGHT_THUMB;
					}
					else if (event.getAction() == KeyEvent.ACTION_UP && ((controllers.get(i).buttons & Controller.RIGHT_THUMB) == Controller.RIGHT_THUMB) )
					{
						controllers.get(i).buttons = controllers.get(i).buttons - Controller.RIGHT_THUMB;
					}
				}
				else if (key == KeyEvent.KEYCODE_DPAD_UP)
				{
					if(event.getAction() == KeyEvent.ACTION_DOWN || event.getAction() == KeyEvent.ACTION_MULTIPLE)
					{
						controllers.get(i).buttons = controllers.get(i).buttons | Controller.UP_DPAD;
					}
					else if (event.getAction() == KeyEvent.ACTION_UP && ((controllers.get(i).buttons & Controller.UP_DPAD) == Controller.UP_DPAD) )
					{
						controllers.get(i).buttons = controllers.get(i).buttons - Controller.UP_DPAD;
					}
				}
				else if (key == KeyEvent.KEYCODE_DPAD_DOWN)
				{
					if(event.getAction() == KeyEvent.ACTION_DOWN || event.getAction() == KeyEvent.ACTION_MULTIPLE)
					{
						controllers.get(i).buttons = controllers.get(i).buttons | Controller.DOWN_DPAD;
					}
					else if (event.getAction() == KeyEvent.ACTION_UP && ((controllers.get(i).buttons & Controller.DOWN_DPAD) == Controller.DOWN_DPAD) )
					{
						controllers.get(i).buttons = controllers.get(i).buttons - Controller.DOWN_DPAD;
					}
				}
				else if (key == KeyEvent.KEYCODE_DPAD_LEFT)
				{
					if(event.getAction() == KeyEvent.ACTION_DOWN || event.getAction() == KeyEvent.ACTION_MULTIPLE)
					{
						controllers.get(i).buttons = controllers.get(i).buttons | Controller.LEFT_DPAD;
					}
					else if (event.getAction() == KeyEvent.ACTION_UP && ((controllers.get(i).buttons & Controller.LEFT_DPAD) == Controller.LEFT_DPAD) )
					{
						controllers.get(i).buttons = controllers.get(i).buttons - Controller.LEFT_DPAD;
					}
				}
				else if (key == KeyEvent.KEYCODE_DPAD_RIGHT)
				{
					if(event.getAction() == KeyEvent.ACTION_DOWN || event.getAction() == KeyEvent.ACTION_MULTIPLE)
					{
						controllers.get(i).buttons = controllers.get(i).buttons | Controller.RIGHT_DPAD;
					}
					else if (event.getAction() == KeyEvent.ACTION_UP && ((controllers.get(i).buttons & Controller.RIGHT_DPAD) == Controller.RIGHT_DPAD) )
					{
						controllers.get(i).buttons = controllers.get(i).buttons - Controller.RIGHT_DPAD;
					}
				}
				
				//test
				//Log.i("Saul", "Controller bitmask after:" + Integer.toHexString(controllers.get(i).buttons));
				
				//return
				return true;
			}
		}//END for
		
		//test
		//Log.i("Saul", "No controller located.");
		
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
		Log.i("Saul", "Installed Memory: " + this.memoryBytes + "\n");
		Log.i("Saul", "Available Memory: " + this.bytesAvails + "\n");
		Log.i("Saul", "Mem Threshold: " + this.threshold + "\n");
		Log.i("Saul", "Is Memory Low: " + this.lowMemory + "\n");
		Log.i("Saul", "Bit Architecture: " + this.osArchitecture + "\n");
		Log.i("Saul", "Detected Device Count: " + this.detectedDeviceCount + "\n");
		
		for(int i = 0; i < this.detectedDevices.size(); i++)
		{
			Log.i("Saul", "     Device #" + i + ": " + "\n" + "          Name = " + this.detectedDevices.get(i).name + 
					"\n" + "          ID = " +Integer.toHexString(Integer.parseInt(this.detectedDevices.get(i).id)) + 
					"\n" + "          Vendor ID = " + Integer.toHexString(this.detectedDevices.get(i).vendorID) + 
					"\n" + "          Bitmask = " + Integer.toHexString(this.detectedDevices.get(i).testMask) + "\n"
 				  + "\n" + "          Type = " + this.detectedDevices.get(i).deviceType + 
 				  "\n" +   "          Controller Index = " + this.detectedDevices.get(i).controllerIndex + 
 				  "\n" +   "          Display Index = " + this.detectedDevices.get(i).displayIndex +
 				  "\n" +   "          Storage Index = " + this.detectedDevices.get(i).storageIndex +
 				  "\n" +   "          Sensor Index = " + this.detectedDevices.get(i).sensorsIndex +
 				  "\n" +   "          Default = " + this.detectedDevices.get(i).isDefault);
		}
		for(int i =0; i < this.controllers.size(); i++)
		{
			Log.i("Saul", "     Controller #" + i + ": " + "\n" + "          Key Code = " + this.controllers.get(i).keycode + 
					"\n" + "          ID = " +Integer.toHexString(Integer.parseInt(this.detectedDevices.get(i).id)) + 
					"\n" + "          Vendor ID = " + Integer.toHexString(this.detectedDevices.get(i).vendorID) + 
					"\n" + "          Bitmask = " + Integer.toHexString(this.detectedDevices.get(i).testMask) + "\n"
 				  + "\n" + "          Type = " + this.detectedDevices.get(i).deviceType + "\n"
					);
		}
		for(int i =0; i < this.displays.size(); i++)
		{
			Log.i("Saul", "     Display #" + i + ": " + "\n" + "          Current Rotation = " + Integer.toHexString(this.displays.get(i).currentRotation) + 
					"\n" + "          Native Rotation = " +Integer.toHexString(this.displays.get(i).nativeRotation) + 
					"\n" + "          Preferred Rotation = " + Integer.toHexString(this.displays.get(i).rotationPreference) + 
					"\n" + "          Raw X DPI = " + this.displays.get(i).rawDPIX + 
					"\n" + "          Raw Y DPI = " + this.displays.get(i).rawDPIY + 
					"\n" + "          Logical DPI = " + this.displays.get(i).logicalDPI + 
					"\n" + "          Resolution Scale = " + this.displays.get(i).resolutionScale + "\n");
		}
		for(int i =0; i < this.storages.size(); i++)
		{
			Log.i("Saul", "     Storage #" + i + ": " + "\n" + "          Emulated = " + this.storages.get(i).isEmulated + 
					"\n" + "          Bytes Available = " + this.storages.get(i).bytesAvails + 
					"\n" + "          Total Bytes = " + this.storages.get(i).totalBytes + 
					"\n" + "          Path = " + this.storages.get(i).path);
		}
		for(int i =0; i < this.sensors.size(); i++)
		{
			Log.i("Saul", "     Sensor #" + i + ": " + "\n" + "          Minimum Delay = " + this.sensors.get(i).minDelay + 
					"\n" + "          Type = " + this.sensors.get(i).type + 
					"\n" + "          Version = " + this.sensors.get(i).version + 
					"\n" + "          Name = " + this.sensors.get(i).name +
					"\n" + "          Vendor = " + this.sensors.get(i).vendor +
					"\n" + "          Name = " + this.sensors.get(i).power +
					"\n" + "          Resolution = " + this.sensors.get(i).resolution +
					"\n" + "          Max Range = " + this.sensors.get(i).maxRange +
					"\n" + "          FIFO Max Event Count = " + this.sensors.get(i).fifoMaxEventCount +
					"\n" + "          FIFO Reserved Event Count = " + this.sensors.get(i).fifoReservedEventCount +
					"\n" + "          String Type = " + this.sensors.get(i).stringType +
					"\n" + "          Max Delay = " + this.sensors.get(i).maxDelay +
					"\n" + "          Reporting Mode = " + this.sensors.get(i).reportingMode +
					"\n" + "          Is Wake Up Sensor? = " + this.sensors.get(i).isWakeUpSensor + "\n"
					);
		}
		
		Log.i("Saul", "Error Code: " + this.error + "\n");
	}//end testLog


}//END class
