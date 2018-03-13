#include "accel.h"
#include <stdio.h>
#include <errno.h>
#include <wiringPiI2C.h>

/* 
 * When power is applied to the LSM6DS3 module, it performs a 20ms boot
 * procedure, then configures the accelerometer and gyroscope in power-down
 * mode.  Init sets up wiring pi, enables the accelerometer, and returns
 * the file descriptor for the i2c module.  If init returns a value less than 0,
 * a fatal error occurred.
 */
int init() {
    // Initialize wiring pi
    int fd = wiringPiI2CSetup(I2C_ADDR);
    if (fd < 0) {
        printf("Error setting up i2c for address 0x%X - errorno %d\n", I2C_ADDR, errno);
        return fd;
    }

    // Enable the accelerometer
    printf("CTRL1_XL: 0x%X\n", wiringPiI2CReadReg8(fd, REG_CTRL1_XL));
    printf("CTRL6_C:  0x%X\n", wiringPiI2CReadReg8(fd, REG_CTRL6_C));

    // Verify the Who am I register to ensure everything is working
    int whoami = wiringPiI2CReadReg8(fd, REG_WHO_AM_I);
    if (whoami != VALUE_WHO_AM_I) {
        errno = EBADE;
        return -1;
    }

    return fd;
}

int main() {
    int fd = init();
    if (fd < 0) {
        return errno;
    }

    return 0;
}
