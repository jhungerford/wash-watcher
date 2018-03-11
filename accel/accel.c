#include <stdio.h>
#include <errno.h>
#include <wiringPiI2C.h>

int main() {
    printf("Running...\n");

    int fd = wiringPiI2CSetup(I2C_ADDR);
    if (fd < 0) {
        printf("Error setting up i2c for address 0x%X - errorno %d\n", I2C_ADDR, errno);
        return errno;
    }

    int who_are_you = wiringPiI2CReadReg8(fd, REG_WHO_AM_I);
    printf("Who am I ID: 0x%X", who_are_you);

    return 0;
}