package infected.android;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class WelcomeDrawThread extends Thread{
	private int span = 100;//睡眠的毫秒數 
	private SurfaceHolder surfaceHolder;
	private WelcomeView welcomeView;//歡迎界面的引用
	private boolean flag = false;
    public WelcomeDrawThread(SurfaceHolder surfaceHolder, WelcomeView welcomeView) {//構造器
        this.surfaceHolder = surfaceHolder;//SurfaceHolder的引用
        this.welcomeView = welcomeView;//歡迎界面的引用
    }
    public void setFlag(boolean flag) {//設置標準位
    	this.flag = flag;
    }
	public void run() {//重寫的run方法
		Canvas c;
        while (this.flag) {//循環
            c = null;
            try {
            	// 鎖定整個畫布，在內存要求比較高的情況下，建議參數不要為null
                c = this.surfaceHolder.lockCanvas(null);
                synchronized (this.surfaceHolder) {//同步
                	welcomeView.doDraw(c);//調用繪製方法
                }
            } finally {//用finally保證一定被執行
                if (c != null) {
                	//更新屏幕顯示內容
                    this.surfaceHolder.unlockCanvasAndPost(c);
                }
            }
            try{
            	Thread.sleep(span);//睡眠指定毫秒數
            }catch(Exception e){//捕獲異常
            	e.printStackTrace();//打印異常信息
            }
        }
	}
}
