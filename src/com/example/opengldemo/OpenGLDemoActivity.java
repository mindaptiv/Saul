package com.example.opengldemo;

import android.app.Activity;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

import com.mindaptiv.saul.Cylon;

public class OpenGLDemoActivity extends Activity  {
    
	//Variable Declaration
	Cylon saul;
	//End Variable Declaration
	
	//Methods
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Go fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        GLSurfaceView view = new GLSurfaceView(this);
        view.setRenderer(new OpenGLRenderer(this));
        setContentView(view);
       
        //test
        this.saul = new Cylon();
        this.saul.testLog();                      
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.open_gldemo, menu);
		return true;
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) 
	{
		Log.i("Saul", Integer.toString(event.getKeyCode()));
		Log.i("Saul", Integer.toString(event.getAction()));
		
		if(saul != null)
		{
			return this.saul.handleKeyEvent(event);
		}
		else
		{
			return false;
		}
	}
	

}

		

