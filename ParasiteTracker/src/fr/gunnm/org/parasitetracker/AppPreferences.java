package fr.gunnm.org.parasitetracker;

import fr.gunnm.org.parasitetracker.R;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
 
public class AppPreferences extends PreferenceActivity {
    
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        Button sendButton  = new Button(this);
        sendButton.setText("Send all scores");
        Button resetButton = new Button(this);
        resetButton.setText("Reset scores");

		sendButton.setOnClickListener(new OnClickListener()
		{
			public void onClick(View arg0) {
				Scores.getInstance().sendAll();
			}
		});
		
		resetButton.setOnClickListener(new OnClickListener() 
		{

			public void onClick(View arg0) {
				Scores.getInstance().reset();
			}
		});
        this.getListView().addFooterView (sendButton);
        this.getListView().addFooterView (resetButton);
        
    }
 
}
