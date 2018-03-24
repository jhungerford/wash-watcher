package main

/*
#include <stdio.h>

void greeting() {
	printf("Hello CGO\n");
}
*/
import "C"

func main() {
	C.greeting()
}

