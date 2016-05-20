/* compare.c */
int BetterNormal(Individual p1, Individual p2);
int BetterEpsilon(Individual p1, Individual p2, double epsilon);

/* epsilon.c */
global int EpsilonFlag;			/* control epsilon or not */
global VALUE Epsilon init(0);		/* epsilon level */
global VALUE Epsilon_0;			/* epsilon(0) */
global VALUE EpsilonRatio init(0.2);	/* generations (%) for e(t) control */
global int EpsilonGenerations;		/* generations for e(t) control */

void InitEpsilon(int *argc, char *argv[], Option *O, int *n);
double RankError(int rank, Individual P, int n);
void StatError(Individual P, int n,
		double *average, double *best, double *worst);

/* epsilon_0.c */
global char *E0_Name init("rank");	/* type of epsilon(0) */
global int E0_Type;
global VALUE E0_RankRatio init(0.2);	/* rank % of rank-type epsilon(0) */

void InitEpsilon0(void);
void ShowEpsilon0(void);
double Epsilon0(Individual P, int n);

/* epsilon_t.c */
global char *Et_Name init("pow");	/* type of epsilon(t) */
global int Et_Type;
global VALUE EpsilonFinal init(0);	/* value of final epsilon */
global VALUE EpsilonCP init(5.0);	/* a parameter cp for epsilon(t) */
global VALUE EpsilonAP init(0);		/* a parameter ap */
global VALUE Et_RankRatio init(0.2);	/* rank % of rank-type epsilon(t) */

void InitEpsilonT(void);
void ShowEpsilonT(void);
double EpsilonT(int t, Individual P, int n);
