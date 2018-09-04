package infected.android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class WelcomeView extends SurfaceView implements SurfaceHolder.Callback{
	private Infection activity;
	private WelcomeDrawThread wdt;
	private Bitmap background;
	private Bitmap buttonstart;
	private Bitmap buttonquit;
	int tick = 0;
	public WelcomeView(Context context){
		super(context);
		getHolder().addCallback(this);
		this.activity = (Infection)context;
		
		background = BitmapFactory.decodeResource(getResources(), R.drawable.opening);
		buttonstart = BitmapFactory.decodeResource(getResources(), R.drawable.buttonstart);
		buttonquit = BitmapFactory.decodeResource(getResources(), R.drawable.buttonquit);
	}
	public void doDraw(Canvas canvas){
		canvas.drawBitmap(background,0,0,null);
		canvas.drawBitmap(buttonstart,40,350,null);
		canvas.drawBitmap(buttonquit,40,410,null);
	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		wdt = new WelcomeDrawThread(getHolder(),this);
		wdt.setFlag(true);
		wdt.start();
		Log.i("TTTTTTTTT","aaaaaaa");
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
		wdt.setFlag(false);
        while (retry) {
            try {
            	wdt.join();
                retry = false;
            } 
            catch (InterruptedException e) {//不斷地循環，直到刷幀線程結束
            }
        }		
	}	
}
