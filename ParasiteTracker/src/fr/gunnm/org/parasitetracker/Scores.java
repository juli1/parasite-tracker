package fr.gunnm.org.parasitetracker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;


public class Scores extends SQLiteOpenHelper {

		private final static String TAG = "ParasiteRecordDatabase";
	
	   	private static final int DATABASE_VERSION = 2;
	   	private static final String DATABASE_NAME = "parasiterecords";
	    private static final String TABLE_NAME = "scores";
	    private static final String TABLE_CREATE =
	                "CREATE TABLE " + TABLE_NAME + " (" +
	                "score " + " INTEGER, " +
	                "record_date  " + " TEXT);";
	    private SharedPreferences preferences;
	    private static Scores instance = null;
	  
	    public static Scores getInstance (Context context)
	    {
	    	if (Scores.instance == null)
	    	{
	    		Scores.instance = new Scores (context);
	    	}
	    	return Scores.getInstance ();
	    }
	    
	    public static Scores getInstance ()
	    {
	    	return Scores.instance;
	    }
	    
	    public Scores(Context context) 
	    {
	        super(context, DATABASE_NAME, null, DATABASE_VERSION);
            preferences = PreferenceManager.getDefaultSharedPreferences(context);
	    }

	    public void onCreate(SQLiteDatabase db) {
	        db.execSQL(TABLE_CREATE);
	    }

	    public Integer[] getHighScores ()
	    {
	    	Integer[] scores;
	    	int i = 0;
	    	int colIndex;
	    	SQLiteDatabase db = this.getWritableDatabase ();
	    	Cursor cursor = db.query (TABLE_NAME, null, null, null, null, null, null, null);

	    	scores = new Integer[cursor.getCount()];
    		cursor.moveToNext();

	    	while (! cursor.isLast())
	    	{    	
		    	try 
		    	{
		    		colIndex = cursor.getColumnIndex ("score");
			    	
			 
			    	colIndex = cursor.getColumnIndex ("record_date");
			 
		    	}
		    	catch (Exception e)
		    	{
//		    		Log.e (TAG, "EXCEPTION" + e.toString());
		    	}
		    	
	    		
	    		i++;
	    		cursor.moveToNext();
	    	}
	    	cursor.close();
	    	return scores;
	    }
	    
	    public void registerScore (int score)
	    {
	    	String date;
	    	Calendar c = Calendar.getInstance();
	    	date = c.get(Calendar.YEAR) + "-" +
	    		   c.get(Calendar.MONTH) + "-" +
	    		   c.get(Calendar.DAY_OF_MONTH) + "-" +
	    		   c.get(Calendar.HOUR) + "-" +
	    		   c.get(Calendar.MINUTE);
	    	ContentValues scoreValues = new ContentValues();
	    	scoreValues.put ("score", score);
	    	scoreValues.put ("record_date", date);
	    	
	    	SQLiteDatabase db = this.getWritableDatabase ();
	    	
	    	if (db.insert (TABLE_NAME, null, scoreValues) == -1)
	    	{
	    		//Log.e (TAG, "ERROR WHEN INSERT");
	    	}
	    	else
	    	{
	    		//Log.i (TAG, "INSERT SUCCESFUL");
	    	}
	    	this.sendScore (score, date);

	    }

    	private void sendScore(int score, String date) 
    	{
    		String username;
    		username = preferences.getString ("username", "F. Fillon");
    		String location;
    		location = preferences.getString ("location", "Ef213");
    		HttpClient httpclient = new DefaultHttpClient();
    		
    		HttpPost httppost = new HttpPost("http://pok.safety-critical.net/chronopost.php");

    		try {
    		
    			List nameValuePairs = new ArrayList(3);

    		
    			nameValuePairs.add(new BasicNameValuePair("score", ""+score));
    			nameValuePairs.add(new BasicNameValuePair("date", date));
    			nameValuePairs.add(new BasicNameValuePair("user", username + "@" + location));
    			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

    			HttpResponse response = httpclient.execute(httppost);


    		} catch (ClientProtocolException e) {
    			
    		} catch (IOException e) {
    			
    		}
    	}
	    
	    
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
			onCreate (db);
		}
	
		
		public void reset()
		{
			
			SQLiteDatabase db = this.getWritableDatabase ();
			db.delete (TABLE_NAME, null, null);
		}
		
		public void sendAll()
		{
			
			String dateStr;
	    	int score;
	    	int i = 0;
	    	int colIndex;
	    	int count;
	    	SQLiteDatabase db = this.getWritableDatabase ();
	    	Cursor cursor = db.query (TABLE_NAME, null, null, null, null, null, null, null);
	    	
	    	
	    	count =  cursor.getCount();
    		cursor.moveToFirst();
	    	for (i = 0 ; i < count ; i++)
	    	{
		    	try 
		    	{
		    		dateStr = cursor.getString(1);
		    		
		    		score   = cursor.getInt (0);
		    		sendScore(score, dateStr);
		    	}
		    	catch (Exception e)
		    	{
		    
		    	}
	    		cursor.moveToNext();
	    	}
	    	cursor.close();
		}
		
		
		public String[] getScores()
		{
			
			String[] dateStr;
			Date dateObj;
	    	String[] scores;
	    	int i = 0;
	    	int colIndex;
	    	int count;
	    	SQLiteDatabase db = this.getWritableDatabase ();
	    	Cursor cursor = db.query (TABLE_NAME, null, null, null, null, null, "score", null);
	    	
	    	count =  cursor.getCount();
	    	scores = new String[count];
    		cursor.moveToLast();
	    	for (i = 0 ; i < count ; i++)
	    	{
		    	try 
		    	{
		    		dateStr = cursor.getString(1).split("-");
		    		dateObj = new Date (Integer.parseInt(dateStr[0]),
		    				Integer.parseInt(dateStr[1]),
		    				Integer.parseInt(dateStr[2]),
		    				Integer.parseInt(dateStr[3]),
		    				Integer.parseInt(dateStr[4]),
		    				0);
		    		scores[i] = cursor.getInt (0) + "@" +  dateObj.toString();
		    	}
		    	catch (Exception e)
		    	{
		    
			    	scores[i] = "score " + i ;
		    	}
	    		cursor.moveToPrevious();
	    	}
	    	cursor.close();
	    	return scores;
		}
				
}
