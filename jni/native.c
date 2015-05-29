//includes
#include <jni.h>
#include <string.h>
#include <android/log.h>

#define DEBUG_TAG "NDK_Android_Saul_Test"

//Test method
void Java_com_mindaptiv_saul_Cylon_helloLog (JNIEnv* env, jobject this, jstring logThis)
{
	jboolean isCopy;
	const char* szLogThis = (*env)->GetStringUTFChars(env, logThis, &isCopy);

	__android_log_print(ANDROID_LOG_DEBUG, DEBUG_TAG, "NDK:LC: [%s]", szLogThis);

	(*env)->ReleaseStringUTFChars(env, logThis, szLogThis);

}//end method
