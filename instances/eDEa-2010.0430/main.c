#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#define MAINPROGRAM
#include "eOPT.h"
#include "lib/rand.h"

static void Statistics(int t, Individual P, int n);
static int BestGen, BestEval, BestConst, BestGradient;
#ifdef SUCCESS_CHECK
static int isSuccess;
#endif

void Updated(Individual P)
{
	int updated=0, isfirst=0;

	if(Best->state<0) {
		isfirst=1;
#ifdef SUCCESS_CHECK
		isSuccess=0;
#endif
	}

/* update best individuals */
	if(Best->state<0 || (BetterNormal(P, Best)
#ifdef CONSTRAINTS
		&& (Evaluations==0 || NConst<=Evaluations)
#else
		&& (Evaluations==0 || NEval<=Evaluations)
#endif
	)) {
		Copy(Best, P);
		BestGen=Gen;
		BestEval=NEval;
#ifdef CONSTRAINTS
		BestConst=NConst;
#ifdef GRADIENT
		BestGradient=NGradient;
#endif
#endif
#ifdef SUCCESS_CHECK
		if(Success>=0 && !isSuccess && Best->error==0) {
#ifdef CONSTRAINTS
			UpdateIndividual(Best);
#endif
			if(MaximizationFlag && Optimal-Best->f<Success
				|| !MaximizationFlag && Best->f-Optimal<Success) {
				printf("Success %d %d %d %g %g %d %d %d %d\n",
					NEval, NConst, NGradient,
					Best->f, Optimal,
					SuccessEval, FailEval,
					SuccessSkip, FailSkip);
				isSuccess=1;
			}
		}
#endif
		updated=1;
	}
#ifdef CEC_CONTEST
	InformationCEC(Best, isfirst, updated);
#endif
}

int main(int argc, char *argv[])
{
	unsigned long seed;
	Individual Pop, New, Temp;

	Parameters(argc, argv);	/* InitOptimizer() is called */

	Pop=NewIndividuals(Popsize*2);
	New=&Pop[Popsize];
	Best=NewIndividuals(1);

	for(Exp=1; Exp<=Experiments; Exp++) {
		seed=Random0();
//		printf("seed=%u\n", seed);
		SetRand(seed);
	// Initialize parameters
		Gen=NEval=0;
#ifdef CONSTRAINTS
		NConst=NOutside=0;
#ifdef GRADIENT
		NGradient=0;
#endif
#endif
#ifdef SUCCESS_CHECK
		SuccessEval=FailEval=SuccessSkip=FailSkip=0;
#endif
		Best->state=-1;

#ifdef NORMALIZE_CONSTRAINTS
		ConstraintInit(Pop, Popsize);
#endif
		Initialize(Pop, Popsize);
#ifdef NORMALIZE_CONSTRAINTS
		ConstraintNormalize(Pop, Popsize);
#endif
#ifdef CONSTRAINTS
		if(EpsilonFlag)
			Epsilon=Epsilon0(Pop, Popsize);
#endif
		if(Report>0)
			Statistics(Gen, Pop, Popsize);

		for(Gen=1; ; Gen++) {
#ifdef DEBUG
			Print(Pop, Popsize);
#endif
			if(Generations>0 && Gen>=Generations) break;
#ifdef CONSTRAINTS
			if(Evaluations>0 && NConst>=Evaluations) break;
#endif
			if(Evaluations>0 && NEval>=Evaluations) break;
#ifdef SUCCESS_CHECK
			if(StopFlag && isSuccess) break;
#endif

			Optimizer(Pop, Popsize, New);

			if(SwapPopulationFlag) {
				Temp=Pop; Pop=New; New=Temp;
			}
#ifdef CONSTRAINTS
#ifdef NORMALIZE_CONSTRAINTS
			ConstraintNormalize(Pop, Popsize);
#endif
			if(EpsilonFlag && Gen<EpsilonGenerations)
				Epsilon=EpsilonT(Gen, Pop, Popsize);
			else	Epsilon=0;
#endif
			if(Report>0 && Gen%Report==0)
				Statistics(Gen, Pop, Popsize);
		}
		if(Report>=0) {
			printf("Gen %d ", BestGen);
#ifdef CONSTRAINTS
#ifdef GRADIENT
			printf("Eval %d %d Const %d %d Grad %d %d Out %d ",
				BestEval, NEval, BestConst, NConst,
				BestGradient, NGradient, NOutside);
#else
			printf("Eval %d %d Const %d %d Out %d ",
				BestEval, NEval, BestConst, NConst, NOutside);
#endif
#else
			printf("Eval %d ", NEval);
#endif
#ifdef CONSTRAINTS
			UpdateIndividual(Best);
#endif
			Print(Best, 1);
		}
	}
	ExitProblem();
	return 0;
}

static void Statistics(int t, Individual P, int n)
{
	int i;
	int best, worst;
	double sum=0.0;
#ifdef CONSTRAINTS
	double sum_error=0.0;
#endif

	best=worst=0;
	for(i=0; i<n; i++) {
		sum+=P[i].f;
#ifdef CONSTRAINTS
		sum_error+=P[i].error;
#endif
		if(Better(&P[i], &P[best])) best=i;
		else if(Better(&P[worst], &P[i])) worst=i;
	}
#ifdef CONSTRAINTS
	printf("+%d %d %d %d %g %.8g/%g %.8g/%g %.8g/%g\n",
			Exp, t, NEval, NConst, Epsilon,
			P[best].f, P[best].error,
			sum/n, sum_error/n,
			P[worst].f, P[worst].error);
#else
	printf("+%d %d %.8g %.8g %.8g\n",
		Exp, t, P[best].f, sum/n, P[worst].f);
#endif
}
