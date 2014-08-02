import java.util.ArrayList;
import java.util.Hashtable;
import java.awt.Color;


public class Node {
	
	private ArrayList<Node> _boundNodes = new ArrayList<Node>();
	private Hashtable<Node,Arc> _hashArcs = new Hashtable<Node,Arc>();
	private String _name;
	private int _index = 0;
	
	public Color color = new Color(0,0,0);;
	public int x = 0;
	public int y = 0;
	public boolean token = false;
	public boolean exist = true;
	
	Node(String name){
		_name=name;
	}
	
	public boolean bind(Node node, Arc arc){
		if(_hashArcs.get(node)==null){
			arc.setLeftNode(this);
			arc.setRightNode(node);
			_hashArcs.put(node, arc);
			_boundNodes.add(node);
			node.bind(this,arc);
			return true;
		}
		return false;
	}
	
	public boolean isBoundTo(Node node){
		return _hashArcs.get(node)!=null;
	}
	
	public void remove(Arc arc, Node node){
		_hashArcs.remove(node);
		_boundNodes.remove(node);
		
	}
	
	public int numberOfBoundNodes(){
		return _boundNodes.size();
	}

	public ArrayList<Node> getBoundNodes(){
		return _boundNodes;
	}
	
	public String getName(){
		return _name;
	}
	
	public int getIndex(){
		return _index;
	}
	
	public Arc getArc(Node n){
		return _hashArcs.get(n);
	}
	
	public Node getNode( int i ){
		return _boundNodes.get(i);
	}
	
	public Arc getArc(int i){
		return getArc(getNode(i));
	}
	
	public void setIndex(int i){
		_index =i;
	}
	
	public String toString(){
		return _name;
	}

	public void hide(){
		this.exist = false;
	}
	
	public void show(){
		this.exist = true;
	}
}
