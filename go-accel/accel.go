package main

/*
#cgo LDFLAGS: -lm -lwiringPi
#include <errno.h>
#include <wiringPiI2C.h>
*/
import "C"
import "fmt"

const I2C_ADDR = 0x6B
const REG_WHO_AM_I = 0x0F

func setup() C.int {
	var fd = C.wiringPiI2CSetup(I2C_ADDR)
	var whoami = int(C.wiringPiI2CReadReg8(fd, REG_WHO_AM_I))
	fmt.Printf("Who am I: 0x%X\n", whoami)

	return fd
}

func main() {
	setup()
}
