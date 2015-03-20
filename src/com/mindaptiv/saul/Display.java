//Display.java
//Java version of the displayStruct structure from Cylon.h
//josh@mindaptiv.com

package com.mindaptiv.saul;

import java.lang.Integer;
import java.lang.Float;

public class Display 
{
	//parent device
	Device superDevice;
	
	//properties
	Integer rotationPreference;
	Integer currentRotation;
	Integer nativeRotation;
	Integer resolutionScale;
	Integer isStereoscopicEnabled;
	Float   logicalDPI;
	Float	rawDPIX;
	Float	rawDPIY;
	
}
