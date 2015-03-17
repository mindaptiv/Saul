# Saul

##Abstract
Grabs system and user information in an Android context.

##Goal
Saul is an Android (Jellybean and KitKat) targeted library for gathering both user and system infromation for the current device.  In order to be compatible with a variety of Android devices, Saul only makes use of Android API levels 16-19 (as they hold [~80% of the user market](https://developer.android.com/about/dashboards/index.html) as of this writing), in the case where a certain piece of functionality may be deprecated in a newer API from this range, or whenever older API's don't have access to particular API functionality, Saul will return the minimum required value to run Android (in the case of hardware), or an error/unknown/invalid value (in the case of user information).  The final version of this library will gather as much information as it can to create a profile for the logged in user, as well as in-depth stats on the hardware and system settings for their device.  This profile information will be stored in a struct that can be exported and utilized by other applications.

##Documentation
For more detailed information, please visit the [Saul wiki.](github.com/mindaptiv/saul/wiki)

##Contact
josh@mindaptiv.com
