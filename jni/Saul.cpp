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
		jfieldID fid_name = env->GetFieldID(deviceClass, "name", "Ljava/lang/String;");
		jfieldID fid_id = env->GetFieldID(deviceClass, "id", "Ljava/lang/String;");

		//Retrieve Java Strings
		jstring j_name = (jstring) env->GetObjectField(device, fid_name);
		jstring j_id = (jstring) env->GetObjectField(device, fid_id);

		//Convert to std::strings
		std::string name;
		std::string id;
		GetJStringContent(env, j_name, name);
		GetJStringContent(env, j_id, id);

		//Set deviceStruct strings
		nativeDevice.name = name;
		nativeDevice.id = id;

		//==INTS==
		//Retrieve fields
		jfieldID fid_panelLocation = env->GetFieldID(deviceClass, "panelLocation", "I");
		jfieldID fid_inLid = env->GetFieldID(deviceClass, "inLid", "I");
		jfieldID fid_inDock = env->GetFieldID(deviceClass, "inDock", "I");
		jfieldID fid_isDefault = env->GetFieldID(deviceClass, "isDefault", "I");
		jfieldID fid_isEnabled = env->GetFieldID(deviceClass, "isEnabled", "I");
		jfieldID fid_orientation = env->GetFieldID(deviceClass, "orientation", "I");
		jfieldID fid_vendorID = env->GetFieldID(deviceClass, "vendorID", "I");
		jfieldID fid_deviceType = env->GetFieldID(deviceClass, "deviceType", "I");
		jfieldID fid_displayIndex = env->GetFieldID(deviceClass, "displayIndex", "I");
		jfieldID fid_controllerIndex = env->GetFieldID(deviceClass, "controllerIndex", "I");
		jfieldID fid_storageIndex = env->GetFieldID(deviceClass, "storageIndex", "I");
		jfieldID fid_sensorsIndex = env->GetFieldID(deviceClass, "sensorsIndex", "I");

		//set deviceStruct ints
		nativeDevice.panelLocation = (uint32_t)env->GetIntField(device, fid_panelLocation);
		nativeDevice.inLid = (uint32_t)env->GetIntField(device, fid_inLid);
		nativeDevice.inDock = (uint32_t)env->GetIntField(device, fid_inDock);
		nativeDevice.isDefault = (uint32_t)env->GetIntField(device, fid_isDefault);
		nativeDevice.isEnabled = (uint32_t)env->GetIntField(device, fid_isEnabled);
		nativeDevice.orientation = (uint32_t)env->GetIntField(device, fid_orientation);
		nativeDevice.vendorID = (uint32_t)env->GetIntField(device, fid_vendorID);
		nativeDevice.deviceType = (uint32_t)env->GetIntField(device, fid_deviceType);
		nativeDevice.displayIndex = (uint32_t)env->GetIntField(device, fid_displayIndex);
		nativeDevice.controllerIndex = (uint32_t)env->GetIntField(device, fid_controllerIndex);
		nativeDevice.storageIndex = (uint32_t)env->GetIntField(device, fid_storageIndex);
		nativeDevice.sensorsIndex = (uint32_t)env->GetIntField(device, fid_storageIndex);

		//Return
		return nativeDevice;
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
		jfieldID fid_picturePath    = env->GetFieldID(cylonClass, "pictureType", "Ljava/lang/String;");

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
		jfieldID fid_processorCount	  = env->GetFieldID(cylonClass, "processorCount", "I");
		jfieldID fid_allocationGranularity = env->GetFieldID(cylonClass, "allocationGranularity", "I");
		jfieldID fid_lowMemory	  = env->GetFieldID(cylonClass, "lowMemory", "I");
		jfieldID fid_detectedDeviceCount = env->GetFieldID(cylonClass, "detectedDeviceCount", "I");
		jfieldID fid_error		  = env->GetFieldID(cylonClass, "error", "I");

		//set cylonStruct uint32_ts
		cylon.milliseconds =  (uint32_t) env->GetIntField(saul, fid_milliseconds);
		cylon.seconds = (uint32_t) env->GetIntField(saul, fid_seconds);
		cylon.minutes = (uint32_t) env->GetIntField(saul, fid_minutes);
		cylon.hours  = (uint32_t) env->GetIntField(saul, fid_hours);
		cylon.day  = (uint32_t) env->GetIntField(saul, fid_day);
		cylon.date =   (uint32_t) env->GetIntField(saul, fid_date);
		cylon.month  = (uint32_t) env->GetIntField(saul, fid_month);
		cylon.year  = (uint32_t) env->GetIntField(saul, fid_year);
		cylon.dst    = (uint32_t) env->GetIntField(saul, fid_dst);
		cylon.allocationGranularity  = (uint32_t) env->GetIntField(saul, fid_allocationGranularity);
		cylon.lowMemory  = (uint32_t) env->GetIntField(saul, fid_lowMemory);
		cylon.detectedDeviceCount  = (uint32_t) env->GetIntField(saul, fid_detectedDeviceCount);
		cylon.pageSize = (uint32_t) env->GetIntField(saul, fid_pageSize);

		//set cylonStruct int32_ts
		cylon.timeZone  = (int32_t) env->GetIntField(saul, fid_timeZone);
		cylon.error  = (int32_t) env->GetIntField(saul, fid_error);

		//set cylonStruct uint64_ts
		cylon.processorCount = (uint64_t) env->GetIntField(saul, fid_processorCount);

		//log progress
		__android_log_print(ANDROID_LOG_DEBUG, "Saul", "NDK:LC: [%s]", "cylonStruct ints: done");


		//===LONGS===
		//Retrieve fields
		jfieldID fid_memoryBytes = env->GetFieldID(cylonClass, "memoryBytes", "J");
		jfieldID fid_threshold = env ->GetFieldID(cylonClass, "threshold", "J");
		jfieldID fid_bytesAvails = env-> GetFieldID(cylonClass, "bytesAvails", "J");

		//set cylonStruct uint64_ts
		cylon.memoryBytes = (uint64_t) env->GetLongField(saul, fid_memoryBytes);
		cylon.threshold = (uint64_t) env->GetLongField(saul, fid_threshold);
		cylon.bytesAvails = (uint64_t) env->GetLongField(saul, fid_bytesAvails);

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
		jfieldID fid_devices = env->GetFieldID(cylonClass, "detectedDevices", "Ljava/util/LinkedList;");
		jfieldID fid_controllers = env->GetFieldID(cylonClass, "controllers", "Ljava/util/LinkedList;");
		jfieldID fid_displays = env->GetFieldID(cylonClass, "displays", "Ljava/util/LinkedList;");
		jfieldID fid_storages = env->GetFieldID(cylonClass, "storages", "Ljava/util/LinkedList;");
		jfieldID fid_sensors = env->GetFieldID(cylonClass, "sensors", "Ljava/util/LinkedList;");

		//Retrieve lists
		jobject j_devices = env->GetObjectField(saul, fid_devices);
		jobject j_controllers = env->GetObjectField(saul, fid_controllers);
		jobject j_displays = env->GetObjectField(saul, fid_displays);
		jobject j_storages = env->GetObjectField(saul, fid_storages);
		jobject j_sensors = env->GetObjectField(saul, fid_sensors);

		//convert lists to arrays
		jobjectArray arr_devices = (jobjectArray)env->CallObjectMethod(j_devices, m_toArray);
		jobjectArray arr_controllers = (jobjectArray)env->CallObjectMethod(j_controllers, m_toArray);
		jobjectArray arr_displays = (jobjectArray)env->CallObjectMethod(j_displays, m_toArray);
		jobjectArray arr_storages = (jobjectArray)env->CallObjectMethod(j_storages, m_toArray);
		jobjectArray arr_sensors = (jobjectArray)env->CallObjectMethod(j_sensors, m_toArray);

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

		//temp return
		return j_username;
	}
}
