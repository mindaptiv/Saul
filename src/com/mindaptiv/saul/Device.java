//Device.java
//Java version of the deviceStruct structure from Cylon.h
//josh@mindaptiv.com

package com.mindaptiv.saul;

//imports
import java.lang.Integer;
import java.lang.String;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.hardware.Camera.CameraInfo;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.fingerprint.FingerprintManager;
import android.media.midi.MidiDeviceInfo;
import android.os.Build;
import android.view.InputDevice;
import android.view.Surface;

@SuppressWarnings("deprecation")
public class Device 
{
	//Device Type
	final static int ERROR_TYPE = 0;
	final static int GENERIC_TYPE = 1;
	final static int AUDIO_CAPTURE_TYPE = 2;
	final static int AUDIO_RENDER_TYPE = 3;
	final static int STORAGE_TYPE = 4;
	final static int VIDEO_CAPTURE_TYPE = 5;
	final static int IMAGE_SCANNER_TYPE = 6;
	final static int LOCATION_AWARE_TYPE = 7;
	final static int DISPLAY_TYPE = 8;
	final static int MOUSE_TYPE = 9;
	final static int KEYBOARD_TYPE = 10;
	final static int CONTROLLER_TYPE = 11;
	final static int TOUCHSCREEN_TYPE = 12;
	final static int TOUCH_PAD_TYPE = 13;
	final static int TRACKBALL_TYPE = 14;
	final static int STYLUS_TYPE = 15;
	final static int POSITION_TYPE = 16;
	final static int HID_TYPE	= 17;
	final static int RUMBLE_TYPE = 18;
	final static int SENSOR_TYPE = 19;
	final static int BLUETOOTH_RADIO_TYPE = 20;
	final static int PHYSICAL_TYPE = 21;
	final static int PRINTER_TYPE = 22;
	final static int HUB_TYPE = 23;
	final static int COMMUNICATIONS_DATA_TYPE = 24;
	final static int SMART_CARD_TYPE = 25;
	final static int CONTENT_SECURITY_TYPE = 26;
	final static int PERSONAL_HEALTHCARE_TYPE = 27;
	final static int BILLBOARD_TYPE = 28;
	final static int WIRELESS_PHONE_TYPE = 29;
	final static int MIDI_TYPE = 30;

	//Location
	final static int UNKNOWN_PANEL_LOCATION = 0;
	final static int FRONT_PANEL = 1;
	final static int BACK_PANEL = 2;
	final static int TOP_PANEL = 3;
	final static int BOTTOM_PANEL = 4;
	final static int LEFT_PANEL = 5;
	final static int RIGHT_PANEL = 6;

	//Rotation
	final static int NO_ROTATION = 0;
	final static int LANDSCAPE = 1;
	final static int PORTRAIT = 2;
	final static int FLIPPED_LANDSCAPE = 4;
	final static int FLIPPED_PORTRAIT = 8;
	//END Constants
	
	//device properties
	int panelLocation;
	int inLid;
	int inDock;
	int isDefault;
	int isEnabled;
	int orientation;
	public String 	name;
	public String	id;
	public int  vendorID;
	
	//type
	public int deviceType;
	
	//indexes
	int displayIndex;
	int controllerIndex;
	int storageIndex;
	int sensorsIndex;
	public int testMask;
	
