import java.util.*; 
import java.io.*; 
import java.net.*;

public class POPServerConnection implements Runnable
{
    private POPServer _host = null; 
    Socket _clientSocket = null;
    
    POPServerConnection(POPServer host, Socket clientSocket)
    {
        _host = host;
        _clientSocket = clientSocket;
    }

    public void run()
    {
        try
        {
            // load stream data
            InputStream is = _clientSocket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line;
            
            // read each line
            while ((line = br.readLine()) != null)
            {
                System.out.println(line);  
            }
        }
        catch (Exception ex)
        {
            // TODO: handle this exception
        }
    }
}

