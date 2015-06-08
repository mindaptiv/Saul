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

	JNIEXPORT jstring JNICALL
	Java_com_mindaptiv_saul_Cylon_buildCylon(JNIEnv *env, jobject obj, jobject saul)
	{
		//Retrieve class
		jclass cylonClass = env->GetObjectClass(saul);

		//Create cylon
		struct cylonStruct cylon;

		//===NAMES + STRINGS===
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

		//temp return
		return j_username;
	}
}
