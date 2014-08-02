
public class NodeCouple {
	private Node _begin;
	private Node _end;
	
	NodeCouple(Node begin, Node end){
		this.begin(begin);
		this.end(end);
	}
	
	public Node begin() {
		return _begin;
	}
	public void begin(Node begin) {
		this._begin = begin;
	}
	
	public Node end() {
		return _end;
	}
	public void end(Node end) {
		this._end = end;
	}
}