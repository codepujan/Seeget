package blue.arche.sample_1;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.graphics.Matrix;
import android.view.Gravity;
import android.view.Surface;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

/**  import co.tanvas.haptics.service.adapter.HapticServiceAdapter;
 import co.tanvas.haptics.service.app.HapticApplication;
 import co.tanvas.haptics.service.model.HapticMaterial;
 import co.tanvas.haptics.service.model.HapticSprite;
 import co.tanvas.haptics.service.model.HapticTexture;
 import co.tanvas.haptics.service.model.HapticView;

 **/

public class SurfaceViewActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    TextView testView;

    private  int  baseWidth=150;
    private  int   baseHeight=150;

    Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;

    PictureCallback rawCallback;
    ShutterCallback shutterCallback;
    PictureCallback jpegCallback;


    private int currentCameraID=Camera.CameraInfo.CAMERA_FACING_FRONT;
    private Bitmap bmp;

    /** private HapticView mHapticView;
     private HapticTexture mHapticTexture;
     private HapticMaterial mHapticMaterial;
     private HapticSprite mHapticSprite;
     **/

    private View augmentedControl;
    private ImageView augmentedImageView;
    LayoutInflater controlInflater = null;


    //For the Pinch Zoom and scale thing
    private Matrix matrix = new Matrix();


    private DiscreteSeekBar imageResizer;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_surface_view);

        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        surfaceHolder.addCallback(this);

        // deprecated setting, but required on Android versions prior to 3.0
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        jpegCallback = new PictureCallback() {
            public void onPictureTaken(byte[] data, Camera camera) {
                FileOutputStream outStream = null;
                try {
                    outStream = new FileOutputStream(String.format("/sdcard/%d.jpg", System.currentTimeMillis()));
                    outStream.write(data);
                    outStream.close();
                    Log.d("Log", "onPictureTaken - wrote bytes: " + data.length);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                }
                Toast.makeText(getApplicationContext(), "Picture Saved", Toast.LENGTH_LONG).show();
                refreshCamera();
            }
        };


        controlInflater = LayoutInflater.from(getBaseContext());
        View view = controlInflater.inflate(R.layout.control, null);
        ViewGroup.LayoutParams layoutParamsControl
                = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT);
        this.addContentView(view, layoutParamsControl);


        augmentedControl=(View) view.findViewById(R.id.imageView);
        augmentedImageView=(ImageView ) view.findViewById(R.id.imageView);
        augmentedImageView.setImageBitmap(StaticHolder.getCurrentStore().productImage);

        imageResizer=(DiscreteSeekBar)view.findViewById(R.id.tryout_seekbar);
        imageResizer.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(150*value, 150*value);
                //0 applies for top of the parent
                if(StaticHolder.getCurrentStore().metadataLocation==0)
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP,RelativeLayout.CENTER_HORIZONTAL);
                else if(StaticHolder.getCurrentStore().metadataLocation==1)
                params.addRule(RelativeLayout.CENTER_IN_PARENT);
                else if(StaticHolder.getCurrentStore().metadataLocation==2)
                    params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);


                augmentedImageView.setLayoutParams(params);

            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

            }
        });
        //   initHaptics();

        ImageButton changeCamera=(ImageButton)view.findViewById(R.id.change_camera);
        changeCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                camera.stopPreview();

                //NB: if you don't release the current camera before switching, you app will crash
                camera.release();

