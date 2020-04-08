import java.io.PrintWriter;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import javax.servlet.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.*;

@WebServlet( urlPatterns={"/Crazy8s"} )
public class Crazy8s extends HttpServlet{
    public boolean newSelect = false;
    synchronized public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
    {
	response.setHeader("Cache-Control", "no-cache");
	PrintWriter servletOut = response.getWriter();
	HttpSession session = request.getSession();
       	String id = session.getId();
	String signIn = (String)session.getAttribute("signIn");
       	if(session.isNew() || (signIn == null)){
	    response.setContentType("text/html; charset=\"UTF-8\"");
	    printSignInForm(servletOut, response.encodeURL("Crazy8s"));
       	}
       	else{
	    response.setContentType("text/html; charset=\"UTF-8\"");
	    printGameSelection(servletOut, signIn, response.encodeURL("Crazy8s"), request, id);
	    session.setAttribute("signIn", signIn);
	}
    }

    synchronized public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException{
	response.setHeader("Cache-Control", "no-cache");
	PrintWriter servletOut = response.getWriter();
	String signIn = request.getParameter("signIn");
	HttpSession session = request.getSession();
	if(signIn != null){
	    newSelect = true;
	    response.setContentType("text/html; charset=\"UTF-8\"");
	    printGameSelection(servletOut, signIn, response.encodeURL("Crazy8s"),null, request.getSession().getId());
	    session.setAttribute("signIn", signIn);
	}
	else
	    printSignInForm(servletOut, response.encodeURL("Crazy8s"));

    }

