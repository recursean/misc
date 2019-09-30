import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.HttpURLConnection;
import java.net.FileNameMap;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.text.SimpleDateFormat;
import java.text.DateFormatSymbols;

/** 
 * A single-thread web server that returns files located in
 * the current user directory with the appropriate MIME types.
 */
public class SeanStudentWebServer {
 
    public static void main (String args[]) {
        try {
            // Create server socket bound to port 8080
            ServerSocket mySocket = new ServerSocket(8080);

            // Repeat until someone kills us
            while (true) {

                // Listen for a connection
                Socket yourSocket = mySocket.accept();

                // If we reach this line, someone connected to our port!
                
                // Create the Date header value string
                SimpleDateFormat formatter = 
                    new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss zzz",
                                         new DateFormatSymbols(Locale.US));
                formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
                String dateTime = formatter.format(new Date());
                
                // Read the request, write the response
                // YOUR CODE GOES HERE
	       
	        Scanner input = new Scanner(yourSocket.getInputStream()); //echo request
		String test = input.nextLine();
		String URI = "";
		String URLlh = "http://localhost:8080/";
		StringTokenizer st = new StringTokenizer(test);
		boolean GET = false;
		while(st.hasMoreTokens()){  //strip URI
		    String nt = st.nextToken();
		    if(nt.startsWith("GET"))
			GET = true;
		    if(nt.startsWith("/") && GET){
			URI = nt.substring(1);
			URLlh += URI;
			    break;
		    }
		}

	
		
		String request = "";
		while(test.length() != 0){
		    request += "\n" +  test;
		    test = input.nextLine();
		}
	       	System.out.println(request);
		File file = new File(URI);
		URL u = new URL("http://localhost:8080/" + URI);
		PrintWriter pw = new PrintWriter(yourSocket.getOutputStream());
		if(file.exists()){
		    URLConnection lh = (URLConnection) u.openConnection();
		    FileNameMap f = lh.getFileNameMap();
		    pw.println("HTTP/1.1 200 OK");
		    pw.println("Content-Length: " + file.length());
		    pw.println("Content-Type: " + f.getContentTypeFor(URI));
		    System.out.println(f.getContentTypeFor(URI));
		    pw.println("Date: " + dateTime);
		    pw.flush();
		    System.out.println();
		    FileInputStream is = new FileInputStream(file);
		    while(is.available() > 0)
			yourSocket.getOutputStream().write((char)is.read());
		    yourSocket.getOutputStream().flush();
		}
		else {
		    pw.println("HTTP/1.1 404 Not Found");
		    pw.println("Content-Length: 0");
		    pw.println("Content-Type: text/plain");
		    pw.println("Date: " + dateTime);
		    pw.flush();
		}
		
		
		
                // Done with this connection. Close the socket.
                yourSocket.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }
}
