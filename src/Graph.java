import java.awt.Color;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.JFrame;


public class Graph {
	
	public static int infinite = -1;
	private Hashtable<String,Integer> _nodesId = new Hashtable<String,Integer>();
	private ArrayList<Node> _nodes = new ArrayList<Node>();
	
	public Graph(){}
	
	public void displayGraph(){
		JFrame frame = new JFrame("Graph");
		GraphInterface GI = new GraphInterface(this);
		frame.setSize(800, 800);
		frame.setBackground(new Color(200,200,200));
		frame.setResizable(false);
		frame.setContentPane(GI);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public int findSmalestWeight(int dist[]){
		int smalest = -1;
		int min = infinite;
		for(int i = 0; i < dist.length; i++){
			if( dist[i] != infinite && (dist[i] <= min || min == infinite)  && !this.getNode(i).token){
				smalest = i;
				min = dist[i];
			}
		}
		return smalest;
	}
	
	public Path getSmallestPath(NodeCouple travel){
		return this.getSmallestPath(travel.begin(), travel.end());
	}
	
	public Path getSmallestPath(Node A, Node B){
		
		if (!A.exist || !B.exist){
			//aucune solution possible
			return new Path();
		}
		
		ArrayList<Node> M = new ArrayList<Node>();
		
		Node prec[] = new Node[order()];
		int dist[] = new int[order()];
		
		for(int i=0;i<order();++i){
			dist[i] = (getNode(i).getArc(A)==null || !getNode(i).exist)?infinite:getNode(i).getArc(A).getWeight();
			prec[i] = A;
			getNode(i).setIndex(i);
			if(getNode(i).exist){
				M.add(getNode(i));
			}
		}
		
		M.remove(A);
		A.token = true;
		Node m;
		while(!M.isEmpty()){
			
			m = this.getNode(this.findSmalestWeight(dist));
			
			if(m == null){
				break;
			}
			
			m.token=true;
			M.remove(m);
			
			for(int i = 0; i < m.numberOfBoundNodes(); i++){
				Node y = m.getNode(i);
				if( y.exist && M.contains(y)){
					int v = dist[m.getIndex()] + m.getArc(m.getNode(i)).getWeight();
					if(dist[y.getIndex()] == infinite || v < dist[y.getIndex()]){
						dist[y.getIndex()] = v;
						prec[y.getIndex()] = m;
					}
				}
			}
			
		}
		
		for(int i=0;i<order();++i){
			//reset des tokens
			this.getNode(i).token=false;
		}
		
		if (dist[B.getIndex()] == infinite){
			//il n'y a pas de chemin entre A et B
			return new Path();
		}

		//construction du chemin représentant la solution
		Path res = new Path();
		Node node = B;
		while(true){
			res.add(0, node);
			node=prec[node.getIndex()];
			if(node==A){
				res.add(0,node);
				break;
			}
		}
		return res;
	}
	
	public int order(){
		return this._nodes.size();
	}
	
	public Node getNode(String s){
		return this._nodes.get(_nodesId.get(s));
	}
	
	public Node getNode(int i){
		try{
			return this._nodes.get(i);
		}
		catch(Exception e){
			return null;
		}
	}
	
	public void setNode(String s, Node node){
		if(_nodesId.get(s)==null){
			this._nodes.add(node);
			this._nodesId.put(s, this._nodes.size()-1);
		}
	}
}
