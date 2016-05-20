#include <stdio.h>
#include <gsl/gsl_blas.h>
#include <gsl/gsl_linalg.h>
#include "eOPT.h"

#ifdef GRADIENT
static int ginverse(gsl_matrix *A, gsl_matrix *G)
{
  int transpose, i, succeed;
  gsl_matrix *temp;
  double v;

  int m=A->size1;
  int n=A->size2;

  if(m<n) {
// A is transposed, since gsl_linalg_SV_decomp needs m>=n.
	transpose=1;
	gsl_matrix_transpose_memcpy(G, A);
	temp=A; A=G; G=temp;
	i=m; m=n; n=i;
  }
  else	transpose=0;

  gsl_vector *s=gsl_vector_alloc(n);
  gsl_matrix *S=gsl_matrix_alloc(n, n);
  gsl_matrix *V=gsl_matrix_alloc(n, n);
  gsl_vector *work=gsl_vector_alloc(n);
  gsl_matrix *B=gsl_matrix_alloc(n, n);

  /* A = U S V^T (A is replaced with U) */
  gsl_linalg_SV_decomp (A, V, s, work);

#ifdef DEBUG
  printf("U=");
  gsl_matrix_fprintf(stdout, A, "%f");
  printf("s=");
  gsl_vector_fprintf(stdout, s, "%f");
  printf("V=");
  gsl_matrix_fprintf(stdout, V, "%f");
#endif

  /* A^- = V S^- U^T */
  gsl_matrix_set_zero(S);
  for(i=0; i<n; i++) {
	v=gsl_vector_get(s, i);
	if(v>ZERO_THRESHOLD)
		gsl_matrix_set(S, i, i, 1.0/v);
  }
  succeed=(gsl_matrix_get(S, 0, 0)>0);
  if(succeed) {
	gsl_blas_dgemm(CblasNoTrans, CblasNoTrans, 1.0, V, S, 0.0, B);
	gsl_blas_dgemm(CblasNoTrans, CblasTrans, 1.0, B, A, 0.0, G);

#ifdef DEBUG
  printf("B=");
  gsl_matrix_fprintf(stdout, B, "%f");
  printf("G=");
  gsl_matrix_fprintf(stdout, G, "%f");
#endif

	if(transpose) { // return A, because A and G are swapped
		gsl_matrix_transpose_memcpy(A, G);
	}
  }

  gsl_matrix_free(V);
  gsl_matrix_free(S);
  gsl_matrix_free(B);
  gsl_vector_free(s);
  gsl_vector_free(work);

  return succeed;
}

static int gsolve(gsl_matrix *A, gsl_vector *b, gsl_vector *x)
{
  gsl_matrix *G=gsl_matrix_alloc(A->size2, A->size1);
  int succeed;

  succeed=ginverse(A, G);
  if(succeed)
  	gsl_blas_dgemv(CblasNoTrans, 1.0, G, b, 0.0, x);

  gsl_matrix_free(G);
  return succeed;
}

int Gsolve(double *a, int m, int n, double *b, double *x)
{
  gsl_matrix_view Av=gsl_matrix_view_array(a, m, n);
  gsl_vector_view bv=gsl_vector_view_array(b, m);
  gsl_vector_view xv=gsl_vector_view_array(x, n);

  gsl_matrix *A=&Av.matrix;
  gsl_vector *B=&bv.vector;
  gsl_vector *X=&xv.vector;

  return gsolve(A, B, X);
}

#ifdef TEST
int Ginverse(double *a, int m, int n, double *g)
{
  gsl_matrix_view Av=gsl_matrix_view_array(a, m, n);
  gsl_matrix_view Gv=gsl_matrix_view_array(g, n, m);

  gsl_matrix *A=&Av.matrix;
  gsl_matrix *G=&Gv.matrix;

  return ginverse(A, G);
}

int main (void)
{
  int row, col;
  int i, j;

  printf("number of row="); scanf("%d", &row);
  printf("number of col="); scanf("%d", &col);

  double a[row][col];
  double g[col][row];

  for(i=0; i<row; i++)
    for(j=0; j<col; j++) {
      printf("a[%d][%d]=", i, j);
      scanf("%lf", &a[i][j]);
    }

  Ginverse((double *)a, row, col, (double *)g);

  for(i=0; i<col; i++) {
    for(j=0; j<row; j++)
      printf("%g ", g[i][j]);
    printf("\n");
  }
  printf("\n");

  return 0;  
}
#endif
#endif
