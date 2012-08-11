package edu.purdue.cs.pictureprinter;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.GridView;
import android.widget.ImageView;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

public class FacebookPictureViewer extends Activity {

	public final static String FACEBOOK_APP_ID = "127866960690796";
	Facebook fb = new Facebook(FACEBOOK_APP_ID);
	GridView gridView;
	ProgressDialog dialog;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_picture_viewer);
        final Activity thisActivity = this;
        
        Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				if(fb.isSessionValid() != true) {
		        	fb.authorize(thisActivity, new String[] {"user_photos"}, new DialogListener() {

		        		@Override
		        		public void onFacebookError(FacebookError e) {
		        			Log.e("Facebook API", e.getMessage());
		        			finish();
		        		}

		        		@Override
		        		public void onError(DialogError e) {
		        			Log.e("Facebook API", e.getMessage());
		        			finish();
		        		}

		        		@Override
		        		public void onComplete(Bundle values) {
		        			// Fill screen with pictures
		        			getAllFacebookPictures();
		        		}

		        		@Override
		        		public void onCancel() {
		        			finish();
		        		}
		        	});
		        }				
			}
        	
        });
        
        gridView = (GridView) findViewById(R.id.gridView1);
        
        dialog = ProgressDialog.show(this, "Loading", "Loading. Please wait...", true);

        t.start();
                
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_facebook_picture_viewer, menu);
        return true;
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fb.authorizeCallback(requestCode, resultCode, data);
    }
    
    public void getAllFacebookPictures() {
    	final Activity thisActivity = this;
    	
    	Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
		    	String jsonFromFacebook = null;
		    	try {
					jsonFromFacebook = fb.request("me/photos");
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				}
		    	
		    	final ArrayList<FacebookPicture> facebookPictures = new ArrayList<FacebookPicture>();
		    	try {
					JSONObject jsonObject = new JSONObject(jsonFromFacebook);
					JSONArray dataArray = jsonObject.getJSONArray("data");
					for(int i = 0; i<dataArray.length(); i++) {
						FacebookPicture pic = new FacebookPicture();
						JSONObject pictureObject = dataArray.getJSONObject(i);
						pic.iconURL = pictureObject.getString("picture");
						pic.sourceURL = pictureObject.getString("source");
						pic.id = pictureObject.getString("id");
						
						
						InputStream is = null;
				    	try {
							URL url = new URL(pic.iconURL);
							HttpURLConnection conn = (HttpURLConnection) url.openConnection();
							conn.setReadTimeout(10000);
							conn.setConnectTimeout(15000);
							conn.setRequestMethod("GET");
							conn.setDoInput(true);
							
							// Starts the query
							conn.connect();
							int response = conn.getResponseCode();
							Log.d("Test", "The response is: " + response);
							is = conn.getInputStream();
							
							pic.picture = BitmapFactory.decodeStream(is);
							is.close();
							conn.disconnect();
							
							facebookPictures.add(pic);
							
						} catch (MalformedURLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Log.e("ERROR", e.getMessage());
					e.printStackTrace();
				}		    	
		    	
		    	runOnUiThread(new Runnable() {

					@Override
					public void run() {

						FacebookPicture pics[] =  new FacebookPicture[facebookPictures.size()];
						pics = facebookPictures.toArray(new FacebookPicture[facebookPictures.size()]);
						gridView.setAdapter(new ImageAdapter(thisActivity, pics));
						dialog.dismiss();
					}
		    		
		    	});

			}
    		
    	});
    	
    	t.start();



    }

    
}
