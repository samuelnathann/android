package cct.mad.lab;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * This class takes care of surface for drawing and touches
 * 
 */

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

	/* Member (state) fields   */
	private GameLoopThread gameLoopThread;
	private Paint paint;
	private Paint over;//Reference a paint object 
    /** The drawable to use as the background of the animation canvas */
    private Bitmap mBackgroundImage;
    private Sprite sprite;
    private int hitCount;
    /* For the countdown timer */
    private long  startTime ;			//Timer to count down from
    private final long interval = 1 * 1000; 	//1 sec interval
    private CountDownTimer countDownTimer; 	//Reference to class
    private boolean timerRunning = false;
    private String displayTime; 		//To display time on the screen
    private boolean gameOver;

    private SoundPool sound;
    private int hitSound;
	public GameView(Context context) {
		super(context);
		mBackgroundImage = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.background);
// Focus must be on GameView so that events can be handled.
		this.setFocusable(true);
		// For intercepting events on the surface.
		this.getHolder().addCallback(this);
		sound = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		hitSound = sound.load(context, R.raw.hit, 1);
	}
	 /* Called immediately after the surface created */
	public void surfaceCreated(SurfaceHolder holder) {
		mBackgroundImage = Bitmap.createScaledBitmap(mBackgroundImage, getWidth(), getHeight(), true);// We can now safely setup the game start the game loop.
		ResetGame();//Set up a new game up - could be called by a 'play again option'
		gameLoopThread = new GameLoopThread(this.getHolder(), this);
		gameLoopThread.running = true;
		gameLoopThread.start();
	}
		
	//To initialise/reset game
	private void ResetGame(){
		sprite = new Sprite(this);/* Set paint details */
		hitCount=0;
	    paint = new Paint();
		paint.setColor(Color.BLACK); 
		paint.setTextSize(20);
		over =new Paint();
		over.setColor(Color.RED); 
		over.setTextSize(60);
		
		//Set timer
		startTime = 30;//Start at 10s to count down
		//Create new object - convert startTime to milliseconds
		countDownTimer=new MyCountDownTimer(startTime*1000,interval);
		countDownTimer.start();//Start it running
		timerRunning = true;
		gameOver = false;

	}

	//This class updates and manages the assets prior to drawing - called from the Thread
	public void update(){
		if (gameOver != true)
			{
				sprite.update();
			}
	}
	/**
	 * To draw the game to the screen
	 * This is called from Thread, so synchronisation can be done
	 */
	public void doDraw(Canvas canvas) {
		canvas.drawBitmap(mBackgroundImage, 0, 0, null);//Draw all the objects on the canvas
		canvas.drawText("The Game ",5,25, paint);
		canvas.drawText("The Number of Hits-"+String.valueOf(hitCount),5,45, paint);
		canvas.drawText("TIME REMAINING:"+String.valueOf(displayTime)+"s",5,80, paint);
		if (gameOver != false)
				{
					canvas.drawText("GAME OVER!",80,250, over);
					canvas.drawText("Press the back key to return to main menu.",55,270, paint);
				};
		sprite.draw(canvas); 
	}
	
	//To be used if we need to find where screen was touched
	public boolean onTouchEvent(MotionEvent event) {
		if (sprite.wasItTouched(event.getX(), event.getY())){
			/* For now, just renew the Sprite */
					sprite = new Sprite(this);
			    	   	hitCount++; 
			    	   	sound.play(hitSound, 1.0f, 1.0f, 0, 0, 1.0f);
			    	}
		return true;
	}

	private class MyCountDownTimer extends CountDownTimer {

		  public MyCountDownTimer(long startTime, long interval) {
				super(startTime, interval);
		  }
		  public void onFinish() {
				displayTime = "Times Up!";
				timerRunning = false;
				countDownTimer.cancel();
				gameOver = true;
		  }
		  public void onTick(long millisUntilFinished) {
				displayTime = " " + millisUntilFinished / 1000;
		  }
		}//End of MyCountDownTimer

	public int getHitCount()
	{
		return hitCount;
	}
	
	public void surfaceDestroyed(SurfaceHolder holder) {
		gameLoopThread.running = false;
		
		// Shut down the game loop thread cleanly.
		boolean retry = true;
		while(retry) {
			try {
				gameLoopThread.join();
				retry = false;
			} catch (InterruptedException e) {}
		}
	}
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		
	}



}
