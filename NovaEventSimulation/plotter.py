import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d import Axes3D
import numpy as np
import os
import math

def parse(lines):
    ts = []
    p1_x = []
    p1_vx = []
    p1_y = []
    p1_vy = []
    p1_z = []
    p1_vz = []
    p2_x = []
    p2_vx = []
    p2_y = []
    p2_vy = []
    p2_z = []
    p2_vz = []
    for i in range(0,len(lines),3):
        t, x1, y1, z1, vx1, vy1, vz1 = lines[i].split()
        t, x2, y2, z2, vx2, vy2, vz2 = lines[i+1].split()
        #print(t,x1,x2,y1,y2,z1,z2,vx1,vx2,vy1,vy2,vz1,vz2)
        ts.append(float(t))
        p1_x.append(float(x1))
        p2_x.append(float(x2))
        p1_y.append(float(y1))
        p2_y.append(float(y2))
        p1_z.append(float(z1))
        p2_z.append(float(z2))
        p1_vx.append(float(vx1))
        p2_vx.append(float(vx2))
        p1_vy.append(float(vy1))
        p2_vy.append(float(vy2))
        p1_vz.append(float(vz1))
        p2_vz.append(float(vz2))
    return ts,p1_x,p2_x,p1_y,p2_y,p1_z,p2_z,p1_vx,p2_vx,p1_vy,p2_vy,p1_vz,p2_vz

def energy(r1, r2, v1, v2):
    r = math.sqrt(math.pow(r1[0]-r2[0],2)+math.pow(r1[1]-r2[1],2)+math.pow(r1[2]-r2[2],2))
    n_v1 = math.pow(v1[0],2) + math.pow(v1[1],2) + math.pow(v1[1],2)
    n_v2 = math.pow(v2[0],2) + math.pow(v2[1],2) + math.pow(v2[1],2)
    return 0.5*n_v1+0.5*n_v2 - (1/r)

def relatives(r1,r2,v1,v2):
    r = math.sqrt(math.pow(r1[0]-r2[0],2)+math.pow(r1[1]-r2[1],2)+math.pow(r1[2]-r2[2],2))
    vnet = [v2[0]-v1[0],v2[1]-v1[1],v2[2]-v1[2]]
    rnet = [r2[0]-r1[0],r2[1]-r1[1],r2[2]-r1[2]]
    vr = ((vnet[0]*rnet[0])+(vnet[1]*rnet[1])+(vnet[2]*rnet[2]))/r
    return r,vr

#fig = plt.figure()
#ax = Axes3D(fig)

files = ["rk4_05.dat","rk4_09.dat","lf_05.dat","lf_09.dat"]
n = 0;

for file in files:
    plt.figure(n)
    lines = [];
    with open(file,"r") as f:
        lines = f.readlines()
    E = []
    Vr = []
    r = []
    t,p1_x,p2_x,p1_y,p2_y,p1_z,p2_z,p1_vx,p2_vx,p1_vy,p2_vy,p1_vz,p2_vz = parse(lines)
    
    for i in range(len(t)):
        r1 = [p1_x[i],p1_y[i],p1_z[i]]
        r2 = [p2_x[i],p2_y[i],p2_z[i]]
        v1 = [p1_vx[i],p1_vy[i],p1_vz[i]]
        v2 = [p2_vx[i],p2_vy[i],p2_vz[i]]
        E.append(energy(r1,r2,v1,v2))
        a, b = relatives(r1,r2,v1,v2)
        Vr.append(a)
        r.append(b)

    
    plt.subplot(1,3,1)
    plt.plot(p1_x,p1_y, c='r', marker = 'o')
    plt.plot(p2_x,p2_y, c='g', marker = 'o')
    plt.title(file +" Position")
    
    plt.subplot(1,3,2)
    plt.plot(r,Vr)
    plt.title(file +" r vs Vr")
    
    plt.subplot(1,3,3)
    plt.plot(t,E)
    plt.title(file +" Energy")
        
    n+=1

plt.show()

