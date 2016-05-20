#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include "eOPT.h"

#ifdef FCNSUITE_DLL
#define DLL
#endif

#if defined(DLL) || defined(DLL_LAZY)
#ifdef linux
#include <dlfcn.h>
#include <pthread.h>
typedef void (*PPROC) (double *, double *, double *, double *, int, int, int, int);
#define LIBHANDLE void *
#define GetProcedure dlsym
#define CloseDynalink dlclose
#else
#include <windows.h>
#include <process.h>
typedef void (WINAPI * PPROC) (double *, double *, double *, double *, int, int, int, int);
#define LIBHANDLE HANDLE
#define GetProcedure GetProcAddress
#define CloseDynalink FreeLibrary
#endif

PPROC pfcn;
LIBHANDLE hLibrary;

#ifdef TIMES
#include <time.h>
#include <sys/times.h>
extern int total_time;
extern struct tms before_time, end_time;
#endif

void InitDLL()
{
	char ProblemName[100];
#ifdef linux
#define LIBRARY	"CEC2010/fcnsuite.so"
//	hLibrary = dlopen ("./fcnsuite.so", RTLD_NOW);
	hLibrary = dlopen (LIBRARY, RTLD_NOW);
#else
#define LIBRARY	"CEC2010/fcnsuite.dll"
//	hLibrary = LoadLibrary ("fcnsuite.dll");
	if(hLibrary==NULL)
		hLibrary = LoadLibrary (LIBRARY);
#endif
	if(hLibrary==NULL) {
		printf("Library %s cannot be loaded\n", LIBRARY);
		exit(1);
	}
	sprintf(ProblemName, "C%02d", Problem);
	pfcn = (PPROC) GetProcedure (hLibrary, ProblemName); /* c01 to c18 is valid */
	if(pfcn==NULL) {
		printf("Function %s cannot be loaded\n", ProblemName);
		exit(1);
	}
}

void ExitDLL(void)
{
	CloseDynalink(hLibrary);
}

void EvaluateFC(VARIABLE x[], VALUE *fp, VALUE c[])
{
	pfcn(x, fp, c, c+Inequalities, Length, 1, Inequalities, Equalities);
}
#endif
