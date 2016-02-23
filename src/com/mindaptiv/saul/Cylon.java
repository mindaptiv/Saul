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
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.AlertDialog;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.fingerprint.FingerprintManager;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.display.*;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.location.LocationManager;
import android.Manifest;
import android.media.midi.*;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.StatFs;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;


@SuppressWarnings("deprecation")
public class Cylon
{
    //native code init
    static
    {
        System.loadLibrary("saulStuff");
    }

    //Variable Declaration:
    //Constants
    //Days
    final static int SUNDAY = 0;
    final static 	int MONDAY = 1;
    final static int TUESDAY =2;
    final static int WEDNESDAY = 3;
    final static int THURSDAY = 4;
    final static int FRIDAY = 5;
    final static int SATURDAY = 6;

    //Months
    final static int JANUARY = 1;
    final static int FEBRUARY = 2;
    final static int MARCH = 3;
    final static int APRIL = 4;
    final static int MAY = 5;
    final static int JUNE = 6;
    final static int JULY = 7;
    final static int AUGUST = 8;
    final static int SEPTEMBER = 9;
    final static int OCTOBER = 10;
    final static int NOVEMBER = 11;
    final static int DECEMBER = 12;

    //DST
    final static int STANDARD_TIME = 0;
    final static int DAYLIGHT_TIME = 1;
    //END constants

    //names
    public String username;
    public  String deviceName;

    //time
    public int milliseconds;
    public int seconds;
    public int minutes;
    public int hours;

    //date
    public int day;
    public int date;
    public int month;
    public int year;

    //time zone
    public int dst;
    public int timeZone;
    public String timeZoneName;

    //processor
    public String architecture;
    public int pageSize;
    public int processorCount;
    public int allocationGranularity;
    public float hertz;

    //memory
    public long memoryBytes;
    public long threshold;
    public long bytesAvails;
    public int lowMemory;
    public int osArchitecture;

    //avatar
    public String picturePath; //represents picture URI location

    //devices
    public int installedDeviceCount;
    public int detectedDeviceCount;
    public int videoCount;
    public int micCount;
    public int speakerCount;
    public int locationCount;
    //int scannerCount; Note: Unused in Android side of Centurion
    public LinkedList<Device> detectedDevices;
    public LinkedList<Controller> controllers;
    public LinkedList<Display> displays;
    public LinkedList<Storage> storages;
    public LinkedList<Sensor> sensors;
    public LinkedList<Midi> midiDevices;
    public int keycode;

    //Android
    public Context context;
    public Application app;
    public Activity activity;

    //Permissions
    private  final static String cameraRationale = "Camera permission is needed to retrieve device hardware information for Essence.";
    private  final static String contactsRationale = "Contacts permission is needed so the app may refer to you by your chosen name and use your avatar for your personal profile.";
    private final static String locationRationale = "Location permission is needed so the app can enhance your experience via GPS.";
    private final static String bluetoothRationale = "Bluetooth permission is needed so the app can interface with your wireless devices.";
    private final static String storageRationale = "Storage permission is needed so the app can measure the amount of free space that is available to utilize.";
    private final static String fingerprintRationale = "Fingerprint use is needed to the app can lock your content securely (when requested).";
    private  final static int REQUEST_CAMERA = 0;
    private  final static int REQUEST_CONTACTS = 1;
    private  final static int REQUEST_BLUETOOTH = 2;
    private  final static int REQUEST_LOCATION = 3;
    private  final static int REQUEST_STORAGE = 4;
    private final static int REQUEST_FINGERPRINT = 5;
    private boolean nonAnswersDone;
    private boolean contactsAnswered;
    private boolean cameraAnswered;
    private boolean bluetoothAnswered;
    private boolean storageAnswered;
    private boolean locationAnswered;
    private boolean fingerprintAnswered;
    private boolean logged;

    //error
    private int error;

    //native conversion tracker
    private boolean nativeConverted;
    //END variable declaration

