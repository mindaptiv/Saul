//Device.java
//Java version of the deviceStruct structure from Cylon.h
//josh@mindaptiv.com

package com.mindaptiv.saul;

//imports
import java.lang.Integer;
import java.lang.String;

import android.view.InputDevice;

public class Device 
{
	//device properties
	Integer panelLocation;
	Integer inLid;
	Integer inDock;
	Integer isDefault;
	Integer isEnabled;
	String 	name;
	String	id;
	
	//type
	Integer deviceType;
	String  stringType;
	
	//indexes
	Integer displayIndex;
	Integer controllerIndex;
	
	//Constructor
	public Device(InputDevice idvice)
	{
		
	}//END constructor
	
}//end class

