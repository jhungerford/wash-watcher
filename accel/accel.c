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
    wiringPiI2CWriteReg8(fd, REG_CTRL9_XL, 0x38);  // x, y, z axies
    wiringPiI2CWriteReg8(fd, REG_CTRL1_XL, 0x60);  // 416Hz (high performance mode)
    wiringPiI2CWriteReg8(fd, REG_INT1_CTRL, 0x01); // Acc data ready interrupt on INT1

    // Verify the Who am I register to ensure everything is working
    int whoami = wiringPiI2CReadReg8(fd, REG_WHO_AM_I);
    if (whoami != VALUE_WHO_AM_I) {
        errno = EBADE;
        return -1;
    }

    return fd;
}

int readAxis(int fd, AxisData axis) {
    int low = wiringPiI2CReadReg8(fd, axis.reg_low);
    int high = wiringPiI2CReadReg8(fd, axis.reg_high);

    return (high & 0xFF) << 8 | low;
}

int main() {
    int fd = init();
    if (fd < 0) {
        return errno;
    }

    while (1) {
        int status = wiringPiI2CReadReg8(fd, REG_STATUS);
        if ((status & MASK_STATUS_XLDA) == 0) {
            continue;
        }

        printf("x: %X  y: %X  z: %X\n", readAxis(fd, XL_X_AXIS), readAxis(fd, XL_Y_AXIS), readAxis(fd, XL_Z_AXIS));
    }

    return 0;
}
