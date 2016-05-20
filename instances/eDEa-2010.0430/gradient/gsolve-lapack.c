#include <stdio.h>
#include "eOPT.h"

#ifdef GRADIENT
#define LAPACK_SVD
// LAPACK subroutine for FORTRAN
#ifdef LAPACK_SVD
extern int dgelsd_(int *m, int *n, int *nrhs,
	double *a, int *lda, double *b, int *ldb, double *s,
	double *rcond, int *rank, double *work, int *lwork,
	int *iwork, int *info);
#else
// dgelsy is faster than dgelsd, but seems to be less accurate.
extern int dgelsy_(int *m, int *n, int *nrhs, 
	double *a, int *lda, double *b, int *ldb, int *jpvt,
	double *rcond, int *rank, double *work, int * lwork,
	int *info);
#endif

int Gsolve(double *a, int m, int n, double *b, double *x)
{
  int i, j;

  int M=m, N=n, NRHS=1;

// column major
  int LDA=M;
  double *A=New(double, M*N, "A in Gsolve");
  for(i=0; i<M; i++)
	for(j=0; j<N; j++)
		A[j*M+i]=a[i*N+j];

  int MAXMN=(M>=N? M : N);
  double *B=New(double, MAXMN, "B in Gsolve");
  for(i=0; i<m; i++)
	B[i]=b[i];

//  double RCOND=-1;
  double RCOND=1e-5;
  int RANK, INFO;
  int MINMN=(M<=N? M : N);

#ifdef LAPACK_SVD
#define NLVL	10
#define SMLSIZ	25
  double *S=New(double, MINMN*MINMN, "S in Gsolve");
  int LWORK=12*MAXMN+2*MAXMN*SMLSIZ+8*MAXMN*NLVL+MAXMN*NRHS+square(SMLSIZ+1);
  int LIWORK=3*MINMN*NLVL+11*MINMN;
  double *WORK=New(double, LWORK, "WORK in Gsolve");
  int *IWORK=New(int, LIWORK, "WORK in Gsolve");

// SVD with divide and conquer 
  dgelsd_(&M, &N, &NRHS, A, &M, B, &MAXMN, S, &RCOND,
		&RANK, WORK, &LWORK, IWORK, &INFO);
#else
#define NB	10
  int *JPVT=New(int, N, "JPVT in Gsolve");
  int LWORK=MINMN+2*N+NB*(N+1);
  double *WORK=New(double, LWORK, "WORK in Gsolve");

  dgelsy_(&M, &N, &NRHS, A, &M, B, &MAXMN, JPVT, &RCOND,
		&RANK, WORK, &LWORK, &INFO);
#endif

  free(A); free(B); free(WORK);
#ifdef LAPACK_SVD
  free(S); free(IWORK);
#else
  free(JPVT);
#endif
  if(INFO==0) { // successful exit
  	for(i=0; i<n; i++)
    		x[i]=B[i];
	return 1;
  }
  return 0;
}

#ifdef TEST
int Length;
int main (void)
{
  int row, col;
  int i, j;

  printf("number of row="); scanf("%d", &row);
  printf("number of col="); scanf("%d", &col);

  double a[row][col];
  double b[row];
  double x[col];

  for(i=0; i<row; i++)
    for(j=0; j<col; j++) {
      printf("a[%d][%d]=", i, j);
      scanf("%lf", &a[i][j]);
    }
  for(i=0; i<row; i++) {
      printf("b[%d]=", i);
      scanf("%lf", &b[i]);
  }

  Gsolve((double *)a, row, col, b, x);

  for(j=0; j<col; j++)
      printf("%g ", x[j]);
    printf("\n");

  return 0;  
}
#endif
#endif
