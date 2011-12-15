all: POPServer SMTPServer Server

POPServer:
	javac -cp ".:twitter4j-core-2.2.5.jar" POPServerConnection.java POPServer.java

SMTPServer:
	javac -cp ".:twitter4j-core-2.2.5.jar" Base64.java SMTPServerConnection.java SMTPServer.java

Server:
	javac -cp ".:twitter4j-core-2.2.5.jar" Server.java

clean:
	rm -f *.class

