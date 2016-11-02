package cct.mad.lab;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;


public class Sprite {
	
	private Random random = new Random();
    
	//x,y position of sprite - initial position (0,50)
	private int x = random.nextInt(300)+1; 
	private int y = random.nextInt(300)+1;
    private int xSpeed = 10;//Horizontal increment of position (speed)
    private int ySpeed = 5;// Vertical increment of position (speed)
    private GameView gameView;
    private Bitmap spritebmp;
    //Width and Height of the Sprite image
    private int bmp_width;
	private int bmp_height;
    // Needed for new random coordinates.

  	
  	private static final int ANIMATION_ROWS = 4;
  	private static final int ANIMATION_COLUMNS = 4;
  	private int currentFrame = 0;
	long lastFrameChangeTime=0;
	int frameLengthInMilliseconds=100;
	int frameCount= ANIMATION_COLUMNS;
    
    public Sprite(GameView gameView) {
          this.gameView=gameView;
          spritebmp = BitmapFactory.decodeResource(gameView.getResources(),
                  R.drawable.seac);
          this.bmp_width = spritebmp.getWidth()/ANIMATION_COLUMNS;
  		  this.bmp_height= spritebmp.getHeight()/ANIMATION_ROWS;
     }
    //update the position of the sprite
    public void update() {
    	x = x + xSpeed;
     	y = y + ySpeed;
        wrapAround(); //Adjust motion of sprite.
    }
    
    public void getCurrentFrame(){
    	long time = System.currentTimeMillis();

		if (time > lastFrameChangeTime +frameLengthInMilliseconds){
    		lastFrameChangeTime = time;
    		currentFrame++;

			if (currentFrame >= frameCount){
    			currentFrame =0;
    		}
    	}
    }
    /* Checks if the Sprite was touched. */
    public boolean wasItTouched(float ex, float ey){
    	boolean touched = false; 
    	if ((x <= ex) && (ex < x + bmp_width) &&
    		(y <= ey) && (ey < y + bmp_height)) {
          		touched = true;
    	}
    	return touched;
    }//End of wasItTouched

    public void draw(Canvas canvas) {
    	int srcX = currentFrame * bmp_width;//frame - x direction
    	   int srcY; 					//row
    	   if (xSpeed > 0){//Sprite going right; row = 0
    	     		srcY = 2 * bmp_height;
    	   }
    	   else { //Going left; row = 1
    	        	srcY = 1 * bmp_height;
    	   }
    	   //Create Rect around the source image to be drawn
    	   Rect src = new Rect(srcX, srcY, srcX+bmp_width, srcY + bmp_height);
    	   //Rect for destination image
    	   Rect dst = new Rect(x, y, x + bmp_width, y + bmp_height);
    	   //draw the image frame
    	   getCurrentFrame();
    	   canvas.drawBitmap(spritebmp, src, dst, null);

    }
    
    public void wrapAround(){
    	//Code to wrap around	
      	if (x < 0) x = x + gameView.getWidth(); //increment x whilst not off screen
    	if (x >= gameView.getWidth()){ //if gone of the right sides of screen
    			xSpeed=-10;
    	}
    	if (x + xSpeed <0){
    		xSpeed=10;
    	}
    	if (y < 0) y = y + gameView.getHeight();//increment y whilst not off screen
    	if (y >= gameView.getHeight()){//if gone of the bottom of screen
    		ySpeed=-5;
    	}
    	if (y + ySpeed<70){
    		ySpeed=5;
    	}
    }
  
}  
