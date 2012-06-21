package fr.gunnm.org.parasiterecord;

import fr.gunnm.org.parasiterecord.R;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
 
public class AppPreferences extends PreferenceActivity {
 
    private Scores scores;
    
    protected void onCreate (Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        // Add a button to the header list.
      
        Button sendButton  = new Button(this);
        sendButton.setText("Send all scores");
        Button resetButton = new Button(this);
        resetButton.setText("Reset scores");

		sendButton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Scores.getInstance().sendAll();
			}
 
		});
		
		resetButton.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				Scores.getInstance().reset();
			}
 
		});
        this.getListView().addFooterView (sendButton);
        this.getListView().addFooterView (resetButton);
        
    }
 
}
