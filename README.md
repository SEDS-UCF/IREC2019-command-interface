# IREC2019-command-interface (Launch Computer)

## Overview
This application will be the user controlled portion of the ground support/flight computer equipment.
The primary goal of this system is to provide a GUI for interfacing with the GSE computer
and flight computer. We provide a network interface using point to point wifi for communicating with the GSE computer. All launch and diagnostic commands will be issued through this application using a request/response
model. The code for the GSE computer can be found in the [IREC2012-ground-controller](https://github.com/SEDS-UCF/IREC2020-ground-controller)
repository.

## Notable Features
* Live video feed of ground support equipment
* Real time status and diagnostic data for pre-launch operations
* Semi-real time status and diagnostic data for post-launch operations
* Persistent event and error logs
* Developer command line console for direct commands
* Custom user interface

## Dependencies
* [JFoenix](https://github.com/jfoenixadmin/JFoenix) for some of the user interface components
* [MessagePack](https://msgpack.org/index.html) for message serialization

