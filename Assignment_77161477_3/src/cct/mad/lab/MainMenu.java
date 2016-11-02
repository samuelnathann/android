package cct.mad.lab;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;

public class MainMenu extends Activity {
	public static final String PREPS_NAME = "Score";
	SharedPreferences settings;
	SharedPreferences.Editor editor;
	int high;
	int gameScore;
	MediaPlayer sound;

	private static final int SCORE_REQUEST_CODE = 1;// The request code for the intent
    GameActivity gameActivity;
    GameView gameView;
	TextView tvScore;
	String score;
	Intent gameIntent;
	TextView highScore;
		
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		settings = getSharedPreferences(PREPS_NAME, 0);
		
		setContentView(R.layout.game_start);
		tvScore=(TextView)findViewById(R.id.tvGuessGame);
		highScore=(TextView)findViewById(R.id.textView3);
		
		high = settings.getInt("highScore", 0);
		gameScore = 0;
		highScore.setText(Integer.toString(high));
		
		sound= MediaPlayer.create(MainMenu.this, R.raw.bgmusic);
		sound.setLooping(true);
		sound.start();
	}
	
	public void startGame(View v){
		gameIntent = new Intent(this,GameActivity.class);
	    startActivityForResult(gameIntent, SCORE_REQUEST_CODE );  
	}
    /* Create Options Menu */
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	// Respond to item selected on OPTIONS MENU
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		//put data in Intent
		case R.id.easy:
			Toast.makeText(this, "Easy chosen", Toast.LENGTH_SHORT).show();
			return true;
		case R.id.medium:
			Toast.makeText(this, "Medium chosen", Toast.LENGTH_SHORT).show();
			return true;
		case R.id.hard:
			Toast.makeText(this, "Hard chosen", Toast.LENGTH_SHORT).show();
			return true;
		case R.id.other:
			Toast.makeText(this, "Other chosen", Toast.LENGTH_SHORT).show();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent retIntent) {
	    // Check which request we're responding to
	    if (requestCode == SCORE_REQUEST_CODE) {
	        // Make sure the request was successful
	        if (resultCode == RESULT_OK) {
	        	if (retIntent.hasExtra("GAME_SCORE")) {
				gameScore = retIntent.getExtras().getInt("GAME_SCORE");
				tvScore.setText(gameScore+"");
				
				if (gameScore >high){
					editor = settings.edit();
					editor.putInt("highScore", gameScore);
					editor.commit();
					Toast.makeText(MainMenu.this, "Congratulations! You have beaten the high score of "+high+ "! NEW HIGH SCORE: "+ gameScore, Toast.LENGTH_LONG).show();
					high = settings.getInt("highScore", 0);
				}
				highScore.setText(Integer.toString(high));
				Toast.makeText(MainMenu.this, "High Score "+high, Toast.LENGTH_LONG).show();
	        	}
	        }	
	    }

	}
 public void clearPreferences(View V){
	 editor = settings.edit();
	 editor.clear();
	 editor.commit();
	 Toast.makeText(MainMenu.this, "Data Reset",Toast.LENGTH_LONG).show();
	 highScore.setText("0");
 }

}
