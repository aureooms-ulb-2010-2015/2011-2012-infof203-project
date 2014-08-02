import java.awt.Color;
import java.util.ArrayList;


public class Arc {

	static int globalId=0;	
	private static ArrayList<Arc> _arcs = new ArrayList<Arc>();
	
	private Node _nodeL;
	private Node _nodeR;
	private int _weight;
	private boolean _isTempory = false;
	public Color color= new Color(0,0,0);
	int id = globalId++;
	
	public static Arc getArc(int i){
		return _arcs.get(i);
	}
	
	public static int getNumberOfArcs(){
		return _arcs.size();
	}
	
	public Arc(int weight){
		this._weight = weight;
		_arcs.add(this);
	}
	public boolean isTempory(){
		return this._isTempory;
	}
	
	public void remove(){
		getLeftNode().remove(this, getRightNode());
		getRightNode().remove(this, getLeftNode());
		_arcs.remove(this);
	}
	
	public void setTempory(){
		this._isTempory=true;
	}
	
	public Node getLeftNode(){
		return this._nodeL;
	}
	public Node getRightNode(){
		return this._nodeR;
	}
	public int getWeight(){
		return this._weight;
	}
	public void setWeight(int weight){
		this._weight = weight;
	}
	public void setRightNode(Node node){
		this._nodeR = node; 
	}
	public void setLeftNode(Node node){
		this._nodeL = node;
	}
	public Node getOposit(Node node){
		return (this.getLeftNode()==node)?this.getRightNode():this.getLeftNode();
	}

}
