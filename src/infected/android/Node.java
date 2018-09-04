package infected.android;

import android.graphics.Bitmap;

public class Node {
	private int lifePoint;
	private int maxLifePoint;
	protected int x;
	protected int y;
	protected Bitmap bitmap;
	protected Bitmap selectBitmap;
	private Bitmap notActiveBitmap;
	private int offset = 20;
	private boolean selected;
	private Infection infection;
	public Node(Infection infection,Bitmap b,Bitmap b2,Bitmap b3,int x,int y,int lp,int mp){
		this.infection = infection;
		this.bitmap = b;
		this.x = x;
		this.y = y;
		this.lifePoint = lp;
		this.maxLifePoint = mp;
		selectBitmap = b2;
		selected = false;
		notActiveBitmap = b3;
	}
	public Bitmap getBitmap(){
		return bitmap;
	}
	public Bitmap getSelectBitmap(){
		return selectBitmap;
	}
	public Bitmap getNotActiveBitmap(){
		return notActiveBitmap;
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public int getLifePoint(){
		return lifePoint;
	}
	public int getMaxLifePoint(){
		return maxLifePoint;
	}
	public void minusLifePoint(int value){
		lifePoint -= value;
	}
	public boolean isSelect(){
		return selected;
	}
	public int getOffset(){
		return offset;
	}
	public void setSelect(boolean s){
		selected = s;
	}
	public Infection getInfection(){
		return infection;
	}
	public void setOffset(int value){
		offset = value;
	}
}
