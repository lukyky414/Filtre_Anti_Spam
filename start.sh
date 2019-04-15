cd src;
rm */*.class 2> /dev/null;
clear;clear;
javac */*;
if [ $# != 2 ]
then
	java filtre/FiltreAntiSpam;
else
	java filtre/FiltreAntiSpam ../$1 ../$2;
fi
rm */*.class;