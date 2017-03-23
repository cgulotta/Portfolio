#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include "nrutil.h"
#include "nr.h"

int num_points;
int soften;
double *m;

void rk4(double y[], double f[], int n, double x, double h, double yout[], void (*derivs) (double, double [], double []));

void lf(double y[], double f[], int n, double x, double h, double yout[], void (*derivs) (double, double [], double []));

double energy(double y[]){
  int i,j;
  double Ke = 0.0;
  double Pe = 0.0;
  for(i=1; i<=num_points; i++){
    Ke += m[i]/2*(pow(y[2+id(i)],2)+pow(y[4+id(i)],2)+pow(y[6+id(i)],2));
      for(j=1; j<=num_points; j++){
	if (i > j) {
	  Pe += -m[i]*m[j]/sqrt(pow(y[1+id(i)]-y[1+id(j)],2)+pow(y[3+id(i)]-y[3+id(j)],2)+pow(y[5+id(i)]-y[5+id(j)],2)+pow(soften,2));
	}
      }
  }
  return Ke+Pe;
}

void func(double x, double y[], double f[]){
  double denom, xij, xji, yij, yji, zij, zji;
  int i,j;
  //ZERO OUT
  for(i = 1; i <= num_points; i++){
    f[2+id(i)] = 0.0;
    f[4+id(i)] = 0.0;
    f[6+id(i)] = 0.0;
  }
  //CALCULATE ACCELERATIONS
  for(i = 1; i <= num_points; i++){
    f[1+id(i)] = y[2+id(i)];
    f[3+id(i)] = y[4+id(i)];
    f[5+id(i)] = y[6+id(i)];
    
    for(j = 1; j <= num_points; j++){
      if(j>i){
	denom = -m[j]/pow(pow(y[1+id(i)]-y[1+id(j)],2)+pow(y[3+id(i)]-y[3+id(j)],2)+pow(y[5+id(i)]-y[5+id(j)],2)+pow(soften,2),(3/2));
	xij = (y[1+id(i)]-y[1+id(j)])*denom;
	xji = -xij;
	yij = (y[3+id(i)]-y[3+id(j)])*denom;
	yji = -yij;
	zij = (y[5+id(i)]-y[5+id(j)])*denom;
	zji = -zij;
		
	f[2+id(i)] += xij;
	f[4+id(i)] += yij;
	f[6+id(i)] += zij;

	f[2+id(j)] += xji;
	f[4+id(j)] += yji;
	f[6+id(j)] += zji;
      }
    }
  }
}

int id(int x){
  return 6*(x-1);
}

int count_lines(FILE *fp){
  char ch;
  int lines = 0;
  if (fp == NULL){
    printf("NO FILE!");
  }
  while(1){
    ch = fgetc(fp);
    if(ch == EOF){
      break;
    }else{
      lines++;
    }
  }
  rewind(fp);
  return lines;
}

void main(int argc, char ** argv){
  // input param convention
  // argv[1] = estimator
  // argv[2] = softening parameter
  // argv[3] = step size / timestep
  // argv[4] = number of steps
  // argv[5] = output frequency
  // argv[6] = input file
  // argv[7] = output file

  int i,k;
  char* estimator = argv[1];
  soften = atoi(argv[2]);
  double h = atof(argv[3]);
  int nstep = atoi(argv[4]);
  int out_freq = atoi(argv[5]);
  char* input = argv[6];
  char* output = argv[7];
  int n = 6; //number of equations

  //OPEN OUTPUT FILE
   FILE *ofp = fopen(output, "w");
  //OPEN INPUT FILE
  FILE *ifp = fopen(input, "r");
  //COUNT POINTS
  num_points = 2;    //count_lines(ifp);
  
  m = dvector(1,num_points);
  double *y = dvector(1,n*num_points);
  double *f = dvector(1,n*num_points);
  double *yout = dvector(1,n*num_points);
  double t;

  //LOAD VALUES
  double mi,xi,yi,zi,vxi,vyi,vzi;
  for (i = 1; i <= num_points; i++){
    fscanf(ifp,"%lf\t%lf\t%lf\t%lf\t%lf\t%lf\t%lf",&mi,&xi,&yi,&zi,&vxi,&vyi,&vzi);
    m[i]= mi;
    y[1+id(i)] = xi;
    y[2+id(i)] = vxi;
    y[3+id(i)] = yi;
    y[4+id(i)] = vyi;
    y[5+id(i)] = zi;
    y[6+id(i)] = vzi;
  }
  
  double cum_m, v_rad;
  //ESTIMATE AND SAVE
  for (k=0;k<nstep;k++) {

    func(t,y,f);
    if (!strcmp(estimator,"rk4")) {
      rk4(y, f, n*num_points, t, h, yout,func);
    } else if (!strcmp(estimator,"lf")) {
      lf(y, f, n*num_points, t, h, yout,func);
    }
    //SET OUTPUTS
    for (i = 1; i <= (n*num_points); i++){
      y[i] = yout[i];
    }
    for (i = 1; i <= num_points; i++){
      fprintf(ofp,"%f\t%f\t%f\t%f\t%f\t%f\t%f\n",t,y[1+id(i)],y[3+id(i)],y[5+id(i)],y[2+id(i)],y[4+id(i)],y[6+id(i)]);
    }
    fprintf(ofp,"\n");
    t += h;
  }

  //CLEANUP
  fclose(ifp);
  fclose(ofp);
  free_dvector(m,1,num_points);
  free_dvector(y,1,n*num_points);
  free_dvector(f,1,n*num_points);
  free_dvector(yout,1,n*num_points);

}
