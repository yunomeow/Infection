package infected.android;

public class Info {
	private int type;
	private Node node;
	public Info(int type,Node node){
		this.node = node;
		this.type = type;
	}
	public int getType(){
		return type;
	}
	public Node getNode(){
		return node;
	}
}
