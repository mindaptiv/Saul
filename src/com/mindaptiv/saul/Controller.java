//Controller.java
//Java version of the controllerStruct structure from Cylon.h
//josh@mindaptiv.com

package com.mindaptiv.saul;

//imports
import java.lang.Integer;

import android.os.Build;
import android.view.InputDevice;
import android.view.KeyEvent;

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
	int keycode;
	
	//buttons mapping
	/*
	 * 1st Byte: (Right)(Left)(Down)(Up)
	 * 2nd Byte: (Right Thumbstick)(Left Thumbstick)(Back)(Start)
	 * 3rd Byte: (Don't Care)(Don't Care)(R1)(L1)
	 * 4th Byte: (Y)(X)(B)(A)
	 */

	
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
	
	public void handleKeyEvent(KeyEvent keyEvent)
	{
		//Variable declaration
		int key = keyEvent.getKeyCode();
		
		//test
		this.keycode = key;
		
		//parse keycode
		if (key == KeyEvent.KEYCODE_BUTTON_A)
		{
			
		}
		else if (key == KeyEvent.KEYCODE_BUTTON_B)
		{
			
		}
		else if (key == KeyEvent.KEYCODE_BUTTON_X)
		{
			
		}
		else if (key == KeyEvent.KEYCODE_BUTTON_Y)
		{
			
		}
		else if (key == KeyEvent.KEYCODE_BUTTON_B)
		{
			
		}
		else if (key == KeyEvent.KEYCODE_BUTTON_L1)
		{
			
		}
		else if (key == KeyEvent.KEYCODE_BUTTON_R1)
		{
			
		}
		else if (key == KeyEvent.KEYCODE_BUTTON_START)
		{
			
		}
		else if (key == KeyEvent.KEYCODE_BUTTON_SELECT)
		{
			
		}
		else if (key == KeyEvent.KEYCODE_BUTTON_THUMBL)
		{
			
		}
		else if (key == KeyEvent.KEYCODE_BUTTON_THUMBR)
		{
			
		}
		else if (key == KeyEvent.KEYCODE_DPAD_UP)
		{
			
		}
		else if (key == KeyEvent.KEYCODE_DPAD_DOWN)
		{
			
		}
		else if (key == KeyEvent.KEYCODE_DPAD_LEFT)
		{
			
		}
		else if (key == KeyEvent.KEYCODE_DPAD_RIGHT)
		{
			
		}
	}//END handleKeyEvent()
	

}
