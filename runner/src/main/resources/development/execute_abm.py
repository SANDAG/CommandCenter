# encoding: utf-8
from __future__ import print_function
import os, fileinput, subprocess, sys

escaped_working_dir = os.getcwd().replace('\\', '\\\\')

for line in fileinput.input('gisdk/sandag_abm_master.rsc', inplace=True):
    print(line.replace('${workpath}', escaped_working_dir), end='')

#Compile the GISDK
subprocess.check_call(['C:\PROGRA~1\TRANSCA~1\rscc.exe', '–u compiled_gisdk_db', 'rsc1', 'rsc2'])
 
#Run the GISDK
sys.exit(subprocess.call(['C:\PROGRA~1\TRANSCA~1\tcw.exe', '–a compiled_gisdk_db' '–ai "Run SANDAG ABM"']))
