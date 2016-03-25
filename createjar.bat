javac ./SocketAWT/*.java
jar cvfm Demo.jar manifest.mf SocketAWT\*.class SocketAWT\*.java 
java -jar Demo.jar