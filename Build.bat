cd ./src
javac -encoding UTF-8 *.java
jar cfe ../RPG.jar PL2RPG *.class
del *.class
PAUSE