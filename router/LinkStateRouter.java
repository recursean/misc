import java.util.ArrayList;
import java.util.Hashtable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Enumeration;

public class LinkStateRouter{
	Graph network = new Graph();
	ArrayList<Interface> interfaces = new ArrayList<Interface>();
	Hashtable forwardingTable = new Hashtable();

	Node localhost = new Node("127.0.0.1");

	public class Graph{
		ArrayList<Node> nodes = new ArrayList<Node>();

		public Graph(){

		}

		public void addNode(Node n){
			nodes.add(n);
		}

		public void addEdge(Node src, Node dest, int cost){
			src.addEdge(new Edge(dest, cost));
			dest.addEdge(new Edge(src, cost));
		}

		public void updateCost(Interface srcInterface, Node src, Node dest, int cost){
			src.getEdges().get(src.getPeers().indexOf(dest)).cost = cost;
			dest.getEdges().get(dest.getPeers().indexOf(src)).cost = cost;

			if(cost != Integer.MAX_VALUE && 
				!src.getEdges().get(src.getPeers().indexOf(dest)).up){

				src.getEdges().get(src.getPeers().indexOf(dest)).setUp(true);
				dest.getEdges().get(dest.getPeers().indexOf(src)).setUp(true);

				if(src.equals(localhost)){
					srcInterface.setLinkUp(true);
				}
			}
		}

		public void handleBrokenLink(Interface srcInterface, Node srcNode, Node destNode){
			srcNode.getEdges().get(srcNode.getPeers().indexOf(destNode)).setUp(false);
			destNode.getEdges().get(destNode.getPeers().indexOf(srcNode)).setUp(false);

			if(srcNode.equals(localhost)){
				srcInterface.setLinkUp(false);
			}
		}

		public ArrayList<Node> getNodes(){
			return nodes;
		}

		@Override
		public String toString(){
			String printGraph = "";
			for(Node n: nodes){
				printGraph += n + " : " + n.getEdges() + "\n";
			}
			return printGraph;
		}
	}

	public class Node{
		String label;
		ArrayList<Node> peers = new ArrayList<Node>();
		ArrayList<Edge> edges = new ArrayList<Edge>();
		int leastCost = -1;
		Node pred = localhost;

		public Node(String label){
			this.label = label;
		}

		public void addEdge(Edge e){
			peers.add(e.dest);
			edges.add(e);
		}

		public ArrayList<Node> getPeers(){
			return peers;
		}

		public ArrayList<Edge> getEdges(){
			return edges;
		}

		@Override
		public String toString(){
			return label;
		}
	}

	public class Edge{
		Node dest;
		int cost;
		boolean up;

		public Edge(Node dest, int cost){
			this.dest = dest;
			this.cost = cost;
			if(cost != Integer.MAX_VALUE){
				up = true;
			}
			else{
				up = false;
			}
		}

		public void setUp(boolean up){
			this.up = up;
		}

		@Override
		public String toString(){
			return dest + ", " + cost;
		}
	}

	public class Interface{
		int id;
		boolean connectedToRouter;
		boolean linkUp;
		Node connectedTo;


		public Interface(int id, int connection, Node connectedTo){
			this.id = id;

			if(connection == 0){
				connectedToRouter = false;
			}

			else if(connection == 1){
				connectedToRouter = true;
			}

			this.connectedTo = connectedTo;

			linkUp = true;
		}

		public void setLinkUp(boolean up){
			linkUp = up;
		}
	}

	public LinkStateRouter(FileInputStream fstream){
		BufferedReader in = new BufferedReader(new InputStreamReader(fstream));
		createInitialGraph(in);
		simulateRouter(in);
	}

	public void createInitialGraph(BufferedReader in){
		String connection = "";
		String sections[];

		network.addNode(localhost);

		try{
			while(!(connection = in.readLine()).equals("0,0,0.0.0.0,0")){
				sections = connection.split(",");

				Node newNode = new Node(sections[2]);
				network.addNode(newNode);

				network.addEdge(localhost, newNode, Integer.parseInt(sections[3]));

				interfaces.add(new Interface(Integer.parseInt(sections[1]), Integer.parseInt(sections[0]), newNode));
			}
			for(Interface i: interfaces){
				if(i.connectedToRouter){
					for(Edge e: localhost.edges){
						simPrint(1, i.id, localhost.label, e.dest.label, e.cost);
					}
				}
			}
		}

		catch(IOException e){
			System.out.println("Error reading file");
		}
	}

	public void simulateRouter(BufferedReader in){
		String packet = "";
		String sections[];

		generateShortestPath();
		createForwardingTable();
		try{
			while((packet = in.readLine()) != null){
				sections = packet.split(",");
				if(sections[0].equals("0")){
					handleLinkAdvertisement(sections);
				}

				else if(sections[0].equals("1")){
					forwardDatagram(sections[2], sections[3]);
				}
			}
		}
		catch(IOException e){
			System.out.println("Error reading file");
		}
	}