//swap the id of the camera to be used
                if(currentCameraID == Camera.CameraInfo.CAMERA_FACING_BACK){
                    currentCameraID = Camera.CameraInfo.CAMERA_FACING_FRONT;
                }
                else {
                    currentCameraID = Camera.CameraInfo.CAMERA_FACING_BACK;
                }

                camera = Camera.open(currentCameraID);
                setCameraDisplayOrientation(camera);


                try {
                    camera.setPreviewDisplay(surfaceHolder);
                    camera.startPreview();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }



    public void refreshCamera() {
        if (surfaceHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            camera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here
        // start preview with new settings
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (Exception e) {

        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // Now that the size is known, set up the camera parameters and begin
        // the preview.
        setCameraDisplayOrientation(camera);

        refreshCamera();
    }



    public void setCameraDisplayOrientation(android.hardware.Camera camera) {
        Camera.Parameters parameters = camera.getParameters();

        android.hardware.Camera.CameraInfo camInfo =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(getBackFacingCameraId(), camInfo);


        Display display = ((WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int rotation = display.getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (camInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (camInfo.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (camInfo.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }




    private int getBackFacingCameraId() {
        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {

                cameraId = i;
                break;
            }
        }
        return cameraId;
    }

    public void surfaceCreated(SurfaceHolder holder) {
        try {
            // open the camera
            camera = Camera.open(currentCameraID);
        } catch (RuntimeException e) {
            // check for exceptions
            System.err.println(e);
            return;
        }
        Camera.Parameters param;
        param = camera.getParameters();

        // modify parameter
        param.setPreviewSize(352, 288);
        camera.setParameters(param);
        try {
            // The Surface has been created, now tell the camera where to draw
            // the preview.
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (Exception e) {
            // check for exceptions
            System.err.println(e);
            return;
        }

        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.noise_texture);



    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // stop preview and release camera
        camera.stopPreview();
        camera.release();
        camera = null;
    }

/**
 public void initHaptics() {
 try {
 // Get the service adapter
 HapticServiceAdapter serviceAdapter =
 HapticApplication.getHapticServiceAdapter();
 // Create a haptic view and activate it
 mHapticView = HapticView.create(serviceAdapter);
 mHapticView.activate();
 // Set the orientation of the haptic view
 Display display = ((WindowManager)
 getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
 int rotation = display.getRotation();
 HapticView.Orientation orientation =
 HapticView.getOrientationFromAndroidDisplayRotation(rotation);

 mHapticView.setOrientation(orientation);
 // Retrieve texture data from the bitmap
 Bitmap hapticBitmap = StaticHolder.getCurrentStore().tanvasImage;

 // Bitmap hapticBitmap = BitmapFactory.decodeResource(getResources(),
 //       R.drawable.noise_texture);

 byte[] textureData =
 HapticTexture.createTextureDataFromBitmap(hapticBitmap);
 // Create a haptic texture with the retrieved texture data
 mHapticTexture = HapticTexture.create(serviceAdapter);
 int textureDataWidth = hapticBitmap.getRowBytes() / 4; // 4 channels,
 // i.e., ARGB
 int textureDataHeight = hapticBitmap.getHeight();
 mHapticTexture.setSize(textureDataWidth, textureDataHeight);
 mHapticTexture.setData(textureData);
 // Create a haptic material with the created haptic texture
 mHapticMaterial = HapticMaterial.create(serviceAdapter);

 mHapticMaterial.setTexture(0, mHapticTexture);
 // Create a haptic sprite with the haptic material
 mHapticSprite = HapticSprite.create(serviceAdapter);
 mHapticSprite.setMaterial(mHapticMaterial);
 // Add the haptic sprite to the haptic view
 mHapticView.addSprite(mHapticSprite);
 } catch (Exception e) {
 Log.e(null, e.toString());

 }
 }
 **/

/**
 @Override
 public void onWindowFocusChanged(boolean hasFocus) {
 super.onWindowFocusChanged(hasFocus);
 // The activity is gaining focus
 Log.i("On Window Focus Changed","Window Focus Changed has been called");
 if (hasFocus) {
 try {
 // Set the size and position of the haptic sprite to correspond to
 //the view we created
 //View view = findViewById(R.id.view);
 int[] location = new int[2];
 augmentedControl.getLocationOnScreen(location);
 mHapticSprite.setSize(augmentedImageView.getWidth(), augmentedImageView.getHeight());
 mHapticSprite.setPosition(location[0], location[1]);
 } catch (Exception e) {
 Log.e(null, e.toString());
 }
 }
 }



 @Override
 public void onDestroy() {
 super.onDestroy();
 try {
 mHapticView.deactivate();
 } catch (Exception e) {
 Log.e(null, e.toString());
 }
 }

 **/

}