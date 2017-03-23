 _____ _   _  _____ ___________ _   _ _____ _____ _____ _____ _   _  _____ 
|_   _| \ | |/  ___|_   _| ___ \ | | /  __ \_   _|_   _|  _  | \ | |/  ___|
  | | |  \| |\ `--.  | | | |_/ / | | | /  \/ | |   | | | | | |  \| |\ `--. 
  | | | . ` | `--. \ | | |    /| | | | |     | |   | | | | | | . ` | `--. \
 _| |_| |\  |/\__/ / | | | |\ \| |_| | \__/\ | |  _| |_\ \_/ / |\  |/\__/ /
 \___/\_| \_/\____/  \_/ \_| \_|\___/ \____/ \_/  \___/ \___/\_| \_/\____/ 
                                                                           
                                                                          

1.) run "sh ./autorun.sh"

this will launch the makefile to compile the binary, launch the binary which generates all the integration results, and launch the python script plotter.py which generates plots of the particle positions, r vs Vr, and Energy vs time for the runge-kutta and leapfrog integrations.

NOTE: nr.h has been changed to accomodate double precision rk4. 'results' directory and all subdirectories (as submitted) required.

cleanup:
To rerun, run the following commands:
"make clean"
"sh ./autorun.sh"

