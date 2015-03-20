//Device.java
//Java version of the deviceStruct structure from Cylon.h
//josh@mindaptiv.com

package com.mindaptiv.saul;

//imports
import java.lang.Integer;
import java.lang.String;

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
	
	//indexes
	Integer displayIndex;
	Integer controllerIndex;
}
