package infected.android;

import java.util.Iterator;
import java.util.LinkedList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

public class Infection extends Activity {
	/** Called when the activity is first created. */
	private Node base;
	private Node[] nodePool;
	private Bitmap base_bitmap;
	private Bitmap energy_collector_bitmap;
	private Bitmap s_base_bitmap;
	private Bitmap s_energy_collector_bitmap;	
	private Bitmap notActiveEnergyCollector;
	private Bitmap attacker;
	private Bitmap enemyBitmap ;
	private Bitmap enemyBitmap2;
	private final int maxNodeNumber = 30;
	private int nowNodeNumber;
	private GameView gameView;
	private Mode mode;
	private LinkedList<LinkedList<NodeInfo>> adjList;
	private Node selectedNode;
	private int selectedNodeNumber;
	private GameThread gameThread;
	private EnergyCalculator energycal;
	private LinkedList<Integer> addList;
	private WelcomeView welcomeView;
	private int nowState;
	private boolean endGame;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		nowState = 0;
		welcomeView = new WelcomeView(this);
		setContentView(welcomeView);
	}
	public void onPause(){
		getGameView().getMediaPlayer().stop();
		super.onDestroy();
	}
	public void toGameView(){
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		nowState = 1;
		initialize();
		energycal = new EnergyCalculator(this);
		gameThread = new GameThread(this);
		gameView = new GameView(this);
		endGame = false;
		setContentView(gameView);
	}
	public boolean getEndGame(){
		return endGame;
	}
	public void setEndGame(boolean b){
		endGame = b;
	}
	public void toWelcomeView(){
		nowState = 0;
		setContentView(welcomeView);
	}
	/*遊戲初始化*/
	public void initialize(){
		int i;
		/*圖片的匯入*/
		enemyBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.creep);
		enemyBitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.creep2);
		base_bitmap = BitmapFactory.decodeResource( getResources(), R.drawable.base);
		energy_collector_bitmap = BitmapFactory.decodeResource( getResources(), R.drawable.energycollector);
		s_base_bitmap = BitmapFactory.decodeResource( getResources(), R.drawable.selectbase);
		s_energy_collector_bitmap = BitmapFactory.decodeResource( getResources(), R.drawable.selectec);
		attacker = BitmapFactory.decodeResource(getResources(), R.drawable.attacker);

		/*初始化基地和相鄰串列*/
		base  = new Node(this,base_bitmap,s_base_bitmap,null,160,380,10,1000);
		base.setOffset(30);
		nodePool = new Node[maxNodeNumber];
		nodePool[0] = base;
		adjList = new LinkedList<LinkedList<NodeInfo>>();
		addList = new LinkedList<Integer>();
		for(i=0;i<=maxNodeNumber;i++){
			adjList.add(new LinkedList<NodeInfo>());
		}
		nowNodeNumber = 1;
		//addNode(new EnergyCollector(this,energy_collector_bitmap,s_energy_collector_bitmap,notActiveEnergyCollector,50,67,48,100)); //測試用node
		mode = Mode.UNSELECT;

	}

	/*獲取資源的method*/
	public Bitmap getEnemyBitmap(int i){
		if(i == 0)
			return enemyBitmap;
		else
			return enemyBitmap2;
	}
	public Node[] getNodePool(){
		return nodePool;
	}
	public int getMaxNodeNumber(){
		return maxNodeNumber;
	}
	public LinkedList<LinkedList<NodeInfo>> getAdjList(){
		return adjList;
	}
	public Mode getMode(){
		return mode;
	}
	public GameThread getGameThread(){
		return gameThread;
	}
	public GameView getGameView(){
		return gameView;
	}
	public EnergyCalculator getEnergyCalculator(){
		return energycal;
	}	
	public Node getSelectedNode(){
		return selectedNode;
	}
	public LinkedList<Integer> getAddList(){
		return addList;
	}
	public Bitmap getEnergyCollectorBitmap(){
		return energy_collector_bitmap;
	}
	public Bitmap getSelectEnergyCollectorBitmap(){
		return s_energy_collector_bitmap;
	}
	public int getSelectNodeNumber(){
		return selectedNodeNumber;
	}
	/*處理節點的變更*/
	public void addNode(Node node){
		int i=0,j=0;
		if(nowNodeNumber == maxNodeNumber)
			return;
		while(true){
			if(nodePool[i] == null){
				nodePool[i] = node;
				nowNodeNumber++;
				double dis = 0;
				while(!addList.isEmpty()){
					j = addList.getFirst().intValue();
					dis = Math.sqrt((nodePool[j].getX()-node.getX())*(nodePool[j].getX()-node.getX())
							+ (nodePool[j].getY()-node.getY())*(nodePool[j].getY()-node.getY()));
					adjList.get(addList.getFirst()).add(new NodeInfo(i,dis));
					adjList.get(i).add(new NodeInfo(addList.getFirst(),dis));
					addList.removeFirst();
				}
				return;
			}
			i++;
			if(i >= maxNodeNumber)i = 0;
		}
	}
	public void deleteNode(Node node){
		int i = 0;
		if(node == base)return;
		while(true){
			if(nodePool[i] == node){
				synchronized(adjList) {
					for(LinkedList<NodeInfo> list:adjList){
						Iterator<NodeInfo> iterator = list.iterator();
						while (iterator.hasNext()) {
							NodeInfo n = (NodeInfo) iterator.next();
							if (n.number == i){
								iterator.remove();
								list.remove(n);
							}
						}
					}
					adjList.get(i).clear();
					nodePool[i] = null;
					nowNodeNumber--;
					Log.i("OnCreate", "#########Test in java");
					return;
				}
			}
			i++;
			Log.i("OnCreate",((Integer)i).toString());
			if(i >= maxNodeNumber)i = 0;
		}    	
	}
	public void change(Node node){
		int x,y,state = 0;
		if(node instanceof EnergyCollector)
			state = 1;
		else if(node instanceof Attacker)
			state = 2;
		if(state > 0){
			x = node.getX();
			y = node.getY();
			int i=0;
			while(true){
				if(nodePool[i] == node)
					break;
				i++;
				if(i >= maxNodeNumber)i = 0;
			}
			addList.clear();
			for(NodeInfo n :adjList.get(i)){
				addList.add(n.number);
				Log.i("node",((Integer)(n.number)).toString());
			}
			deleteNode(node);
			if(state == 1)
				addNode(new Attacker(this,attacker,attacker,attacker,x,y,100,100));
			else if(state == 2)
				addNode(new EnergyCollector(this,energy_collector_bitmap,s_energy_collector_bitmap,notActiveEnergyCollector,x,y,100,100));
		}
	}


	/*處理所有螢幕事件*/
	public boolean onTouchEvent(MotionEvent event) {
		if(nowState == 0){
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				int x = (int)event.getX();
				int y = (int)event.getY();
				if(x >= 40 && x <= 260 && y <= 460 && y >= 410)
					finish();
				if(x >= 40 && x <= 260 && y <= 400 && y >= 350)
					toGameView();
			}
		}
		if(nowState == 1){
			if(endGame == false){
				int i,x,y;
				x = (int)event.getX();
				y = (int)event.getY();
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					synchronized(nodePool){
						if(mode == Mode.UNSELECT){
							i = 0;
							for(Node m:nodePool){
								if(m!=null)
								m.setSelect(false);
							}
							for(Node m:nodePool){
								if(m != null){
									if(x >= m.getX()-m.getOffset()&&x < m.getX()+m.getOffset()&&
											y >= m.getY()-m.getOffset()&&y < m.getY()+m.getOffset() ){
										m.setSelect(true);
										selectedNode = m;
										selectedNodeNumber = i;
										mode = Mode.SELECTED;
										break;
									}
								}
								i++;
							}
						}else if (mode == Mode.SELECTED){
							if(x >= 10 && x < 110 && y <=460 && y >=400 && selectedNode != null){
								mode = Mode.CONSTRUCT;
							}else if (x >= 110 && x < 210 && y <=460 && y >=400 &&selectedNode != null){

								deleteNode(selectedNode);
								energycal.useEnergy(-20);
								if(selectedNode != null)
									selectedNode.setSelect(false);
								selectedNode = null;
								mode = Mode.UNSELECT;
							}else if( x >= 210  && x < 310 && y <=460 && y >=400 &&selectedNode != null){
								if(energycal.getNowEnergy() >= 100){
									energycal.useEnergy(100);
									change(selectedNode);
								}
								if(selectedNode != null)
									selectedNode.setSelect(false);
								selectedNode = null;
								mode = Mode.UNSELECT;
							}else{
								if(selectedNode != null)
									selectedNode.setSelect(false);
								selectedNode = null;
								mode = Mode.UNSELECT;
							}


						}else if (mode == Mode.CONSTRUCT){
							if(selectedNode == null){
							}else if(energycal.getNowEnergy() >= 100){
								(new ConstructNode(this,x,y)).start();
							}
							if(selectedNode != null)
								selectedNode.setSelect(false);
							mode = Mode.UNSELECT;
						}
					}
				}
				gameView.invalidate();
			}else{
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
						toWelcomeView();
				}
			}
		}
		return super.onTouchEvent(event);

	}
}