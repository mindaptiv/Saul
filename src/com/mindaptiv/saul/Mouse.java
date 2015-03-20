package com.mindaptiv.saul;

//imports
import java.lang.Integer;

//Java version of the mouseStruct structure from Cylon.h
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
