cd src;
rm */*.class 2> /dev/null;
clear;clear;
javac */*;
java filtre/FiltreAntiSpam ../$1 ../$2;
rm */*.class;