	public void generateShortestPath(){
		ArrayList<Node> np = new ArrayList<Node>();

		ArrayList<Edge> leastCosts = new ArrayList<Edge>();

		//init
		np.add(localhost);
		for(Node v: network.nodes){
			if(!v.equals(localhost)){
				if(localhost.getPeers().contains(v) && localhost.getEdges().get(localhost.getPeers().indexOf(v)).up){
					v.leastCost = localhost.getEdges().get(localhost.getPeers().indexOf(v)).cost;		
					v.pred = localhost;	
				}

				else{
					v.leastCost = Integer.MAX_VALUE;
				}
			}
		}
		
		//loop
		while(np.size() != network.nodes.size()){
			int minCost = Integer.MAX_VALUE;
			Node w = null;
			for(Node node: network.nodes){
				if(!np.contains(node) && node.leastCost < minCost){
					minCost = node.leastCost;
					w = node;
				}
			}

			np.add(w);

			for(Node v: w.getPeers()){
				if(!np.contains(v)){
					if(w.getEdges().get(w.getPeers().indexOf(v)).up){
						v.leastCost = Math.min(v.leastCost, w.leastCost + w.getEdges().get(w.getPeers().indexOf(v)).cost);
					}

					if(v.leastCost == w.leastCost + w.getEdges().get(w.getPeers().indexOf(v)).cost){
						v.pred = w;
					}
				}
			}
		}

		updateForwardingTable();
	}

	public void createForwardingTable(){
		Node prevNode;
		for(Node n: network.nodes){
			if(!n.equals(localhost)){
				prevNode = n;
				while(prevNode.pred != localhost){
					prevNode = prevNode.pred;
				}
				for(Interface i: interfaces){
					if(i.connectedTo.equals(prevNode)){
						forwardingTable.put(n.label, i.id);
						break;
					}
				}
			}
		}
	}

	public void updateForwardingTable(){
		Node prevNode;
		for(Node n: network.nodes){
			if(!n.equals(localhost)){
				prevNode = n;
				while(prevNode.pred != localhost){
					prevNode = prevNode.pred;
				}
				for(Interface i: interfaces){
					if(i.connectedTo.equals(prevNode)){
						forwardingTable.put(n.label, i.id);
						break;
					}
				}
			}
		}
	}

	public void printForwardingTable(){
		Enumeration e = forwardingTable.keys();
    	while (e.hasMoreElements()) {
	    	String key = (String) e.nextElement();
	    	System.out.println("\t\t" + key + " : " + forwardingTable.get(key));
    	}
	}

	public void forwardDatagram(String srcIP, String destIP){
		if(forwardingTable.get(destIP) == null){
			System.out.println("No path to host: " + destIP);
		}
		else{
			simPrint(1, (int)forwardingTable.get(destIP), srcIP, destIP, 0);
		}
	}

	public void handleLinkAdvertisement(String[] sections){
		Interface srcInterface = null;

		for(Interface i: interfaces){
			if(Integer.parseInt(sections[1]) == i.id){
				srcInterface = i;
			}
		}

		Node peerNode = srcInterface.connectedTo;

		Node srcNode = null;
		if(peerNode.label.equals(sections[2])){
			srcNode = peerNode;
		}
		else{
			for(Node n: network.getNodes()){
				if(n.label.equals(sections[2])){
					srcNode = n;
					break;
				}
			}

			if(srcNode == null){
				System.out.println("Source IP must already exist on network");
			}
		}

		Node destNode = null;
		for(Node n: network.getNodes()){
			if(n.label.equals(sections[3])){
				destNode = n;
			}
		}

		if(destNode != null){
			if(destNode.getPeers().contains(srcNode)){
				if(destNode.getEdges().get(destNode.getPeers().indexOf(srcNode)).cost == Integer.parseInt(sections[4])){
					//do nothing, edge already exists
					return;
				}
				else{
					network.updateCost(srcInterface, srcNode, destNode, Integer.parseInt(sections[4]));

					if(Integer.parseInt(sections[4]) == Integer.MAX_VALUE){
						network.handleBrokenLink(srcInterface, srcNode, destNode);
					}

					generateShortestPath();
					advertiseLinkUpdate(srcInterface, srcNode, destNode, Integer.parseInt(sections[4]));
				}
			}

			else{
				network.addEdge(srcNode, destNode, Integer.parseInt(sections[4]));
				generateShortestPath();
				advertiseLinkUpdate(srcInterface, srcNode, destNode, Integer.parseInt(sections[4]));
			}
		}

		else{
			destNode = new Node(sections[3]);
			network.addNode(destNode);
			network.addEdge(srcNode, destNode, Integer.parseInt(sections[4]));

			generateShortestPath();

			advertiseLinkUpdate(srcInterface, srcNode, destNode, Integer.parseInt(sections[4]));
		}
	}

	public void advertiseLinkUpdate(Interface srcInterface, Node srcNode, Node destNode, int cost){
		for(Interface i: interfaces){
			if(i.id != srcInterface.id && i.connectedToRouter && i.linkUp){
				simPrint(0, i.id, srcNode.label, destNode.label, cost);
			}
		}
	}

	public void simPrint(int flag, int inf, String addr1, String addr2, int cost){
		System.out.println(flag + "," + inf + "," + addr1 + "," + addr2 + "," + cost);
	}

	public void debugPrint(){
		for(Node n: network.getNodes()){
			System.out.println("\t" + n.label + " " + n.pred + " " + n.leastCost);
		}
		printForwardingTable();
	}

	public static void main(String[] args){
		if(args.length != 1){
			System.out.println("Usage: java LinkStateRouter inputfile");
			System.exit(1);
		}

		else{
			try {
  				FileInputStream fstream = new FileInputStream(args[0]);
  				LinkStateRouter router = new LinkStateRouter(fstream);
			} 

			catch (FileNotFoundException e) {
				  e.printStackTrace();
			}
		}
	}
}