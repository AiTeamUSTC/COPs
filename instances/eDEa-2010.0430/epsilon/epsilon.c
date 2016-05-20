#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include "eOPT.h"

static OptionRec Options[]={
{ "CONSTRAINTS", "options for epsilon constrained method" },
#include "epsilon.def"
};
#define OPTIONS	Numberof(Options)

void InitEpsilon(int *argc, char *argv[], Option *O, int *n)
{
	*O=Options; *n=OPTIONS;
	AnalyzeParameters(*O, *n, argc, argv);
//	InitEpsilon0();
//	InitEpsilonT();
}

int CompError(const void *a1, const void *a2)
{
	double delta=(*(Individual *)a1)->error-(*(Individual *)a2)->error;

	return(delta<0.0? -1 : delta>0.0? 1 : 0);
}

double RankError(int rank, Individual P, int n)
{
	int i;
	Individual list[n];

	for(i=0; i<n; i++)
		list[i]=&P[i];
	qsort(list, n, sizeof(list[0]), CompError);
	if(rank>=n) rank=n-1;
	return(list[rank]->error);
}

void StatError(Individual P, int n,
		double *average, double *best, double *worst)
{
	int i;
	double sum_error, error_best, error_worst;

	sum_error=0.0;
	error_best=error_worst=P[0].error;
	for(i=0; i<n; i++) {
		sum_error+=P[i].error;
		if(P[i].error<error_best)
			error_best=P[i].error;
		else if(P[i].error>error_worst)
			error_worst=P[i].error;
	}
	*average=sum_error/n;
	*best=error_best;
	*worst=error_worst;
}

double Epsilon0(Individual P, int n)
{

	if(E0_RankRatio>=1) {
		double average, best, worst;
		StatError(P, n, &average, &best, &worst);
		Epsilon_0=worst*E0_RankRatio;
	}
	else	Epsilon_0=RankError((int)(E0_RankRatio*n), P, n);
	EpsilonGenerations=Generations*EpsilonRatio;
	return(Epsilon_0);
}

double EpsilonT(int gen, Individual P, int n)
{
	double t;

	t=(double)gen/EpsilonGenerations;
	return(Epsilon_0*pow((1-t),EpsilonCP));
}
