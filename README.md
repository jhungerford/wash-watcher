# Wash Watcher
Wash watcher is an application that tells you when your laundry is done.  It uses an accelerometer connected to a raspberry pi that sits on your washer/dryer to detect when the machine stops shaking.

## Hardware

* Raspberry Pi 3
* [LSM6DS3 Breakout Board](https://www.sparkfun.com/products/13339)

## Wiring
Connect the LSM6DS3 breakout board to these pins: (`O` is the mounting hole, `x` is a wire to the breakout board, `.` is a disconnected pin) 

```
---------\
      O  |
         |
3.3V  x. |
SDA   x. |
SCL   x. |
      .. |
GND   x. |
      .. |
```

## Installation
On the Raspberry Pi running raspbian, enable i2c in `sudo raspi-config`, then run `sudo apt-get install -y i2c-tools`.  When the board is connected, `sudo i2cdetect -y 1` should show an i2c device with address `6b`:
 
```
pi@raspberrypi3:~/downloads/bcm2835-1.52 $ sudo i2cdetect -y 1
     0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f
00:          -- -- -- -- -- -- -- -- -- -- -- -- --
10: -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
20: -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
30: -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
40: -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
50: -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
60: -- -- -- -- -- -- -- -- -- -- -- 6b -- -- -- --
70: -- -- -- -- -- -- -- --
```

Download and install the [BCM2835](http://www.airspayce.com/mikem/bcm2835) library:

```
wget http://www.airspayce.com/mikem/bcm2835/bcm2835-1.52.tar.gz
tar -xzf bcm2835-1.52.tar.gz
cd bcm2835-1.52
./configure
make
sudo make check
sudo make install
```

Clone and install WiringPi ([Instructions](http://wiringpi.com/download-and-install/):

```
git clone git://git.drogon.net/wiringPi
cd wiringPi
./build
```

Test wiringPi's installation with the following - should print the wiringPi version, and a pinout table:
```
gpio -v
gpio readall
```

## Links:
* [RaspberryPi-GPIO](https://github.com/alanbarr/RaspberryPi-GPIO) - alternative raspi GPIO library
* [LSM6DS3 Board - examples and datasheets](https://www.sparkfun.com/products/13339)
* [Raspberry Pi SPI and I2C Tutorial](https://learn.sparkfun.com/tutorials/raspberry-pi-spi-and-i2c-tutorial)
* [I2C Tutorial](https://learn.sparkfun.com/tutorials/i2c)
* [Wiring Pi](http://wiringpi.com/reference/i2c-library/)

## License:
Wash Watcher: MIT
Wiring Pi is released under the GNU Lesser Public License version 3.