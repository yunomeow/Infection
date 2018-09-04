package infected.android;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class Attacker extends Node{
	private final int attackRange = 80;
	private Bitmap[] bitmap;
	private int state = 0;
	private boolean attacked = false;
	public Attacker(Infection infection,Bitmap b,Bitmap b2,Bitmap b3,int x,int y,int lp,int mp){
		super(infection,b,b2,b3,x,y,lp,mp);
		bitmap = new Bitmap[10];
		bitmap[0] = BitmapFactory.decodeResource(infection.getResources(), R.drawable.attacker00);
		bitmap[1] = BitmapFactory.decodeResource(infection.getResources(), R.drawable.attacker01);
		bitmap[2] = BitmapFactory.decodeResource(infection.getResources(), R.drawable.attacker02);
		bitmap[3] = BitmapFactory.decodeResource(infection.getResources(), R.drawable.attacker03);
		bitmap[4] = BitmapFactory.decodeResource(infection.getResources(), R.drawable.attacker04);
		bitmap[5] = BitmapFactory.decodeResource(infection.getResources(), R.drawable.attacker05);
		bitmap[6] = BitmapFactory.decodeResource(infection.getResources(), R.drawable.attacker06);
		bitmap[7] = BitmapFactory.decodeResource(infection.getResources(), R.drawable.attacker07);
		bitmap[8] = BitmapFactory.decodeResource(infection.getResources(), R.drawable.attacker08);
	}
	public void attack(){
		double dis,nearestDis = 1e9,degree,slope;
		int k = 0,nearestNode = 0,tx=0,ty=0;
		for(Node m: getInfection().getGameThread().getEnemyList()){
			if(m == null)continue;
			dis = Math.sqrt(  (this.x-m.getX())  *  (this.x-m.getX())+ (this.y-m.getY())  *  (this.y-m.getY())  );
			if(nearestDis > dis){
				nearestDis = dis;
				nearestNode = k;
				tx = m.getX();
				ty = m.getY();
			}
			k++;
		}
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
		degree+=360;
		degree %= 360;
		//Log.i("Math","Degree: " + degree);
		if(nearestDis < attackRange && getInfection().getGameThread().getEnemyList().get(nearestNode) != null){
			getInfection().getGameThread().getEnemyList().get(nearestNode).minusLifePoint(15);
			getInfection().getEnergyCalculator().useEnergy(5);
			state = direction(degree)+1;
			attacked = true;
			getInfection().getGameView().playSound(3, 0);
		}else{
			state = 0;
		}
	}
	public Bitmap getBitmap(){
		if(state == 9)
			state = 0;
		if(attacked == false)
			state = 0;
		attacked = false;
		return bitmap[state];
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
}
