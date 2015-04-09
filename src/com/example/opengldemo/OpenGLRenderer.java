package com.example.opengldemo;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.hardware.input.InputManager;
import android.hardware.input.InputManager.InputDeviceListener;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;

public class OpenGLRenderer implements Renderer, InputDeviceListener {

        public Cube mCube = new Cube();
        private float mCubeRotation;

        public OpenGLRenderer(Context context)
        {
    		//test
            InputManager inputManager = (InputManager) context.getSystemService(Context.INPUT_SERVICE);
            inputManager.registerInputDeviceListener(this, null);
        }
        
        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f); 
                
            gl.glClearDepthf(1.0f);
            gl.glEnable(GL10.GL_DEPTH_TEST);
            gl.glDepthFunc(GL10.GL_LEQUAL);

            gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT,
                      GL10.GL_NICEST);       
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);        
            gl.glLoadIdentity();
            
            gl.glTranslatef(0.0f, 0.0f, -10.0f);
            gl.glRotatef(mCubeRotation, 1.0f, 1.0f, 1.0f);
                
            mCube.draw(gl);
               
            gl.glLoadIdentity();                                    
                
            mCubeRotation -= 0.15f; 
            
          
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            gl.glViewport(0, 0, width, height);
            gl.glMatrixMode(GL10.GL_PROJECTION);
            gl.glLoadIdentity();
            GLU.gluPerspective(gl, 45.0f, (float)width / (float)height, 0.1f, 100.0f);
            gl.glViewport(0, 0, width, height);

            gl.glMatrixMode(GL10.GL_MODELVIEW);
            gl.glLoadIdentity();
        }

		@Override
		public void onInputDeviceAdded(int deviceId) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onInputDeviceRemoved(int deviceId) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onInputDeviceChanged(int deviceId) {
			// TODO Auto-generated method stub
			
		}

}
