# Saul

##Abstract
Grabs system and user information in an Android context.

##Goal
Saul is an Android (Jellybean and KitKat) targeted library for gathering both user and system infromation for the current device.  In order to be compatible with a variety of Android devices, Saul only makes use of Android API levels 16-22 (as they hold [~80% of the user market](https://developer.android.com/about/dashboards/index.html) as of this writing), in the case where a certain piece of functionality may be deprecated in a newer API from this range, or whenever older API's don't have access to particular API functionality, Saul will return the minimum required value to run Android (in the case of hardware), or an error/unknown/invalid value (in the case of user information).  The final version of this library will gather as much information as it can to create a profile for the logged in user, as well as in-depth stats on the hardware and system settings for their device.  This profile information will be stored in a struct that can be exported and utilized by other applications.

##Targeted API's
Saul was developed with a target level API of 22, and a minimum API level of 16.  

##Permissions
Make sure your manifest file for your Android project holds the following permissions or some of the functionality may not work properly:
   ` <uses-permission android:name="android.permission.READ_PROFILE"/>
    <uses-permission android:name="android.permission.READ_CONTACTS"/>
    <uses-permission android:name="android.permission.VIBRATE"/>
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
    <uses-permission android:name="android.permission.CAMERA"/>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
    <uses-permission android:name="android.permission.RECORD_AUDIO"/>
    <uses-permission android:name="android.permission.BLUETOOTH_ADMIN"/>
    <uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS"/>
    <uses-permission android:name="android.permission.BLUETOOTH"/>`

##Documentation
For more detailed information, please visit the [Saul wiki.](https://github.com/mindaptiv/Saul/wiki)

##Contact
josh@mindaptiv.com
