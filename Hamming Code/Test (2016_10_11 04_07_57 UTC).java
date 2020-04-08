import java.io.PrintWriter;
import java.io.IOException;
public class Test{
    public static void main(String[] args) throws IOException{
	PrintWriter pw = new PrintWriter("test.txt");
	for(int i = 0; i < 256; i++){
	    pw.print((char)i);
	}
	pw.flush();
	pw.close();
    }
}
