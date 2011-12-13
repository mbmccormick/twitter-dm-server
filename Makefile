all: POPServer SMTPServer Server

POPServer:
	javac POPServerConnection.java POPServer.java

SMTPServer:
	javac SMTPServerConnection.java SMTPServer.java

Server:
	javac Server.java

clean:
	rm -f *.class

