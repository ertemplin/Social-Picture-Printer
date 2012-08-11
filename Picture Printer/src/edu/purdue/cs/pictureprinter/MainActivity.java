package edu.purdue.cs.pictureprinter;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.support.v4.app.NavUtils;

public class MainActivity extends Activity {

	
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Button facebookButton = (Button) findViewById(R.id.buttonFacebook);
        facebookButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Do facebook stuff.
				goToFacebookPage();
			}
        	
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    public void goToFacebookPage() {
    	Intent launchFacebookPictureViewerIntent = new Intent(this, FacebookPictureViewer.class);
    	startActivity(launchFacebookPictureViewerIntent);
    }
    
}
