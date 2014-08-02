import java.awt.Color;
import java.util.ArrayList;

public class Path {
	
	//attr
	private ArrayList<Node> _nodes = new ArrayList<Node>();
	
	//constr
	Path(){}
	
	Path(ArrayList<Node> nodes){
		this.nodes(nodes);
	}
	
	
	
	//SG
	public void nodes(ArrayList<Node> nodes){
		this._nodes = nodes;
	}
	
	public ArrayList<Node> nodes(){
		return this._nodes;
	}
	//arrayList look-alike
	public int size(){
		return this.nodes().size();
	}
	
	public boolean isEmpty(){
		return this.nodes().isEmpty();
	}
	
	public Node get(int index){
		return this.nodes().get(index);
	}

	public boolean add(Node node){
		return this.nodes().add(node);
	}

	public void add(int index, Node node){
		this.nodes().add(index,node);
	}
	
	public Node remove(int index){
		return this.nodes().remove(index);
	}
	
	public void flush(){
		while(!this.isEmpty()){
			this.pop();
		}
	}
	
	public void append(Path other){
		for(int index = 0; index < other.size(); index++){
			this.add(other.get(index));
		}
	}
	
	public void copy(Path other){
		this.flush();
		this.append(other);
	}
	
	public int lastIndex(){
		return this.size()-1;
	}

	public Node first(){
		return this.get(0);
	}
	
	public Node last(){
		return this.get(this.lastIndex());
	}
	
	public Node pop(){
		return this.remove(this.lastIndex());
	}
	
	public boolean has(Node node){
		for (int index = 0; index < this.size(); index++){
			if (this.get(index) == node){
				//ref comparison
				return true;
			}
		}
		return false;
	}
	
	public String toString(){
		String result = "";
		if (!this.isEmpty()){
			result += this.first().toString();
			for (int index = 1; index < this.size(); index++){
				result += " > " + this.get(index).toString();
			}
			result += " " + this.weight();
		}
		return result;
	}
	
	//spec
	public void lock(){
		for (int i = 0 ; i < this.size() ; i++){
			this.get(i).hide();
		}
	}
	
	public void unlock(){

		for (int i = 0 ; i < this.size() ; i++){
			this.get(i).show();
		}
	}
	
	public int weight(){
		int weight = 0;
		for(int i=1;i<this.size();++i){
			weight += this.get(i).getArc(this.get(i-1)).getWeight();
		}
		return weight;
	}
	
	public boolean equals(Path other){
		if ( this.size() != other.size() ){
			return false;
		}
		
		for (int index = 0; index < this.size(); index++){
			if (this.get(index) != other.get(index)){
				return false;
			}
		}
		
		return true;
	}
	
	public boolean isFromTo(Node begin, Node end){
		return this.first() == begin && this.last() == end;
	}

	public boolean crosses(Path other) {
		int min = Math.min(this.size(), other.size());
		for (int index = 0; index < min; index++){
			if (this.get(index) == other.get(index)){
				return true;
			}
		}
		return false;
	}

	public void show(Color color){
		if(!this.isEmpty()){
			for(int i=1;i<this.size();++i){
				if(this.get(i).getArc(this.get(i-1)).color.equals(Color.BLACK)){
					this.get(i).getArc(this.get(i-1)).color = color;
				}
				else{
					int rgb[] = {0,0,0};
					rgb[0] = (this.get(i).getArc(this.get(i-1)).color.getRed()+color.getRed())/2;
					rgb[1] = (this.get(i).getArc(this.get(i-1)).color.getGreen()+color.getGreen())/2;
					rgb[2] = (this.get(i).getArc(this.get(i-1)).color.getBlue()+color.getBlue())/2;
					this.get(i).getArc(this.get(i-1)).color = new Color(rgb[0],rgb[1],rgb[2]);
				}
			}
			for(int i=0;i<this.size();++i){
				if(this.get(i).color.equals(Color.BLACK)){
					this.get(i).color = color;
				}
				else{
					int rgb[] = {0,0,0};
					rgb[0] = (this.get(i).color.getRed()+color.getRed())/2;
					rgb[1] = (this.get(i).color.getGreen()+color.getGreen())/2;
					rgb[2] = (this.get(i).color.getBlue()+color.getBlue())/2;
					this.get(i).color = new Color(rgb[0],rgb[1],rgb[2]);
				}
			}
		}
	}
}
