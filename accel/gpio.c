#include <bcm2835.h>
#include "gpio.h"

int gpio_init()
{
	bcm2835_gpio_fsel(LATCH_PIN, BCM2835_GPIO_FSEL_OUTP);
	bcm2835_gpio_fsel(PULSE_PIN, BCM2835_GPIO_FSEL_OUTP);
	bcm2835_gpio_fsel(DATA_PIN, BCM2835_GPIO_FSEL_INPT);

	bcm2835_gpio_write(LATCH_PIN, LOW);
	bcm2835_gpio_write(PULSE_PIN, LOW);

	return 1;
}

int gpio_close()
{
	bcm2835_gpio_write(LATCH_PIN, LOW);
	bcm2835_gpio_write(PULSE_PIN, LOW);

	return 1;
}
