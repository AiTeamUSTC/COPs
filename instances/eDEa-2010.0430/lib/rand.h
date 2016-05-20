#include <stdlib.h>
// 2^31-1 and 2^32-1
#define RAND_MAX31	2147483647.0
#define RAND_MAX32	4294967295.0
// 64bit/32bit system
typedef unsigned int UNSIGNED32;
// 32bit/16bit
//typedef unsigned long UNSIGNED32;

extern double gaussrand(void);
extern double cauchyrand(void);

/*
	Linear Congruential random number Generator
	LCG(a,c): Xn+1=(a Xn + c) mod m
*/
extern UNSIGNED32 LCG32_seed;
extern void LCG32_init(UNSIGNED32 seed);
extern UNSIGNED32 LCG32_rand(void);

/*
	xorshift random number generator (k=4,5)
*/
extern void xorshift32_init(UNSIGNED32 seed);
extern UNSIGNED32 xorshift32_rand(void);

extern void xorshift_init(UNSIGNED32 seed, int n);
#define xorshift128_init(seed) xorshift_init(seed, 4)

extern UNSIGNED32 xorshift128_rand(void);
#define xorshift128_rand1() (xorshift128_rand()*(1.0/RAND_MAX32))
#define xorshift128_rand2() (xorshift128_rand()*(1.0/(RAND_MAX32+1)))
