# twitter-dm-server

POP and SMTP email servers for Twitter Direct Messages.


## Requirements

To run these applications on your server, you will need to have the Java Development Kit (JDK) installed.


## Installation

This application is designed to be run on a LAMP development platform. The scripts and configuration files cannot be run through a desktop environment.

To build this application, just run `make` to compile the necessary assemblies.


## Usage

This application is a POP and SMTP server for sending and receiving Direct Messages on Twitter. The POP server runs on port 110 and the SMTP server runs on port 25. The necessary configuration settings are shown below.

```
Incoming POP Server

Email Address: <yourhandle>@twitter.com
Hostname: <yourserveraddress>
Port: 110
Security: none
Authentication Method: password
Username: <youraccesstoken>
Password: <youraccesssecret>
```

```
Outgoing SMTP Server

Email Address: <yourhandle>@twitter.com
Hostname: <yourserveraddress>
Port: 25
Security: none
Authentication Method: password
Username: <youraccesstoken>
Password: <youraccesssecret>
```

You will need to obtain your Access Token (which is used as your username) and Access Secret (which is used as your password) from Twitter in order to properly authenticate. If you are building this on your own machine, you will need to create a new application on Twitter and modify this code with your Consumer Key and Consumer Secret that Twitter provides. This application requires read/write/directmessage permissions on Twitter.

To start this application, just run `java -cp ".:twitter4j-core-2.2.5.jar" Server`. At this point, the server will be running and you can connect using your email client.


## Disclaimer

Use this application at your own risk. While this application has been tested thoroughly, on the above requirements, your mileage may vary. I take no responsibility for any harmful actions this application might cause.


## License

This software, and its dependencies, are distributed free of charge and licensed under the GNU General Public License v3. For more information about this license and the terms of use of this software, please review the LICENSE.txt file.