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
	Integer userIndex;
	
	//button state
	Integer packetNumber;
	Integer	buttons; 
	Integer leftTrigger;
	Integer rightTrigger;
	Integer thumbLeftX;
	Integer thumbLeftY;
	Integer thumbRightX;
	Integer thumbRightY;
	Float   fLeftTrigger;
	Float	fRightTrigger;
	Float   fThumbLeftX;
	Float	fThumbLeftY;
	Float	fThumbRightX;
	Float	fThumbRightY;
	
	//test
	public Integer keycode;
	
	//Constructor
	public Controller(Device superDevice, InputDevice idvice)
	{
		this.superDevice = superDevice;
		
		//set fields to null values that aren't available from Android
		this.packetNumber = 0;
		
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
		{	
			this.userIndex = idvice.getControllerNumber();
		}
		else
		{
			this.userIndex = 0;
		}
		
		//parse buttons
		
		
		
	}//END constructor
	
	//state setter
	public void setState(Integer state)
	{
		this.buttons = state;
	}//end state setter

}//END class