	//Constructors
	//Build device using InputDevice object
	public Device(InputDevice idvice)
	{
		//temporary zeroing of values that may change later
		this.controllerIndex = 0;
		this.displayIndex 	 = 0;
		this.storageIndex = 0;
		this.sensorsIndex = 0;
		
		//Parse through all the metadata of the InputDevice and map it to the Device
		
		//Grab and set name
		this.name = idvice.getName();
		
		//Grab and set ids
		this.id 		= Integer.toString(idvice.getId());
		
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
		{	
			this.vendorID = idvice.getVendorId();
		}
		else
		{
			this.vendorID = 0;
		}
		
		//Assume true/false values that are otherwise unavailable from InputDevice
		this.isDefault 		= 0;
		this.isEnabled  	= 1;
		this.inDock 		= 0;
		this.inLid			= 0;
		this.panelLocation 	= Device.UNKNOWN_PANEL_LOCATION;
		this.orientation    = NO_ROTATION;
		
		//Parse source mask
		this.testMask = idvice.getSources();
		
		//gamepad takes precedence as it can have multiple features, but should just be considered a gamepad
		if((this.testMask & InputDevice.SOURCE_GAMEPAD) == InputDevice.SOURCE_GAMEPAD)
		{
			this.deviceType = Device.CONTROLLER_TYPE;
		}
		//joystick will also default to gamepad for our purposes for now
		else if((this.testMask & InputDevice.SOURCE_JOYSTICK) == InputDevice.SOURCE_JOYSTICK)
		{
			this.deviceType = Device.CONTROLLER_TYPE;			
		}
		else if((this.testMask & InputDevice.SOURCE_TOUCHSCREEN) == InputDevice.SOURCE_TOUCHSCREEN)
		{
			this.deviceType = Device.TOUCHSCREEN_TYPE;
		}
		else if((this.testMask & InputDevice.SOURCE_TOUCHPAD) == InputDevice.SOURCE_TOUCHPAD)
		{
			this.deviceType = Device.TOUCH_PAD_TYPE;
		}
		else if((this.testMask & InputDevice.SOURCE_TRACKBALL) == InputDevice.SOURCE_TRACKBALL)
		{
			this.deviceType = Device.TRACKBALL_TYPE;
		}
		else if((this.testMask & InputDevice.SOURCE_STYLUS) == InputDevice.SOURCE_STYLUS)
		{
			this.deviceType = Device.STYLUS_TYPE;
		}
		else if((this.testMask & InputDevice.SOURCE_MOUSE) == InputDevice.SOURCE_MOUSE)
		{
			this.deviceType = Device.MOUSE_TYPE;
		}
		//if none of these other ones go through, but these bits are still hot, default to touchpad
		else if((this.testMask & InputDevice.SOURCE_TOUCH_NAVIGATION) == InputDevice.SOURCE_TOUCH_NAVIGATION)
		{
			this.deviceType = Device.TOUCH_PAD_TYPE;
		}
		else if((this.testMask & InputDevice.SOURCE_KEYBOARD) == InputDevice.SOURCE_KEYBOARD)
		{
			this.deviceType = Device.KEYBOARD_TYPE;
		}
		//default dpad to gamepad if none of the previous values fit
		else if((this.testMask & InputDevice.SOURCE_DPAD) == InputDevice.SOURCE_DPAD)
		{
			this.deviceType = Device.CONTROLLER_TYPE;
		}
		//default classes to appropriate device type (or approximation) if none of the previous types have been addressed
		else if((this.testMask & InputDevice.SOURCE_CLASS_JOYSTICK) == InputDevice.SOURCE_CLASS_JOYSTICK)
		{
			//default joystick class to gamepad if deviceType not already captured
			this.deviceType = Device.CONTROLLER_TYPE;
		}
		else if((this.testMask & InputDevice.SOURCE_CLASS_TRACKBALL) == InputDevice.SOURCE_CLASS_TRACKBALL)
		{
			this.deviceType = Device.TRACKBALL_TYPE;
		}
		else if((this.testMask & InputDevice.SOURCE_CLASS_POINTER) == InputDevice.SOURCE_CLASS_POINTER)
		{
			//default pointer to mouse type
			this.deviceType = Device.MOUSE_TYPE;
		}
		else if((this.testMask & InputDevice.SOURCE_CLASS_POSITION) == InputDevice.SOURCE_CLASS_POSITION)
		{
			//default position to new position type if deviceType not already captured
			this.deviceType = Device.POSITION_TYPE;
		}
		//if any remaining bits are set, we know its not necessarily a bogus reading so save as generic
		else if((this.testMask | 0xffffffff) > InputDevice.SOURCE_CLASS_NONE)
		{
			this.deviceType = Device.GENERIC_TYPE;
		}
		//otherwise, error/missing/invalid/unknown
		else
		{
			this.deviceType = Device.ERROR_TYPE;
		}
	}//END input constructor
	
	//Constructor for Device tied to Display object
	public Device(android.view.Display display)
	{
		//Temporary zeroing of values that may change later
		this.displayIndex = 0;
		
		//Set values not available in this context (creating from an android Display object)
		this.panelLocation 	 = Device.UNKNOWN_PANEL_LOCATION;
		this.inLid 			 = 0;
		this.inDock			 = 0;
		this.isEnabled		 = 1;
		this.controllerIndex = 0;
		this.testMask		 = 0;
		this.vendorID		 = 0;
		this.sensorsIndex	 = 0;
		this.storageIndex    = 0;
		
		//set orientation
		if(display.getRotation() == Surface.ROTATION_0)
		{
			this.orientation = PORTRAIT;
		}
		else if(display.getRotation() == Surface.ROTATION_180)
		{
			this.orientation = FLIPPED_PORTRAIT;
		}
		else if (display.getRotation()== Surface.ROTATION_270)
		{
			this.orientation = FLIPPED_LANDSCAPE;
		}
		else if (display.getRotation() == Surface.ROTATION_90)
		{
			this.orientation = LANDSCAPE;
		}
		else
		{
			this.orientation = NO_ROTATION;
		}
		
		//Set name
		this.name = display.getName();
		
		//Set id
		this.id = Integer.toString(display.getDisplayId());
		
		//set deviceType for Saul
		this.deviceType = Device.DISPLAY_TYPE;
		
		//set isDefault
		this.isDefault = 0;
		if(display.getDisplayId() == android.view.Display.DEFAULT_DISPLAY)
		{
			this.isDefault = 1;
		}
	}//END display constructor
	
