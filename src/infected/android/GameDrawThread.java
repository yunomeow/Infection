package infected.android;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameDrawThread extends Thread{
	GameView father;
	SurfaceHolder surfaceHolder;
	int sleepSpan = 100;
	boolean flag;
	public GameDrawThread(GameView father,SurfaceHolder surfaceHolder){
		this.father = father;
		this.surfaceHolder = surfaceHolder;
		this.flag = true;
	}
	public void run(){
		Canvas canvas = null;
		while(flag){
			try{
				canvas = surfaceHolder.lockCanvas(null);
				synchronized(surfaceHolder){
					father.doDraw(canvas);
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
			finally{
				if(canvas != null){
					surfaceHolder.unlockCanvasAndPost(canvas);
				}
			}
			try{
				Thread.sleep(sleepSpan);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	public void setFlag(boolean b){
		flag = false;
	}
}
