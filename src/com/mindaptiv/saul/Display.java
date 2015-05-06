//Display.java
//Java version of the displayStruct structure from Cylon.h
//josh@mindaptiv.com

package com.mindaptiv.saul;

import java.lang.Integer;
import java.lang.Float;

import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.view.Surface;

public class Display 
{
	//Variable Declaration:
	//CONSTANTS
	final static int NO_ROTATION = 0;
	final static int LANDSCAPE = 1;
	final static int PORTRAIT = 2;
	final static int FLIPPED_LANDSCAPE = 4;
	final static int FLIPPED_PORTRAIT = 8;
	//END Constants
	
	//parent device
	Device superDevice;
	
	//properties
	/* 
	 * Rotation Bit Masks:
	 * 0 = none
	 * 1 = landscape
	 * 2 = portrait
	 * 4 = flipped landscape
	 * 8 = flipped portrait
	 * 
	 */
	Integer rotationPreference;
	Integer currentRotation;
	Integer nativeRotation;
	Float resolutionScale;
	Integer isStereoscopicEnabled;
	Float   logicalDPI;
	Float	rawDPIX;
	Float	rawDPIY;
	//end variable declaration
	
	
	//Constructor
	public Display(android.view.Display source, Context displayContext, Device device)
	{
		//set parent device
		this.superDevice = device;
		
		//set values otherwise unavailable on current Android platforms
		this.isStereoscopicEnabled = 0;
		
		//Get configuration then rotation
		Configuration configuration = displayContext.getResources().getConfiguration();
		int rotation 				= source.getRotation();
		
		//Compare values of current rotation and orientation to determine local values
		if((rotation == Surface.ROTATION_0) && (Configuration.ORIENTATION_LANDSCAPE == configuration.orientation))
		{
			this.nativeRotation 	 = Display.LANDSCAPE;
			this.currentRotation  	 = Display.LANDSCAPE;
			this.rotationPreference  = Display.LANDSCAPE;
		}
		else if((rotation == Surface.ROTATION_90) && (Configuration.ORIENTATION_LANDSCAPE == configuration.orientation))
		{
			this.nativeRotation 	 = Display.LANDSCAPE;
			this.currentRotation  	 = Display.PORTRAIT;
			this.rotationPreference  = Display.LANDSCAPE;
		}
		else if((rotation == Surface.ROTATION_180) && (Configuration.ORIENTATION_LANDSCAPE == configuration.orientation))
		{
			this.nativeRotation 	 = Display.LANDSCAPE;
			this.currentRotation  	 = Display.FLIPPED_LANDSCAPE;
			this.rotationPreference  = Display.LANDSCAPE;
		}
		else if((rotation == Surface.ROTATION_270) && (Configuration.ORIENTATION_LANDSCAPE == configuration.orientation))
		{
			this.nativeRotation 	 = Display.LANDSCAPE;
			this.currentRotation  	 = Display.FLIPPED_PORTRAIT;
			this.rotationPreference  = Display.LANDSCAPE;
		}
		else if((rotation == Surface.ROTATION_0) && (Configuration.ORIENTATION_PORTRAIT == configuration.orientation))
		{
			this.nativeRotation 	 = Display.PORTRAIT;
			this.currentRotation  	 = Display.PORTRAIT;
			this.rotationPreference  = Display.PORTRAIT;
		}
		else if((rotation == Surface.ROTATION_90) && (Configuration.ORIENTATION_PORTRAIT == configuration.orientation))
		{
			this.nativeRotation 	 = Display.PORTRAIT;
			this.currentRotation  	 = Display.LANDSCAPE;
			this.rotationPreference  = Display.PORTRAIT;
		}
		else if((rotation == Surface.ROTATION_180) && (Configuration.ORIENTATION_PORTRAIT == configuration.orientation))
		{
			this.nativeRotation 	 = Display.PORTRAIT;
			this.currentRotation  	 = Display.FLIPPED_PORTRAIT;
			this.rotationPreference  = Display.PORTRAIT;
		}
		else if((rotation == Surface.ROTATION_270) && (Configuration.ORIENTATION_PORTRAIT == configuration.orientation))
		{
			this.nativeRotation 	 = Display.PORTRAIT;
			this.currentRotation  	 = Display.FLIPPED_LANDSCAPE;
			this.rotationPreference  = Display.PORTRAIT;
		}
		else if(Configuration.ORIENTATION_UNDEFINED == configuration.orientation)
		{
			//error/missing/unknown explicit undefined case
			this.nativeRotation 	 = Display.NO_ROTATION;
			this.currentRotation  	 = Display.NO_ROTATION;
			this.rotationPreference  = Display.NO_ROTATION;
		}
		else
		{
			//error/missing/unknown otherwise case
			this.nativeRotation 	 = Display.NO_ROTATION;
			this.currentRotation  	 = Display.NO_ROTATION;
			this.rotationPreference  = Display.NO_ROTATION;
		}//END ROTATIONS
		
		//Grab metrics
		DisplayMetrics metrics = new DisplayMetrics(); 
		source.getRealMetrics(metrics);  //NOTE: API 17+ 
		
		//Get estimated DPI's
		this.rawDPIX 			= metrics.xdpi;
		this.rawDPIY 			= metrics.ydpi;
		this.logicalDPI		 	= (float) metrics.densityDpi;
		this.resolutionScale	= metrics.density;
		
	}//END constructor
	
}//END class
