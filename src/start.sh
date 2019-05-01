
rm filtre/*.class 2> /dev/null;
clear;clear;

javac filtre/*;

java filtre/FiltreAntiSpam $@;

rm filtre/*.class 2> /dev/null;