	//Constructor built for device tied to a Storage object
	public Device(String path, boolean isDefault)
	{
		//temporary zeroing of values that may change later
		this.storageIndex = 0;
		
		//Set values not available in this context (creating from a storage path)
		this.panelLocation 	= Device.UNKNOWN_PANEL_LOCATION;
		this.inDock 		= 0;
		this.inLid			= 0;
		this.isEnabled		= 1;
		this.vendorID  		= 0;
		this.displayIndex	= 0;
		this.controllerIndex = 0;
		this.testMask		= 0;
		this.id				= "0";
		this.sensorsIndex   = 0;
		this.orientation     = NO_ROTATION;
		
		//set device type
		this.deviceType = Device.STORAGE_TYPE;
		
		//Set value for being default storage location
		this.isDefault = 0;
		if(isDefault == true)
		{
			this.isDefault = 1;
		}
		
		//set name value to that of the storage path
		this.name = path;
	}//END storage constructor
	
	//Constructor from rumble device
	public Device()
	{
		//Set values not available in this context (creating from system rumble)
		this.panelLocation = Device.UNKNOWN_PANEL_LOCATION;
		this.inDock = 0;
		this.inLid = 0;
		this.isEnabled = 1;
		this.isDefault = 1;
		this.vendorID = 0;
		this.displayIndex = 0;
		this.controllerIndex = 0;
		this.storageIndex = 0;
		this.sensorsIndex = 0;
		this.testMask = 0;
		this.id = "0";
		this.orientation     = NO_ROTATION;
		
		//set device type
		this.deviceType = Device.RUMBLE_TYPE;
		
		//set name
		this.name = "System Rumble Service";
	}
	
	//Constructor from GPS device
	public Device(boolean enabled)
	{
		//Set values not available in this conext (creating from system GPS)
		this.panelLocation = Device.UNKNOWN_PANEL_LOCATION;
		this.inDock		   = 0;
		this.inLid		   = 0;
		this.vendorID      = 0;
		this.displayIndex  = 0;
		this.controllerIndex = 0;
		this.storageIndex  = 0;
		this.sensorsIndex  = 0;
		this.testMask      = 0;
		this.id            = "0";
		this.isDefault	   = 0;
		this.orientation     = NO_ROTATION;
		
		//set device type to locationAware
		this.deviceType = Device.LOCATION_AWARE_TYPE;
		
		//set name
		this.name = "System GPS Service";
		
		//set enabled
		this.isEnabled = 0;
		if (enabled)
		{
			this.isEnabled = 1;
		}
	}
	
	//Constructor from Sensor device
	public Device(android.hardware.Sensor sensor, Integer defaulty)
	{
		//set values that may change later
		this.sensorsIndex = 0;
		
		//set values not available in this context
		this.panelLocation = Device.UNKNOWN_PANEL_LOCATION;
		this.inLid 			= 0;
		this.inDock			= 0;
		this.vendorID		= 0;
		this.displayIndex   = 0;
		this.controllerIndex = 0;
		this.storageIndex    = 0;
		this.isEnabled = 1;
		this.testMask = 0;
		this.id = "0";
		this.orientation     = 0;
		
		//set device type to sensor
		this.deviceType = Device.SENSOR_TYPE;
		
		//set if this is the default for its type
		this.isDefault = defaulty;
		
		//set name
		this.name = sensor.getName();
	}
	
