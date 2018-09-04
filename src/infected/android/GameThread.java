package infected.android;

import java.util.Iterator;
import java.util.LinkedList;

import android.util.Log;


/*遊戲中用來更新的線程*/
public class GameThread extends Thread{
	private int tick=0;
	private boolean isPlaying = true;
	private Infection infection;
	private LinkedList<EnemyNode> enemynodelist;
	public GameThread(Infection infection){
		this.infection = infection;
		enemynodelist = new LinkedList<EnemyNode>();
	}
	public int getTick(){
		return tick;
	}
	public LinkedList<EnemyNode> getEnemyList(){
		return enemynodelist;
	}
	public void setPlaing(boolean b){
		isPlaying = b;
	}
	public LinkedList<Info> toDoList;
	private double rate=1;
	public void run(){
		while(isPlaying){
			//	Log.i("Thread","now: "+ tick);

			if(infection.getEndGame() == false){
				if(tick%500 == 0){
					if(rate < 10)
						rate+=1;
				}
				if(tick % (int)(200/rate) == 0)
					produceEnemy();
				if(tick%3 == 0)
					calEnemy();
				if(tick%10 == 1)
					enemyAttack();
				if(tick%5 == 0){
					goodAttack();
					produceEnergy();
				}
				checkAlive();
			}
			tick++;
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	private void calEnemy(){
		for(EnemyNode en: enemynodelist){
			en.move(infection.getNodePool());
		}
	}
	private void produceEnemy(){
		EnemyNode en;
		en = new EnemyNode(infection,infection.getEnemyBitmap(0),infection.getEnemyBitmap(1),infection.getEnemyBitmap(1),160,20,100,100);
		en.setOffset(25);
		enemynodelist.add(en);
	}
	private void enemyAttack(){
		for(EnemyNode en: enemynodelist){
			en.attack();
		}
	}
	private void goodAttack(){
		for(Node m:infection.getNodePool()){
			if(m == null)continue;
			if(m instanceof Attacker){
				((Attacker) m).attack();
			}
		}
	}
	private void checkAlive(){
		for(Node m:infection.getNodePool()){
			if(m == null)continue;
			if(m.getLifePoint() <= 0){
				if(m == infection.getNodePool()[0]){
					if(infection.getEndGame() == false){
						infection.getGameView().setEndSecond(infection.getGameView().getTimer().getSecond());
						infection.getGameView().setEndMinute(infection.getGameView().getTimer().getMinute());
						infection.getGameView().playSound(2, 0);
						infection.getGameView().getMediaPlayer().stop();
					}
					infection.setEndGame(true);
				}
				if(infection.getEndGame() == false)
					infection.getGameView().playSound(1, 0);
				infection.deleteNode(m);
			}
		}
		Iterator<EnemyNode> iterator = enemynodelist.iterator();
		while (iterator.hasNext()) {
			EnemyNode n = (EnemyNode) iterator.next();
			if (n.getLifePoint() <= 0){
				iterator.remove();
				enemynodelist.remove(n);
			}
		}
	}
	public void setIsPlaying(Boolean b){
		isPlaying = b;
	}
	public void produceEnergy(){
		infection.getEnergyCalculator().produceEnergy();
	}
	public LinkedList<Info> getToDoList(){
		return toDoList;
	}
	private void printNodePool(){
		int k = 0;
		for(Node m: infection.getNodePool()){
			if(m == null)continue;
			Log.i("NodePool","NodeNumber " + k);
			k++;
		}
	}
}
