//Device.java
//Java version of the deviceStruct structure from Cylon.h
//josh@mindaptiv.com

package com.mindaptiv.saul;

//imports
import java.lang.Integer;
import java.lang.String;

import android.os.Build;
import android.view.InputDevice;

public class Device 
{
	//device properties
	Integer panelLocation;
	Integer inLid;
	Integer inDock;
	Integer isDefault;
	Integer isEnabled;
	public String 	name;
	public String	id;
	public Integer  vendorID;
	
	//type
	public Integer deviceType;
	
	//indexes
	Integer displayIndex;
	Integer controllerIndex;
	Integer storageIndex;
	public Integer testMask;
	
	//Constructors
	//Build device using InputDevice object
	public Device(InputDevice idvice)
	{
		//temporary zeroing of values that may change later
		this.controllerIndex = 0;
		this.displayIndex 	 = 0;
		
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
		this.panelLocation 	= 0;
		
		//Parse source mask
		this.testMask = idvice.getSources();
		
		//gamepad takes precedence as it can have multiple features, but should just be considered a gamepad
		if((this.testMask & InputDevice.SOURCE_GAMEPAD) == InputDevice.SOURCE_GAMEPAD)
		{
			this.deviceType = 11;
			
			//TODO: build controller
			//build controller
			
		}
		//joystick will also default to gamepad for our purposes for now
		else if((this.testMask & InputDevice.SOURCE_JOYSTICK) == InputDevice.SOURCE_JOYSTICK)
		{
			this.deviceType = 11;
			
			//TODO: build controller
			//build controller
			
		}
		else if((this.testMask & InputDevice.SOURCE_TOUCHSCREEN) == InputDevice.SOURCE_TOUCHSCREEN)
		{
			this.deviceType = 12;
		}
		else if((this.testMask & InputDevice.SOURCE_TOUCHPAD) == InputDevice.SOURCE_TOUCHPAD)
		{
			this.deviceType = 13;
		}
		else if((this.testMask & InputDevice.SOURCE_TRACKBALL) == InputDevice.SOURCE_TRACKBALL)
		{
			this.deviceType = 14;
		}
		else if((this.testMask & InputDevice.SOURCE_STYLUS) == InputDevice.SOURCE_STYLUS)
		{
			this.deviceType = 15;
		}
		else if((this.testMask & InputDevice.SOURCE_MOUSE) == InputDevice.SOURCE_MOUSE)
		{
			this.deviceType = 9;
		}
		//if none of these other ones go through, but these bits are still hot, default to touchpad
		else if((this.testMask & InputDevice.SOURCE_TOUCH_NAVIGATION) == InputDevice.SOURCE_TOUCH_NAVIGATION)
		{
			this.deviceType = 13;
		}
		else if((this.testMask & InputDevice.SOURCE_KEYBOARD) == InputDevice.SOURCE_KEYBOARD)
		{
			this.deviceType = 10;
		}
		//default dpad to gamepad if none of the previous values fit
		else if((this.testMask & InputDevice.SOURCE_DPAD) == InputDevice.SOURCE_DPAD)
		{
			this.deviceType = 11;
		}
		//default classes to appropriate device type (or approximation) if none of the previous types have been addressed
		else if((this.testMask & InputDevice.SOURCE_CLASS_JOYSTICK) == InputDevice.SOURCE_CLASS_JOYSTICK)
		{
			//default joystick class to gamepad if deviceType not already captured
			this.deviceType = 11;
		}
		else if((this.testMask & InputDevice.SOURCE_CLASS_TRACKBALL) == InputDevice.SOURCE_CLASS_TRACKBALL)
		{
			this.deviceType = 14;
		}
		else if((this.testMask & InputDevice.SOURCE_CLASS_POINTER) == InputDevice.SOURCE_CLASS_POINTER)
		{
			//default pointer to mouse type
			this.deviceType = 9;
		}
		else if((this.testMask & InputDevice.SOURCE_CLASS_POSITION) == InputDevice.SOURCE_CLASS_POSITION)
		{
			//default position to new position type if deviceType not already captured
			this.deviceType = 16;
		}
		//if any remaining bits are set, we know its not necessarily a bogus reading so save as generic
		else if((this.testMask | 0xffffffff) > InputDevice.SOURCE_CLASS_NONE)
		{
			this.deviceType = 1;
		}
		//otherwise, error/missing/invalid/unknown
		else
		{
			this.deviceType = 0;
		}
	}//END input constructor
	
	//Constructor for Device tied to Display object
	public Device(android.view.Display display)
	{
		//Temporary zeroing of values that may change later
		this.displayIndex = 0;
		
		//Set values not available in this context (creating from an android Display object)
		this.panelLocation 	 = 0;
		this.inLid 			 = 0;
		this.inDock			 = 0;
		this.isEnabled		 = 1;
		this.controllerIndex = 0;
		this.testMask		 = 0;
		this.vendorID		 = 0;
		
		//Set name
		this.name = display.getName();
		
		//Set id
		this.id = Integer.toString(display.getDisplayId());
		
		//set deviceType for Saul
		this.deviceType = 8;
		
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
		this.panelLocation 	= 0;
		this.inDock 		= 0;
		this.inLid			= 0;
		this.isEnabled		= 1;
		this.vendorID  		= 0;
		this.displayIndex	= 0;
		this.controllerIndex = 0;
		this.testMask		= 0;
		this.id				= "0";
		
		//Set value for being default storage location
		this.isDefault = 0;
		if(isDefault == true)
		{
			this.isDefault = 1;
		}
		
		//set name value to that of the storage path
		this.name = path;
	}//END storage constructor
	
}//end class