    private void printSignInForm(PrintWriter servletOut, String action){
	servletOut.println(
	    "<!DOCTYPE html \n" +
	    "    PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \n" +
	    "    \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\"> \n" +
	    "<html xmlns='http://www.w3.org/1999/xhtml'> \n" +
	    "  <head> \n" +
	    "    <title> \n" +
	    "      LOGIN \n" +
	    "    </title> \n" +
	    "  </head> \n" +
	    "  <body> \n" +
	    "    <form method='post' action='" + action + "'> \n" +
	    "       <div> \n" +
	    "          <label> \n" +
	    "            Please sign in: \n" +
	    "            <input type='text' name='signIn' /> \n" +
	    "          </label> \n" +
	    "          <br /> \n" +
	    "          <input type='submit' name='doit' value='Sign In' /> \n " +
	    "       </div> \n" +
	    "    </form> \n" +
	    "  </body> \n" +
	    "</html> ");

	servletOut.close();
    }
    private void printGameSelection(PrintWriter servletOut, String name, String action, HttpServletRequest request, String id){
	String title = null;
	int gamePlayed = 0;
	int cardsPlayedInWin = 0;
	String newWinner = "";
	name = WebTechUtil.escapeXML(name);
	if(!newSelect){
	
	    if(request.getParameter("game") != null && request.getParameter("cardsPlayed") != null && request.getParameter("win") != null){
		gamePlayed = Integer.parseInt(request.getParameter("game"));
		cardsPlayedInWin = Integer.parseInt(request.getParameter("cardsPlayed"));
		title = request.getParameter("win");
	     }
	    if(gamePlayed > 0){
		if(Data.getUserData(gamePlayed-1) == null)
		    Data.setUserData(gamePlayed-1, new ArrayList());
		if(Data.getWinners(gamePlayed-1) == null)
		    Data.setWinners(gamePlayed-1, new ArrayList());
	    }
	    if(gamePlayed > 0 && !Data.getUserData(gamePlayed-1).contains(name)){
		Data.getUserData(gamePlayed-1).add(name);
		Data.setPlayer(gamePlayed-1, Data.getPlayers(gamePlayed-1) + 1);
	    }
	    
	}
	if(title != null){
	    if(title.equals("Y")){
		title = "Congrats on the win, " + name;
		if(!Data.getWinners(gamePlayed-1).contains(name))
		    Data.getWinners(gamePlayed-1).add(name);
		if(cardsPlayedInWin < Data.getCardsPlayed(gamePlayed-1) || Data.getCardsPlayed(gamePlayed-1) == 0){
		    Data.setNewWinners(gamePlayed-1, " style='background-color:yellow'");
		    Data.setCardsPlayed(gamePlayed-1, cardsPlayedInWin);
		    Data.setQuickWinner(gamePlayed-1, name);
		}
		else{
		    Data.setNewWinners(gamePlayed-1, "");
		}
		Data.setPercWin(gamePlayed-1, ((double)Data.getWinners(gamePlayed-1).size() / (double)Data.getPlayers(gamePlayed-1)) * 100);

	    }
	    else if(title.equals("N")){
		title = "Sorry about the loss, " + name;
		Data.setNewWinners(gamePlayed-1, "");
		if(!Data.getWinners(gamePlayed-1).contains(name))
		    Data.setPercWin(gamePlayed-1, ((double)Data.getWinners(gamePlayed-1).size() / (double)Data.getPlayers(gamePlayed-1)) * 100);
	    }
	}
	else
	    title = "Welcome, " + name + "!";
	if(newSelect)
	    newSelect = false;

	servletOut.println(
	    "<!DOCTYPE html \n" +
	    "    PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \n" +
	    "    \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\"> \n" +
	    "<html xmlns='http://www.w3.org/1999/xhtml'> \n" +
	    "  <head> \n" +
	    "    <title> \n" +
	    "      Select a Game \n" +
	    "    </title> \n" +
	    "    <style type='text/css'> \n" +
            "      col { width:20%; } \n" +
	    "      td,th { text-align: center; } \n" +
	    "      table, td, th { border: 1px solid gray } \n" +
	    "    </style> \n" +
	    "  </head> \n" +
	    "  <body> \n" +
	    "    <h1> \n" +
	           title + "\n" +
	    "    </h1> \n" +
	    "  <table> \n" +
	    "    <caption style='border:dashed; border-color:blue; padding:5px; margin:5px;'> \n" +
	    "Choose from any of the hands below to play Crazy Eights and possibly get your name on the board! \n" +
	    "    </caption> \n" +
	    "    <colgroup> \n" +
	    "      <col> \n" +
	    "      <col> \n" +
	    "      <col> \n" +
	    "      <col> \n" +
	    "      <col> \n" +
	    "    </colgroup> \n" +
	    "    <tbody> \n" +
	    "      <tr> \n" +
	    "        <th>Hand</th> \n" +
	    "        <th>Players</th> \n" +
	    "        <th>% Players<br>winning</th> \n" +
	    "        <th>Fewest cards<br>played in win</th> \n" +
	    "        <th>Player playing<br>fewest cards</th> \n" +
	    "      </tr> \n" +
	    "      <tr> \n" +
	    "        <td><a href='game/Crazy8.html;jsessionid=" + id + "?seed=0x6904acd2&game=1'>1</a></td> \n"+
	    "        <td>" + Data.getPlayers(0) + "</td> \n" +
	    "        <td>" + Data.getPercWin(0) + "</td> \n" +
	    "        <td>" + Data.getCardsPlayed(0) + "</td> \n" +
	    "        <td" + Data.getNewWinners(0) + ">" + Data.getQuickWinner(0) + "</td> \n" +
	    "      </tr> \n" +
	    "      <tr> \n" +
	    "        <td><a href='game/Crazy8.html;jsessionid=" + id + "?seed=0xe03d8ca4&game=2'>2</a></td> \n"+
	    "        <td>" + Data.getPlayers(1) + "</td> \n" +
	    "        <td>" + Data.getPercWin(1) + "</td> \n" +
	    "        <td>" + Data.getCardsPlayed(1) + "</td> \n" +
	    "        <td" + Data.getNewWinners(1) + ">" + Data.getQuickWinner(1) + "</td> \n" +
	    "      </tr> \n" +
	    "      <tr> \n" +
	    "        <td><a href='game/Crazy8.html;jsessionid=" + id + "?seed=0x500aee51&game=3'>3</a></td> \n"+
	    "        <td>" + Data.getPlayers(2) + "</td> \n" +
	    "        <td>" + Data.getPercWin(2) + "</td> \n" +
	    "        <td>" + Data.getCardsPlayed(2) + "</td> \n" +
	    "        <td" + Data.getNewWinners(2) + ">" + Data.getQuickWinner(2) + "</td> \n" +
	    "      </tr> \n" +
	    "      <tr> \n" +
	    "        <td><a href='game/Crazy8.html;jsessionid=" + id + "?seed=0x8752f900&game=4'>4</a></td> \n"+
	    "        <td>" + Data.getPlayers(3) + "</td> \n" +
	    "        <td>" + Data.getPercWin(3) + "</td> \n" +
	    "        <td>" + Data.getCardsPlayed(3) + "</td> \n" +
	    "        <td" + Data.getNewWinners(3) + ">" + Data.getQuickWinner(3) + "</td> \n" +
	    "      </tr> \n" +
	    "      <tr> \n" +
	    "        <td><a href='game/Crazy8.html;jsessionid=" + id + "?seed=0xbb905669&game=5'>5</a></td> \n"+
	    "        <td>" + Data.getPlayers(4) + "</td> \n" +
	    "        <td>" + Data.getPercWin(4) + "</td> \n" +
	    "        <td>" + Data.getCardsPlayed(4) + "</td> \n" +
	    "        <td" + Data.getNewWinners(4) + ">" + Data.getQuickWinner(4) + "</td> \n" +
	    "      </tr> \n" +
	    "    </tbody> \n" +
	    "   </table> \n" +
	    "  </body> \n" +
	    "</html> ");
	servletOut.close();
	Data.resetNewWinners();
    }

