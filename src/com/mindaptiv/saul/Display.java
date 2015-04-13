//Display.java
//Java version of the displayStruct structure from Cylon.h
//josh@mindaptiv.com

package com.mindaptiv.saul;

import java.lang.Integer;
import java.lang.Float;

import android.content.Context;
import android.content.res.Configuration;
import android.view.Surface;

public class Display 
{
	//Variable Declaration:
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
	Integer resolutionScale;
	Integer isStereoscopicEnabled;
	Float   logicalDPI;
	Float	rawDPIX;
	Float	rawDPIY;
	//end variable declaration
	
	
	//Constructor
	public Display(android.view.Display source, Context displayContext)
	{
		//Get configuration then rotation
		Configuration configuration = displayContext.getResources().getConfiguration();
		int rotation 				= source.getRotation();
		
		//Compare values of current rotation and orientation to determine local values
		if((rotation == Surface.ROTATION_0) && (Configuration.ORIENTATION_LANDSCAPE == configuration.orientation))
		{
			this.nativeRotation 	 = 1;
			this.currentRotation  	 = 1;
			this.rotationPreference  = 1;
		}
		else if((rotation == Surface.ROTATION_90) && (Configuration.ORIENTATION_LANDSCAPE == configuration.orientation))
		{
			this.nativeRotation 	 = 1;
			this.currentRotation  	 = 2;
			this.rotationPreference  = 1;
		}
		else if((rotation == Surface.ROTATION_180) && (Configuration.ORIENTATION_LANDSCAPE == configuration.orientation))
		{
			this.nativeRotation 	 = 1;
			this.currentRotation  	 = 4;
			this.rotationPreference  = 1;
		}
		else if((rotation == Surface.ROTATION_270) && (Configuration.ORIENTATION_LANDSCAPE == configuration.orientation))
		{
			this.nativeRotation 	 = 1;
			this.currentRotation  	 = 8;
			this.rotationPreference  = 1;
		}
		else if((rotation == Surface.ROTATION_0) && (Configuration.ORIENTATION_PORTRAIT == configuration.orientation))
		{
			this.nativeRotation 	 = 2;
			this.currentRotation  	 = 2;
			this.rotationPreference  = 2;
		}
		else if((rotation == Surface.ROTATION_90) && (Configuration.ORIENTATION_PORTRAIT == configuration.orientation))
		{
			this.nativeRotation 	 = 2;
			this.currentRotation  	 = 1;
			this.rotationPreference  = 2;
		}
		else if((rotation == Surface.ROTATION_180) && (Configuration.ORIENTATION_PORTRAIT == configuration.orientation))
		{
			this.nativeRotation 	 = 2;
			this.currentRotation  	 = 8;
			this.rotationPreference  = 2;
		}
		else if((rotation == Surface.ROTATION_270) && (Configuration.ORIENTATION_PORTRAIT == configuration.orientation))
		{
			this.nativeRotation 	 = 2;
			this.currentRotation  	 = 4;
			this.rotationPreference  = 2;
		}
		else if(Configuration.ORIENTATION_UNDEFINED == configuration.orientation)
		{
			//error/missing/unknown explicit undefined case
			this.nativeRotation 	 = 0;
			this.currentRotation  	 = 0;
			this.rotationPreference  = 0;
		}
		else
		{
			//error/missing/unknown otherwise case
			this.nativeRotation 	 = 0;
			this.currentRotation  	 = 0;
			this.rotationPreference  = 0;
		}//END ROTATIONS
	}//END constructor
	
}//END class
