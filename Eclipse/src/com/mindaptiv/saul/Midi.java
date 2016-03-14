//Midi.java
//Java version of the midiStruct structure from Cylon.h
//josh@mindaptiv.com

package com.mindaptiv.saul;

//imports
import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.media.midi.*;
import java.util.LinkedList;

public class Midi
{
    final static int TYPE_VIRTUAL   = 0;
    final static int TYPE_USB       = 1;
    final static int TYPE_BLUETOOTH = 2;

    //"Parent" device structure
    Device superDevice;
    int devicesIndex;

    //Virtual/Bluetooth/USB
    int type;

    //Id number
    int id;

    //Port counts
    int outCount;
    int inCount;
    LinkedList<MidiPort> ports;

    //String names and numbers
    String vendorName;
    String productName;
    String deviceName;
    String serialNumber;
    String versionNumber;

    //Constructors
    //Build Midi using midi device info and Device parent
    @SuppressLint("NewApi")
    public Midi(MidiDeviceInfo info, Device superaDevice)
    {
        //Set parent
        this.superDevice = superaDevice;

        //Setup list
        this.ports = new LinkedList<MidiPort>();

        //Set type
        int infoType = info.getType();

        if(infoType == MidiDeviceInfo.TYPE_BLUETOOTH)
        {
            this.type = TYPE_BLUETOOTH;
        }
        else if(infoType == MidiDeviceInfo.TYPE_USB)
        {
            this.type = TYPE_USB;
        }

        //Check that we're on Marshmallow
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            //Grab bundle
            Bundle properties = info.getProperties();

            //Set fields
            this.vendorName = properties.getString(MidiDeviceInfo.PROPERTY_MANUFACTURER);
            this.versionNumber = properties.getString(MidiDeviceInfo.PROPERTY_VERSION);
            this.serialNumber = properties.getString(MidiDeviceInfo.PROPERTY_SERIAL_NUMBER);
            this.deviceName = properties.getString(MidiDeviceInfo.PROPERTY_NAME);
            this.productName = properties.getString(MidiDeviceInfo.PROPERTY_PRODUCT);

            //Grab ports
            this.inCount = info.getInputPortCount();
            this.outCount = info.getOutputPortCount();

            MidiDeviceInfo.PortInfo portData [] = info.getPorts();

            for(int i = 0; i < portData.length; i++)
            {
                //Build Port
                MidiPort port = new MidiPort();
                port.name   = portData[i].getName();
                port.number = portData[i].getPortNumber();
                port.type   = portData[i].getType();

                //Add to ports list for device
                this.ports.addLast(port);
            }
        }//END if Android M

    }//END Constructor

    //Build a dummy Midi for testing
    public Midi()
    {
        this.vendorName = "0";
        this.versionNumber = "0";
        this.serialNumber = "0";

        //Setup list
        this.ports = new LinkedList<MidiPort>();

        //Build Port
        MidiPort port = new MidiPort();
        port.name   = "0";
        port.number = 0;
        port.type   = 0;

        //Add to ports list for device
        this.ports.addLast(port);
    }//END dummy constructor
}//End class

class MidiPort
{
    //Constants
    final static int IN = 0;
    final static int OUT = 1;
    //END Constants

    //Fields
    String name;
    int number;
    int type;
    //END Fields
}//END class