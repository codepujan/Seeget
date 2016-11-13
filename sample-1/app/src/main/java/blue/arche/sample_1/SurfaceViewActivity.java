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
        import android.widget.ImageView;

        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Log;
        import android.view.Display;
        import android.view.LayoutInflater;
        import android.view.MotionEvent;
        import android.view.ScaleGestureDetector;
        import android.view.SurfaceHolder;
        import android.view.SurfaceView;
        import android.view.View;
        import android.view.ViewGroup;
        import android.view.WindowManager;
        import android.widget.TextView;
        import android.widget.Toast;

        import co.tanvas.haptics.service.adapter.HapticServiceAdapter;
        import co.tanvas.haptics.service.app.HapticApplication;
        import co.tanvas.haptics.service.model.HapticMaterial;
        import co.tanvas.haptics.service.model.HapticSprite;
        import co.tanvas.haptics.service.model.HapticTexture;
        import co.tanvas.haptics.service.model.HapticView;


public class SurfaceViewActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    TextView testView;

    Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;

    PictureCallback rawCallback;
    ShutterCallback shutterCallback;
    PictureCallback jpegCallback;


    private Bitmap bmp;

    private HapticView mHapticView;
    private HapticTexture mHapticTexture;
    private HapticMaterial mHapticMaterial;
    private HapticSprite mHapticSprite;

    private View augmentedControl;
    private ImageView augmentedImageView;
    LayoutInflater controlInflater = null;


    //For the Pinch Zoom and scale thing
    private ScaleGestureDetector scaleGestureDetector;
    private Matrix matrix = new Matrix();


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
        augmentedImageView=(ImageView) view.findViewById(R.id.imageView);
        augmentedImageView.setImageBitmap(StaticHolder.getCurrentStore().productImage);

        initHaptics();
    }

    public void captureImage(View v) throws IOException {
        //take the picture
        camera.takePicture(null, null, jpegCallback);
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
        refreshCamera();
    }

    public void surfaceCreated(SurfaceHolder holder) {
        try {
            // open the camera
            camera = Camera.open();
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
                mHapticSprite.setSize(augmentedControl.getWidth(), augmentedControl.getHeight());
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
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        scaleGestureDetector.onTouchEvent(ev);
        return true;
    }



}