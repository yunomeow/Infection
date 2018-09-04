package infected.android;

public class Timer extends Thread{
	private int tick;
	private boolean flag;
	public Timer(){
		tick = 0;
		flag = true;
	}
	public void run(){
	while(flag){
		tick++;
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	}
	public void setFlag(boolean b){
		flag = b;
	}
	public int getMinute(){
		return tick/60;
	}
	public int getSecond(){
		return tick%60;
	}
}
