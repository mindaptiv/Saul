package com.mindaptiv.saul;

import java.lang.Integer;

//Java version of the displayStruct structure from Cylon.h
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
	//TODO: add logicalDPI
	//TODO: add rawDPIX;
	//TODO: add rawDPIY;
	
}
