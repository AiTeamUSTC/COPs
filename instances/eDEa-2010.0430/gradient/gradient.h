#define ZERO_THRESHOLD	1e-20
/* gradient.c */
global int GradientMutationFlag;
global VALUE GradientMutationRate init(0.1);
					/* rate of gradient mutation */
global int GradientSteps init(3);	/* Steps of gradient */
global void (*CallGradientMutation)(Individual P);
					/* Gradient mutation function */

global int NGradient;			/* Number of gradient evaluations */

void GradientMutation(Individual P);
void InitGradientMutation(int *argc, char *argv[], Option *O, int *n);

/* operation.c */
#ifdef EVALUATE_FC
int GradientMutationOperation(VARIABLE x[], VARIABLE new[], VALUE c[], VALUE *f);
#else
int GradientMutationOperation(VARIABLE x[], VARIABLE new[], VALUE c[]);
#endif

/* gsolve-gsl.c */
int Ginverse(double *a, int m, int n, double *g);
int Gsolve(double *a, int m, int n, double *b, double *x);
