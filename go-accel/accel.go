package main

// #cgo LDFLAGS: -lm -lwiringPi
// #include <stdio.h>
// #include <errno.h>
// #include <wiringPiI2C.h>
import (
	"C"
	"fmt"
)

const I2C_ADDR = 0x6B

// Initializes the accelerometer, returning the file descriptor
func init() C.int {
	var fd = C.wiringPiI2cSetup(I2C_ADDR)
	
	var whoami = int(C.wiringPiI2CReadReg8(fd))
	fmt.Printf("Who am I: %d", whoami)

	return fd
}

func main() {
	init()
}