import java.util.*; 
import java.io.*; 
import java.net.*;

public class POPServerConnection implements Runnable
{
    private POPServer _host = null; 
    private Socket _clientSocket = null;
    
    private int _state = 0; // 0 = authorization, 1 = transaction, 2 = update
    
    private String _username = null;
    private String _password = null;
    
    POPServerConnection(POPServer host, Socket clientSocket)
    {
        _host = host;
        _clientSocket = clientSocket;
    }

    public void run()
    {
        try
        {
            // load input stream
            InputStream is = _clientSocket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            
            // loud output stream
            PrintWriter out = new PrintWriter(_clientSocket.getOutputStream(), true);

            // send welcome message
            out.println("+OK twitter-dm-server 1.0 ready");

            // read each line
            String line;
            while (state < 2 && (line = br.readLine()) != null)
            {
                if (_state == 0)
                {
                    if (line.startsWith("USER"))
                    {
                        _username = line.substring(5, line.length() - 1);
                        out.println("+OK");
                    }
                    else if (line.startsWith("PASS"))
                    {
                        _password = line.substring(5, line.length() - 1);
                        out.println("+OK");
                        
                        // enter transaction state
                        _state = 1;
                    }
                }
                else if (_state == 1)
                {
                    if (line.startsWith("STAT"))
                    {
                        
                    }
                    else if (line.startsWith("LIST"))
                    {
                        
                    }
                    else if (line.startsWith("RETR"))
                    {
                        
                    }
                    else if (line.startsWith("DELE"))
                    {
                        
                    }
                    else if (line.startsWith("NOOP"))
                    {
                        out.println("+OK");
                    }
                    else if (line.startsWith("QUIT"))
                    {
                        // enter update state
                        _state = 2;
                    }
                }
            }
        }
        catch (Exception ex)
        {
            // TODO: handle this exception
        }
    }
}