	//Constructor from CameraInfo object
	public Device(int id, CameraInfo info)
	{	
		//set values not available in this context
		this.inLid 			= 0;
		this.inDock			= 0;
		this.vendorID		= 0;
		this.displayIndex   = 0;
		this.controllerIndex = 0;
		this.storageIndex    = 0;
		this.isEnabled 		= 1;
		this.testMask 		= 0;
		this.sensorsIndex 	= 0;
		this.isDefault		= 0;
		
		//set device type to camera
		this.deviceType = Device.VIDEO_CAPTURE_TYPE;
		
		//set location
		if (info.facing == 0)
		{
			this.panelLocation = Device.FRONT_PANEL;
		}
		else if (info.facing == 1)
		{
			this.panelLocation = Device.BACK_PANEL;
		}
		else
		{
			this.panelLocation = Device.UNKNOWN_PANEL_LOCATION;
		}
		
		//set orientation
		this.orientation = info.orientation;
		
		//set name + id
		this.name = "Camera " + id;
		this.id   = Integer.toString(id);
	}
	
	//Create new Device from CameraCharacteristics
	@SuppressLint("InlinedApi")
	public Device(int id, CameraCharacteristics info, String name)
	{	
		//set values not available in this context
		this.inLid 			= 0;
		this.inDock			= 0;
		this.vendorID		= 0;
		this.displayIndex   = 0;
		this.controllerIndex = 0;
		this.storageIndex    = 0;
		this.isEnabled 		= 1;
		this.testMask 		= 0;
		this.sensorsIndex 	= 0;
		this.isDefault		= 0;
		
		//set device type to camera
		this.deviceType = Device.VIDEO_CAPTURE_TYPE;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
		{
			//set location
			if ( info.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT)
			{
				this.panelLocation = Device.FRONT_PANEL;
			}
			else if (info.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_BACK)
			{
				this.panelLocation = Device.BACK_PANEL;
			}
			else
			{
				this.panelLocation = Device.UNKNOWN_PANEL_LOCATION;
			}
		
			//set orientation
			this.orientation = info.get(CameraCharacteristics.SENSOR_ORIENTATION);
			
			//set name + id
			this.name = "Camera " + name;
			this.id   = Integer.toString(id);
		}
	}//end Constructor
	
	//Device formed from Bluetooth Adapter
	public Device(BluetoothAdapter adapter)
	{
		//device properties that are out of scope for this constructor
		this.panelLocation 	= Device.UNKNOWN_PANEL_LOCATION;
		this.inLid 			= 0;
		this.inDock 		= 0;
		this.orientation 	= NO_ROTATION;
		this.vendorID 		= 0;
		this.displayIndex 		= 0;
		this.controllerIndex 	= 0;
		this.storageIndex		= 0;
		this.sensorsIndex		= 0;
		this.testMask			= 0;
		
		//Set is default (adapter we retrieve is the default one by way of Android's API)
		this.isDefault = 1;
		
		//Determine if the adapter is enabled
		if(adapter.isEnabled())
		{
			this.isEnabled = 1;
		}
		else
		{
			this.isEnabled = 0;
		}
	
		//Set name
		this.name = adapter.getName();
		
		//Set ID based on bluetooth address
		this.id = adapter.getAddress();
		
		//type for bluetooth radio
		this.deviceType = Device.BLUETOOTH_RADIO_TYPE;	
	
	}

	//For fingerprint readers
	public Device(FingerprintManager manager)
	{
		//Set default fields
		this.panelLocation 	= UNKNOWN_PANEL_LOCATION;
		this.inLid			= 0;
		this.inDock			= 0;
		this.isDefault		= 1;
		this.isEnabled		= 1;
		this.orientation	= NO_ROTATION;
		this.name			= "Fingerprint Manager";
		this.id				= "" + manager.hashCode();
		this.vendorID		= 0;
		this.deviceType		= SENSOR_TYPE;
		this.displayIndex 	= 0;
		this.controllerIndex = 0;
		this.storageIndex	= 0;
		this.sensorsIndex	= 0;
		this.testMask		= 0;
	}

	//for MIDI devices
	public Device(MidiDeviceInfo info)
	{
		//Set default fields
		this.panelLocation = UNKNOWN_PANEL_LOCATION;
		this.inLid = 0;
		this.inDock = 0;
		this.isDefault = 0;
		this.isEnabled = 1;
		this.orientation = NO_ROTATION;
		this.displayIndex = 0;
		this.controllerIndex = 0;
		this.storageIndex = 0;
		this.sensorsIndex = 0;
		this.testMask = 0;

		//Set device type
		this.deviceType = 30;

		//Check that we're on Marshmallow
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
		{
			//Set ID
			this.id = "" + info.getId();

			//Set name

			//TODO check if USB

			//TODO
			//Name
			//Vendor Name
			//Serial #
			//Ports I/O
			//Properties?

		}//END if on Android M
		else
		{
			//Handle corner case
			this.id = "" + 0;
			this.vendorID = 0;
			this.name = "Unidentified MIDI Device";
		}//END if not on Android M
	}
}//end class

