package main

/*
#cgo LDFLAGS: -lm -lwiringPi
#include <errno.h>
#include <wiringPiI2C.h>

// Reads accelerometer data out of two registers and returns a single uint
// with 16 bits of data for one axis.
unsigned int readAxis(int fd, int low_addr, int high_addr) {
	unsigned int low = wiringPiI2CReadReg8(fd, low_addr);
	unsigned int high = wiringPiI2CReadReg8(fd, high_addr);

	return (high & 0xFF) << 8 | (low & 0xFF);
}
*/
import "C"
import (
	"fmt"
	"log"
	"net/http"
	"time"
)

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
	low, high C.int
}

var AXIS_X = reg_axis{REG_OUTX_L_XL, REG_OUTX_H_XL}
var AXIS_Y = reg_axis{REG_OUTY_L_XL, REG_OUTY_H_XL}
var AXIS_Z = reg_axis{REG_OUTZ_L_XL, REG_OUTZ_H_XL}

type accel_reading struct {
	time time.Time
	x, y, z uint
}

// Connect to the LSM6DS3 module, verify the connection, and return the file descriptor.
// When power is applied to the module, it performs a 20ms boot procedure then configures
// the accelerometer and gyroscope in power-down mode.
// Returns the file descriptor, or an error if the module could not be set up.
func setup() (C.int, error) {
	// Initialize WiringPi
	fd, err := C.wiringPiI2CSetup(I2C_ADDR)
	if (fd < 0) {
		return 0, fmt.Errorf("Error setting up i2c for address 0x%X - %s", I2C_ADDR, err)
	}

	// Enable the accelerometer
	C.wiringPiI2CWriteReg8(fd, REG_CTRL9_XL, 0x38)  // accel: x, y, z axes
	C.wiringPiI2CWriteReg8(fd, REG_CTRL1_XL, 0x60)  // accel: 416Hz (high performance mode)
	C.wiringPiI2CWriteReg8(fd, REG_INT1_CTRL, 0x01) // accel: data ready interrupt on INT1
	
	// Verify the whoami register to ensure everything is working
	var whoami = int(C.wiringPiI2CReadReg8(fd, REG_WHOAMI))
	if (whoami != VALUE_WHOAMI) {
		return 0, fmt.Errorf("Wrong value (0x%X) for the whoami register.", whoami)
	}

	return fd, nil
}

func readAccel(fd C.int) accel_reading {
	return accel_reading{
		time.Now(),
		uint(C.readAxis(fd, AXIS_X.low, AXIS_X.high)),
		uint(C.readAxis(fd, AXIS_Y.low, AXIS_Y.high)),
		uint(C.readAxis(fd, AXIS_Z.low, AXIS_Z.high)),
	}
}

func isAccelReady(fd C.int) bool {
	var status = C.wiringPiI2CReadReg8(fd, REG_STATUS)
	return (status & MASK_STATUS_XLDA) != 0
}

func handler(w http.ResponseWriter, r *http.Request) {
	fmt.Fprintf(w, "Hello world")
}

func main() {
	http.HandleFunc("/", handler)
	log.Fatal(http.ListenAndServe(":8080", nil))

	fd, err := setup()
	if err != nil {
		log.Fatal(err)
	}

	for {
		if isAccelReady(fd) {
			reading := readAccel(fd)

			fmt.Printf("Time: %s x: %04X y: %04X z: %04X\n",
				reading.time.Format(time.RFC3339),
				reading.x, reading.y, reading.z)
		}
	}
}
