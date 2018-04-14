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
	"sync"
	"github.com/jhungerford/wash-watcher/pkg/numbuffer"
	"math"
)

const I2CAddr = 0x6B

// Interrupt 1
const RegInt1Ctrl = 0x0D

// Who am I - constant value register
const RegWhoami = 0x0F
const ValueWhoami = 0x69

// Accelerometer data rate
const RegCtrl1Xl = 0x10

// Enable accelerometer axes
const RegCtrl9Xl = 0x18

// Accelerometer axis data
const RegOutxLXl = 0x28
const RegOutxHXl = 0x29
const RegOutyLXl = 0x2A
const RegOutyHXl = 0x2B
const RegOutzLXl = 0x2C
const RegOutzHXl = 0x2D

// Status
const RegStatus = 0x1E
const MaskStatusXlda = 0x01

type regAxis struct {
	low, high C.int
}

var AxisX = regAxis{RegOutxLXl, RegOutxHXl}
var AxisY = regAxis{RegOutyLXl, RegOutyHXl}
var AxisZ = regAxis{RegOutzLXl, RegOutzHXl}

type accelReading struct {
	time time.Time
	x, y, z int16
}

type sharedReading struct {
	mu      sync.Mutex
	reading accelReading
}

type sharedReadingBuffer struct {
	mu sync.Mutex
	buffer *numbuffer.Buffer
	variance int
}

// Connect to the LSM6DS3 module, verify the connection, and return the file descriptor.
// When power is applied to the module, it performs a 20ms boot procedure then configures
// the accelerometer and gyroscope in power-down mode.
// Returns the file descriptor, or an error if the module could not be set up.
func setup() (C.int, error) {
	// Initialize WiringPi
	fd, err := C.wiringPiI2CSetup(I2CAddr)
	if fd < 0 {
		return 0, fmt.Errorf("Error setting up i2c for address 0x%X - %s", I2CAddr, err)
	}

	// Enable the accelerometer
	C.wiringPiI2CWriteReg8(fd, RegCtrl9Xl, 0x38)  // accel: x, y, z axes
	C.wiringPiI2CWriteReg8(fd, RegCtrl1Xl, 0x60)  // accel: 416Hz (high performance mode)
	C.wiringPiI2CWriteReg8(fd, RegInt1Ctrl, 0x01) // accel: data ready interrupt on INT1
	
	// Verify the whoami register to ensure everything is working
	var whoami = int(C.wiringPiI2CReadReg8(fd, RegWhoami))
	if whoami != ValueWhoami {
		return 0, fmt.Errorf("Wrong value (0x%X) for the whoami register.", whoami)
	}

	return fd, nil
}

func readAccel(fd C.int) accelReading {
	return accelReading{
		time.Now(),
		int16(C.readAxis(fd, AxisX.low, AxisX.high)),
		int16(C.readAxis(fd, AxisY.low, AxisY.high)),
		int16(C.readAxis(fd, AxisZ.low, AxisZ.high)),
	}
}

func isAccelReady(fd C.int) bool {
	var status = C.wiringPiI2CReadReg8(fd, RegStatus)
	return (status & MaskStatusXlda) != 0
}

// Listens to the accelerometer, writing readings to the channel and shared variable
func listenAccel(fd C.int, reading *sharedReading, reading_ch chan<- accelReading) {
	for {
		if isAccelReady(fd) {
			read := readAccel(fd)

			reading.mu.Lock()
			reading.reading = read
			reading.mu.Unlock()

			reading_ch <- read
		}
	}
}

// Writes the reading to the shared variable
func bufferReading(readings *sharedReadingBuffer, reading_ch <-chan accelReading) {
	for reading := range reading_ch {
		readings.mu.Lock()
		readings.buffer.Add(magnitude(reading))
		readings.mu.Unlock()
	}
}

// Computes the variance over the values in the buffer
func computeVariance(readings *sharedReadingBuffer) float64 {
	readings.mu.Lock()

	mean := float64(readings.buffer.Fold(numbuffer.Sum)) / float64(readings.buffer.Length())

	variance := readings.buffer.Fold(func(acc, value float64) float64 {
		return acc + math.Pow(float64(value) - mean, 2)
	}) / float64(readings.buffer.Length())

	readings.mu.Unlock()

	return variance
}

// Returns the magnitude of the given accelerometer reading
func magnitude(reading accelReading) float64 {
	return math.Sqrt(
		math.Pow(float64(reading.x), 2) +
		math.Pow(float64(reading.y), 2) +
		math.Pow(float64(reading.z), 2))
}

func makeReadingHandler(reading *sharedReading) func(w http.ResponseWriter, r *http.Request) {
	return func(w http.ResponseWriter, r *http.Request) {
		reading.mu.Lock()

		fmt.Fprintf(w, "Time: %s x: %04X y: %04X z: %04X\n",
			reading.reading.time.Format(time.RFC3339),
			reading.reading.x, reading.reading.y, reading.reading.z)

		reading.mu.Unlock()
	}
}

func makeVarianceHandler(readings *sharedReadingBuffer) func(w http.ResponseWriter, r *http.Request) {
	return func(w http.ResponseWriter, r *http.Request) {
		fmt.Fprintf(w, "%+v", computeVariance(readings))
	}
}

func main() {
	readingCh := make(chan accelReading)

	reading := &sharedReading{
		sync.Mutex{},
		accelReading{time.Now(), 0, 0, 0},
	}

	variance := &sharedReadingBuffer{
		sync.Mutex{},
		numbuffer.New(100),
		0,
	}

	fd, err := setup()
	if err != nil {
		log.Fatal(err)
	}

	go listenAccel(fd, reading, readingCh)
	go bufferReading(variance, readingCh)

	http.HandleFunc("/reading", makeReadingHandler(reading))
	http.HandleFunc("/variance", makeVarianceHandler(variance))
	log.Fatal(http.ListenAndServe(":8080", nil))
}
