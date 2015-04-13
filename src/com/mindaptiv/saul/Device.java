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
	public Integer testMask;
	
	//Constructor
	//Build device using InputDevice object
	public Device(InputDevice idvice)
	{
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
	}//END constructor
	
	public Device(android.view.Display display)
	{
		
	}
	
}//end class

