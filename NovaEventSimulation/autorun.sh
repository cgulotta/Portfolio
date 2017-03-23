#!/bin/bash
echo "Compiling..."
set -e
make -f Makefile

echo "Running problem set 6..."

{ "./P6" rk4 0 0.05 1000 0 inp_05.dat rk4_05.dat; } &
{ "./P6" rk4 0 0.003 1500 0 inp_09.dat rk4_09.dat; } &
{ "./P6" lf 0 0.05 1000 0 inp_05.dat lf_05.dat; } &
{ "./P6" lf 0 0.003 1500 0 inp_09.dat lf_09.dat; } &
wait

python -W ignore plotter.py

echo "Complete!"
