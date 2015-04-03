//Controller.java
//Java version of the controllerStruct structure from Cylon.h
//josh@mindaptiv.com

package com.mindaptiv.saul;

//imports
import java.lang.Integer;

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

	
	//Constructor
	public Controller()
	{
		
	}

}
