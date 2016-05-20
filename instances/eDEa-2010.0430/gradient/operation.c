#include <stdio.h>
#include <math.h>
#include <stdlib.h>
#include "lib/rand.h"

#ifdef TEST
#define MAINPROGRAM
#endif
#include "eOPT.h"
#include "nabla.h"

#ifdef GRADIENT
#define SKIP_MOVE_RATE	0.5

#ifdef TEST
static void PrintVector(int n, double a[n])
{
	int j;

	for(j=0; j<n; j++)
		printf("%g ", a[j]);
	printf("\n");
}

static void PrintMatrix(int m, int n, double a[m][n])
{
	int i, j;

	for(i=0; i<m; i++) {
		for(j=0; j<n; j++)
			printf("%10f ", a[i][j]);
		printf("\n");
	}
	printf("\n");
}
#endif

int GradientMutationOperation(VARIABLE x[],
#ifdef EVALUATE_FC
	VARIABLE new[], VALUE c[], VALUE *f)
#else
	VARIABLE new[], VALUE c[])
#endif
{
	VARIABLE dx[Length];
	VALUE nabla[Constraints][Length];
	int j, k, t, num;
	double e, v, r;

#ifdef TEST
	printf("x="); PrintVector(Length, x);
#endif

	if(new!=x)
		for(j=0; j<Length; j++)
			new[j]=x[j];

	for(t=0; t<GradientSteps; t++) {
#ifdef EVALUATE_FC
		EvaluateFC(new, f, c);
#else
		EvaluateC(new, c);
#endif
		NConst++;
#if 0
		for(k=0; k<Constraints; k++)
			if(isinf(c[k]) || isnan(c[k])) return;
#endif
		e=0;
		num=0;
		for(k=0; k<Inequalities; k++) {
			v=c[k];
			if(v>0) { e+=v; num++; }
		}
		for(; k<Constraints; k++) {
			v=fabs(c[k])-Delta;
			if(v>0) { e+=v; num++; }
		}
#ifdef TEST
		printf("e=%g\n", e);
#endif
// return if feasible
	if(e==0) return 1;
// considering Epsilon value
//	if(e<Epsilon) return 1;

// for reducing computation time
		if(num==1) {
			r=Rand();
			if(r<SKIP_MOVE_RATE) return 1;
		}

// Calculate gradient of C
		EvaluateNabla(new, Length, c, nabla);
// Eliminate satisfied inequalities
		num=0;
		for(k=0; k<Inequalities; k++) {
			if(c[k]>0) {
				if(num<k) {
					for(j=0; j<Length; j++)
						nabla[num][j]=nabla[k][j];
					c[num]=c[k];
				}
				num++;
			}
		}
		for(; k<Constraints; k++) {
			v=(c[k]>=0? c[k] : -c[k]);
			if(v>Delta) {
				if(num<k) {
					for(j=0; j<Length; j++)
						nabla[num][j]=nabla[k][j];
#if 1 // considering Delta value
					if(c[k]>0)
						c[num]=c[k]-Delta;
					else	c[num]=c[k]+Delta;
#else // ignoring Delta value
					c[num]=c[k];
#endif
				}
				num++;
			}
		}
#ifdef DEBUG
		printf("nabla=");
		for(k=0; k<num; k++) {
			for(j=0; j<Length; j++)
				printf("%g ", nabla[k][j]);
			printf("=> %g\n", c[k]);
		}
#endif
		if(!Gsolve((double *)nabla, num, Length, c, dx))
			return 1;

		for(j=0; j<Length; j++)
			if(isinf(dx[j]) || isnan(dx[j])) return 1;

		for(j=0; j<Length; j++) {
			new[j]=new[j]-dx[j];
			if(new[j]<Ranges[j].min)
				new[j]=Ranges[j].min;
			else if(new[j]>Ranges[j].max)
				new[j]=Ranges[j].max;
		}
#ifdef TEST
		printf("num=%d\n", num);
		printf("dx="); PrintVector(Length, dx);
		printf("x="); PrintVector(Length, new);
#endif
	}
	return 0;
}

#ifdef TEST
#ifdef EVALUATE_FC
#define EvaluateC(x, C)	EvaluateFC(x, &ignore, C)
#endif

main(int argc, char *argv[])
{
    int j;
    double ignore;

    if(argc==2)
	Problem=atoi(argv[1]);
    else {
	printf("usage: %s ProblemID\n", argv[0]);
	exit(1);
    }
    InitProblem();

    /* memory allocation */
    Individual P=NewIndividuals(1);
    Individual Q=NewIndividuals(1);
    double e, v, C[Constraints];

    /* generate a point */
    srand(time(NULL));
    for(j=0; j<Length; j++)
#if 1
	P->x[j]=Ranges[j].min+Rand()*Ranges[j].width;
#else
	{ printf("x[%d]=", j); scanf("%lf", &P->x[j]); }
#endif

    EvaluateC(P->x, C);
    printf("C=");
    for(j=0; j<Constraints; j++)
	printf("%g ", C[j]);
    printf("\n");

#ifdef DEBUG
    printf("h_i=");
    for(j=0; j<Length-1; j++)
	printf("%g ", square(square(P->x[j])-P->x[j+1]));
    printf("\n");
#endif

#ifdef EVALUATE_FC
    if(!GradientMutationOperation(P->x, Q->x, C, &ignore))
#else
    if(!GradientMutationOperation(P->x, Q->x, C))
#endif

    	EvaluateC(Q->x, C);

    printf("C=");
    for(j=0; j<Constraints; j++)
	printf("%g ", C[j]);
    printf("\n");
    ConstraintViolation(Q->x, C, &e, &v);
    printf("e=%g, v=%g\n", e, v);
}
#endif
#endif
