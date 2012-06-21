package fr.gunnm.org.parasiterecord;

import fr.gunnm.org.parasiterecord.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.ImageView;
import android.widget.ListView;

public class MainActivity extends Activity implements OnChronometerTickListener{
    private static String 	TAG = "ParasiteRecord";
    private Scores 			scores;
    private boolean 		isStarted = false;
    
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main2);
    	Chronometer chrono = (Chronometer) findViewById(R.id.chronometer1);
    	chrono.setOnChronometerTickListener (this);
    	scores = Scores.getInstance (getApplicationContext());
    	this.updateScores ();

    }
    
    public void onResume() {
    super.onResume();
    	this.updateScores ();
    }
    

    
    private void updateScores ()
    {
    	ListView lv = (ListView) findViewById(R.id.scores_list);
    	lv.setAdapter (new ArrayAdapter<String> (this, R.layout.score_item, scores.getScores()));
    }
   
    public void onChronometerTick(Chronometer chronometer)
    {
    	int imageId;
    	if (((SystemClock.elapsedRealtime() / 1000) % 2) == 1)
    	{
    		imageId = R.drawable.parasite1;
    	}
    	else
    	{
    		imageId = R.drawable.parasite2;
    	}
    	ImageView iv = (ImageView) findViewById(R.id.image);
    	iv.setImageResource (imageId);
    }
    

    public void startStopChrono (View view)
    {
    	Chronometer chrono;
    	if (isStarted == false)
    	{
	    	chrono = (Chronometer) findViewById(R.id.chronometer1);
	    	chrono.setBase(SystemClock.elapsedRealtime());
	    	chrono.start();
	    	ImageView iv = (ImageView) findViewById(R.id.image);
	    	iv.setImageResource (R.drawable.parasite1);
	    	isStarted = true;
    	}
    	else
    	{
    		chrono = (Chronometer) findViewById(R.id.chronometer1);
        	chrono.stop();
        	long currentTime = SystemClock.elapsedRealtime();
        	long baseTime = chrono.getBase ();
        	long elapsed = (currentTime - baseTime) / 1000;
        	ImageView iv = (ImageView) findViewById(R.id.image);
        	iv.setImageResource (R.drawable.parasite2);
        	scores.registerScore ( (int)elapsed);
        	scores.getHighScores();
        	this.updateScores();
        	isStarted = false;
    	}
    }
    
    
    public void editPreferences (View view)
    {
    	Intent intent = new Intent(this, AppPreferences.class);
    	startActivity(intent);
    }
    
    public void resetScores (View view)
    {
    	scores.reset();
    	this.updateScores();
    }
} 
