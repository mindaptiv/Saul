//Controller.java
//Java version of the controllerStruct structure from Cylon.h
//josh@mindaptiv.com

package com.mindaptiv.saul;

//imports
import java.lang.Integer;

import android.os.Build;
import android.view.InputDevice;

public class Controller 
{
	//parent device
	Device superDevice;
	
	//player index
	int userIndex;
	
	//button state
	int packetNumber;
	int	buttons; 
	float   fLeftTrigger;
	float	fRightTrigger;
	float   fThumbLeftX;
	float	fThumbLeftY;
	float	fThumbRightX;
	float	fThumbRightY;
	
	//Buttons values 
	//Adopted from XINPUT bit mask for controller state
	public static final Integer A_BUTTON 		= 0x1000;
	public static final Integer B_BUTTON 		= 0x2000;
	public static final Integer X_BUTTON 		= 0x4000;
	public static final Integer Y_BUTTON 		= 0x8000;
	public static final Integer UP_DPAD  		= 0x0001;
	public static final Integer DOWN_DPAD 		= 0x0002;
	public static final Integer LEFT_DPAD   	= 0x0004;
	public static final Integer RIGHT_DPAD  	= 0x0008;
	public static final Integer START_BUTTON	= 0x0010;
	public static final Integer SELECT_BUTTON   = 0x0020;
	public static final Integer LEFT_THUMB      = 0x0040;
	public static final Integer RIGHT_THUMB     = 0x0080;
	public static final Integer LEFT_SHOULDER   = 0x0100;
	public static final Integer RIGHT_SHOULDER  = 0x0200;
	
	//test
	public Integer keycode;
	
	//Constructor
	public Controller(Device superDevice, InputDevice idvice)
	{
		this.superDevice = superDevice;
		
		//init buttons mask
		this.buttons = 0x0000;
		
		//set fields to null values that aren't available from Android
		this.packetNumber = 0;
		
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
		{	
			this.userIndex = idvice.getControllerNumber() - 1;
		}
		else
		{
			this.userIndex = 0;
		}
	}//END constructor
	
	//state setter
	public void setState(Integer state)
	{
		this.buttons = state;
	}//end state setter

}//END class
