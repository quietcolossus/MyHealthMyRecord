package ca.imdc.newp;

/**
 * Created by prabhleen on 2017-02-02.
 */

import android.Manifest;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static android.app.PendingIntent.getActivity;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class CameraApi extends AppCompatActivity {

    private static final int REQUEST_CAMERA_PERMISSION_RESULT = 0;
    private static final int REQUEST_EXTERNAL_STORAGE_PERMISSION_RESULT = 1;
    private TextureView mTextureView;
    private TextureView.SurfaceTextureListener mSurfaceListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
            setupCamera(i, i1);
            connectCamera();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

        }
    };

    private CameraDevice mCameraDevice;
    private CameraDevice.StateCallback mCameraDeviceStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice cameraDevice) {

            Toast.makeText(getApplicationContext(), "Camera connected", Toast.LENGTH_SHORT).show();
            mCameraDevice = cameraDevice;
            //code below called only once when you first install on devices marshmallow or later
            if(mIsRecording){
                try {
                    createVideoFileName();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                startRecord();
                mMediaRecorder.start();
                mChronometer.setBase(SystemClock.elapsedRealtime());
                mChronometer.setVisibility(View.VISIBLE);
                mChronometer.start();
            }
            else {
                startPreview();
            }
        }

        @Override
        public void onDisconnected(CameraDevice cameraDevice) {
            cameraDevice.close();
            mCameraDevice = null;
        }

        @Override
        public void onError(CameraDevice cameraDevice, int i) {
            cameraDevice.close();
            mCameraDevice = null;
        }
    };
    private HandlerThread mBackgroundHandlerThread;
    private Handler mBackgroundHandler;
    private String mCameraId;
    private Size mPreviewSize;
    private CaptureRequest.Builder mCaptureRequestBuilder;
    private ImageButton mRecordImageButton;
    private boolean mIsRecording = false;
    private ImageButton mFlipCamera;
    private Size mVideoSize;
    private MediaRecorder mMediaRecorder;
    private Chronometer mChronometer;
    //storage members
    private File mVideoFolder;
    private String mVideoFileName;

    private int mTotalRotation;
    private static SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 0);
        ORIENTATIONS.append(Surface.ROTATION_90, 90);
        ORIENTATIONS.append(Surface.ROTATION_180, 180);
        ORIENTATIONS.append(Surface.ROTATION_270, 270);
    }

    private static class CompareSizeByArea implements Comparator<Size> {
        @Override
        public int compare(Size lhs, Size rhs) {
            return Long.signum((long) lhs.getWidth() * lhs.getHeight() / (long) rhs.getWidth() * rhs.getHeight());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cameraapi);

        //storage method call to create folder to save videos in
        createVideoFolder();

        mMediaRecorder = new MediaRecorder();
        mChronometer = (Chronometer) findViewById(R.id.chronometer); //the timer
        mTextureView = (TextureView) findViewById(R.id.textureView);

        mFlipCamera = (ImageButton) findViewById(R.id.cameraFlipImageButton);
        mRecordImageButton = (ImageButton) findViewById(R.id.videoOnlineImageButton);
        //onclicklistener when you stop recording
        mRecordImageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(mIsRecording){
                    mChronometer.stop();
                    mChronometer.setVisibility(View.INVISIBLE);
                    mIsRecording = false;
                    mRecordImageButton.setImageResource(R.mipmap.btn_video_online);
                    //startPreview();
                    mMediaRecorder.stop();
                    Log.d("Debug msg", "Video recording stopped");
                    mMediaRecorder.reset();
                    startPreview();
                    dialog3 whatNext = new dialog3();
                    whatNext.show(getFragmentManager(), "dialog3");
                }
                else{
                    checkWriteStoragePermission();
                }
            }
        });
        mFlipCamera.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

            }
        });
    }

    public class dialog3 extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstance) {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
            //LayoutInflater inflater = getActivity().getLayoutInflater();
            builder.setTitle("Your video had been saved! " +
                    "Would you like to make another video or go back to the home screen?")
                    .setPositiveButton("Another video", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                        //stays on cameraapi
                    })
                    .setNeutralButton("Home", new DialogInterface.OnClickListener() {
                        public void onClick (DialogInterface dialog, int id){
                            finish();
                      }
                    });
            return builder.create();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startBackgroundThread();

        if (mTextureView.isAvailable()) {
            setupCamera(mTextureView.getWidth(), mTextureView.getHeight());
            connectCamera();
        } else {
            mTextureView.setSurfaceTextureListener(mSurfaceListener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CAMERA_PERMISSION_RESULT){
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED ) //if something is wrong with the request
            {
                Toast.makeText(getApplicationContext(), "Without camera permissions application will not run", Toast.LENGTH_SHORT).show();
            }
            if(grantResults[1] != PackageManager.PERMISSION_GRANTED ) //if something is wrong with the request
            {
                Toast.makeText(getApplicationContext(), "Without audio permissions application will not run", Toast.LENGTH_SHORT).show();
            }
        }
        if(requestCode == REQUEST_EXTERNAL_STORAGE_PERMISSION_RESULT){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                mIsRecording = true;
                mRecordImageButton.setImageResource(R.mipmap.btn_video);
                try {
                    createVideoFileName();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(this,"Storage permission successfully granted",Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this,"Application needs to save video to run",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onPause() {
        closeCamera();
        stopBackgroundThread();
        super.onPause();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        View decorView = getWindow().getDecorView();
        if (hasFocus) {
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    }

    private void setupCamera(int width, int height) {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            for (String cameraId : cameraManager.getCameraIdList()) {
                CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId);


                if (MainActivity.clicked) {
                    if (cameraCharacteristics.get(CameraCharacteristics.LENS_FACING) ==
                            CameraCharacteristics.LENS_FACING_FRONT) {

                        MainActivity.clicked = false;
                        closeCamera();
                        stopBackgroundThread();

                        startBackgroundThread();


                        cameraId = cameraManager.getCameraIdList()[1];
                    } else {
                        continue;
                    }
                }
                StreamConfigurationMap map = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                int deviceOrientation = getWindowManager().getDefaultDisplay().getRotation();
                mTotalRotation = sensorToDeviceRotation(cameraCharacteristics, deviceOrientation);
                boolean swapRotation = mTotalRotation == 90 || mTotalRotation == 270;
                int rotatedWidth = width;
                int rotatedHeight = height;
                if (swapRotation) {
                    rotatedWidth = height;
                    rotatedHeight = width;
                }
                mPreviewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class), rotatedWidth, rotatedHeight);
                //sets up rotation and size for preview of camera
                mVideoSize = chooseOptimalSize(map.getOutputSizes(MediaRecorder.class), rotatedWidth, rotatedHeight);



               mCameraId=cameraId;


                //sets up rotation and size for recording of video
                if (MainActivity.clicked) {
                    mCameraId = "1";
                }
                else{
                    mCameraId=cameraId;
                }




                return;
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void connectCamera() {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            //checks to see if its marshmallow android version or later
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    cameraManager.openCamera(mCameraId, mCameraDeviceStateCallback, mBackgroundHandler);
                }
                else{ //if you previously denied access to camera and you're starting up application again
                    if(shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                        Toast.makeText(this, "You must grant access to your camera,this video application requires it", Toast.LENGTH_SHORT).show();
                    }
                    requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}, REQUEST_CAMERA_PERMISSION_RESULT);
                    //above line if you're starting up application for teh first time its getting camera permissions
                }

            }
            else{
                cameraManager.openCamera(mCameraId, mCameraDeviceStateCallback, mBackgroundHandler);
            }

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void startRecord(){

        try {
            setUpMediaRecorder();
            SurfaceTexture surfaceTexture = mTextureView.getSurfaceTexture();
            surfaceTexture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            Surface previewSurface = new Surface(surfaceTexture);
            Surface recordSurface = mMediaRecorder.getSurface();
            mCaptureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);
            mCaptureRequestBuilder.addTarget(previewSurface);
            mCaptureRequestBuilder.addTarget(recordSurface);
            mCameraDevice.createCaptureSession(Arrays.asList(previewSurface, recordSurface),new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession cameraCaptureSession) {
                    try {
                        cameraCaptureSession.setRepeatingRequest(mCaptureRequestBuilder.build(), null,null );
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {

                }
            }, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startPreview(){
        SurfaceTexture surfaceTexture = mTextureView.getSurfaceTexture();
        surfaceTexture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
        Surface previewSurface = new Surface(surfaceTexture);
        try {
            mCaptureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            mCaptureRequestBuilder.addTarget(previewSurface);
            mCameraDevice. createCaptureSession(Arrays.asList(previewSurface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession cameraCaptureSession) { //method you enter when everything is successful
                    try {
                        cameraCaptureSession.setRepeatingRequest(mCaptureRequestBuilder.build(),null, mBackgroundHandler);
                        //so it keeps updating the display for video (preview)
                    }
                    catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) { //method you enter when something failed
                    Toast.makeText(getApplicationContext(), "Camera preview cannot be set up", Toast.LENGTH_SHORT).show();
                }
            }, null);
        }
        catch(CameraAccessException e){
            e.printStackTrace();
        }
    }

    private void closeCamera(){
        if(mCameraDevice != null){
            mCameraDevice.close();
            mCameraDevice = null;
        }
    }

    private void startBackgroundThread(){
        mBackgroundHandlerThread = new HandlerThread("CameraApi");
        mBackgroundHandlerThread.start();
        mBackgroundHandler = new Handler(mBackgroundHandlerThread.getLooper());
    }

    private void stopBackgroundThread(){
        mBackgroundHandlerThread.quitSafely();
        try {
            mBackgroundHandlerThread.join();
            mBackgroundHandlerThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static int sensorToDeviceRotation(CameraCharacteristics cameraCharacteristics, int deviceOrientation){
        int sensorOrientation = cameraCharacteristics.get(cameraCharacteristics.SENSOR_ORIENTATION);
        deviceOrientation = ORIENTATIONS.get(deviceOrientation);
        return (sensorOrientation + deviceOrientation + 360) % 360;
    }

    private static Size chooseOptimalSize(Size[] choices, int width, int height){
        List<Size> bigEnough = new ArrayList<Size>();
        for(Size option : choices){
            if(option.getHeight() == option.getWidth() * height / width && option.getWidth() >= width && option.getHeight() >= height) {
                bigEnough.add(option);
            }
        }
        if(bigEnough.size() > 0) {
            return Collections.min(bigEnough, new CompareSizeByArea());
        }
        else{
            return choices[0];
        }
    }

    //storage method #1 creates folder to save videos in
    private void createVideoFolder(){
        File movieFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
        mVideoFolder = new File(movieFile, "CameraApiVideos"); //creates our folder in movies direcotry in device
        if(!mVideoFolder.exists()){
            mVideoFolder.mkdirs(); //only creates folder if it hasnt been already created
            //dont need to create one every time we run it
        }


    }

    //storage method #2 creates unique file for each video
    private File createVideoFileName() throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String prepend = "VIDEO_" + timeStamp + "_";
        File videoFile = File.createTempFile(prepend,".mp4",mVideoFolder); //creates the actual file
        mVideoFileName = videoFile.getAbsolutePath(); //name of file name is stored
        return videoFile;


    }

    //storage method #3
    private void checkWriteStoragePermission(){
        //check to see for if permission granted for newer versions of android
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                mIsRecording = true;
                mRecordImageButton.setImageResource(R.mipmap.btn_video);
                try {
                    createVideoFileName();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                startRecord();
                mMediaRecorder.start();
                mChronometer.setBase(SystemClock.elapsedRealtime()); //gets real time
                mChronometer.setVisibility(View.VISIBLE);
                mChronometer.start();
            }
            else{
                if(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    Toast.makeText(this,"Application needs storage permissions to save videos",Toast.LENGTH_SHORT).show();
                }
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE_PERMISSION_RESULT );
                //don't need to check first time you open app, just need to request permissions
            }
        }
        else{ //older versions of android, older than marshmallow
            mIsRecording = true;
            mRecordImageButton.setImageResource(R.mipmap.btn_video);
            try {
                createVideoFileName();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            startRecord();
            mMediaRecorder.start();
            mChronometer.setBase(SystemClock.elapsedRealtime());
            mChronometer.setVisibility(View.VISIBLE);
            mChronometer.start();
        }
    }

    private void setUpMediaRecorder()throws IOException {
        //don't take these out of order
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mMediaRecorder.setOutputFile(mVideoFileName);
        mMediaRecorder.setVideoEncodingBitRate(1000000);
        mMediaRecorder.setVideoFrameRate(30);
        mMediaRecorder.setVideoSize(mVideoSize.getWidth(), mVideoSize.getHeight());
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mMediaRecorder.setOrientationHint(mTotalRotation);
        mMediaRecorder.prepare();
    }

}
