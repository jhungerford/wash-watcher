#include <stdio.h>
#include "lsm6ds3.c"


int main() {
	LSM6DS3* module = malloc(sizeof LSM6DS3);
    if (module == 0) {
        printf("Error allocating module memory\n");
        exit(1);
    }

    setDefaults(module);

    begin(module);

    printf("x: %f\n", readFloatGyroX(module));

	return 0;
}