    //Constructor
    public Cylon(Context context, Application app, Activity activity)
    {
        //Android
        this.context = context;
        this.app     = app;
        this.activity = activity;

        //native data conversion
        nativeConverted = false;

        //producers (that can be called w/o permissions requests)
        this.produceDeviceName();
        this.produceDateTime();
        this.produceProcessorInfo();
        this.produceMemoryInfo();
        this.produceDevices();

        //set flags
        bluetoothAnswered 	= false;
        contactsAnswered 	= false;
        cameraAnswered 		= false;
        locationAnswered 	= false;
        storageAnswered 	= false;
        fingerprintAnswered = false;
        logged				= false;
        nonAnswersDone 		= true;

        //ask for permissions
        makeRequest(Manifest.permission.READ_CONTACTS, REQUEST_CONTACTS);
        makeRequest(Manifest.permission.CAMERA, REQUEST_CAMERA);
        makeRequest(Manifest.permission.BLUETOOTH, REQUEST_BLUETOOTH);
        makeRequest(Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_LOCATION);
        makeRequest(Manifest.permission.WRITE_EXTERNAL_STORAGE, REQUEST_STORAGE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            makeRequest(Manifest.permission.USE_FINGERPRINT, REQUEST_FINGERPRINT);
        }
    }//END Constructor

