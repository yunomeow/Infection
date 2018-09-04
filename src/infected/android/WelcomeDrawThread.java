package infected.android;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class WelcomeDrawThread extends Thread{
	private int span = 100;//�ίv���@��� 
	private SurfaceHolder surfaceHolder;
	private WelcomeView welcomeView;//�w��ɭ����ޥ�
	private boolean flag = false;
    public WelcomeDrawThread(SurfaceHolder surfaceHolder, WelcomeView welcomeView) {//�c�y��
        this.surfaceHolder = surfaceHolder;//SurfaceHolder���ޥ�
        this.welcomeView = welcomeView;//�w��ɭ����ޥ�
    }
    public void setFlag(boolean flag) {//�]�m�зǦ�
    	this.flag = flag;
    }
	public void run() {//���g��run��k
		Canvas c;
        while (this.flag) {//�`��
            c = null;
            try {
            	// ��w��ӵe���A�b���s�n�D����������p�U�A��ĳ�ѼƤ��n��null
                c = this.surfaceHolder.lockCanvas(null);
                synchronized (this.surfaceHolder) {//�P�B
                	welcomeView.doDraw(c);//�ե�ø�s��k
                }
            } finally {//��finally�O�Ҥ@�w�Q����
                if (c != null) {
                	//��s�̹���ܤ��e
                    this.surfaceHolder.unlockCanvasAndPost(c);
                }
            }
            try{
            	Thread.sleep(span);//�ίv���w�@���
            }catch(Exception e){//���򲧱`
            	e.printStackTrace();//���L���`�H��
            }
        }
	}
}
