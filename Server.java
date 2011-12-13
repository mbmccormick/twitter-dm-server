import java.util.*; 
import java.io.*; 
import java.net.*;

public class Server
{
    public static void main(String[] args)
    {
        // create new instance of POP server
        POPServer pop = new POPServer();
        
        Thread t1 = new Thread(pop);        
        t1.start();
        
        // create new instance of SMTP server
        SMTPServer smtp = new SMTPServer();
        
        Thread t2 = new Thread(smtp);        
        t2.start();
    }
}

