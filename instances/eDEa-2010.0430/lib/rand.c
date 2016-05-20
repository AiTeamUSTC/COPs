#include <stdlib.h>
#include <math.h>
#include "lib/rand.h"

/*
	Linear Congruential random number Generator
	LCG(m,a,c): Xn+1=(a Xn + c) mod m
*/
//
//	LCG(2^32-1,A,C)
//
// posted in "sci.stat.math" by Marsaglia, George (1999-01-12) 
// see "random number generation" in Wikipedia
//
#define LCG32_A		69069
#define LCG32_C		1234567

UNSIGNED32 LCG32_seed;
void LCG32_init(UNSIGNED32 seed)
{
	LCG32_seed=seed;
}

UNSIGNED32 LCG32_rand(void)
{
	return(LCG32_seed=LCG32_A*LCG32_seed+LCG32_C);
} 

/*
	XORShift random number generator
	proposed by George Marsaglia in 2003
*/
UNSIGNED32 xorshift_seed[5]={
	123456789, 362436069, 521288629, 88675123, 886756453 
};

// set initial seeds
UNSIGNED32 xorshift32_seed=123456789UL;
void xorshift32_init(UNSIGNED32 seed)
{
	xorshift32_seed=seed;
}

// [a,b,c]=[13,17,5]
UNSIGNED32 xorshift32_rand(void)
{
#define y	xorshift32_seed
	y^=(y<<13); y^=(y>>17); y^=(y<<5);
	return(y);
#undef y
}

// generate an integer of 32 bits
void xorshift_init(UNSIGNED32 seed, int n)
{
	int i;

	LCG32_init(seed);
	for (i=0; i<n; i++)
		xorshift_seed[i]=LCG32_rand();
}

#define x	xorshift_seed[0]
#define y	xorshift_seed[1]
#define z	xorshift_seed[2]
#define w	xorshift_seed[3]
// [a,b,c]=[5,14,1], [15,4,21], [23,24,3], [5,12,29]
UNSIGNED32 xorshift128_rand(void)
{
	UNSIGNED32 t;
//	t=(x^(x<<a)); x=y; y=z; z=w; w=(w^(w>>c))^(t^(t>>b));
	t=(x^(x<<5)); x=y; y=z; z=w; w=(w^(w>>1))^(t^(t>>14));
	return(w);
}
#undef x
#undef y
#undef z
#undef w

/*
	Marsaglia polar method (modification of Box-Muller method)
	See Normal_random_variable in Wikipedia
*/
double gaussrand()
{ 
	static double U, V, W;
	static int stage=0;
	double S, X;

	if(stage==0) {
		do {
			U=((double)rand()/RAND_MAX)*2-1;
			V=((double)rand()/RAND_MAX)*2-1;
			S=U*U+V*V;
		} while(S>=1 || S==0);
		W=sqrt(-2*log(S)/S);
		X=U*W;
	} else {
		X=V*W;
	}
	stage=1-stage;
	return(X);
}

double cauchyrand()
{
	return tan(M_PI*((double)rand()/RAND_MAX-0.5));
}

#ifdef TEST
#include <stdio.h>

void SetRand(int type, UNSIGNED32 seed)
{
	switch(type) {
	case 1:	LCG32_init(seed); break;
	case 2: xorshift32_init(seed); break;
	case 3: xorshift128_init(seed); break;
	default:srand(seed);
	}
}

double Rand(int type)
{
	switch(type) {
	case 1:	return(LCG32_rand()/RAND_MAX32);
	case 2:	return(xorshift32_rand()/RAND_MAX32);
	case 3:	return(xorshift128_rand()/RAND_MAX32);
	default:return(rand()/RAND_MAX31);
	}
}

#define POINTS	(600000*30)
#undef N
#define	N	50
main(int argc, char *argv[])
{
	unsigned int i, k, C[N];
	int type;

	if(argc<2) {
		printf("usage: %s number\n", argv[0]);
		printf("0:rand, 1:LCG, 2: XORSHIFT32 3:XORSHIFT128\n");
		exit(0);
	}
	type=atoi(argv[1]);

	for(i=0; i<N; i++) C[i]=0;

	SetRand(type, 123456789);
	for(i=0; i<POINTS; i++) {
		k=(int)(Rand(type)*N);
		if(k>=N) {
			printf("%d exceeds %d\n", k, N);
			exit(1);
		}
		C[k]++;
	}
	for(i=0; i<N; i++) {
		printf("%6d ", C[i]);
		if(i%10==9) printf("\n");
	}
	int max=0;
	double avg=POINTS/N;
	double sum=0;
	for(i=0; i<N; i++) {
		if(fabs(C[i]-avg)>max) max=fabs(C[i]-avg);
		sum+=(C[i]-avg)*(C[i]-avg);
	}
	printf("max=%d std=%g\n", max, sqrt(sum/N));

	int inside=0;
	for(i=0; i<POINTS; i++) {
		double x=-1+2.0*Rand(type);
		double y=-1+2.0*Rand(type);
		double z=-1+2.0*Rand(type);
		if(x*x+y*y+z*z<=1) inside++;
	}
	double volume=(double)inside/POINTS*8;
	printf("4/3PI=%g (error=%g)\n", volume, volume-4.0/3*M_PI);
}
#endif
