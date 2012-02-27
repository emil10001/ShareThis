package com.feigdev.sharethis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;

public class ShareThisActivity extends Activity {
	private static final String TAG = "ShareThis";
	private static final int CAMERA_PIC_REQUEST = 1337; 
	private File _photoFile;
	private Uri _fileUri;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ((Button)findViewById(R.id.button1)).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				String storageState = Environment.getExternalStorageState();
		        if(storageState.equals(Environment.MEDIA_MOUNTED)) {
		        	// http://stackoverflow.com/a/5054673/974800
		            String path = Environment.getExternalStorageDirectory().getName() 
		            		+ File.separatorChar + "Android/data/" 
		            		+ ShareThisActivity.this.getPackageName() 
		            		+ "/files/" + System.currentTimeMillis() + ".jpg";
		            _photoFile = new File(path);
		            try {
		                if(_photoFile.exists() == false) {
		                    _photoFile.getParentFile().mkdirs();
		                    _photoFile.createNewFile();
		                }

		            } catch (IOException e) {
		                Log.e(TAG, "Could not create file.", e);
		            }
		            Log.i(TAG, path);

		            _fileUri = Uri.fromFile(_photoFile);
		            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE );
		            intent.putExtra( MediaStore.EXTRA_OUTPUT, _fileUri);
		            startActivityForResult(intent, CAMERA_PIC_REQUEST);
		        }   else {
		            new AlertDialog.Builder(ShareThisActivity.this)
		            .setMessage("External Storeage (SD Card) is required.\n\nCurrent state: " + storageState)
		            .setCancelable(true).create().show();
		        }
			}
			
        });
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	String uri = null; 
        if (requestCode == CAMERA_PIC_REQUEST ) {
        	 Bitmap b = null;
        	 try {
				InputStream is = getContentResolver().openInputStream(_fileUri);
		        b = BitmapFactory.decodeStream(is);
			    is.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			 }
        	 if (null != b){
        		 Log.d(TAG,"uri=" + uri);
	        	 ImageView iv = new ImageView(this.getApplicationContext());
	        	 iv.setImageBitmap(b);
	        	 LinearLayout ll = (LinearLayout)findViewById(R.id.main_layout);
	        	 ll.addView(iv);
        	 }
        }
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
         Log.d(TAG, "Intent type: " + intent.getType());
         Log.d(TAG, "Intent categories: " + intent.getCategories());
         
         Bundle extras = intent.getExtras();
         
         // If there are extras, let's print them to see what we're getting
         if (null != extras){
	         for(String key : extras.keySet()){
	        	 Log.d(TAG,"key: " + key + ", value: " + extras.get(key).toString());
	         }
         }
         
         // launcher intent
         if (null == intent.getType()){
        	 return;
         }
         // from the browser
         else if (intent.getType().equals("text/plain")){
	         uri = intent.getStringExtra(Intent.EXTRA_TEXT); 
	         title = intent.getStringExtra(Intent.EXTRA_SUBJECT); 
	         Log.d(TAG,"uri=" + uri + ", title=" + title);
         }
         // from the gallery
         else {
        	 Bitmap b = null;
        	 try {
				InputStream is = null;
				if (null != intent.getExtras().get(Intent.EXTRA_STREAM)){
					is = getContentResolver().openInputStream((Uri)intent.getExtras().get(Intent.EXTRA_STREAM));
				}
				if (null != is){
			        b = BitmapFactory.decodeStream(is);
			        is.close();
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
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