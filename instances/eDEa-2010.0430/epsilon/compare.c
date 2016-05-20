#include "eOPT.h"

#ifdef CONSTRAINTS
/* normal comparison where epsilon=0 in epsilon comparison */
int BetterNormal(Individual p1, Individual p2)
{
	double e1, e2;

	e1=p1->error; e2=p2->error;
	if(e1<e2) return(1);
	else if(e1>e2) return(0);
	else {
		UpdateIndividual(p1);
		UpdateIndividual(p2);
		return(better(p1->f, p2->f));
	}
}

/* epsilon comparison */
int BetterEpsilon(Individual p1, Individual p2, double epsilon)
{
	double e1, e2;

	e1=p1->error; e2=p2->error;
	if(epsilon==0.0 || e1>epsilon || e2>epsilon) {
		if(e1<e2) return(1);
		else if(e1>e2) return(0);
		else {
			UpdateIndividual(p1);
			UpdateIndividual(p2);
			return(better(p1->f, p2->f));
		}
	}
	else {
		UpdateIndividual(p1);
		UpdateIndividual(p2);
		return(p1->f==p2->f? e1<e2 : better(p1->f, p2->f));
	}
}
#endif
