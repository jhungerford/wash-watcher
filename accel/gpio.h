#ifndef _GPIO_H_
#define _GPIO_H_

#define DURATION_POLLING_IN_MS		17 // TODO: lower to 200ns?
#define DURATION_LATCH_SIGNAL_IN_MS	12
#define DURATION_PULSE_SIGNAL_IN_US	6

#define LATCH_PIN	RPI_GPIO_P1_15
#define PULSE_PIN	RPI_GPIO_P1_16
#define DATA_PIN	RPI_GPIO_P1_18

int gpio_init();
int gpio_close();

#endif
