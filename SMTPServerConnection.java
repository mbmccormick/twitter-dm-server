import java.util.*; 
import java.io.*; 
import java.net.*;
import java.text.*;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.DirectMessage;
import twitter4j.Paging;
import twitter4j.conf.ConfigurationBuilder;

public class SMTPServerConnection implements Runnable
{
    private SMTPServer _host = null; 
    private Socket _clientSocket = null;
    
	private int _state = 0; // 0 = authorization, 1 = transaction, 2 = update
    private String[] _message;
	
	private String _username = null;
    private String _password = null;
    
    private Twitter _twitter;
	
    SMTPServerConnection(SMTPServer host, Socket clientSocket)
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
            
            // load output stream
            PrintWriter out = new PrintWriter(_clientSocket.getOutputStream(), true);

            // send welcome message
            out.println("220 twitter-dm-server 1.0 ready");

            // read each line
            String line;
            while (_state < 2 && (line = br.readLine()) != null)
            {
                System.out.println(line);
                
                if (_state == 0)
                {
					if (line.startsWith("HELO") || line.startsWith("EHLO"))
					{
						out.println("250-OK");
						out.println("250 AUTH PLAIN");
					}
					else if (line.startsWith("AUTH PLAIN"))
					{
						String authentication = Base64.decodeString(line.substring(11, line.length()));
						authentication = authentication.substring(1, authentication.length());
						
						_username = authentication.substring(0, authentication.indexOf("\0"));
						_password = authentication.substring(authentication.indexOf("\0") + 1, authentication.length());
						
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

                            out.println("235 welcome " + user.getScreenName());
                            
                            // enter transaction state
                            _state = 1;
                        }
                        catch (TwitterException te)
                        {
                            out.println("535 invalid credentials");
                        }
					}
					else
					{
						out.println("530 authentication required");
					}
				}
				else if (_state == 1)
				{
					if (line.startsWith("MAIL"))
					{
						_message = new String[2];
						
						out.println("250 OK");
					}
					else if (line.startsWith("RCPT"))
					{
						_message[0] = line.substring(9, line.indexOf("@"));
						
						out.println("250 OK");
					}
					else if (line.startsWith("DATA"))
					{
						out.println("354 begin");
						
						_message[1] = "";
						boolean begin = false;
						
						while ((line = br.readLine()) != null && line.equals(".") == false)
						{
							if (begin == true)
							{
								_message[1] = _message[1] + line;
							}
							else
							{
								if (line.equals("") == true)
									begin = true;
							}
						}
						
						out.println("250 OK");
					}
					else if (line.startsWith("QUIT"))
					{
						out.println("221 goodbye");
						
						// enter update state
						_state = 2;
					}
				}
            }
			
			if (_state == 2)
            {
                try
                {
                    _twitter.sendDirectMessage(_message[0], _message[1]);
                }
                catch (TwitterException te)
                {
                    out.println("550 " + te.getMessage());
                }
            }
            
            out.close();
            br.close();
            
            _clientSocket.close();
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }
}
