//Mouse.java
//Java version of the mouseStruct structure from Cylon.h
//josh@mindaptiv.com

package com.mindaptiv.saul;

//imports
import java.lang.Integer;

public class Mouse 
{
	//parent device
	Device superDevice;
	
	//properties of available mice
	Integer anyLeftRightSwapped;
	Integer anyVerticalWheelPresent;
	Integer anyHorizontalWheelPresent;
	Integer maxNumberOfButtons;
}
