import java.util.*; 
import java.io.*; 
import java.net.*;

public class SMTPServerConnection implements Runnable
{
    private SMTPServer _host = null; 
    Socket _clientSocket = null;
    
    POPServerConnection(SMTPServer host, Socket clientSocket)
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

