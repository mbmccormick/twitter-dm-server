import java.util.*; 
import java.io.*; 
import java.net.*;

public class POPServer implements Runnable
{
    private int _port = 11000;
    ArrayList<POPServerConnection> _connections = null;
    
    POPServer()
    {
        _connections = new ArrayList<POPServerConnection>();
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
                POPServerConnection cn = new POPServerConnection(this, clientSocket);                
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

