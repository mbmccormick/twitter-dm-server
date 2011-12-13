all: POPServer SMTPServer Server

POPServer:
	javac -classpath ".;twitter4j-core.jar" POPServerConnection.java POPServer.java

SMTPServer:
	javac -classpath ".;twitter4j-core.jar" SMTPServerConnection.java SMTPServer.java

Server:
	javac -classpath ".;twitter4j-core.jar" Server.java

clean:
	rm -f *.class

