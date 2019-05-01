
rm */*.class 2> /dev/null;
clear;clear;

javac */*;

java filtre/FiltreAntiSpam $@;

rm */*.class;
