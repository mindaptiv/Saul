#include <jni.h>
#include <string>
#include <android/log.h>
#include <list>
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
	//Variable Declaration
	struct cylonStruct testCylon;

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
	//END GetJStringContent

	//Convert Device to deviceStruct
	deviceStruct buildDevice(JNIEnv *env, jobject device)
	{
		//Retrieve class
		jclass deviceClass = env->GetObjectClass(device);

		//Create device
		struct deviceStruct nativeDevice;

		//Cred to Robert @ stackoverflow for retrieval code
		//===STRINGS===
		//Retrieve fields
		jfieldID fid_name 	= env->GetFieldID(deviceClass, "name", "Ljava/lang/String;");
		jfieldID fid_id 	= env->GetFieldID(deviceClass, "id_string", "Ljava/lang/String;");

		//Retrieve Java Strings
		jstring j_name 	= (jstring) env->GetObjectField(device, fid_name);
		jstring j_id 	= (jstring) env->GetObjectField(device, fid_id);

		//Convert to std::strings
		std::string name;
		std::string id_string;
		GetJStringContent(env, j_name, name);
		GetJStringContent(env, j_id, id_string);

		//Set deviceStruct strings
		nativeDevice.name 		= name;
		nativeDevice.id_string 	= id_string;

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
		jfieldID fid_midiIndex		= env->GetFieldID(deviceClass, "midiIndex", "I");
		jfieldID fid_id_int			= env->GetFieldID(deviceClass, "id_int", "J");

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
		nativeDevice.sensorsIndex 	= (uint32_t)env->GetIntField(device, fid_sensorsIndex);
		nativeDevice.midiIndex		= (uint32_t)env->GetIntField(device, fid_midiIndex);
		nativeDevice.id_int			= (uint32_t)env->GetLongField(device, fid_id_int);

		//set unavailable fields
		nativeDevice.usb_bus			= 0;
		nativeDevice.udev_deviceNumber 	= 0;

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
		jfieldID fid_deviceIndex			= env->GetFieldID(displayClass, "devicesIndex", "I");

		//set displayStruct uint32_ts
		nativeDisplay.rotationPreference 	= (uint32_t)env->GetIntField(display, fid_rotationPreference);
		nativeDisplay.currentRotation 		= (uint32_t)env->GetIntField(display, fid_currentRotation);
		nativeDisplay.nativeRotation 		= (uint32_t)env->GetIntField(display, fid_nativeRotation);
		nativeDisplay.isStereoscopicEnabled = (uint32_t)env->GetIntField(display, fid_isStereoscopicEnabled);
		nativeDisplay.deviceIndex			= (uint32_t)env->GetIntField(display, fid_deviceIndex);

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

		//unavailable fields
		nativeDisplay.horizontalResolution 	= 0;
		nativeDisplay.verticalResolution 	= 0;
		nativeDisplay.upperLeftX			= 0;
		nativeDisplay.upperLeftY			= 0;
		nativeDisplay.refreshRate			= 0;
		nativeDisplay.driverData			= NULL;
		nativeDisplay.colorData				= NULL;
		nativeDisplay.colorLength			= 0;

		//===DEVICE===
		//Retrieve field
		jfieldID fid_superDevice = env->GetFieldID(displayClass, "superDevice", "Lcom/mindaptiv/saul/Device;");

		//Retrieve object
		jobject j_device = env->GetObjectField(display, fid_superDevice);

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
		jfieldID fid_deviceIndex			= env->GetFieldID(sensorClass, "devicesIndex", "I");

		//set sensortStruct uint32_t's
		nativeSensor.minDelay 				= (uint32_t)env->GetIntField(sensor, fid_minDelay);
		nativeSensor.type 					= (uint32_t)env->GetIntField(sensor, fid_type);
		nativeSensor.version 				= (uint32_t)env->GetIntField(sensor, fid_version);
		nativeSensor.fifoMaxEventCount 		= (uint32_t)env->GetIntField(sensor, fid_fifoMaxEventCount);
		nativeSensor.fifoReservedEventCount = (uint32_t)env->GetIntField(sensor, fid_fifoReservedEventCount);
		nativeSensor.maxDelay 				= (uint32_t)env->GetIntField(sensor, fid_maxDelay);
		nativeSensor.reportingMode 			= (uint32_t)env->GetIntField(sensor, fid_reportingMode);
		nativeSensor.isWakeUpSensor 		= (uint32_t)env->GetIntField(sensor, fid_isWakeUpSensor);
		nativeSensor.deviceIndex			= (uint32_t)env->GetIntField(sensor, fid_deviceIndex);


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
		jfieldID fid_isEmulated 	= env->GetFieldID(storageClass, "isEmulated", "I");
		jfieldID fid_deviceIndex	= env->GetFieldID(storageClass, "devicesIndex", "I");

		//set storageStruct uint32_t's
		nativeStorage.isEmulated = (uint32_t)env->GetIntField(storage, fid_isEmulated);
		nativeStorage.deviceIndex = (uint32_t)env->GetIntField(storage, fid_deviceIndex);


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


		//===DEVICE===
		//Retrieve field
		jfieldID fid_superDevice = env->GetFieldID(storageClass, "superDevice", "Lcom/mindaptiv/saul/Device;");

		//Retrieve object
		jobject j_device = env->GetObjectField(storage, fid_superDevice);

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
		jfieldID fid_deviceIndex	= env->GetFieldID(controllerClass, "devicesIndex", "I");

		//set controllerStruct uint32_t's
		nativeController.packetNumber 	= (uint32_t)env->GetIntField(controller, fid_packetNumber);
		nativeController.userIndex 		= (uint32_t)env->GetIntField(controller, fid_userIndex);
		nativeController.deviceIndex	= (uint32_t)env->GetIntField(controller, fid_deviceIndex);
		nativeController.buttons 		= (uint32_t)env->GetIntField(controller, fid_buttons);
		nativeController.id				= nativeController.userIndex;


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

		//Return
		return nativeController;
	}

	midiPortStruct buildMidiPort(JNIEnv *env, jobject midiPort)
	{
		//Retrieve class
		jclass midiPortClass = env->GetObjectClass(midiPort);

		//Create struct
		struct midiPortStruct nativeMidiPort;

		//===STRINGS===
		//Retrieve fields
		jfieldID fid_name = env->GetFieldID(midiPortClass, "name", "Ljava/lang/String;");

		//Retrieve Java Strings
		jstring j_name = (jstring) env->GetObjectField(midiPort, fid_name);

		//Concert to std::strings
		std::string name;
		GetJStringContent(env, j_name, name);

		//Set port strings
		nativeMidiPort.name = name;

		//===INTS===
		jfieldID fid_number 	= env->GetFieldID(midiPortClass, "number", "I");
		jfieldID fid_type 		= env->GetFieldID(midiPortClass, "type", "I");

		//set midiPortStruct uint32_t's
		nativeMidiPort.type 	= (uint32_t)env->GetIntField(midiPort, fid_type);
		nativeMidiPort.number 	= (uint32_t)env->GetIntField(midiPort, fid_number);

		//Return
		return nativeMidiPort;
	}

	midiStruct buildMidi(JNIEnv *env, jobject midi)
	{
		//Retrieve class
		jclass midiClass = env->GetObjectClass(midi);

		//Create struct
		struct midiStruct nativeMidi;

		//===STRINGS===
		//Retrieve fields
		jfieldID fid_vendorName 	= env->GetFieldID(midiClass, "vendorName", "Ljava/lang/String;");
		jfieldID fid_productName 	= env->GetFieldID(midiClass, "productName", "Ljava/lang/String;");
		jfieldID fid_deviceName 	= env->GetFieldID(midiClass, "deviceName", "Ljava/lang/String;");
		jfieldID fid_serialNumber	= env->GetFieldID(midiClass, "serialNumber", "Ljava/lang/String;");
		jfieldID fid_versionNumber	= env->GetFieldID(midiClass, "versionNumber", "Ljava/lang/String;");

		//Retrieve Java Strings
		jstring j_vendorName = (jstring) env->GetObjectField(midi, fid_vendorName);
		jstring j_productName = (jstring) env->GetObjectField(midi, fid_productName);
		jstring j_deviceName = (jstring) env->GetObjectField(midi, fid_deviceName);
		jstring j_serialNumber = (jstring) env->GetObjectField(midi, fid_serialNumber);
		jstring j_versionNumber = (jstring) env->GetObjectField(midi, fid_versionNumber);

		//Convert to std::strings
		std::string vendorName;
		std::string productName;
		std::string deviceName;
		std::string serialNumber;
		std::string versionNumber;
		GetJStringContent(env, j_vendorName, vendorName);
		GetJStringContent(env, j_productName, productName);
		GetJStringContent(env, j_deviceName, deviceName);
		GetJStringContent(env, j_serialNumber, serialNumber);
		GetJStringContent(env, j_versionNumber, versionNumber);

		//Set midi strings
		nativeMidi.vendorName = vendorName;
		nativeMidi.productName = productName;
		nativeMidi.deviceName = deviceName;
		nativeMidi.serialNumber = serialNumber;
		nativeMidi.versionNumber = versionNumber;

		//===INTS===
		//Retrieve fields
		jfieldID fid_type 			= env->GetFieldID(midiClass, "type", "I");
		jfieldID fid_id 			= env->GetFieldID(midiClass, "id", "I");
		jfieldID fid_outCount 		= env->GetFieldID(midiClass, "outCount", "I");
		jfieldID fid_inCount		= env->GetFieldID(midiClass, "inCount", "I");
		jfieldID fid_deviceIndex	= env->GetFieldID(midiClass, "devicesIndex", "I");

		//set midiStruct ints
		nativeMidi.type = (uint32_t)env->GetIntField(midi, fid_type);
		nativeMidi.id = (uint32_t)env->GetIntField(midi, fid_id);
		nativeMidi.outCount = (uint32_t)env->GetIntField(midi, fid_outCount);
		nativeMidi.inCount = (uint32_t)env->GetIntField(midi, fid_inCount);
		nativeMidi.deviceIndex = (uint32_t)env->GetIntField(midi, fid_deviceIndex);

		//===DEVICE===
		//Retrieve field
		jfieldID fid_superDevice = env->GetFieldID(midiClass, "superDevice", "Lcom/mindaptiv/saul/Device;");

		//Retrieve object
		jobject j_device = env->GetObjectField(midi, fid_superDevice);

		//===PORTS LIST===
		//Retrieve class
		jclass listClass = env->FindClass("java/util/LinkedList");

		//Retrieve method ID
		jmethodID m_toArray = env->GetMethodID(listClass, "toArray", "()[Ljava/lang/Object;");
		if(m_toArray == NULL)
		{
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "NDK:LC: [%s]", "WARNING: toArray not found!");
		}

		//Retrieve fields
		jfieldID fid_ports = env->GetFieldID(midiClass, "ports", "Ljava/util/LinkedList;");

		//Retrieve list
		jobject j_ports = env->GetObjectField(midi, fid_ports);

		//convert list to array
		jobjectArray arr_ports = (jobjectArray)env->CallObjectMethod(j_ports, m_toArray);

		//iterate through the ports
		for(int i = 0; i < env->GetArrayLength(arr_ports); i++)
		{
			//Grab element object from array
			jobject j_port = env->GetObjectArrayElement(arr_ports, i);

			//Build midiPort
			midiPortStruct midiPort = buildMidiPort(env, j_port);

			//Add midi port to end of midi's list
			nativeMidi.ports.push_back(midiPort);
		}

		//Return
		return nativeMidi;
	}
	//END buildMidi


	void produceJniLog()
	{
	__android_log_print(ANDROID_LOG_DEBUG, "Saul", "Cylon @: %p", &testCylon);
	__android_log_print(ANDROID_LOG_DEBUG, "Saul", "Username: %s", testCylon.username.c_str());
	__android_log_print(ANDROID_LOG_DEBUG, "Saul", "Time: %d:%d:%d:%d", testCylon.hours, testCylon.minutes, testCylon.seconds, testCylon.milliseconds);
	__android_log_print(ANDROID_LOG_DEBUG, "Saul", "Date: %d, %d/%d/%d", testCylon.day, testCylon.month, testCylon.date, testCylon.year);
	__android_log_print(ANDROID_LOG_DEBUG, "Saul", "Timezone: %d %d %s",testCylon.dst, testCylon.timeZone, testCylon.timeZoneName.c_str() );
	__android_log_print(ANDROID_LOG_DEBUG, "Saul", "Device Name: %s", testCylon.deviceName.c_str());
	__android_log_print(ANDROID_LOG_DEBUG, "Saul", "Architecture: %s", testCylon.architecture.c_str());
	__android_log_print(ANDROID_LOG_DEBUG, "Saul", "Processor Level: %d", testCylon.processorLevel);
	__android_log_print(ANDROID_LOG_DEBUG, "Saul", "Processor Count: %llu", (long long unsigned int)testCylon.processorCount);
	__android_log_print(ANDROID_LOG_DEBUG, "Saul", "Page Size: %d", testCylon.pageSize);
	__android_log_print(ANDROID_LOG_DEBUG, "Saul", "Allocation Granularity: %d", testCylon.allocationGranularity);
	__android_log_print(ANDROID_LOG_DEBUG, "Saul", "Min App Address: %llX", (long long unsigned int)testCylon.minAppAddress);
	__android_log_print(ANDROID_LOG_DEBUG, "Saul", "Max App Address: %llX", (long long unsigned int)testCylon.maxAppAddress);
	__android_log_print(ANDROID_LOG_DEBUG, "Saul", "Hertz: %f", testCylon.hertz);
	__android_log_print(ANDROID_LOG_DEBUG, "Saul", "Memory Bytes: %llu", (long long unsigned int)testCylon.memoryBytes);
	__android_log_print(ANDROID_LOG_DEBUG, "Saul", "OS Architecture: %d", testCylon.osArchitecture);
	__android_log_print(ANDROID_LOG_DEBUG, "Saul", "Low Memory: %d", testCylon.lowMemory);
	__android_log_print(ANDROID_LOG_DEBUG, "Saul", "Low Memory Threshold: %llu", (long long unsigned int)testCylon.threshold);
	__android_log_print(ANDROID_LOG_DEBUG, "Saul", "Bytes Available: %llu", (long long unsigned int)testCylon.bytesAvails);
	__android_log_print(ANDROID_LOG_DEBUG, "Saul", "Picture Type: %s", testCylon.pictureType.c_str());
	__android_log_print(ANDROID_LOG_DEBUG, "Saul", "Picture Path: %s", testCylon.picturePath.c_str());
	__android_log_print(ANDROID_LOG_DEBUG, "Saul", "Picture Location: %llX", (long long unsigned int)testCylon.pictureLocation);
	__android_log_print(ANDROID_LOG_DEBUG, "Saul", "Installed Device Count: %d", testCylon.installedDeviceCount);
	__android_log_print(ANDROID_LOG_DEBUG, "Saul", "Detected Device Count: %d", testCylon.detectedDeviceCount);
	__android_log_print(ANDROID_LOG_DEBUG, "Saul", "Portable Storage Count: %d", testCylon.portableStorageCount);
	__android_log_print(ANDROID_LOG_DEBUG, "Saul", "Video Device Count: %d", testCylon.videoCount);
	__android_log_print(ANDROID_LOG_DEBUG, "Saul", "Mic Count %d: ", testCylon.micCount);
	__android_log_print(ANDROID_LOG_DEBUG, "Saul", "Speaker Count: %d", testCylon.speakerCount);
	__android_log_print(ANDROID_LOG_DEBUG, "Saul", "Location Aware Device Count: %d", testCylon.locationCount);
	__android_log_print(ANDROID_LOG_DEBUG, "Saul", "Scanner Count: %d", testCylon.scannerCount);
	__android_log_print(ANDROID_LOG_DEBUG, "Saul", "Error Code: %d", testCylon.error);

	//device lists
		__android_log_print(ANDROID_LOG_DEBUG, "Saul", "==============Devices");
		for(std::list<deviceStruct>::const_iterator iterator = testCylon.detectedDevices.begin(), end = testCylon.detectedDevices.end(); iterator != end; ++iterator)
		{
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tName: %s", iterator->name.c_str());
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tType: %d", iterator->deviceType);
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tVendor ID: %X", iterator->vendorID);
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tID String: %s", iterator->id_string.c_str());
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tID Int: %llu", (long long unsigned int)iterator->id_int);
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tOrientation: %d", iterator->orientation);
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tIs Default? %d", iterator->isDefault);
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tIs Enabled? %d", iterator->isEnabled);
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tController Index: %d", iterator->controllerIndex);
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tDisplay Index: %d", iterator->displayIndex);
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tStorage Index: %d", iterator->storageIndex);
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tSensors Index: %d", iterator->sensorsIndex);
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tMIDI Index: %d\n", iterator->midiIndex);
		}//END for devices

		__android_log_print(ANDROID_LOG_DEBUG, "Saul", "==============Game Controllers");
		for(std::list<controllerStruct>::const_iterator iterator = testCylon.controllers.begin(), end = testCylon.controllers.end(); iterator != end; ++iterator)
		{
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tController #: %d", iterator->userIndex);
			//__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\tInstance ID: %d", iterator->id);
			//__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\tName: %s", iterator->name.c_str());
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tButtons Mask: %X", iterator->buttons);
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tLeft Trigger: %f", iterator->leftTrigger);
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tRight Trigger: %f", iterator->rightTrigger);
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tLeft X: %f", iterator->thumbLeftX);
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tLeft Y: %f", iterator->thumbLeftY);
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tRight X: %f", iterator->thumbRightX);
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tRight Y: %f", iterator->thumbRightY);
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tDevice Index: %d", iterator->deviceIndex);
		}//END for controllers

		__android_log_print(ANDROID_LOG_DEBUG, "Saul", "==============Displays");
		for(std::list<displayStruct>::const_iterator iterator = testCylon.displayDevices.begin(), end = testCylon.displayDevices.end(); iterator != end; ++iterator)
		{
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tName: %s", iterator->superDevice.name.c_str());
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tDisplay #: %s", iterator->superDevice.id_string.c_str());
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tDevice Index: %d", iterator->deviceIndex );
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tRotation Preference: %d", iterator->rotationPreference);
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tCurrent Rotation: %d", iterator->currentRotation);
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tNative Rotation: %d", iterator->nativeRotation);
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tHorizontal Res: %d", iterator->horizontalResolution);
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tVertical Res: %d", iterator->verticalResolution);
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tUpper Left X: %d", iterator->upperLeftX);
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tUpper Left Y: %d", iterator->upperLeftY);
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tRefresh Rate: %f", iterator->refreshRate);
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tResolution Scale: %f", iterator->resolutionScale);
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tLogical DPI: %f", iterator->logicalDPI);
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tRaw DPI X: %f", iterator->rawDPIX);
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tRaw DPI Y: %f", iterator->rawDPIY);
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tDriver Data: %p", iterator->driverData);
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tColor Data: %s", iterator->colorData);
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tColor Length: %d", iterator->colorLength);
		}//END for displays

		__android_log_print(ANDROID_LOG_DEBUG, "Saul", "==============Sensors");
		for(std::list<sensorStruct>::const_iterator iterator = testCylon.sensors.begin(), end = testCylon.sensors.end(); iterator != end; ++iterator)
		{
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tName: %s", iterator->name.c_str());
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tDevice Index: %d", iterator->deviceIndex);
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tMin Delay: %d", iterator->minDelay);
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tType: %d", iterator->type);
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tString Type: %s", iterator->stringType.c_str());
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tVendor: %s", iterator->vendor.c_str());
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tPower: %f", iterator->power);
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tResolution: %f", iterator->resolution);
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tMax Range: %f", iterator->maxRange);
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tFifo Reserved Events: %d", iterator->fifoReservedEventCount);
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tFifo Max Events: %d", iterator->fifoMaxEventCount);
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tMax Delay: %d", iterator->maxDelay);
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tReporting Mode: %d", iterator->reportingMode);
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tIs Wakeup Sensor? %d", iterator->isWakeUpSensor);
		}//END for sensors

		__android_log_print(ANDROID_LOG_DEBUG, "Saul", "==============Storage Drives");
		for(std::list<storageStruct>::const_iterator iterator= testCylon.storages.begin(), end = testCylon.storages.end(); iterator != end; ++iterator)
		{
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tPath: %s", iterator->path.c_str());
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tDevice Index: %d", iterator->deviceIndex);
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tBytes Avails: %llu", (long long unsigned int)iterator->bytesAvails);
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tTotal Bytes: %llu", (long long unsigned int)iterator->totalBytes);
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tIs Emulated? %d", iterator->isEmulated);
		}//END storage structs

		__android_log_print(ANDROID_LOG_DEBUG, "Saul", "==============MIDI Devices");
		for(std::list<midiStruct>::const_iterator iterator = testCylon.midiDevices.begin(), end = testCylon.midiDevices.end(); iterator != end; ++iterator)
		{
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tID: %d", iterator->id);
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tDevice Index: %d", iterator->deviceIndex);
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tType: %d", iterator->type);
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tOut Count: %d", iterator->outCount);
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tIn Count: %d", iterator->inCount);
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tVendor Name: %s", iterator->vendorName.c_str());
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tDevice Name: %s", iterator->deviceName.c_str());
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tProduct Name: %s", iterator->productName.c_str());
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tVersion Number: %s", iterator->versionNumber.c_str());
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tSerial Number: %s", iterator->serialNumber.c_str());
			__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\tPorts:");
			for(std::list<midiPortStruct>::const_iterator iterator1 = iterator->ports.begin(), end1 = iterator->ports.end(); iterator1 != end1; ++iterator1)
			{
				__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\t\tPort Name: %s", iterator1->name.c_str());
				__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\t\tNumber: %d", iterator1->number);
				__android_log_print(ANDROID_LOG_DEBUG, "Saul", "\t\t\tType: %d", iterator->type);
			}//END for ports
		}//END for midis

	}//END produce log

	JNIEXPORT jstring JNICALL
	Java_com_mindaptiv_saul_Cylon_buildCylon(JNIEnv *env, jobject saul)
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
		jfieldID fid_micCount				= env->GetFieldID(cylonClass, "micCount", "I");;
		jfieldID fid_speakerCount			= env->GetFieldID(cylonClass, "speakerCount", "I");;
		jfieldID fid_videoCount				= env->GetFieldID(cylonClass, "videoCount", "I");;
		jfieldID fid_locationCount			= env->GetFieldID(cylonClass, "locationCount", "I");;

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
		cylon.micCount					= (uint32_t) env->GetIntField(saul, fid_micCount);
		cylon.speakerCount				= (uint32_t) env->GetIntField(saul, fid_speakerCount);
		cylon.videoCount				= (uint32_t) env->GetIntField(saul, fid_videoCount);
		cylon.locationCount				= (uint32_t) env->GetIntField(saul, fid_locationCount);

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
		jfieldID fid_midiDevices	= env->GetFieldID(cylonClass, "midiDevices", "Ljava/util/LinkedList;");

		//Retrieve lists
		jobject j_devices 		= env->GetObjectField(saul, fid_devices);
		jobject j_controllers 	= env->GetObjectField(saul, fid_controllers);
		jobject j_displays 		= env->GetObjectField(saul, fid_displays);
		jobject j_storages 		= env->GetObjectField(saul, fid_storages);
		jobject j_sensors 		= env->GetObjectField(saul, fid_sensors);
		jobject j_midiDevices		= env->GetObjectField(saul, fid_midiDevices);

		//convert lists to arrays
		jobjectArray arr_devices 		= (jobjectArray)env->CallObjectMethod(j_devices, m_toArray);
		jobjectArray arr_controllers 	= (jobjectArray)env->CallObjectMethod(j_controllers, m_toArray);
		jobjectArray arr_displays 		= (jobjectArray)env->CallObjectMethod(j_displays, m_toArray);
		jobjectArray arr_storages 		= (jobjectArray)env->CallObjectMethod(j_storages, m_toArray);
		jobjectArray arr_sensors 		= (jobjectArray)env->CallObjectMethod(j_sensors, m_toArray);
		jobjectArray arr_midiDevices			= (jobjectArray)env->CallObjectMethod(j_midiDevices, m_toArray);

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

			//Set counter
			int counter = 0;

			//Synch lists
			for(std::list<deviceStruct>::iterator iterator = cylon.detectedDevices.begin(), end = cylon.detectedDevices.end(); iterator != end; ++iterator)
			{
				//if the device index matches that of the index in the list
				if(counter == newController.deviceIndex)
				{
					//set the superDevice
					newController.superDevice = *iterator;

					//bail
					break;
				}

				//increment counter
				counter++;
			}

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

			//Set counter
			int counter = 0;

			//Synch lists
			for(std::list<deviceStruct>::iterator iterator = cylon.detectedDevices.begin(), end = cylon.detectedDevices.end(); iterator != end; ++iterator)
			{
				//if the device index matches that of the index in the list
				if(counter == newDisplay.deviceIndex)
				{
					//set the superDevice
					newDisplay.superDevice = *iterator;

					//bail
					break;
				}

				//increment counter
				counter++;
			}

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

			//Set counter
			int counter = 0;

			//Synch lists
			for(std::list<deviceStruct>::iterator iterator = cylon.detectedDevices.begin(), end = cylon.detectedDevices.end(); iterator != end; ++iterator)
			{
				//if the device index matches that of the index in the list
				if(counter == newSensor.deviceIndex)
				{
					//set the superDevice
					newSensor.superDevice = *iterator;

					//bail
					break;
				}

				//increment counter
				counter++;
			}

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

			//Set counter
			int counter = 0;

			//Synch lists
			for(std::list<deviceStruct>::iterator iterator = cylon.detectedDevices.begin(), end = cylon.detectedDevices.end(); iterator != end; ++iterator)
			{
				//if the device index matches that of the index in the list
				if(counter == newStorage.deviceIndex)
				{
					//set the superDevice
					newStorage.superDevice = *iterator;

					//bail
					break;
				}

				//increment counter
				counter++;
			}

			//Add to end of list
			cylon.storages.push_back(newStorage);
		}

		__android_log_print(ANDROID_LOG_DEBUG, "Saul", "NDK:LC: [%s]", "storageStructs: done");
		__android_log_print(ANDROID_LOG_DEBUG, "Saul", "Length of storages: [%d]", (int)cylon.storages.size());

		for (int i = 0; i < env->GetArrayLength(arr_midiDevices); i++)
		{
			//Grab element objects from array
			jobject j_midi = env->GetObjectArrayElement(arr_midiDevices, i);

			//Build midiStruct
			midiStruct newMidi = buildMidi(env, j_midi);

			//Set counter
			int counter = 0;

			//Synch lists
			for(std::list<deviceStruct>::iterator iterator = cylon.detectedDevices.begin(), end = cylon.detectedDevices.end(); iterator != end; ++iterator)
			{
				//if the device index matches that of the index in the list
				if(counter == newMidi.deviceIndex)
				{
					//set the superDevice
					newMidi.superDevice = *iterator;

					//bail
					break;
				}

				//increment counter
				counter++;
			}

			//Add to end of list
			cylon.midiDevices.push_back(newMidi);
		}

		__android_log_print(ANDROID_LOG_DEBUG, "Saul", "NDK:LC: [%s]", "midiStructs: done");
		__android_log_print(ANDROID_LOG_DEBUG, "Saul", "Length of midiDevices: [%d]", (int)cylon.midiDevices.size());

		//Unavailable fields will be set to default cases
		//Avatar fields unused in Android context
		cylon.pictureType = "0";
		cylon.pictureLocation = 0;

		//Set device type counts
		cylon.installedDeviceCount = cylon.detectedDeviceCount;
		cylon.portableStorageCount = (uint32_t)cylon.storages.size();
		cylon.scannerCount = 0;

		//Mice struct not used in android context
		cylon.mice.anyHorizontalWheelPresent = 0;
		cylon.mice.anyLeftRightSwapped = 0;
		cylon.mice.anyVerticalWheelPresent = 0;
		cylon.mice.maxNumberOfButons = 0;
		cylon.mice.superDevice.controllerIndex = 0;
		cylon.mice.superDevice.deviceType = 0;
		cylon.mice.superDevice.displayIndex = 0;
		cylon.mice.superDevice.id_string = "0";
		cylon.mice.superDevice.id_int = 0;
		cylon.mice.superDevice.inDock = 0;
		cylon.mice.superDevice.inLid = 0;
		cylon.mice.superDevice.isDefault = 0;
		cylon.mice.superDevice.isEnabled = 0;
		cylon.mice.superDevice.name = "0";
		cylon.mice.superDevice.orientation = 0;
		cylon.mice.superDevice.panelLocation = 0;
		cylon.mice.superDevice.sensorsIndex = 0;
		cylon.mice.superDevice.storageIndex = 0;
		cylon.mice.superDevice.vendorID = 0;

		//unused fields in Android Context
		cylon.processorLevel = 0;
		cylon.minAppAddress = 0;
		cylon.maxAppAddress = 0;

		//Grab Wordsize
		cylon.osArchitecture = __WORDSIZE;

		//Confirm we've reached this point, so native conversion should be done
		jfieldID fid_nativeConverted = env->GetFieldID(cylonClass, "nativeConverted", "Z");
		env->SetBooleanField(saul, fid_nativeConverted, (jboolean) true);

		//set test cylon
		testCylon = cylon;

		//log
		produceJniLog();

		//temp return
		return j_username;
	}

	JNIEXPORT void JNICALL
	Java_com_mindaptiv_saul_Cylon_updateController(JNIEnv *env, jobject obj, jint controllerIndex, jobject controller)
	{
		//Grab index value
		int32_t index = (int32_t) controllerIndex;

		//set counter
		int counter = 0;

		//Retrieve class
		jclass controllerClass = env->GetObjectClass(controller);

		//Retrieve Fields
		//===INTS===
		jfieldID fid_buttons 		= env->GetFieldID(controllerClass, "buttons", "I");

		//===FLOATS===
		jfieldID fid_fLeftTrigger 	= env->GetFieldID(controllerClass, "fLeftTrigger", "F");
		jfieldID fid_fRightTrigger 	= env->GetFieldID(controllerClass, "fRightTrigger", "F");
		jfieldID fid_fThumbLeftX 	= env->GetFieldID(controllerClass, "fThumbLeftX", "F");
		jfieldID fid_fThumbLeftY	= env->GetFieldID(controllerClass, "fThumbLeftY", "F");
		jfieldID fid_fThumbRightX 	= env->GetFieldID(controllerClass, "fThumbRightX", "F");
		jfieldID fid_fThumbRightY 	= env->GetFieldID(controllerClass, "fThumbRightY", "F");
		//END Retrieve Fields

		//Identify and isolate correct controllerStruct in controllers
		for(std::list<controllerStruct>::iterator iterator = testCylon.controllers.begin(), end = testCylon.controllers.end(); iterator != end; ++iterator)
		{
			//if the iterated controllerStruct is at the requested index in the list
			if(counter == index)
			{
				//Update the axes and buttons mask
				//set controllerStruct uint32_t's
				iterator->buttons = (uint32_t)env->GetIntField(controller, fid_buttons);

				//set controllerStruct floats
				iterator->leftTrigger 	= (float)env->GetFloatField(controller, fid_fLeftTrigger);
				iterator->rightTrigger 	= (float)env->GetFloatField(controller, fid_fRightTrigger);
				iterator->thumbLeftX 	= (float)env->GetFloatField(controller, fid_fThumbLeftX);
				iterator->thumbLeftY 	= (float)env->GetFloatField(controller, fid_fThumbLeftY);
				iterator->thumbRightX 	= (float)env->GetFloatField(controller, fid_fThumbRightX);
				iterator->thumbRightY 	= (float)env->GetFloatField(controller, fid_fThumbRightY);

				//bail
				break;
			}//END if

			//increment the counter
			counter++;
		}//END For
	}//END updateController()

}//END extern C
