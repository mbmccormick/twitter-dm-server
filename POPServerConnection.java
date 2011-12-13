import java.util.*; 
import java.io.*; 
import java.net.*;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.DirectMessage;
import twitter4j.conf.ConfigurationBuilder;

public class POPServerConnection implements Runnable
{
    private POPServer _host = null; 
    private Socket _clientSocket = null;
    
    private int _state = 0; // 0 = authorization, 1 = transaction, 2 = update
    
    private String _username = null;
    private String _password = null;
    
    private Twitter _twitter;
    
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
            while (_state < 2 && (line = br.readLine()) != null)
            {
                if (_state == 0)
                {
                    if (line.startsWith("USER"))
                    {
                        _username = line.substring(5, line.length());
                        out.println("+OK hello, please authenticate");
                    }
                    else if (line.startsWith("PASS"))
                    {
                        if (_username == null)
                        {
                            out.println("-ERR please send USER first");
                            continue;
                        }
                        
                        _password = line.substring(5, line.length());

                        try
                        {
                            ConfigurationBuilder cb = new ConfigurationBuilder();
                            cb.setDebugEnabled(true)
                              .setOAuthConsumerKey("5gRxJYfsiP70XJjySPZrmA")
                              .setOAuthConsumerSecret("8NNYTQc3g21Aiwec6r61HQDPW442Q9jBp4RissEEG0")
                              .setOAuthAccessToken(_username)
                              .setOAuthAccessTokenSecret(_password);
                            TwitterFactory tf = new TwitterFactory(cb.build());
                            _twitter = tf.getInstance();
                            
                            User user = _twitter.verifyCredentials();

                            out.println("+OK welcome " + user.getScreenName());
                            
                            // enter transaction state
                            _state = 1;
                        }
                        catch (TwitterException te)
                        {
                            out.println("-ERR invalid credentials");
                        }
                    }
                }
                else if (_state == 1)
                {
                    if (line.startsWith("STAT"))
                    {
                        List<DirectMessage> messages = _twitter.getDirectMessages();
                        int n = messages.size();
                        int m = 0;
                        for (DirectMessage d : messages)
                        {
                            m += d.getText().length();
                        }
                        
                        out.println("+OK " + n + " " + m);
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
                        out.println("+OK goodbye");
                        
                        // enter update state
                        _state = 2;
                    }
                }
            }
            
            out.close();
            br.close();
            
            _clientSocket.close();
        }
        catch (Exception ex)
        {
            // TODO: handle this exception
        }
    }
}

