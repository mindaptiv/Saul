#include <jni.h>
#include <string>
#include <android/log.h>
#include "Cylon.h"
#define DEBUG_TAG "NDK_Android_Saul_Test"


/*
 * Quick JNI type reference
 * Java    JNI      Bits
 * boolean jboolean unsigned 8 bits
 * byte	   jbyte    signed 8 bits
 * char    jchar    unsigned 16 bits
 * short   jshort   signed 16 bits
 * int     jint     signed 32 bits
 * int     jsize    signed 32 bits
 * long    jlong    signed 64 bits
 * float   jfloat   32 bits
 * double  jdouble  64 bits
 * void    void     n/a
 *
 * JNI_FALSE = 0
 * JNI_TRUE  = 1
 *
 */

extern "C"
{


	//Test method
	JNIEXPORT jstring JNICALL
	Java_com_mindaptiv_saul_Cylon_stringFromJNI (JNIEnv *env, jobject obj)
	{
		return env->NewStringUTF("GLARG");
	}

	//Converters
	//Convert jstring to std::string
	//Credit to trashkalmar @ stackoverflow for conversion code
	void GetJStringContent(JNIEnv *AEnv, jstring AStr, std::string &ARes)
	{
		if (!AStr)
		{
			ARes.clear();
			return;
		}

		const char* s = AEnv->GetStringUTFChars(AStr, NULL);
		ARes = s;
		AEnv->ReleaseStringUTFChars(AStr, s);
	}


	//Create cylonStruct and report back the size to Java
	//Cred to Robert @ stackoverflow for retrieval code
	JNIEXPORT jstring JNICALL
	Java_com_mindaptiv_saul_Cylon_stringTest (JNIEnv *env, jobject obj, jobject saul)
	{
		//Retrieve class
		jclass cylonClass = env->GetObjectClass(saul);

		//get field ID
	    jfieldID fid_username = env->GetFieldID(cylonClass, "username", "Ljava/lang/String;");

		//get jstring
		jstring jstr = (jstring) env->GetObjectField(saul, fid_username);

		//get std::string
		std::string username;
		GetJStringContent(env, jstr, username);

		//test
		const char* nativeString = env->GetStringUTFChars(jstr,0);
		__android_log_print(ANDROID_LOG_DEBUG, "Saul", "NDK:LC: [%s]", nativeString);
		return jstr;
	}

	deviceStruct buildDevice(JNIEnv *env, jobject device)
	{
		//Retrieve class
		jclass deviceClass = env->GetObjectClass(device);

		//Create device
		struct deviceStruct nativeDevice;

		//===STRINGS===
		//Retrieve fields
		jfieldID fid_name 	= env->GetFieldID(deviceClass, "name", "Ljava/lang/String;");
		jfieldID fid_id 	= env->GetFieldID(deviceClass, "id", "Ljava/lang/String;");

		//Retrieve Java Strings
		jstring j_name 	= (jstring) env->GetObjectField(device, fid_name);
		jstring j_id 	= (jstring) env->GetObjectField(device, fid_id);

		//Convert to std::strings
		std::string name;
		std::string id;
		GetJStringContent(env, j_name, name);
		GetJStringContent(env, j_id, id);

		//Set deviceStruct strings
		nativeDevice.name 	= name;
		nativeDevice.id 	= id;

		//==INTS==
		//Retrieve fields
		jfieldID fid_panelLocation 	= env->GetFieldID(deviceClass, "panelLocation", "I");
		jfieldID fid_inLid 			= env->GetFieldID(deviceClass, "inLid", "I");
		jfieldID fid_inDock 		= env->GetFieldID(deviceClass, "inDock", "I");
		jfieldID fid_isDefault 		= env->GetFieldID(deviceClass, "isDefault", "I");
		jfieldID fid_isEnabled 		= env->GetFieldID(deviceClass, "isEnabled", "I");
		jfieldID fid_orientation 	= env->GetFieldID(deviceClass, "orientation", "I");
		jfieldID fid_vendorID 		= env->GetFieldID(deviceClass, "vendorID", "I");
		jfieldID fid_deviceType 	= env->GetFieldID(deviceClass, "deviceType", "I");
		jfieldID fid_displayIndex 	= env->GetFieldID(deviceClass, "displayIndex", "I");
		jfieldID fid_controllerIndex = env->GetFieldID(deviceClass, "controllerIndex", "I");
		jfieldID fid_storageIndex 	= env->GetFieldID(deviceClass, "storageIndex", "I");
		jfieldID fid_sensorsIndex 	= env->GetFieldID(deviceClass, "sensorsIndex", "I");

		//set deviceStruct ints
		nativeDevice.panelLocation 	= (uint32_t)env->GetIntField(device, fid_panelLocation);
		nativeDevice.inLid			= (uint32_t)env->GetIntField(device, fid_inLid);
		nativeDevice.inDock 		= (uint32_t)env->GetIntField(device, fid_inDock);
		nativeDevice.isDefault 		= (uint32_t)env->GetIntField(device, fid_isDefault);
		nativeDevice.isEnabled 		= (uint32_t)env->GetIntField(device, fid_isEnabled);
		nativeDevice.orientation 	= (uint32_t)env->GetIntField(device, fid_orientation);
		nativeDevice.vendorID 		= (uint32_t)env->GetIntField(device, fid_vendorID);
		nativeDevice.deviceType 	= (uint32_t)env->GetIntField(device, fid_deviceType);
		nativeDevice.displayIndex 	= (uint32_t)env->GetIntField(device, fid_displayIndex);
		nativeDevice.controllerIndex = (uint32_t)env->GetIntField(device, fid_controllerIndex);
		nativeDevice.storageIndex 	= (uint32_t)env->GetIntField(device, fid_storageIndex);
		nativeDevice.sensorsIndex 	= (uint32_t)env->GetIntField(device, fid_storageIndex);

		//Return
		return nativeDevice;
	}//END buildDevice

	displayStruct buildDisplay(JNIEnv* env, jobject display)
	{
		//Retrieve class
		jclass displayClass = env->GetObjectClass(display);

		//Create device
		struct displayStruct nativeDisplay;

		//===INTs===
		//Retrieve fields
		jfieldID fid_rotationPreference 	= env->GetFieldID(displayClass, "rotationPreference", "I");
		jfieldID fid_currentRotation		= env->GetFieldID(displayClass, "currentRotation", "I");
		jfieldID fid_isStereoscopicEnabled 	= env->GetFieldID(displayClass, "isStereoscopicEnabled", "I");
		jfieldID fid_nativeRotation 		= env->GetFieldID(displayClass, "nativeRotation", "I");

		//set displayStruct uint32_ts
		nativeDisplay.rotationPreference 	= (uint32_t)env->GetIntField(display, fid_rotationPreference);
		nativeDisplay.currentRotation 		= (uint32_t)env->GetIntField(display, fid_currentRotation);
		nativeDisplay.nativeRotation 		= (uint32_t)env->GetIntField(display, fid_nativeRotation);
		nativeDisplay.isStereoscopicEnabled = (uint32_t)env->GetIntField(display, fid_isStereoscopicEnabled);


		//===FLOATS===
		//Retrieve fields
		jfieldID fid_resolutionScale 	= env->GetFieldID(displayClass, "resolutionScale", "F");
		jfieldID fid_logicalDPI 		= env->GetFieldID(displayClass, "logicalDPI", "F");
		jfieldID fid_rawDPIX 			= env->GetFieldID(displayClass, "rawDPIX", "F");
		jfieldID fid_rawDPIY 			= env->GetFieldID(displayClass, "rawDPIY", "F");

		//set displayStruct floats
		nativeDisplay.resolutionScale 	= (float)env->GetFloatField(display, fid_resolutionScale);
		nativeDisplay.logicalDPI 		= (float)env->GetFloatField(display, fid_logicalDPI);
		nativeDisplay.rawDPIX 			= (float)env->GetFloatField(display, fid_rawDPIX);
		nativeDisplay.rawDPIY			= (float)env->GetFloatField(display, fid_rawDPIY);


		//===DEVICE===
		//Retrieve field
		jfieldID fid_superDevice = env->GetFieldID(displayClass, "superDevice", "Lcom/mindaptiv/saul/Device;");

		//Retrieve object
		jobject j_device = env->GetObjectField(display, fid_superDevice);

		//Build and set deviceStruct field
		nativeDisplay.superDevice = buildDevice(env, j_device);

		//Return
		return nativeDisplay;
	}

	sensorStruct buildSensor(JNIEnv *env, jobject sensor)
	{
		//Retrieve class
		jclass sensorClass = env->GetObjectClass(sensor);

		//Create sensor
		struct sensorStruct nativeSensor;

		//===INTS===
		//Retrieve fields
		jfieldID fid_minDelay 				= env->GetFieldID(sensorClass, "minDelay", "I");
		jfieldID fid_type 					= env->GetFieldID(sensorClass, "type", "I");
		jfieldID fid_version 				= env->GetFieldID(sensorClass, "version", "I");
		jfieldID fid_fifoMaxEventCount 		= env->GetFieldID(sensorClass, "fifoMaxEventCount", "I");
		jfieldID fid_fifoReservedEventCount = env->GetFieldID(sensorClass, "fifoReservedEventCount", "I");
		jfieldID fid_maxDelay 				= env->GetFieldID(sensorClass, "maxDelay", "I");
		jfieldID fid_reportingMode 			= env->GetFieldID(sensorClass, "reportingMode", "I");
		jfieldID fid_isWakeUpSensor 		= env->GetFieldID(sensorClass, "isWakeUpSensor", "I");

		//set sensortStruct uint32_t's
		nativeSensor.minDelay 				= (uint32_t)env->GetIntField(sensor, fid_minDelay);
		nativeSensor.type 					= (uint32_t)env->GetIntField(sensor, fid_type);
		nativeSensor.version 				= (uint32_t)env->GetIntField(sensor, fid_version);
		nativeSensor.fifoMaxEventCount 		= (uint32_t)env->GetIntField(sensor, fid_fifoMaxEventCount);
		nativeSensor.fifoReservedEventCount = (uint32_t)env->GetIntField(sensor, fid_fifoReservedEventCount);
		nativeSensor.maxDelay 				= (uint32_t)env->GetIntField(sensor, fid_maxDelay);
		nativeSensor.reportingMode 			= (uint32_t)env->GetIntField(sensor, fid_reportingMode);
		nativeSensor.isWakeUpSensor 		= (uint32_t)env->GetIntField(sensor, fid_isWakeUpSensor);


		//===FLOATS===
		//Retrieve fields
		jfieldID fid_power 		= env->GetFieldID(sensorClass, "power", "F");
		jfieldID fid_resolution = env->GetFieldID(sensorClass, "resolution", "F");
		jfieldID fid_maxRange 	= env->GetFieldID(sensorClass, "maxRange", "F");

		//set sensorStruct floats
		nativeSensor.power 		= (float)env->GetFloatField(sensor, fid_power);
		nativeSensor.resolution = (float)env->GetFloatField(sensor, fid_resolution);
		nativeSensor.maxRange 	= (float)env->GetFloatField(sensor, fid_maxRange);


		//===STRINGS===
		//Retrieve fields
		jfieldID fid_name 		= env->GetFieldID(sensorClass, "name", "Ljava/lang/String;");
		jfieldID fid_vendor 	= env->GetFieldID(sensorClass, "vendor", "Ljava/lang/String;");
		jfieldID fid_stringType = env->GetFieldID(sensorClass, "stringType", "Ljava/lang/String;");

		//Retrieve Java Strings
		jstring j_name  	 = (jstring) env->GetObjectField(sensor, fid_name);
		jstring j_vendor	 = (jstring) env->GetObjectField(sensor, fid_vendor);
		jstring j_stringType = (jstring) env->GetObjectField(sensor, fid_stringType);

		//Convert to std::strings
		std::string name;
		std::string vendor;
		std::string stringType;
		GetJStringContent(env, j_name, name);
		GetJStringContent(env, j_vendor, vendor);
		GetJStringContent(env, j_stringType, stringType);

		//===DEVICE===
		//Retrieve field
		jfieldID fid_superDevice = env->GetFieldID(sensorClass, "superDevice", "Lcom/mindaptiv/saul/Device;");

		//Retrieve object
		jobject j_device = env->GetObjectField(sensor, fid_superDevice);

		//Build and set deviceStruct field
		nativeSensor.superDevice = buildDevice(env, j_device);

		//Return
		return nativeSensor;
	}

	storageStruct buildStorage(JNIEnv* env, jobject storage)
	{
		//Retrieve class
		jclass storageClass = env->GetObjectClass(storage);

		//Create device
		struct storageStruct nativeStorage;

		//===INTS===
		//Retrieve fields
		jfieldID fid_isEmulated = env->GetFieldID(storageClass, "isEmulated", "I");

		//set storageStruct uint32_t's
		nativeStorage.isEmulated = (uint32_t)env->GetIntField(storage, fid_isEmulated);


		//===LONGS===
		//Retrieve fields
		jfieldID fid_bytesAvails = env->GetFieldID(storageClass, "bytesAvails", "J");
		jfieldID fid_totalBytes  = env->GetFieldID(storageClass, "totalBytes", "J");

		//set storageStruct uint64_t's
		nativeStorage.bytesAvails = (uint64_t)env->GetLongField(storage, fid_bytesAvails);
		nativeStorage.totalBytes  = (uint64_t)env->GetLongField(storage, fid_totalBytes);


		//===STRINGS===
		//Retrieve fields
		jfieldID fid_path = env->GetFieldID(storageClass, "path", "Ljava/lang/String;");

		//Retrieve Java String
		jstring j_path = (jstring)env->GetObjectField(storage, fid_path);

		//Convert to std::string
		std::string path;
		GetJStringContent(env, j_path, path);

		//Set storageStruct std::strings
		nativeStorage.path = path;

		//return
		return nativeStorage;
	}

	controllerStruct buildController(JNIEnv *env, jobject controller)
	{
		//Retrieve class
		jclass controllerClass = env->GetObjectClass(controller);

		//Create device
		struct controllerStruct nativeController;

		//===INTS===
		//Retrieve fields
		jfieldID fid_packetNumber 	= env->GetFieldID(controllerClass, "packetNumber", "I");
		jfieldID fid_buttons 		= env->GetFieldID(controllerClass, "buttons", "I");
		jfieldID fid_userIndex 		= env->GetFieldID(controllerClass, "userIndex", "I");

		//set controllerStruct uint32_t's
		nativeController.packetNumber 	= (uint32_t)env->GetIntField(controller, fid_packetNumber);
		nativeController.userIndex 		= (uint32_t)env->GetIntField(controller, fid_userIndex);

		//set controllerStruct uint16_t's
		nativeController.buttons = (uint16_t)env->GetIntField(controller, fid_buttons);


		//===FLOATS===
		//Retrieve fields
		jfieldID fid_fLeftTrigger 	= env->GetFieldID(controllerClass, "fLeftTrigger", "F");
		jfieldID fid_fRightTrigger 	= env->GetFieldID(controllerClass, "fRightTrigger", "F");
		jfieldID fid_fThumbLeftX 	= env->GetFieldID(controllerClass, "fThumbLeftX", "F");
		jfieldID fid_fThumbLeftY	= env->GetFieldID(controllerClass, "fThumbLeftY", "F");
		jfieldID fid_fThumbRightX 	= env->GetFieldID(controllerClass, "fThumbRightX", "F");
		jfieldID fid_fThumbRightY 	= env->GetFieldID(controllerClass, "fThumbRightY", "F");

		//set controllerStruct floats
		nativeController.leftTrigger 	= (float)env->GetFloatField(controller, fid_fLeftTrigger);
		nativeController.rightTrigger 	= (float)env->GetFloatField(controller, fid_fRightTrigger);
		nativeController.thumbLeftX 	= (float)env->GetFloatField(controller, fid_fThumbLeftX);
		nativeController.thumbLeftY 	= (float)env->GetFloatField(controller, fid_fThumbLeftY);
		nativeController.thumbRightX 	= (float)env->GetFloatField(controller, fid_fThumbRightX);
		nativeController.thumbRightY 	= (float)env->GetFloatField(controller, fid_fThumbRightY);


		//===DEVICE===
		//Retrieve field
		jfieldID fid_superDevice = env->GetFieldID(controllerClass, "superDevice", "Lcom/mindaptiv/saul/Device;");

		//Retrieve object
		jobject j_device = env->GetObjectField(controller, fid_superDevice);

		//Build and set deviceStruct field
		nativeController.superDevice = buildDevice(env, j_device);

		//Return
		return nativeController;
	}



	JNIEXPORT jstring JNICALL
	Java_com_mindaptiv_saul_Cylon_buildCylon(JNIEnv *env, jobject obj, jobject saul)
	{
		//Retrieve class
		jclass cylonClass = env->GetObjectClass(saul);

		//Create cylon
		struct cylonStruct cylon;

		//===STRINGS===
		//Retrieve fields
		jfieldID fid_username		= env->GetFieldID(cylonClass, "username", "Ljava/lang/String;");
		jfieldID fid_deviceName 	= env->GetFieldID(cylonClass, "deviceName", "Ljava/lang/String;");
		jfieldID fid_timeZoneName 	= env->GetFieldID(cylonClass, "timeZoneName", "Ljava/lang/String;");
		jfieldID fid_architecture   = env->GetFieldID(cylonClass, "architecture", "Ljava/lang/String;");
		jfieldID fid_picturePath    = env->GetFieldID(cylonClass, "picturePath", "Ljava/lang/String;");

		//Retrieve Java Strings
		jstring j_username		= (jstring) env->GetObjectField(saul, fid_username);
		jstring j_deviceName 	= (jstring) env->GetObjectField(saul, fid_deviceName);
		jstring j_timeZoneName  = (jstring) env->GetObjectField(saul, fid_timeZoneName);
		jstring j_architecture  = (jstring) env->GetObjectField(saul, fid_architecture);
		jstring j_picturePath   = (jstring) env->GetObjectField(saul, fid_picturePath);

		//Convert to std::strings
		std::string username;
		std::string deviceName;
		std::string timeZoneName;
		std::string architecture;
		std::string picturePath;
		GetJStringContent(env, j_username, username);
		GetJStringContent(env, j_deviceName, deviceName);
		GetJStringContent(env, j_timeZoneName, timeZoneName);
		GetJStringContent(env, j_architecture, architecture);
		GetJStringContent(env, j_picturePath, picturePath);

		//set cylonStruct strings
		cylon.username 		= username;
		cylon.deviceName	= deviceName;
		cylon.timeZoneName  = timeZoneName;
		cylon.architecture  = architecture;
		cylon.picturePath   = picturePath;
		__android_log_print(ANDROID_LOG_DEBUG, "Saul", "NDK:LC: [%s]", "cylonStruct strings: done");


		//===INTS===
		//Retrieve fields
		jfieldID fid_milliseconds = env->GetFieldID(cylonClass, "milliseconds", "I");
		jfieldID fid_seconds  	  = env->GetFieldID(cylonClass, "seconds", "I");
		jfieldID fid_minutes      = env->GetFieldID(cylonClass, "minutes", "I");
		jfieldID fid_hours		  = env->GetFieldID(cylonClass, "hours", "I");
		jfieldID fid_day		  = env->GetFieldID(cylonClass, "day", "I");
		jfieldID fid_date		  = env->GetFieldID(cylonClass, "date", "I");
		jfieldID fid_month		  = env->GetFieldID(cylonClass, "month", "I");
		jfieldID fid_year		  = env->GetFieldID(cylonClass, "year", "I");
		jfieldID fid_dst		  = env->GetFieldID(cylonClass, "dst", "I");
		jfieldID fid_timeZone	  = env->GetFieldID(cylonClass, "timeZone", "I");
		jfieldID fid_pageSize  	  = env->GetFieldID(cylonClass, "pageSize", "I");
		jfieldID fid_processorCount	  		= env->GetFieldID(cylonClass, "processorCount", "I");
		jfieldID fid_allocationGranularity	= env->GetFieldID(cylonClass, "allocationGranularity", "I");
		jfieldID fid_lowMemory	 			= env->GetFieldID(cylonClass, "lowMemory", "I");
		jfieldID fid_detectedDeviceCount 	= env->GetFieldID(cylonClass, "detectedDeviceCount", "I");
		jfieldID fid_error		  			= env->GetFieldID(cylonClass, "error", "I");

		//set cylonStruct uint32_ts
		cylon.milliseconds 	=  (uint32_t) env->GetIntField(saul, fid_milliseconds);
		cylon.seconds 		= (uint32_t) env->GetIntField(saul, fid_seconds);
		cylon.minutes 		= (uint32_t) env->GetIntField(saul, fid_minutes);
		cylon.hours  		= (uint32_t) env->GetIntField(saul, fid_hours);
		cylon.day  			= (uint32_t) env->GetIntField(saul, fid_day);
		cylon.date 			=   (uint32_t) env->GetIntField(saul, fid_date);
		cylon.month  		= (uint32_t) env->GetIntField(saul, fid_month);
		cylon.year  		= (uint32_t) env->GetIntField(saul, fid_year);
		cylon.dst    		= (uint32_t) env->GetIntField(saul, fid_dst);
		cylon.allocationGranularity  	= (uint32_t) env->GetIntField(saul, fid_allocationGranularity);
		cylon.lowMemory  				= (uint32_t) env->GetIntField(saul, fid_lowMemory);
		cylon.detectedDeviceCount  		= (uint32_t) env->GetIntField(saul, fid_detectedDeviceCount);
		cylon.pageSize 					= (uint32_t) env->GetIntField(saul, fid_pageSize);

		//set cylonStruct int32_ts
		cylon.timeZone  = (int32_t) env->GetIntField(saul, fid_timeZone);
		cylon.error  	= (int32_t) env->GetIntField(saul, fid_error);

		//set cylonStruct uint64_ts
		cylon.processorCount = (uint64_t) env->GetIntField(saul, fid_processorCount);

		//log progress
		__android_log_print(ANDROID_LOG_DEBUG, "Saul", "NDK:LC: [%s]", "cylonStruct ints: done");


		//===LONGS===
		//Retrieve fields
		jfieldID fid_memoryBytes 	= env->GetFieldID(cylonClass, "memoryBytes", "J");
		jfieldID fid_threshold 		= env ->GetFieldID(cylonClass, "threshold", "J");
		jfieldID fid_bytesAvails 	= env-> GetFieldID(cylonClass, "bytesAvails", "J");

		//set cylonStruct uint64_ts
		cylon.memoryBytes 	= (uint64_t) env->GetLongField(saul, fid_memoryBytes);
		cylon.threshold 	= (uint64_t) env->GetLongField(saul, fid_threshold);
		cylon.bytesAvails 	= (uint64_t) env->GetLongField(saul, fid_bytesAvails);

		//log progress
		__android_log_print(ANDROID_LOG_DEBUG, "Saul", "NDK:LC: [%s]", "cylonStruct longs: done");


		//===FLOATS===
		//Retrieve fields
		jfieldID fid_hertz = env->GetFieldID(cylonClass, "hertz", "F");

		//set cylonStruct floats
		cylon.hertz = (float) env->GetFloatField(saul, fid_hertz);

		//log progress
		__android_log_print(ANDROID_LOG_DEBUG, "Saul", "NDK:LC: [%s]", "cylonStruct floats: done");


		//==LISTS==
		//Retrieve classes
		jclass listClass = env->FindClass("java/util/LinkedList");

		//Retrieve method ID
		//credit to pproksch @ stackoverflow for method retrieval code
		jmethodID m_toArray = env->GetMethodID(listClass, "toArray", "()[Ljava/lang/Object;");
		if(m_toArray == NULL)
		{
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "NDK:LC: [%s]", "WARNING: toArray not found!");
		}

		//Retrieve fields
		jfieldID fid_devices 		= env->GetFieldID(cylonClass, "detectedDevices", "Ljava/util/LinkedList;");
		jfieldID fid_controllers 	= env->GetFieldID(cylonClass, "controllers", "Ljava/util/LinkedList;");
		jfieldID fid_displays 		= env->GetFieldID(cylonClass, "displays", "Ljava/util/LinkedList;");
		jfieldID fid_storages 		= env->GetFieldID(cylonClass, "storages", "Ljava/util/LinkedList;");
		jfieldID fid_sensors 		= env->GetFieldID(cylonClass, "sensors", "Ljava/util/LinkedList;");

		//Retrieve lists
		jobject j_devices 		= env->GetObjectField(saul, fid_devices);
		jobject j_controllers 	= env->GetObjectField(saul, fid_controllers);
		jobject j_displays 		= env->GetObjectField(saul, fid_displays);
		jobject j_storages 		= env->GetObjectField(saul, fid_storages);
		jobject j_sensors 		= env->GetObjectField(saul, fid_sensors);

		//convert lists to arrays
		jobjectArray arr_devices 		= (jobjectArray)env->CallObjectMethod(j_devices, m_toArray);
		jobjectArray arr_controllers 	= (jobjectArray)env->CallObjectMethod(j_controllers, m_toArray);
		jobjectArray arr_displays 		= (jobjectArray)env->CallObjectMethod(j_displays, m_toArray);
		jobjectArray arr_storages 		= (jobjectArray)env->CallObjectMethod(j_storages, m_toArray);
		jobjectArray arr_sensors 		= (jobjectArray)env->CallObjectMethod(j_sensors, m_toArray);

		//iterate through devices
		for (int i = 0; i < env->GetArrayLength(arr_devices); i++)
		{
			//Grab element object from array
			jobject j_device = env->GetObjectArrayElement(arr_devices, i);

			//Build Device
			deviceStruct newDevice = buildDevice(env, j_device);

			//Add Device to end of cylon's device list
			cylon.detectedDevices.push_back(newDevice);
		}

		__android_log_print(ANDROID_LOG_DEBUG, "Saul", "NDK:LC: [%s]", "deviceStructs: done");
		__android_log_print(ANDROID_LOG_DEBUG, "Saul", "Length of devices: [%d]", (int)cylon.detectedDevices.size());

		//iterate through controllers
		for (int i = 0; i < env->GetArrayLength(arr_controllers); i++)
		{
			//Grab element object from array
			jobject j_controller = env->GetObjectArrayElement(arr_controllers, i);

			//Build Controller
			controllerStruct newController = buildController(env, j_controller);

			//Add Controller to end of cylon's controllers list
			cylon.controllers.push_back(newController);
		}

		__android_log_print(ANDROID_LOG_DEBUG, "Saul", "NDK:LC: [%s]", "controllerStructs: done");
		__android_log_print(ANDROID_LOG_DEBUG, "Saul", "Length of controllers: [%d]", (int)cylon.controllers.size());

		//iterate through displays
		for (int i = 0; i < env->GetArrayLength(arr_displays); i++)
		{
			//Grab element object from array
			jobject j_display = env->GetObjectArrayElement(arr_displays, i);

			//Build displayStruct
			displayStruct newDisplay = buildDisplay(env, j_display);

			cylon.displayDevices.push_back(newDisplay);
		}

		__android_log_print(ANDROID_LOG_DEBUG, "Saul", "NDK:LC: [%s]", "displayStructs: done");
		__android_log_print(ANDROID_LOG_DEBUG, "Saul", "Length of displays: [%d]", (int)cylon.displayDevices.size());

		//iterate through sensors
		for (int i = 0; i < env->GetArrayLength(arr_sensors); i++)
		{
			//Grab element object from array
			jobject j_sensor = env->GetObjectArrayElement(arr_sensors, i);

			//Build sensorStruct
			sensorStruct newSensor = buildSensor(env, j_sensor);

			//Add to end of list
			cylon.sensors.push_back(newSensor);
		}

		__android_log_print(ANDROID_LOG_DEBUG, "Saul", "NDK:LC: [%s]", "sensorStructs: done");
		__android_log_print(ANDROID_LOG_DEBUG, "Saul", "Length of sensors: [%d]", (int)cylon.sensors.size());

		//iterate through storage paths
		for (int i = 0; i < env->GetArrayLength(arr_storages); i++)
		{
			//Grab element object from array
			jobject j_storage = env->GetObjectArrayElement(arr_storages, i);

			//Build storageStruct
			storageStruct newStorage = buildStorage(env, j_storage);

			//Add to end of list
			cylon.storages.push_back(newStorage);
		}

		__android_log_print(ANDROID_LOG_DEBUG, "Saul", "NDK:LC: [%s]", "storageStructs: done");
		__android_log_print(ANDROID_LOG_DEBUG, "Saul", "Length of storages: [%d]", (int)cylon.storages.size());


		//Unavailable fields will be set to default cases
		cylon.pictureType = "0";


		//temp return
		return j_username;
	}
}
