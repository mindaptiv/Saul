LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_LDLIBS := -llog

LOCAL_MODULE := saulStuff
LOCAL_SRC_FILES := native.c Saul.cpp
LOCAL_C_INCLUDES := Cylon.h

include $(BUILD_SHARED_LIBRARY)