cd src;
clear;clear;
javac */*;
java filtre/FiltreAntiSpam ../$1;
#rm */*.class;