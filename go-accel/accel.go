package main

/*
#cgo LDFLAGS: -lm -lwiringPi
#include <errno.h>
#include <wiringPiI2C.h>
*/
import "C"
import "fmt"
import "time"

const I2C_ADDR = 0x6B

// Interrupt 1
const REG_INT1_CTRL = 0x0D

// Who am I - constant value register
const REG_WHOAMI = 0x0F
const VALUE_WHOAMI = 0x69

// Accelerometer data rate
const REG_CTRL1_XL = 0x10

// Enable accelerometer axes
const REG_CTRL9_XL = 0x18

// Accelerometer axis data
const REG_OUTX_L_XL = 0x28
const REG_OUTX_H_XL = 0x29
const REG_OUTY_L_XL = 0x2A
const REG_OUTY_H_XL = 0x2B
const REG_OUTZ_L_XL = 0x2C
const REG_OUTZ_H_XL = 0x2D

// Status
const REG_STATUS = 0x1E
const MASK_STATUS_XLDA = 0x01

type reg_axis struct {
	low, high int
}

const AXIS_X = reg_axis{REG_OUTX_L_XL, REG_OUTX_H_XL}
const AXIS_Y = reg_axis{REG_OUTY_L_XL, REG_OUTY_H_XL}
const AXIS_Z = reg_axis{REG_OUTZ_L_XL, REG_OUTZ_H_XL}

type accel_reading struct {
	time Time
	x, y, z int
}

// Connect to the LSM6DS3 module, verify the connection, and return the file descriptor.
// When power is applied to the module, it performs a 20ms boot procedure then configures
// the accelerometer and gyroscope in power-down mode.
// Returns the file descriptor, or an error if the module could not be set up.
func setup() (C.int, error) {
	// Initialize WiringPi
	var fd, errno = C.wiringPiI2CSetup(I2C_ADDR)
	if (fd < 0) {
		return nil, fmt.Errorf("Error setting up i2c for address 0x%X - errno %d", I2C_ADDR, errno)
	}

	// Enable the accelerometer
	C.wiringPiI2CWriteReg8(fd, REG_CTRL9_XL, 0x38)  // accel: x, y, z axes
	C.wiringPiI2CWriteReg8(fd, REG_CTRL1_XL, 0x60)  // accel: 416Hz (high performance mode)
	C.wiringPiI2CWriteReg8(fd, REG_INT1_CTRL, 0x01) // accel: data ready interrupt on INT1
	
	// Verify the whoami register to ensure everything is working
	var whoami = int(C.wiringPiI2CReadReg8(fd, REG_WHOAMI))
	if (whoami != VALUE_WHOAMI) {
		return nil, fmt.Errorf("Wrong value (0x%X) for the whoami register.", whoami)
	}

	return fd, nil
}

func readAxis(fd int, axis reg_axis) int {
	var low = C.wiringPiI2CReadReg8(fd, reg_axis.low)
	var high = C.wiringPiI2CReadReg8(fd, reg_axis.high)

	return (high & 0xFF) << 8 | low
}

func readAccel(fd int) accel_reading {
	return accel_reading{
		time.Now(),
		readAxis(fd, AXIS_X),
		readAxis(fd, AXIS_Y),
		readAxis(fd, AXIS_Z),
	}
}

func isAccelReady(fd int) bool {
	var status = wiringPiI2CReadReg8(fd, REG_STATUS)
	return (status & MASK_STATUS_XLDA) != 0
}

func main() {
	fd = setup()

	for {
		if isAccelReady(fd) {
			reading := readAccel(fd)

			fmt.Printf("Time: %s x: %2X y: %2X z: %2X\n",
				reading.time.Format(time.RFC3339),
				reading.x, reading.y, reading.z)
		}
	}
}