    //PERMISSIONS METHODS
    //Credit to nuuneoi @ inthecheesefactory.com for partial method code
    void makeRequest(final String manifestPermission, final int cylonRequestCode)
    {
        //Check if read contacts permission is available
        int permissionCheck = android.support.v4.content.ContextCompat.checkSelfPermission(this.activity, manifestPermission);

        //if permission is not available
        if(permissionCheck == PackageManager.PERMISSION_DENIED)
        {
            //Check for need to show rationale window
            if(ActivityCompat.shouldShowRequestPermissionRationale(this.activity, manifestPermission))
            {
                //Select the appropriate rationale message
                String message = "Hello!";

                if(cylonRequestCode == REQUEST_CONTACTS)
                {
                    message = contactsRationale;
                }
                else if(cylonRequestCode == REQUEST_CAMERA)
                {
                    message = cameraRationale;
                }
                else if(cylonRequestCode == REQUEST_LOCATION)
                {
                    message = locationRationale;
                }
                else if(cylonRequestCode == REQUEST_BLUETOOTH)
                {
                    message = bluetoothRationale;
                }
                else if(cylonRequestCode == REQUEST_STORAGE)
                {
                    message = storageRationale;
                }
                else if(cylonRequestCode == REQUEST_FINGERPRINT)
                {
                    message = fingerprintRationale;
                }

                //build listener for okay button
                class OkListener implements DialogInterface.OnClickListener
                {
                    private Cylon saul;

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(saul.activity, new String[]{manifestPermission}, cylonRequestCode);
                    }

                    public void setCylon(Cylon cylon)
                    {
                        this.saul = cylon;
                    }
                }//END class

                //set listener's cylon so it may access the activity field
                OkListener okListener = new OkListener();
                okListener.setCylon(this);

                //Show GUI window explaining rationale
                new AlertDialog.Builder(this.activity).setMessage(message)
                        .setPositiveButton("OK", okListener)
                        .setNegativeButton("Cancel", null)
                        .create()
                        .show();
            }//END if should show rationale
            else
            {
                //Request Read Contacts Permission
                ActivityCompat.requestPermissions(this.activity, new String[]{manifestPermission}, cylonRequestCode);
                Log.i("Saul", "Showing regular window");
            }
        }//END if permission denied
        else
        {
            Log.i("Saul", "Already have requested permission");
            if(cylonRequestCode == REQUEST_CONTACTS)
            {
                contactsAnswered = true;
                produceUsername();
                produceAvatar();
            }
            else if (cylonRequestCode == REQUEST_CAMERA)
            {
                cameraAnswered = true;
                produceCameras();
            }
            else if(cylonRequestCode == REQUEST_BLUETOOTH)
            {
                bluetoothAnswered = true;
                produceBluetoothDevices();
            }
            else if(cylonRequestCode == REQUEST_LOCATION)
            {
                locationAnswered = true;
                produceGPS();
            }
            else if(cylonRequestCode == REQUEST_STORAGE)
            {
                storageAnswered = true;
                produceStorageDevices();
            }
            else if(cylonRequestCode == REQUEST_FINGERPRINT)
            {
                fingerprintAnswered = true;
                produceFingerprintReader();
            }
        }//END if permission already available
    }//END method

    //Credit to nuuneoi @ inthecheesefactory.com for partial method code
    //Handles permission request results.
    //Call this method within the overriden onRequestPermissionResult() method utilized by your main activity class,
    //or wherever else in your app architecture that permission requests may occur
    public boolean onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        //Return value, denotes if your activity should just call the suepr version of onRequestPermissionsResult
        boolean shouldCallSuperMethod = false;

        if(requestCode == Cylon.REQUEST_CONTACTS)
        {
            Log.i("Saul", "Got contacts return");
            this.contactsAnswered = true;

            if(grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i("Saul", "Got permission for contacts");
                this.produceUsername();
                this.produceAvatar();
            }
            else
            {
                Log.i("Saul", "Didn't get permission for contacts");
            }
        }
        else
        {
            shouldCallSuperMethod = true;
        }//END if request contacts

        if(requestCode == Cylon.REQUEST_FINGERPRINT)
        {
            Log.i("Saul", "Got contacts return");
            this.fingerprintAnswered = true;

            if(grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i("Saul", "Got permission for fingers");
                this.produceFingerprintReader();
            }
            else
            {
                Log.i("Saul", "Didn't get permission for fingers");
            }
        }
        else
        {
            shouldCallSuperMethod = true;
        }//END if request contacts

        if(requestCode == Cylon.REQUEST_CAMERA)
        {
            Log.i("Saul", "Got camera return");
            this.cameraAnswered = true;

            if(grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Log.i("Saul", "Got permission for camera");
                this.produceCameras();
            }
            else
            {
                Log.i("Saul", "Didn't get permission for camera");
            }
        }
        else
        {
            shouldCallSuperMethod = true;
        }//END if request camera

        if(requestCode == Cylon.REQUEST_BLUETOOTH)
        {
            Log.i("Saul", "Got bluetooth return");
            this.bluetoothAnswered = true;

            if(grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Log.i("Saul", "Got permission for bluetooth");
                this.produceBluetoothDevices();
            }
            else
            {
                Log.i("Saul", "Didn't get permission for bluetooth");
            }
        }
        else
        {
            shouldCallSuperMethod = true;
        }//END if request bluetooth

        if(requestCode == Cylon.REQUEST_STORAGE)
        {
            Log.i("Saul", "Got storage return");
            this.storageAnswered = true;

            if(grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Log.i("Saul", "Got permission for storage");
                this.produceStorageDevices();
            }
            else
            {
                Log.i("Saul", "Didn't get permission for storage");
            }
        }
        else
        {
            shouldCallSuperMethod = true;
        }//END if request storage

        if(requestCode == Cylon.REQUEST_LOCATION)
        {
            Log.i("Saul", "Got location return");
            this.locationAnswered = true;

            if(grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Log.i("Saul", "Got permission for location");
                this.produceGPS();
            }
            else
            {
                Log.i("Saul", "Didn't get permission for location");
            }
        }
        else
        {
            shouldCallSuperMethod = true;
        }//END if request location

        return shouldCallSuperMethod;
    }//End onRequestPermissionsResult
    //END PERMISSIONS METHODS

    //Saul Methods
    //Producers
    private void produceUsername()
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

    private void produceDeviceName()
    {
        //Grab model name
        this.deviceName = Build.MODEL;
    }

    private void produceDateTime()
    {
        //Grab the current date/time
        Date now = new Date();

        //Grab the calendar and time zone
        Calendar calendar = new GregorianCalendar();
        TimeZone timeZone = calendar.getTimeZone();

        //Set dst boolean
        if(timeZone.inDaylightTime(now) == true)
        {
            this.dst = Cylon.DAYLIGHT_TIME;
        }
        else
        {
            this.dst = Cylon.STANDARD_TIME;
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

        if(date >= 1 || date <= 31)
        {
            this.date = date;
        }
        else
        {
            this.date = 0;
        }

        //Grab and set Day
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        if(day >= 1 || day <= 7)
        {
            this.day = day - 1 ; //java starts counting days at 1
        }
        else
        {
            this.day = 0;
        }

        //Grab and set Month
        int month = calendar.get(Calendar.MONTH);
        month	  = month + 1; //java starts counting months at 0

        if(month >= 1 || month <= 12)
        {
            this.month = month;
        }
        else
        {
            this.month = 0;
        }

        //Grab and set Year
        int year = calendar.get(Calendar.YEAR);

        if(year > 0)
        {
            this.year = year;
        }
        else
        {
            this.year = 0;
        }

        //Grab and set milliseconds
        int milliseconds = calendar.get(Calendar.MILLISECOND);

        if (milliseconds >= 0 || milliseconds <= 999)
        {
            this.milliseconds = milliseconds;
        }
        else
        {
            this.milliseconds = 0;
        }

        //Grab and set seconds
        int seconds = calendar.get(Calendar.SECOND);

        if(seconds >= 0 || seconds <= 59)
        {
            this.seconds = seconds;
        }
        else
        {
            this.seconds = 0;
        }

        //Grab and set minutes
        int minutes = calendar.get(Calendar.MINUTE);

        if(minutes >= 0 || minutes <= 59)
        {
            this.minutes = minutes;
        }
        else
        {
            this.minutes = 0;
        }

        //Grab and set hours
        int hours = calendar.get(Calendar.HOUR_OF_DAY);

        if(hours >= 1 || hours <= 23)
        {
            this.hours = hours;
        }
        else
        {
            this.hours = 0;
        }
    }

    private void produceProcessorInfo()
    {
        //Grab and set OS Architecture
        String osArch 		= System.getProperty("os.arch", "0");
        this.architecture 	= osArch;

        //Set unobtainables to default error values
        this.pageSize 				= 0;
        this.allocationGranularity  = 0;

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

    private void produceMemoryInfo()
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

            if(mi.lowMemory)
            {
                this.lowMemory = 1;
            }
            else
            {
                this.lowMemory = 0;
            }

        }
        catch (Exception e)
        {
            //if this fails set the default
            this.memoryBytes = 0;
            this.bytesAvails = 0;
            this.threshold 	 = 0;
            this.lowMemory	 = 0;
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

    private void produceInputDevices()
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
            if (device.deviceType == Device.CONTROLLER_TYPE)
            {
                //set the controller index of the corresponding device to what its index WILL be upon insertion
                this.detectedDevices.getLast().controllerIndex = this.controllers.size();

                //Build controller
                Controller controller = new Controller(this.detectedDevices.getLast(), devices[i]);
                controller.devicesIndex = this.detectedDevices.size() - 1;

                //insert into end of controllers list
                this.controllers.addLast(controller);
            }
        }//end for

    }//end produceInputDevices()

    @SuppressLint("InlinedApi")
    private void produceDisplayDevices()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
        {
            //Variable Declaration
            DisplayManager displayManager = (DisplayManager) context.getSystemService(Context.DISPLAY_SERVICE);
            android.view.Display[] displaysies = displayManager.getDisplays();


            //create Device and  Display objects for each item in displaysies
            for (int i = 0; i < displaysies.length; i++)
            {
                //Create display-based context for each Display
                //partial code credit to Tommy Visic @ stackoverflow
                Context displayContext = this.context.createDisplayContext(displaysies[i]);

                //Create new device using Display
                Device device = new Device(displaysies[i]);

                //set index of what the display will be
                device.displayIndex = this.displays.size();

                //add to devices list
                this.detectedDevices.addLast(device);

                //Create display device
                com.mindaptiv.saul.Display display = new Display(displaysies[i], displayContext, this.detectedDevices.getLast());
                display.devicesIndex = this.detectedDevices.size() - 1;

                //Add to list of displays
                this.displays.addLast(display);
            }//END FOR
        }//END if
        else
        {
            return;
        }//END else

    }//END produce display devices
	
	/*
	 * TODO: eventually get a working printer function in place
	private void producePrintingDevices()
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

    private void produceStorageDevices()
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
            File file = new File(paths[i]);
            if(!(file.exists()))
            {
                continue;
            }

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

            //set to what storage index WILL be
            device.storageIndex = this.storages.size();

            //add to detected devices
            this.detectedDevices.addLast(device);

            //Create new Storage object and place at end of list
            Storage storage = new Storage(this.detectedDevices.getLast(), paths[i], bytesAvails, totalBytes, isEmulated);
            storage.devicesIndex = this.detectedDevices.size() - 1;
            this.storages.addLast(storage);
        }//END for

    }//end produce storage

    private void produceUsbDevices()
    {
        //OTG storage paths

        //Grab manager
        UsbManager usbMan = (UsbManager) this.context.getSystemService(Context.USB_SERVICE);

        //Grab Devices
        HashMap<String, UsbDevice> usbDevices = usbMan.getDeviceList();

        //Variable declaration
        long bytesAvails = 0;
        long totalBytes = 0;
        boolean isDefaultEmulated = false;

        //Inspect device class for all attached/detected usb devices
        for(String key : usbDevices.keySet())
        {
            //reset
            totalBytes  = 0;
            bytesAvails = 0;

            //Make sure file exists first
            File file = new File(usbDevices.get(key).getDeviceName());
            if(!(file.exists()))
            {
                continue;
            }

            //Create new File system stats
            StatFs stats = new StatFs(usbDevices.get(key).getDeviceName());

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

            //only store these directories if they actually have storage space avails
            if (totalBytes > 0)
            {
                //Create new Device object
                Device device = new Device(usbDevices.get(key).getDeviceName(), isDefaultEmulated);

                //set to what storage index WILL be
                device.storageIndex = this.storages.size();

                //insert into end of list
                this.detectedDevices.addLast(device);

                //Create new Storage object and save into list
                Storage storage = new Storage(this.detectedDevices.getLast(), usbDevices.get(key).getDeviceName(), bytesAvails, totalBytes, isDefaultEmulated);
                storage.devicesIndex = this.detectedDevices.size() - 1;
                this.storages.addLast(storage);
            }
        }

        //Space located in the Storage directory
        try
        {
            //Get CPU info directory
            File dir = new File("/storage/");

            //Filter to only list the devices we care about
            File[] files = dir.listFiles();

            //iterate through the files
            for(File file : files)
            {
                //reset bytes
                totalBytes = 0;
                bytesAvails = 0;

                if(file.isDirectory())
                {
                    //if file is a directory

                    //Create new File system stats
                    StatFs stats = new StatFs(file.getAbsolutePath());

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
                    } //END INNER IF


                    boolean stored = false;
                    //if directory is already stored as a storage device
                    for (Storage storage : storages)
                    {
                        if(storage.path.equals(file.getAbsolutePath()))
                        {
                            stored = true;
                        }
                    }

                    //if the device isn't already stored in the storages LinkedList AND if the size is greater than 0 bytes
                    //and if the directory isn't the parent emulated directory (cont.) -
                    //- (as this can show up, but holds paths to emulated storage, and should not be treated as its own device)
                    if (!stored && !file.getAbsolutePath().equals("/storage/emulated") && totalBytes > 0)
                    {
                        //Create new Device object
                        Device device = new Device(file.getAbsolutePath(), isDefaultEmulated);

                        //set storage index to what it WILL be
                        device.storageIndex = this.storages.size();

                        //place at end of list
                        this.detectedDevices.addLast(device);

                        //Create new Storage object
                        Storage storage = new Storage(this.detectedDevices.getLast(), file.getAbsolutePath(), bytesAvails, totalBytes, isDefaultEmulated);
                        storage.devicesIndex = this.detectedDevices.size() - 1;

                        //place at end of list
                        this.storages.addLast(storage);
                    }//END STORED
                } //END IF
            }//END FOR
        } //end try
        catch(Exception e)
        {
            //I got nothing...
        }//END CATCH
    }//end method

    private void rumbleTest()
    {
        //variable declaration
        long[] storageBeepOne = {0, 100, 0};
        long[] storageBeepTwo = {0, 100, 110, 100, 0};
        long[] storageBeepThree = {0, 100, 110, 100, 110, 100, 0};

        int storageDevices = this.storages.size();
        Vibrator rumble = (Vibrator) this.context.getSystemService(Context.VIBRATOR_SERVICE);

        if(rumble.hasVibrator())
        {
            if(storageDevices == 1)
            {
                rumble.vibrate(storageBeepOne, -1);
            }
            if(storageDevices == 2)
            {
                rumble.vibrate(storageBeepTwo, -1);
            }
            if(storageDevices == 3)
            {
                rumble.vibrate(storageBeepThree, -1);
            }
        }

        produceSystemRumble();


    }//end rumble test

    //produce rumble device info
    private void produceSystemRumble()
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
            int long_gap = 1000;    // Length of Gap Between Words
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
            rumble.vibrate(empire, -1);

            //Create new device
            Device device = new Device(rumble);
            this.detectedDevices.addLast(device);
        }
    } //END rumble producer


    private void produceGPS()
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

    private void produceSensors()
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

            //Create Device
            Device device = new Device(sensorList.get(i), defaulty);

            //Set sensors index to what it WILL be
            device.sensorsIndex = sensors.size();

            //place at end of list
            this.detectedDevices.addLast(device);

            //Create Sensor
            Sensor sensor = new Sensor(sensorList.get(i), this.detectedDevices.getLast());
            sensor.devicesIndex = this.detectedDevices.size() - 1;
            sensors.addLast(sensor);
        }
    }

    //produce info on cameras
    @SuppressLint("InlinedApi")
    private void produceCameras()
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
            //TODO check API level
            catch (CameraAccessException e)
            {
                //Bail!
                return;
            }

        }
    }//END produceCameras

    private void produceBluetoothDevices()
    {
        //Get the bluetooth adapter
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();

        if(adapter == null)
        {
            //Bail!
            return;
        }
        else
        {
            //Create a device for the radio and store it
            Device radio = new Device(adapter);
            this.detectedDevices.addLast(radio);

            //check if disabled
            if(!adapter.isEnabled())
            {
                //Bail!
                return;
            }
            else
            {
                //Grab paired devices
                Set<BluetoothDevice> pairedDevices = adapter.getBondedDevices();

                if (pairedDevices.size() > 0)
                {
					/*for (BluetoothDevice device : pairedDevices)
					{
						//test
						//Log.i("Saul", device.getName() + ": " + device.getAddress() + " " + device.getBluetoothClass().getDeviceClass() + " " + device.getBluetoothClass().getMajorDeviceClass());
					}*/
                }
            }
        }
    }

    //Credit to Dandre' "imminent" Allison @ Github for partial code
    //grab uri of user profile image (if available)
    private void produceAvatar()
    {
        final ContentResolver content = context.getContentResolver();
        final Cursor cursor = content.query(
                // Retrieves data rows for the device user's 'profile' contact
                Uri.withAppendedPath(
                        ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY),
                ProfileQuery.PROJECTION,

                // Selects only email addresses or names
                ContactsContract.Contacts.Data.MIMETYPE + "=? OR "
                        + ContactsContract.Contacts.Data.MIMETYPE + "=? OR "
                        + ContactsContract.Contacts.Data.MIMETYPE + "=? OR "
                        + ContactsContract.Contacts.Data.MIMETYPE + "=?",
                new String[]{
                        ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE,
                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
                        ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE
                },

                // Show primary rows first. Note that there won't be a primary email address if the
                // user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");

        String mime_type;
        while (cursor.moveToNext())
        {
            mime_type = cursor.getString(ProfileQuery.MIME_TYPE);
            if (mime_type.equals(ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE))
            {
                if(cursor.getString(ProfileQuery.PHOTO) == null)
                {
                    this.picturePath = "0";
                }
                else
                {
                    this.picturePath = cursor.getString(ProfileQuery.PHOTO);
                }//END else
            }//END if
        }//END while

        //Close the cursor
        cursor.close();
    }//END function

    /**
     * Contacts user profile query interface.
     */
    private interface ProfileQuery {
        /** The set of columns to extract from the profile query results */
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
                ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME,
                ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.IS_PRIMARY,
                ContactsContract.CommonDataKinds.Photo.PHOTO_URI,
                ContactsContract.Contacts.Data.MIMETYPE
        };

        /** Column index for the photo in the profile query results */
        int PHOTO = 6;
        /** Column index for the MIME type in the profile query results */
        int MIME_TYPE = 7;
    } //END interface

    //retrieves info on fingerprint reading hardware, if available
    private void produceFingerprintReader()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            //Grab Service
            FingerprintManager fingerprintManager = (FingerprintManager)this.context.getSystemService(Context.FINGERPRINT_SERVICE);

            if (fingerprintManager != null)
            {
                if(fingerprintManager.isHardwareDetected() == true)
                {
                    //Create Device Struct and save to list
                    //TODO test this on physical device
                    Device device = new Device(fingerprintManager);
                    this.detectedDevices.addLast(device);
                }
            }
        }//END if marshmallow
    }//END producer

    //retrieves MIDI device information
    private void produceMidiInfo()
    {
        //Check that we're on Marshmallow
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            //Check that device has MIDI support
            if(this.context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MIDI))
            {
                //Retrieve manager
                MidiManager manager = (MidiManager) this.context.getSystemService(Context.MIDI_SERVICE);

                //Retrieve list of midi devices
                MidiDeviceInfo[] infos = manager.getDevices();
                //TODO add additional device code for handlers here

                manager.registerDeviceCallback(new MidiManager.DeviceCallback() {
                    public void onDeviceAdded( MidiDeviceInfo info ) {
                        Log.i("Saul", "MIDI added");
                    }
                    public void onDeviceRemoved( MidiDeviceInfo info ) {
                        Log.i("Saul", "MIDI removed");
                    }
                }, new Handler(Looper.getMainLooper()));

                //parse DeviceInfo objects and build deviceStructs
                for(int i = 0; i < infos.length; i++)
                {
                    //TODO test to see if devices need to be open before we can check their info
                    //TODO test with physical devices
                    //Create the Device object
                    Device device = new Device(infos[i]);

                    //set to what midi index value WILL be
                    device.midiIndex = this.midiDevices.size();

                    //add to detected devices
                    this.detectedDevices.addLast(device);

                    //Create Midi object
                    Midi midiDevice = new Midi(infos[i], this.detectedDevices.getLast());
                    midiDevice.devicesIndex = this.detectedDevices.size() - 1;

                    //NOTE: As of this writing the only USB devices we are retrieving in the USB producer are storage,
                    //so no need to check mapping here (yet?) since there wont already be retrieved USB MIDI devices in the detectedDevices list

                    //Add to lists and synchronize
                    this.midiDevices.addLast(midiDevice);
                }//END FOR
            }//END if MIDI supported
        } //END if marshmallow
    }//END method

    private void produceDevices()
    {
        //create lists
        this.detectedDevices = new LinkedList<Device>();
        this.controllers	 = new LinkedList<Controller>();
        this.displays		 = new LinkedList<Display>();
        this.storages		 = new LinkedList<Storage>();
        this.sensors		 = new LinkedList<Sensor>();
        this.midiDevices     = new LinkedList<Midi>();

        //wrap all other device producers
        produceInputDevices();
        produceSystemRumble();
        produceSensors();
        produceUsbDevices();
        produceMidiInfo();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
        {
            produceDisplayDevices();
        }

        //set counts
        this.detectedDeviceCount = this.detectedDevices.size();
        this.installedDeviceCount = this.detectedDevices.size();

        for (int i = 0; i < this.detectedDevices.size(); i++)
        {
            //cameras = videoCount goes up
            if(this.detectedDevices.get(i).deviceType == Device.VIDEO_CAPTURE_TYPE)
            {
                this.videoCount += 1;
            }
            //location aware
            else if (this.detectedDevices.get(i).deviceType == Device.LOCATION_AWARE_TYPE)
            {
                this.locationCount += 1;
            }
            //mic
            else if (this.detectedDevices.get(i).deviceType == Device.AUDIO_CAPTURE_TYPE)
            {
                this.micCount += 1;
            }
            //speaker
            else if (this.detectedDevices.get(i).deviceType == Device.AUDIO_RENDER_TYPE)
            {
                this.speakerCount += 1;
            }//END if
        }//END for

        //test
        rumbleTest();
    }
    //END producers

    //motion event handler
    public boolean handleMotionEvent(MotionEvent event)
    {
        //Verify ID of source
        for (int i = 0; i < this.controllers.size(); i++)
        {
            //check if any controller ID matches the source of the event
            if(Integer.parseInt(controllers.get(i).superDevice.id) == event.getDeviceId())
            {
                //set the appropriate trigger and joystick values
                controllers.get(i).fLeftTrigger  = event.getAxisValue(MotionEvent.AXIS_LTRIGGER);
                controllers.get(i).fRightTrigger = event.getAxisValue(MotionEvent.AXIS_RTRIGGER);
                controllers.get(i).fThumbLeftX	 = event.getAxisValue(MotionEvent.AXIS_X);
                controllers.get(i).fThumbLeftY	 = -event.getAxisValue(MotionEvent.AXIS_Y);
                controllers.get(i).fThumbRightX  = event.getAxisValue(MotionEvent.AXIS_Z);
                controllers.get(i).fThumbRightY  = -event.getAxisValue(MotionEvent.AXIS_RZ);

                //return true because event was not an anomaly
                return true;
            }//end if
        }//end for

        //if no controller matched, return false
        return false;
    }

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
        this.keycode = event.getKeyCode();

        //Verify ID of source
        for (int i = 0; i < this.controllers.size(); i++)
        {
            //check if any controller ID matches the source of the event
            if(Integer.parseInt(controllers.get(i).superDevice.id) == event.getDeviceId())
            {
                //if true, then parse code of event
                //Variable declaration
                int key = event.getKeyCode();

                //get keycode
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
                        this.testLog();
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

                //return
                return true;
            }
        }//END for

        //if no controller matched, return false
        return false;
    }//END handler

    //Call this in an update loop to update your Cylon object
    public void updateSaul()
    {
        //Update fields that change rapidly
        this.produceDateTime();
        this.produceMemoryInfo();

        //Check to see if we can log
        if( !logged &&
                contactsAnswered &&
                cameraAnswered &&
                bluetoothAnswered &&
                locationAnswered &&
                storageAnswered &&
                nonAnswersDone
                )
        {
            logged = true;
            testLog();
        }//END if
    }//END method

    private void testLog()
    {
        //Logging
        Log.i("Saul", "REPORT:\n");
        Log.i("Saul", "Cylon: " + this.toString() + "\n");
        Log.i("Saul", "Username: " + this.username + "\n");
        Log.i("Saul", "Profile Image: " + this.picturePath + "\n");
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
                    "\n" + "          ID = " + this.detectedDevices.get(i).id +
                    "\n" + "          Vendor ID = " + Integer.toHexString(this.detectedDevices.get(i).vendorID) +
                    "\n" + "          Bitmask = " + Integer.toHexString(this.detectedDevices.get(i).testMask) + "\n"
                    + "\n" + "          Type = " + this.detectedDevices.get(i).deviceType +
                    "\n" +   "          Controller Index = " + this.detectedDevices.get(i).controllerIndex +
                    "\n" +   "          Display Index = " + this.detectedDevices.get(i).displayIndex +
                    "\n" +   "          Storage Index = " + this.detectedDevices.get(i).storageIndex +
                    "\n" +   "          Sensor Index = " + this.detectedDevices.get(i).sensorsIndex +
                    "\n" +   "          Default = " + this.detectedDevices.get(i).isDefault +
                    "\n" +   "          Panel Location = " + this.detectedDevices.get(i).panelLocation +
                    "\n" +   "          Orientation = " + this.detectedDevices.get(i).orientation);
        }
        for(int i =0; i < this.controllers.size(); i++)
        {
            Log.i("Saul", "     Controller #" + i + ": " + "\n" +
                            "\n" + "          ID = " + this.controllers.get(i).superDevice.id +
                            "\n" + "          Left X = " + this.controllers.get(i).fThumbLeftX +
                            "\n" + "          Left Y = " + this.controllers.get(i).fThumbLeftY +
                            "\n" + "          Right X = " + this.controllers.get(i).fThumbRightX +
                            "\n" + "          Right Y = " + this.controllers.get(i).fThumbRightY +
                            "\n" + "          Left Trigger = " + this.controllers.get(i).fLeftTrigger +
                            "\n" + "          Right Trigger = " + this.controllers.get(i).fRightTrigger +
                            "\n" + "          Buttons = " + this.controllers.get(i).buttons +
                            "\n" + "          User Index = " + this.controllers.get(i).userIndex +
                            "\n" + "          Device Index = " + this.controllers.get(i).devicesIndex +
                            "\n"
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
                    "\n" + "          Device Index = " + this.displays.get(i).devicesIndex +
                    "\n" + "          Resolution Scale = " + this.displays.get(i).resolutionScale + "\n");
        }
        for(int i =0; i < this.storages.size(); i++)
        {
            Log.i("Saul", "     Storage #" + i + ": " + "\n" + "          Emulated = " + this.storages.get(i).isEmulated +
                    "\n" + "          Bytes Available = " + this.storages.get(i).bytesAvails +
                    "\n" + "          Total Bytes = " + this.storages.get(i).totalBytes +
                    "\n" + "          Device Index = " + this.storages.get(i).devicesIndex +
                    "\n" + "          Path = " + this.storages.get(i).path +
                    "\n"
            );
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
                            "\n" + "          Is Wake Up Sensor? = " + this.sensors.get(i).isWakeUpSensor +
                            "\n" + "          Device Index = " + this.sensors.get(i).devicesIndex +
                            "\n"
            );
        }

        Log.i("Saul", "Error Code: " + this.error + "\n");

        //JNI Test
        //Log.i("Saul", stringFromJNI());
        //Log.i("Saul", stringTest(this));
        //Log.i("Saul", buildCylon(this));
        //helloLog("This will log to LogCat via the native call.");
        //END JNI Test

        //JNI Calls
        Log.i("Saul", "Conversion Status: " + this.nativeConverted);
        buildCylon(this);
        Log.i("Saul", "Conversion Status: " + this.nativeConverted);
    }//end testLog

    private native void helloLog(String logThis);
    private native String stringFromJNI();
    public native String stringTest(Cylon saul);
    private native String buildCylon(Cylon saul);
    private native boolean isNativeConverted(Cylon saul);
}//END class
