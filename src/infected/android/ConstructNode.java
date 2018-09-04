package infected.android;

import java.util.LinkedList;

public class ConstructNode extends Thread{
	private Infection infection;
	private Node selectedNode;
	private int x,y;
	private Node[] nodePool;
	private LinkedList<Integer> addList;
	private int selectedNodeNumber;
	public ConstructNode(Infection infection,int x,int y){
		this.infection = infection;
		this.x = x;
		this.y = y;
		selectedNode = infection.getSelectedNode();
		addList = infection.getAddList();
		nodePool = infection.getNodePool();
		selectedNodeNumber = infection.getSelectNodeNumber();
	}
	public void run(){
		Node m;
		double dis = Math.sqrt(  (x-selectedNode.getX())  *  (x-selectedNode.getX())
				+ (y-selectedNode.getY())  *  (y-selectedNode.getY())  );
		int flag = 1,k=-1;
		if(dis > (selectedNode.getOffset()*1.5) && dis < (100)){
			addList.clear();
			addList.add((Integer)selectedNodeNumber);
			for(k=0;k<30;k++){
				m = nodePool[k];
				if(m == null || m == nodePool[selectedNodeNumber])continue;
				dis = Math.sqrt(  (x-m.getX())  *  (x-m.getX())+ (y-m.getY())  *  (y-m.getY())  );
				if(dis < m.getOffset()*1.5)
					flag = 0;
				if(dis < m.getOffset()*5)
					addList.add((Integer)k);
			}
			if(flag == 1){
				infection.getEnergyCalculator().useEnergy(100);
				infection.addNode(new EnergyCollector(infection,infection.getEnergyCollectorBitmap(),infection.getSelectEnergyCollectorBitmap(),
						infection.getEnergyCollectorBitmap(),x,y,100,100));
			}
		}
	}
}
