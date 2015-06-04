#include <jni.h>
#include <string>
#include <android/log.h>
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
	Java_com_mindaptiv_saul_Cylon_buildCylon (JNIEnv *env, jobject obj, jobject saul)
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
}
