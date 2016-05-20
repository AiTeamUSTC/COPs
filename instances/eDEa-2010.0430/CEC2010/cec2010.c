#include <stdio.h>
#include <math.h>
#include "eOPT.h"

#ifdef CEC_CONTEST
void InformationCEC(Individual best, int isfirst, int updated)
{
	static int report_id;
	static int report10[]={ 20000, 100000, 200000, 0 };
	static int report30[]={ 60000, 300000, 600000, 0 };
	static int *report;
	double f, v, e;
#ifdef HOLD_CONSTRAINTS
	double *c;
#else
	double c[Constraints];
#endif
	int i, count[4];

	if(isfirst) {
		report=(Length==10? report10 : report30);
		report_id=0;
	}

	else if(report[report_id]>0 && NConst>=report[report_id]) {
#ifndef EVALUATE_FC
		if(best->state!=STATE_F)
			EvaluateF(best->x, &best->f);	// for statistics
#endif
		printf("FES%d %.15g ", report[report_id], best->f);
		if(best->error>0) {
#ifdef HOLD_CONSTRAINTS
			c=best->c;
#else
#ifdef EVALUATE_FC
			double ignore;
			EvaluateFC(best->x, &ignore, c);// for statistics
#else
			EvaluateC(best->x, c);		// for statistics
#endif
#endif
			for(i=0; i<4; i++)
				count[i]=0;
			e=0.0;
			for(i=0; i<Constraints; i++) {
				v=c[i];
				if(i>=Inequalities) v=fabs(v)-Delta;
				if(v>0) {
					count[0]++;
					if(v>1.0) count[1]++;
					else if(v>0.01) count[2]++;
					else if(v>0.0001) count[3]++;
					e+=v;
				}
			}
			printf("%d = %d %d %d %.15g\t%d %d\n",
				count[0], count[1], count[2], count[3],
				e/Constraints, NEval, NGradient);
		}
		else	printf("0 = 0 0 0 0.0\t%d %d\n", NEval, NGradient);
		report_id++;
	}
	if(LongReport>0 && updated) {
		f=best->f;
		v=best->error/Constraints;
		if(v==0)
			printf("+%d %d %d %g %g\n", Exp, NEval, NConst, f, v);
	}
}
#endif
