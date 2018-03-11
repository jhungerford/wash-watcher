/*
Adaptation of Sparkfun's LSM6DS3 Arduino library to C.

https://github.com/sparkfun/SparkFun_LSM6DS3_Arduino_Library/blob/master/src/SparkFunLSM6DS3.cpp

License: [MIT](http://opensource.org/licenses/MIT).
*/

#include "lsm6ds3.h"
#include "gpio.h"
#include "stdint.h"
#include <bcm2835.h>
#include <unistd.h>


status_t beginCore(LSM6DS3Core* core) {
    if (!gpio_init()) {
        return IMU_HW_ERROR;
    }

    // Let the module warm up for 100 ms
    usleep(100000);

    // Check the ID register to determine if the operation was a success.
    uint8_t readCheck;
    readRegister(core, &readCheck, LSM6DS3_ACC_GYRO_WHO_AM_I_REG);
    
    if (readCheck != 0x69) {
        return IMU_HW_ERROR;
    }

    return IMU_SUCCESS;
}

//The following utilities read and write to the IMU

//ReadRegisterRegion takes a uint8 array address as input and reads
//  a chunk of memory into that array.
status_t readRegisterRegion(LSM6DS3Core* core, uint8_t* outputPointer, uint8_t offset, uint8_t length) {
    return IMU_NOT_SUPPORTED;
}

//readRegister reads one 8-bit register
status_t readRegister(LSM6DS3Core* core, uint8_t* outputPointer, uint8_t offset) {
    return IMU_NOT_SUPPORTED;
}

//Reads two 8-bit regs, LSByte then MSByte order, and concatenates them.
//  Acts as a 16-bit read operation
status_t readRegisterInt16(LSM6DS3Core* core, int16_t* outputPointer, uint8_t offset) {
    return IMU_NOT_SUPPORTED;
}

//Writes an 8-bit byte;
status_t writeRegister(LSM6DS3Core* core, uint8_t offset, uint8_t dataToWrite) {
    return IMU_NOT_SUPPORTED;
}

//Change to embedded page
status_t embeddedPage(LSM6DS3Core* core) {
    return IMU_NOT_SUPPORTED;
}

//Change to base page
status_t basePage(LSM6DS3Core* core) {
    return IMU_NOT_SUPPORTED;
}


void setDefaults(LSM6DS3* module) {
    module->i2cAddress = 0x6B;
    module->settings.gyroEnabled = 1;  //Can be 0 or 1
	module->settings.gyroRange = 2000;   //Max deg/s.  Can be: 125, 245, 500, 1000, 2000
	module->settings.gyroSampleRate = 416;   //Hz.  Can be: 13, 26, 52, 104, 208, 416, 833, 1666
	module->settings.gyroBandWidth = 400;  //Hz.  Can be: 50, 100, 200, 400;
	module->settings.gyroFifoEnabled = 1;  //Set to include gyro in FIFO
	module->settings.gyroFifoDecimation = 1;  //set 1 for on /1

	module->settings.accelEnabled = 1;
	module->settings.accelODROff = 1;
	module->settings.accelRange = 16;      //Max G force readable.  Can be: 2, 4, 8, 16
	module->settings.accelSampleRate = 416;  //Hz.  Can be: 13, 26, 52, 104, 208, 416, 833, 1666, 3332, 6664, 13330
	module->settings.accelBandWidth = 100;  //Hz.  Can be: 50, 100, 200, 400;
	module->settings.accelFifoEnabled = 1;  //Set to include accelerometer in the FIFO
	module->settings.accelFifoDecimation = 1;  //set 1 for on /1

	module->settings.tempEnabled = 1;

	//Select interface mode
	module->settings.commMode = 1;  //Can be modes 1, 2 or 3

	//FIFO control data
	module->settings.fifoThreshold = 3000;  //Can be 0 to 4096 (16 bit bytes)
	module->settings.fifoSampleRate = 10;  //default 10Hz
	module->settings.fifoModeWord = 0;  //Default off

	module->allOnesCounter = 0;
	module->nonSuccessCounter = 0;
}

//Call to apply SensorSettings
status_t begin(LSM6DS3* module) {
    return IMU_NOT_SUPPORTED;
}

//Returns the raw bits from the sensor cast as 16-bit signed integers
int16_t readRawAccelX(LSM6DS3* module) {
    return -1;
}

int16_t readRawAccelY(LSM6DS3* module) {
    return -1;
}

int16_t readRawAccelZ(LSM6DS3* module) {
    return -1;
}

int16_t readRawGyroX(LSM6DS3* module) {
    return -1;
}

int16_t readRawGyroY(LSM6DS3* module) {
    return -1;
}

int16_t readRawGyroZ(LSM6DS3* module) {
    return -1;
}


//Returns the values as floats.  Inside, this calls readRaw___(LSM6DS3*, ;
float readFloatAccelX(LSM6DS3* module) {
    return -1.0;
}

float readFloatAccelY(LSM6DS3* module) {
    return -1.0;
}

float readFloatAccelZ(LSM6DS3* module) {
    return -1.0;
}

float readFloatGyroX(LSM6DS3* module) {
    return -1.0;
}

float readFloatGyroY(LSM6DS3* module) {
    return -1.0;
}

float readFloatGyroZ(LSM6DS3* module) {
    return -1.0;
}


//Temperature related methods
int16_t readRawTemp(LSM6DS3* module) {
    return -1;
}

float readTempC(LSM6DS3* module) {
    return -1.0;
}

float readTempF(LSM6DS3* module) {
    return -1.0;
}


//FIFO stuff
void fifoBegin(LSM6DS3* module) {

}

void fifoClear(LSM6DS3* module) {

}

int16_t fifoRead(LSM6DS3* module) {
    return -1;
}

uint16_t fifoGetStatus(LSM6DS3* module) {
    return -1;
}

void fifoEnd(LSM6DS3* module) {

}


float calcGyro(LSM6DS3* module, int16_t) {
    return -1.0;
}

float calcAccel(LSM6DS3* module, int16_t) {
    return -1.0;
}
