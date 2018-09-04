package infected.android;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback{
	private Bitmap background;
	private Bitmap button;
	private Infection activity;
	private Bitmap range;
	private GameDrawThread gdt;
	private GameThread gamethread;
	private Paint paintRed;
	private Paint paintYellow;
	private Bitmap ending;
	private Bitmap selected;
	private Bitmap selected2;
	private Timer timer;
	private Bitmap boarder;
	private Paint paint3;
	private Paint paint4;
	private int endMinute=0;
	private int endSecond=0;
	private SoundPool soundPool;
    private HashMap<Integer, Integer> soundPoolMap;   
	int tick = 0;
	private MediaPlayer mp = new MediaPlayer();  
	public GameView(Context context){
		super(context);
		getHolder().addCallback(this);
		this.activity = (Infection)context;
		initSounds();
		background = BitmapFactory.decodeResource( getResources(), R.drawable.background);
		range = BitmapFactory.decodeResource(getResources(),R.drawable.range);
		button = BitmapFactory.decodeResource(getResources(),R.drawable.button);
		ending = BitmapFactory.decodeResource(getResources(),R.drawable.ending);
		selected = BitmapFactory.decodeResource(getResources(),R.drawable.selected);
		selected2 = BitmapFactory.decodeResource(getResources(),R.drawable.selected2);
		boarder = BitmapFactory.decodeResource(getResources(),R.drawable.boarder);
		gdt = new GameDrawThread(this,getHolder());
		gamethread = activity.getGameThread();
		paintYellow = new Paint();
		paintYellow.setColor(Color.YELLOW);
		paintYellow.setStrokeWidth((float)4);
		paintRed = new Paint();
		paintRed.setColor(Color.RED);
		paintRed.setStrokeWidth((float)4);
		paint3 = new Paint();
		paint3.setTextSize(18);
		paint4 = new Paint();
		paint4.setTextSize(40);
		paint4.setColor(Color.WHITE);
		timer = new Timer();
		
		/*背景音樂播放*/
		try {
			mp = MediaPlayer.create(context, R.raw.battle);
			mp.setLooping(true);
			//mp.prepare();
			
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		mp.start();  
		
	}
	public MediaPlayer getMediaPlayer(){
		return mp;
	}
	public Timer getTimer(){
		return timer;
	}
	public void setEndMinute(int m){
		endMinute = m;
	}
	public void setEndSecond(int s){
		endSecond = s;
	}
	
	/*刷螢幕的method*/
	public void doDraw(Canvas canvas){
		if(activity.getEndGame()== false){
		int j = 0;
		tick++;
		canvas.drawBitmap(background,0,0,null);
		Paint paint=new Paint();
		Paint paint2 = new Paint();
		paint.setColor(Color.RED);
		paint.setAntiAlias(true); 
		paint.setStrokeWidth((float)1.5);
		paint2.setStrokeWidth(4);
		paint2.setColor(Color.BLACK);
		paint2.setAntiAlias(true);

		for(Node m: activity.getNodePool()){
			if(m != null){
				if(m.isSelect()&&activity.getMode()== Mode.CONSTRUCT){
					canvas.drawBitmap(range,m.getX()-100,m.getY()-100,null);
				}
			}
		}		
		synchronized(activity.getAdjList()) {
			for(LinkedList<NodeInfo> v: activity.getAdjList()){
				for(NodeInfo i: v){
					if(activity.getNodePool()[j] != null && activity.getNodePool()[i.number] != null)
						canvas.drawLine(activity.getNodePool()[j].getX(), activity.getNodePool()[j].getY(), 
								activity.getNodePool()[i.number].getX(), activity.getNodePool()[i.number].getY(), paint2) ;
					canvas.drawLine(activity.getNodePool()[j].getX(), activity.getNodePool()[j].getY(), 
							activity.getNodePool()[i.number].getX(), activity.getNodePool()[i.number].getY(), paint) ;				
				}
				j++;
			}
		}
		
		for(Node m: activity.getNodePool()){
			if(m != null){
				if(m.isSelect()){
					if(tick%4 < 2)
						canvas.drawBitmap(selected,m.getX()-40,m.getY()-40,null);
					else
						canvas.drawBitmap(selected2,m.getX()-40,m.getY()-40,null);
				}
				canvas.drawBitmap(m.getBitmap(),m.getX()-m.getOffset(),m.getY()-m.getOffset(),null);
					
			}
		}
		drawEnemy(canvas);
		float percentage = 0;
		for(Node m: activity.getNodePool()){
			if(m == null)continue;
			percentage = (float)m.getLifePoint()/(float)m.getMaxLifePoint();
			canvas.drawLine((float)m.getX()-m.getOffset(), (float)(m.getY()+m.getOffset()),
					(float)(m.getX()+m.getOffset()),(float) m.getY()+m.getOffset(), paintRed);
			canvas.drawLine((float)m.getX()-m.getOffset(), (float)(m.getY()+m.getOffset()),
					(float)(m.getX()-m.getOffset()+2*m.getOffset()*percentage),(float) m.getY()+m.getOffset(), paintYellow);
		}
		if(activity.getMode()==Mode.SELECTED){
			canvas.drawBitmap(button,10,400,null);
		}
		 
		canvas.drawBitmap(boarder,0,0,null);
		canvas.drawText(String.format("%02d",timer.getMinute())+ " : " + String.format("%02d",timer.getSecond()), 20, 32, paint3);
		canvas.drawText(String.format("%05d",activity.getEnergyCalculator().getNowEnergy()),252,32,paint3);
		}else{
			canvas.drawBitmap(ending,0,0,null);
			canvas.drawText(String.format("%02d",endMinute)+ " : " + String.format("%02d",endSecond), 105, 420, paint4);
		}
		
	}
	private void drawEnemy(Canvas canvas){
		for(EnemyNode en:gamethread.getEnemyList()){
			if(en != null)
				canvas.drawBitmap(en.getBitmap(),en.getX()-en.getOffset(),en.getY()-en.getOffset(),null);
		}
	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if(!gdt.isAlive()){
			gdt.start();
		}
		if(!activity.getGameThread().isAlive()){
			activity.getGameThread().start();
		}
		if(!timer.isAlive()){
			timer.start();
		}
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
		activity.getGameThread().setIsPlaying(false);
		gdt.setFlag(false);
		timer.setFlag(false);
        while (retry) {
            try {
            	gdt.join();
            	activity.getGameThread().join();
            	timer.join();
                retry = false;
            } 
            catch (InterruptedException e) {//不斷地循環，直到刷幀線程結束
            }
        }		
	}
	
	
	
	/*處理音效的事情*/
	public void initSounds(){
	     soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
	     soundPoolMap = new HashMap<Integer, Integer>();   
	     soundPoolMap.put(1, soundPool.load(getContext(), R.raw.boom, 1));
	     soundPoolMap.put(2, soundPool.load(getContext(), R.raw.endboom, 1));
	     soundPoolMap.put(3, soundPool.load(getContext(), R.raw.goodattack, 1));
	     soundPoolMap.put(4, soundPool.load(getContext(), R.raw.badattack, 1));
	} 
	public void playSound(int sound, int loop) {
	    AudioManager mgr = (AudioManager)getContext().getSystemService(Context.AUDIO_SERVICE);   
	    float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);   
	    float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);       
	    float volume = streamVolumeCurrent / streamVolumeMax;   
	    
	    soundPool.play(soundPoolMap.get(sound), volume, volume, 1, loop, 1f);
	}
}
