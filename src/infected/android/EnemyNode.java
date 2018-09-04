package infected.android;
import android.graphics.Bitmap;
import android.util.Log;


public class EnemyNode extends Node{
	private int cnt=0;
	private final int attackRange = 40;
	private int nextx,nexty,randomMove,collision,changeDirection;
	public EnemyNode(Infection infection,Bitmap b,Bitmap b2,Bitmap b3,int x,int y,int lp,int mp){
		super(infection,b,b2,b3,x,y,lp,mp);
	}
	private final int moveRange = 5;		//每一步可以走多少位置
	/*敵人的移動模式*/
	public void move(Node[] nodePool){
		double nearestDis = 1e9,dis = 0,slope,degree;
		int k = 0,tx=0,ty=0,nearestNode=0;
		Node m;
		nextx = this.x;
		nexty = this.y;

		/*尋找離自己最近的點*/
		for(k = 0 ; k < 30;k++){
			m = getInfection().getNodePool()[k];
			if(m == null)continue;
			dis = Math.sqrt(  (this.x-m.getX())  *  (this.x-m.getX())+ (this.y-m.getY())  *  (this.y-m.getY())  );
			if(nearestDis > dis){
				nearestDis = dis;
				tx = m.getX();
				ty = m.getY();
				nearestNode = k;
			}
		}
	//	Log.i("Move nearest" , "k: "+nearestNode);
		/*判斷移動方向*/
		if(tx != this.x){
			slope = (double)(ty-this.y) / (double)(tx - this.x);
			degree = (Math.atan(slope)*180/Math.PI);
			if(tx-this.x < 0)degree+=180;
		}else{
			if(ty > this.y)
				degree = 90;
			else
				degree = 270;
		}
		degree %= 360;
		if(randomMove == 0){
			changePosition(direction(degree));
		}else{
			changePosition(changeDirection);
			randomMove--;
		}
		/*判斷是否發生碰撞 若有則不移動*/
		collision = checkCollision(nextx,nexty);
		if(collision == 0){
			this.x = nextx;
			this.y = nexty;
		}else{
			if(collision == 2){
				randomMove = 5;
				changeDirection = direction(degree);
				while(changeDirection == direction(degree)){
					changeDirection = ((int)(Math.random()*100)%8); 
				}				
			}
		}

	}
	/*敵人的動畫*/
	private int getCount(){
		cnt++;
		return cnt;
	}
	public Bitmap getBitmap(){
		if(getCount()%6 < 3)
			return bitmap;
		else
			return selectBitmap;
	}
	private void changePosition(int direction){
		switch(direction){
		case 0:
			nextx+=moveRange;
			break;
		case 1:
			nextx+=moveRange;
			nexty+=moveRange;
			break;
		case 2:
			nexty+=moveRange;
			break;
		case 3:
			nextx-=moveRange;
			nexty+=moveRange;
			break;
		case 4:
			nextx-=moveRange;
			break;
		case 5:
			nextx-=moveRange;
			nexty-=moveRange;
			break;
		case 6:
			nexty-=moveRange;
			break;
		case 7:
			nextx+=moveRange;
			nexty-=moveRange;
			break;
		}
	}
	private int direction(double degree){
		if((degree >= 0 && degree < 22.5)||(degree < 360 && degree >=337.5))
			return 0;
		else if(degree >= 22.5 && degree < 77.5){
			return 1;
		}else if(degree >= 77.5 && degree < 112.5){
			return 2;
		}else if(degree >= 112.5 && degree < 157.5){
			return 3;
		}else if(degree >= 157.5 && degree < 202.5){
			return 4;
		}else if(degree >= 202.5 && degree < 247.5){
			return 5;
		}else if(degree >= 247.5 && degree < 302.5){
			return 6;
		}else if(degree >= 302.5 && degree < 337.5){
			return 7;
		}
		return 8;
	}
	private int checkCollision(int x,int y){
		double dis =0 ;
		for(Node m:getInfection().getNodePool()){
			if(m == null)continue;
			dis = Math.sqrt(  (x-m.getX())  *  (x-m.getX())+ (y-m.getY())  *  (y-m.getY())  );
			if(dis < m.getOffset())
				return 1;
		}
		for(Node m:getInfection().getGameThread().getEnemyList()){
			if(m == null || m == this)continue;
			dis = Math.sqrt(  (x-m.getX())  *  (x-m.getX())+ (y-m.getY())  *  (y-m.getY())  );
			if(dis < m.getOffset())
				return 2;
		}		
		return 0;
	}
	public void attack(){
		double dis,nearestDis = 1e9;
		int k = 0,nearestNode = 0;
		Node m;
		synchronized(getInfection().getNodePool()){
			for(k = 0 ; k < 30;k++){
				m = getInfection().getNodePool()[k];
				if(m == null)continue;
				dis = Math.sqrt(  (this.x-m.getX())  *  (this.x-m.getX())+ (this.y-m.getY())  *  (this.y-m.getY())  );
				if(nearestDis > dis){
					nearestDis = dis;
					nearestNode = k;
				}
			}
			//Log.i("Node","nearesetNode: "+nearestNode+" x: "+getInfection().getNodePool()[nearestNode].getX()+" y: "+getInfection().getNodePool()[nearestNode].getY());
			if(nearestDis < attackRange && getInfection().getNodePool()[nearestNode] != null){
				getInfection().getNodePool()[nearestNode].minusLifePoint(10);
				getInfection().getGameView().playSound(4, 0);
				//Log.i("Node","nearesetNode: "+nearestNode+" x: "+getInfection().getNodePool()[nearestNode].getX()+" y: "+getInfection().getNodePool()[nearestNode].getY());
			}
		}
	}
}