    public static class Data<T> {
	private static int[] players = {0, 0, 0, 0, 0};
	private static double[] percWin = {0, 0, 0, 0, 0};
	private static int[] cardsPlayed = {0, 0, 0, 0, 0};
	private static String[] quickWinner = {"-", "-", "-", "-", "-"};
	private static ArrayList[] userData = new ArrayList[5];
	private static ArrayList[] winners = new ArrayList[5];
	private static String[] newWinners = {"","","","",""};

	synchronized public static int getPlayers(int i){
	    return players[i];
	}
	synchronized public static double getPercWin(int i){
	    return percWin[i];
	}
	synchronized public static int getCardsPlayed(int i){
	    return cardsPlayed[i];
	}
	synchronized public static String getQuickWinner(int i){
	    return quickWinner[i];
	}
	synchronized public static ArrayList getUserData(int i){
	    return userData[i];
	}
	synchronized public static ArrayList getWinners(int i){
	    return winners[i];
	}
	synchronized public static String getNewWinners(int i){
	    return newWinners[i];
	}
	
	synchronized public static void setPlayer(int i, int player){
	    players[i] = player;
	}
	synchronized public static void setPercWin(int i, double perc){
	    percWin[i] = perc;
	}
	synchronized public static void setCardsPlayed(int i, int cards){
	    cardsPlayed[i] = cards;
	}
	synchronized public static void setQuickWinner(int i, String quickW){
	    quickWinner[i] = quickW;
	}
	synchronized public static void setUserData(int i, ArrayList data){
	    userData[i] = data;
	}
	synchronized public static void setWinners(int i, ArrayList winner){
	    winners[i] = winner;
	}
	synchronized public static void setNewWinners(int i, String newWinner){
	    newWinners[i] = newWinner;
	}

	synchronized public static void resetNewWinners(){
	    for(int i = 0; i < newWinners.length; i++){
		setNewWinners(i, "");
	    }
	}
	
    }
    /**
     * Utilities to support examples in "Web Technologies" textbook
     */
    public static class WebTechUtil {

	/**
	 * Ampersand pattern used by escapeXML
	 */
	static private Pattern pAmp = Pattern.compile("&");
	/**
	 * Less-than pattern used by escapeXML
	 */
	static private Pattern pLT =  Pattern.compile("<");
	/**
	 * Greater-than pattern used by escapeXML
	 */
	static private Pattern pGT =  Pattern.compile(">");
	/**
	 * Double-quote pattern used by escapeQuotes
	 */
	static private Pattern pDQ = Pattern.compile("\"");
	/**
	 * Single-quote pattern used by escapeQuotes
	 */
	static private Pattern pSQ = Pattern.compile("'");


	/**
	 * Return input string with ampersands (&), 
	 * less-than signs (<), and greater-than signs (>)
	 * replaced with character entity references.
	 */
	static public String escapeXML(String inString)
	{
	    Matcher matcher = pAmp.matcher(inString);
	    String modified = matcher.replaceAll("&amp;");
	    matcher = pLT.matcher(modified);
	    modified = matcher.replaceAll("&lt;");
	    matcher = pGT.matcher(modified);
	    modified = matcher.replaceAll("&gt;");
	    return modified;
	}

	/**
	 * Return input string with all quotes replaced
	 * with references.  Use character reference for single quotes
	 * because IE6 does not support &apos; entity reference.
	 */
	static public String escapeQuotes(String inString)
	{
	    Matcher matcher = pDQ.matcher(inString);
	    String modified = matcher.replaceAll("&quot;");
	    matcher = pSQ.matcher(modified);
	    modified = matcher.replaceAll("&#39;");
	    return modified;
	}
    }


}
