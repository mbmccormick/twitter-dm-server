all: POPServer SMTPServer Server

POPServer:
	javac -classpath twitter4j-core-2.2.5.jar POPServerConnection.java POPServer.java

SMTPServer:
	javac -classpath twitter4j-core-2.2.5.jar SMTPServerConnection.java SMTPServer.java

Server:
	javac -classpath . Server.java

clean:
	rm -f *.class

