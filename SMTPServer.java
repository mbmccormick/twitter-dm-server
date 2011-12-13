import java.util.*; 
import java.io.*; 
import java.net.*;

public class SMTPServer implements Runnable
{
    private int _port = 25;
    ArrayList<SMTPServerConnection> _connections = null;
    
    SMTPServer()
    {
        _connections = new ArrayList<SMTPServerConnection>();
    }
    
    public void run()
    {
        try
        {
            // start server socket
            ServerSocket server = new ServerSocket(_port);

            while (true)
            {
                // wait for an incoming connection
                Socket clientSocket = server.accept();
                
                // create a new connection for this socket
                SMTPServerConnection cn = new SMTPServerConnection(this, clientSocket);                
                _connections.add(cn);
                                
                // launch a new thread for this connection
                Thread th = new Thread(cn);
                th.start();
            }
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }
}

