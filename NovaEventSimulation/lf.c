#define NRANSI
#include "nrutil.h"

void lf(double y[], double dydx[], int n, double x, double h, double yout[],
	void (*derivs)(double, double [], double []))
{
  int i;
  double hh = h*0.5;
  
  for (i=1;i<n;i+=2){
    y[i]=y[i]+hh*y[i+1];
  }
  (*derivs)(x,y,dydx);
  for (i=1; i<n; i+=2){
    yout[i+1] = y[i+1]+h*dydx[i+1];
    yout[i] = y[i]+hh*yout[i+1];
  }
}
#undef NRANSI
/* (C) Copr. 1986-92 Numerical Recipes Software ?421.1-9. */
