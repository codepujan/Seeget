package blue.arche.sample_1;

import jp.co.nec.gazirur.rtsearch.lib.bean.SearchResult;
import jp.co.nec.gazirur.rtsearch.lib.clientapi.RTFeatureSearcher;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

// Camera control referred to http://qiita.com/zaburo/items/b5d3815d3ec45b0daf4f
// Special thanks to zaburo!
public class MainActivity extends Activity {

    private SurfaceView mySurfaceView;
    private Camera myCamera; //hardware
    private RTFeatureSearcher feSearcher;
    private SearchResult searchResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Service Authentication
        new ArcheAuth(getApplicationContext()).execute();

        //Create Searcher Instance
        ArcheCreateSearcher CreateSearcher = new ArcheCreateSearcher(
            getApplicationContext(),
            new ArcheCreateSearcherCallbacks() {
                public void postExecute(RTFeatureSearcher result) {
                    feSearcher = result;
                }
            }
        );
        CreateSearcher.execute();

        //SurfaceView
        mySurfaceView = (SurfaceView)findViewById(R.id.mySurfaceVIew);

        //SurfaceHolder
        SurfaceHolder holder = mySurfaceView.getHolder();

        //OnClickListener
        mySurfaceView.setOnClickListener(onSurfaceClickListener);

        //add Callback
        holder.addCallback(callback);
    }

    //Callback
    private SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {

            //CameraOpen
            myCamera = Camera.open();

            //set Portrait
            myCamera.setDisplayOrientation(90);

            //set Display to SurfaceView via surfaceHolder
            try{
                myCamera.setPreviewDisplay(surfaceHolder);
            }catch(Exception e){
                e.printStackTrace();
            }

        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int w, int h) {

            Camera.Parameters params = myCamera.getParameters();
            List<Camera.Size> sizes = params.getSupportedPreviewSizes();
/*
            //set Optimal PreviewSize
            Camera.Size optimalSize = getOptimalPreviewSize(sizes,w,h);
            params.setPreviewSize(optimalSize.width,optimalSize.height);
*/
            params.setPreviewSize(320,240);
            myCamera.setParameters(params);

            //start Preview
            myCamera.startPreview();

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

            //release Camera
            myCamera.release();
            myCamera = null;

            //close ARCHE Search Instance
            feSearcher.CloseFeatureSearcher();
        }
    };

    //OnClick
    private View.OnClickListener onSurfaceClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View view){
            if(myCamera != null){
                //AutoFocusを実行
                myCamera.autoFocus(autoFocusCallback);
            }
        }

    };

    //on AutoFocus
    private Camera.AutoFocusCallback autoFocusCallback = new Camera.AutoFocusCallback(){
        @Override
        public void onAutoFocus(boolean b, Camera camera) {

            //take Preview and Callback
            camera.setOneShotPreviewCallback(previewCallback);
        }
    };

    //previewCallback
    private Camera.PreviewCallback previewCallback = new Camera.PreviewCallback(){

        //OnShotPreview byte[]
        @Override
        public void onPreviewFrame(byte[] frame, Camera camera) {
            //Toast.makeText(getApplicationContext(),"Shot!", Toast.LENGTH_SHORT).show();

            //Identify
            ArcheSearch Search = new ArcheSearch(
                feSearcher,
                frame,
                new ArcheSearchCallbacks() {
                    public void postExecute(SearchResult result) {
                        searchResult = result;
                    }
                }
            );
            Search.execute();

            //get searchResult
            if (searchResult != null) {
                int id = searchResult.getId();
                List<String> appendInfo = searchResult.getAppendInfo();


                String productTarget="Product Store";
                for(int i=0;i<appendInfo.size();i++){
                    if(!appendInfo.get(i).equals(null)&&!appendInfo.get(i).equals("")) {
                        productTarget = appendInfo.get(i);
                        break;
                    }
                }
                navigateToStore(productTarget); // We are assuming that the 0th index exactly will contain the thing

            } else {
                Toast.makeText(getApplicationContext(), "Focus on Logo without shaking", Toast.LENGTH_SHORT).show();
            }
            searchResult = null;

/*
            //get width and hight
            int w = camera.getParameters().getPreviewSize().width;
            int h = camera.getParameters().getPreviewSize().height;

            //YUV to Bitmap conversion
            Bitmap bmp = getBitmapImageFromYUV(frame, w, h);

            //rotate
            Matrix m = new Matrix();
            m.setRotate(90);

            //rotated bitmap for save
            Bitmap rotated_bmp = Bitmap.createBitmap(bmp,0,0,bmp.getWidth(),bmp.getHeight(),m,true);

            //save
            String res = MediaStore.Images.Media.insertImage(getContentResolver(),rotated_bmp,"hoge.jpg",null);
            Log.v("save","res="+res);
*/
        }
    };

    //get Optimal PreviewSize

    private void navigateToStore(String companyName){

        //Make the intent call to transfer to another actiivty
        // to  the store Activity
        //Toast.makeText(MainActivity.this,"Identified"+companyName, Toast.LENGTH_SHORT).show();
        Intent storesintent=new Intent(MainActivity.this,StoreActivity.class);
        storesintent.putExtra("BRAND_NAME",companyName);
        startActivity(storesintent);


    }
    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {

        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio=(double)h / w;

        if (sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    //YUV to Bitmap conversion (cf. http://tech.thecoolblogs.com/2013/02/get-bitmap-image-from-yuv-in-android.html)
    public static Bitmap getBitmapImageFromYUV(byte[] data, int width, int height) {
        YuvImage yuvimage = new YuvImage(data, ImageFormat.NV21, width, height, null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        yuvimage.compressToJpeg(new Rect(0, 0, width, height), 80, baos);
        byte[] jdata = baos.toByteArray();
        BitmapFactory.Options bitmapFatoryOptions = new BitmapFactory.Options();
        bitmapFatoryOptions.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bmp = BitmapFactory.decodeByteArray(jdata, 0, jdata.length, bitmapFatoryOptions);
        return bmp;
    }
}
