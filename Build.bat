cd ./src
javac -encoding UTF-8 *.java
jar cfe ../JobMaster.jar PL2RPG *.class
del *.class
PAUSE