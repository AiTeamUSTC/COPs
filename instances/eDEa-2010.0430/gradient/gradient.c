#include <stdio.h>
#include <stdlib.h>
#include "eOPT.h"

#ifdef GRADIENT
static OptionRec Options[]={
{ "GRADIENT", "options for gradient mutation" },
#include "gradient.def"
};
#define OPTIONS	Numberof(Options)

void GradientMutation(Individual P)
{
	VALUE C[Constraints];

#ifdef EVALUATE_FC
	if(GradientMutationOperation(P->x, P->x, C, &P->f)) {
#else
	if(GradientMutationOperation(P->x, P->x, C)) {
#endif
		// if true, C is the constraints at P->x'
		ConstraintViolation(P->x, C, &P->error, &P->violation);
		P->state=STATE_C;
	}
}

void InitGradientMutation(int *argc, char *argv[], Option *O, int *n)
{
	*O=Options; *n=OPTIONS;
	AnalyzeParameters(*O, *n, argc, argv);
	CallGradientMutation=GradientMutation;
}
#endif
