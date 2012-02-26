package com.feigdev.sharethis;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ShareThisActivity extends Activity {
	private static final String TAG = "ShareThis";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
    }
    
    @Override
    public void onStart(){
    	super.onStart();
    	if (this.getIntent() != null){
    		onNewIntent(this.getIntent());
    	}
    }
    
    protected void onNewIntent(Intent intent) {
    	 super.onNewIntent(intent);
    	 String uri = null; 
         String title = null; 

         Log.d(TAG, "onNewIntent called: " + intent.toString());
         Log.d(TAG, "Intent categories: " + intent.getCategories());
         
         Bundle extras = intent.getExtras();
         if (null != extras){
	         for(String key : extras.keySet()){
	        	 Log.d(TAG,"key: " + key + ", value: " + extras.get(key).toString());
	         }
         }
         if (null == intent.getType()){
        	 return;
         }
         if (intent.getType().equals("text/plain")){
	         uri = intent.getStringExtra(Intent.EXTRA_TEXT); 
	         title = intent.getStringExtra(Intent.EXTRA_SUBJECT); 
	         Log.d(TAG,"uri=" + uri + ", title=" + title);
         }
         else {
        	 byte [] bytes = intent.getByteArrayExtra(Intent.EXTRA_STREAM);
        	 Bitmap b = null;
        	 if (null != bytes){
        		 b = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
             }
        	 else {
            	 Log.d(TAG,"bytes is null =(");
				try {
					InputStream is = getContentResolver().openInputStream((Uri)intent.getExtras().get(Intent.EXTRA_STREAM));
		        	 b = BitmapFactory.decodeStream(is);
		        	 is.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	 }
        	 if (null != b){
        		 	 Log.d(TAG,"uri=" + uri);
	        	 ImageView iv = new ImageView(this.getApplicationContext());
	        	 iv.setImageBitmap(b);
	        	 LinearLayout ll = (LinearLayout)findViewById(R.id.main_layout);
	        	 ll.addView(iv);
        	 }
         }
         if (null != uri){
	      		((TextView)findViewById(R.id.main_text)).setText("uri=" + uri );
	
         }
       }
    
}