package main

/*
#cgo LDFLAGS: -lm -lwiringPi
#include <errno.h>
#include <wiringPiI2C.h>
*/
import "C"
import "fmt"

const I2C_ADDR = 0x6B

func main() {
	var fd := C.wiringPiI2CSetup(I2C_ADDR)
	var whoami = int(C.wiringPiI2CReadReg8(fd))
	fmt.Printf("Who am I: %d", whoami)
}