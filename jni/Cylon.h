//Cylon.h 
//Gives definition for cylonStruct and its supporting structures for use with Saul and other Final Five services.
//"By your command," - Cylon Centurion
//josh@mindaptiv.com

#pragma once

//includes
#include <string>
#include <list>
#include <stdint.h>

//definitions
#define cylon_username__max_Utf8_k 128
#define cylon_deviceName__max_Utf8_k 192

//Type mappings for reference
/*
UBYTE -> unsigned byte  -> uint8_t
WORD  -> unsigned short -> uint16_t
DWORD -> unsigned int   -> uint32_t
unsigned long           -> uint32_t
long unsigned int       -> uint64_t

BYTE      -> signed byte      -> int8_t
SHORT     -> short integer    -> int16_t
long      -> signed integer   -> int32_t
long long -> signed long long -> int64_t

void*                         -> uintptr_t
bool						  -> uint32_t
float32                       -> float
*/

//support structure for cylonStruct for holding the properties of a given device in a single struct
struct deviceStruct
{
	//NOTE: Values of 0 are errors for non-bools
	uint32_t		panelLocation;		//devices panel location on the physical computer
	uint32_t		inLid;				//if the device is located in the lid of the computer 
	uint32_t		inDock;				//if the device is in the docking station of the computer
	uint32_t		isDefault;			//if device is the default for its function
	uint32_t		isEnabled;			//if the device is enabled
	uint32_t		orientation;		//orientation of the device
	uint32_t		vendorID;			//vendor ID
	std::string     name;
	std::string		id;

	//type
	uint32_t				deviceType;	//0 is error, 1 is generic, 2 is portable storage, 
										//3 is audio capture, 4 is audio render, 5 is video capture, 
										//6 is image scanner, 7 is location aware, 8 is display
										//9 is mouse, 10 is keyboard, 11 is gamepad
	uint32_t				displayIndex; //device's index in the displayDevices list if type is 8
	uint32_t				controllerIndex; //device's index in the pointerDevices list if type is 9
	uint32_t				storageIndex;
	uint32_t				sensorsIndex;

};
//END deviceStruct

//for handling DisplayInformation class objects' metadata
struct displayStruct
{
	struct deviceStruct superDevice; //parent deviceStruct object

	uint32_t	rotationPreference;
	uint32_t	currentRotation;
	uint32_t	nativeRotation;
	uint32_t	isStereoscopicEnabled;
	float		resolutionScale;
	float		logicalDPI;
	float		rawDPIX;
	float		rawDPIY;
	//unsigned char*  colorData;
	//unsigned int	colorLength;
};
//end displayStruct

//struct for a gamepad device
struct controllerStruct
{
	struct deviceStruct superDevice;

	uint32_t userIndex; //player number 0-3

	//xinput state
	uint32_t	packetNumber; //for detecting changes
	uint16_t	buttons; //bit mask for what buttons are pressed
	float		leftTrigger;
	float		rightTrigger;
	float		thumbLeftY;
	float		thumbLeftX;
	float		thumbRightX;
	float		thumbRightY;
};
//end controller struct

//for handling device-specific metadata for a mouse in a WinRT context
struct mouseStruct
{
	struct deviceStruct superDevice; //parent deviceStruct object

	//properties of available mice
	uint32_t anyLeftRightSwapped;
	uint32_t anyVerticalWheelPresent;
	uint32_t anyHorizontalWheelPresent;
	uint32_t maxNumberOfButons; //most buttons available for all given mice attached (i.e. if 3 and 5 button mice are attached, return value should be 5)
};
//END mouseStruct

//for handling device-specific metadata for a sensor in an Android context
struct sensorStruct
{
	struct deviceStruct superDevice; //parent deviceStruct object

	//data from Android 16+
	uint32_t minDelay;
	uint32_t type; //based on Android Sensor class type mappings
	uint32_t version;
	std::string name;
	std::string vendor;
	float power;
	float resolution;
	float maxRange;

	//data from Android 19+
	uint32_t fifoMaxEventCount;
	uint32_t fifoReservedEventCount;

	//data from Android 20+
	std::string stringType;

	//data from Android 21+
	uint32_t maxDelay;
	uint32_t reportingMode; //based on Android Sensor class type mappings
	uint32_t isWakeUpSensor;
};

//struct definition for storing user and system data from a WinRT based machine for later use
struct cylonStruct
{
	//time
	uint32_t					milliseconds;
	uint32_t					seconds;
	uint32_t					minutes;
	uint32_t					hours;

	//date
	uint32_t					day; //day of the week, 0-6
	uint32_t					date; //1-31
	uint32_t					month; //1-12
	uint32_t					year; //1601 until the cows come home

	//timezone
	uint32_t					dst;			//0 is standard time, 1 is daylight time, otherwise is invalid
	int32_t						timeZone;		//expressed in minutes +/- UTC
	std::string					timeZoneName;

	//names
	std::string					deviceName;
	std::string					username;

	//processor
	std::string					architecture; //0=error, 1=x64, 2=ARM, 3=Itanium, 4=x86
	uint16_t					processorLevel; //architecture-dependent processor level
	uint32_t					pageSize;  //size of page in bytes
	uint32_t					allocationGranularity; //granularity for starting address where virtual memory can be allocated (assuming in bits?)
	uintptr_t					minAppAddress; //lowest point in memory an application can access
	uintptr_t					maxAppAddress; //highest point in memory an app can access
	float						hertz; //speed of processor (or default lowest possible speed for current OS)
	uint64_t					processorCount; //number of processors

	//memory
	uint64_t					memoryBytes; //system memory measured in bytes
	uint32_t					osArchitecture; //operating system architecture, 16, 32, 64, 128, etc.
	uint32_t					lowMemory;
	uint64_t					threshold;
	uint64_t					bytesAvails;

	//account picture
	//TODO add picture location from IStorageFile
	std::string					pictureType;
	std::string					picturePath;
	uintptr_t					pictureLocation;

	//devices
	uint32_t installedDeviceCount;
	uint32_t detectedDeviceCount;
	uint32_t portableStorageCount;
	uint32_t videoCount;
	uint32_t micCount;
	uint32_t speakerCount;
	uint32_t locationCount;
	uint32_t scannerCount;
	std::list<struct deviceStruct> detectedDevices;
	std::list<struct displayStruct> displayDevices;
	std::list<struct controllerStruct> controllers;
	std::list<struct sensorStruct> sensors;
	struct mouseStruct mice;

	//error
	int32_t			error;
};
//END cylonStruct

