package ca.imdc.newp;


import android.Manifest;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Camera;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.ibm.cloud.sdk.core.http.HttpMediaType;
import com.ibm.cloud.sdk.core.security.Authenticator;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.speech_to_text.v1.SpeechToText;
import com.ibm.watson.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.speech_to_text.v1.model.SpeechRecognitionResults;
import com.ibm.watson.speech_to_text.v1.websocket.BaseRecognizeCallback;
import com.ibm.watson.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.tone_analyzer.v3.model.ToneAnalysis;
import com.ibm.watson.tone_analyzer.v3.model.ToneOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;

public class MainActivity extends AppCompatActivity {
    static final int REQUEST_VIDEO_CAPTURE = 1;
    private static final int REQUEST_TAKE_GALLERY_VIDEO = 3;
    //String cfileName;
    //String encfileName;
    TextView instr;
    ListView listView;
    Thread t = null;
    private String[] Test1 = {"Halo"};
    private DrawerLayout mDrawerLayout;
    protected static String cfileName;
    private RecyclerView mRecyclerView;
    public static RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String[] myDataset;
    private String[] myDate;
    private String[] myTime;
    public Button cancel;
    public Button share;
    public String globName;
    public String[] lastDataset;
    public static JSONObject jRecord = new JSONObject();
    public static JSONObject tRecord = new JSONObject();
    public static JSONObject nRecord = new JSONObject();

    private CameraSource mCameraSource = null;

    private CameraSourcePreview mPreview;
    private GraphicOverlay mGraphicOverlay;

    public JSONObject jTags;

    public static boolean clicked=false;
    public static boolean isOther = false;
    protected static String encfileName;

    protected static File mencVideoFolder;


    public boolean videosExist;
    private File mVideoFolder;
    private Uri fileUri;
    public List<String> namelist;
    public List<String> datelist;

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    public static final String EXTRA_MESSAGE = "ca.imdc.newp.MESSAGE";

    public String transcript;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //String obj = PreferenceManager.getDefaultSharedPreferences(this).getString("tagRecord", null);
        //String transcripts = PreferenceManager.getDefaultSharedPreferences(this).getString("transcriptRecord", null);
        //String names_record = PreferenceManager.getDefaultSharedPreferences(this).getString("nameRecord", null);
        String transcripts = "{\"checkin\":\"I'm just coming to check in %HESITATION and record %HESITATION my pain for today %HESITATION woke up this morning with %HESITATION increased pain on my joints on my hand and %HESITATION its uncomfortable to move %HESITATION and and have my coffee in the morning and its just uncomfortable pain\",\"comparison\":\"%HESITATION comparison to yesterday the pain in my limbs are %HESITATION about the same %HESITATION my leg pain has increased and %HESITATION there was discomfort in our bed %HESITATION it took me a few seconds longer to go to the toilet \",\"goodmorning\":\"good morning %HESITATION todays pain was significantly better %HESITATION I could still feel a numbness the pain but %HESITATION was significantly better than it was yesterday\", \"general\":\"hello %HESITATION today I experienced a slight discomfort going to the toilet %HESITATION I experienced slight diarrhea and %HESITATION discomfort and pain in my legs but my arms seem to be okay or a least not as painful as it was yesterday %HESITATION the headache has subsided hopefully by tomorrow the pain in my legs is less painful\", \"lesspain2\":\"hello %HESITATION today i experienced a slight discomfort going to the toilet %HESITATION i experienced slight diarrhea and %HESITATION discomfort and pain in my legs but my arms seem to be okay or a least not as painful as it was yesterday %HESITATION the headache has subsided hopefully by tomorrow the pain in my legs is less painful\", \"morepain3\":\"good morning %HESITATION today the pain in my my fin my hands and my legs are a lot better %HESITATION but I was just experiencing a little discomfort %HESITATION aches and a headache %HESITATION slight aches not too much %HESITATION but I would say the pain top %HESITATION the pain levels today would be around a five\"}";
        String obj = "{\"checkin\":{\"Arousal\":75,\"Valence\":13,\"Location\":{\"Home\":true,\"Indoors\":true},\"Activity\":{\"Leisure\":true,\"Meds\":true},\"Sharing\":{\"Everyone\":true,\"Medical\":true}}, \"comparison\":{\"Arousal\":89,\"Valence\":83,\"Location\":{\"Home\":true,\"Indoors\":true},\"Activity\":{\"Leisure\":true,\"Meds\":true},\"Sharing\":{\"Everyone\":true,\"Medical\":true}}, \"goodmorning\":{\"Arousal\":7,\"Valence\":26,\"Location\":{\"Home\":true,\"Indoors\":true},\"Activity\":{\"Leisure\":true,\"Meds\":true},\"Sharing\":{\"Everyone\":true,\"Medical\":true}}, \"general\":{\"Arousal\":55,\"Valence\":95,\"Location\":{\"Home\":true,\"Indoors\":true},\"Activity\":{\"Leisure\":true,\"Meds\":true},\"Sharing\":{\"Everyone\":true,\"Medical\":true}}, \"lesspain2\":{\"Arousal\":81,\"Valence\":36,\"Location\":{\"Home\":true,\"Indoors\":true},\"Activity\":{\"Leisure\":true,\"Meds\":true},\"Sharing\":{\"Everyone\":true,\"Medical\":true}}, \"morepain3\":{\"Arousal\":70,\"Valence\":11,\"Location\":{\"Home\":true,\"Indoors\":true},\"Activity\":{\"Leisure\":true,\"Meds\":true},\"Sharing\":{\"Everyone\":true,\"Medical\":true}}}";
        String names_record = "{\"checkin\":\"2022-02-04 14:13\",\"comparison\":\"2022-02-05 14:14\",\"goodmorning\":\"2022-02-07 15:39\", \"general\":\"2022-02-09 15:39\", \"lesspain2\":\"2022-02-10 15:39\", \"morepain3\":\"2022-02-13 15:39\"}";
        System.out.println("TAGS: " + obj + "\n");
        System.out.println("\nSAVED TRANSCRIPTS: " + transcripts + "\n");
        System.out.println("\nVIDEO NAMES: " + names_record);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        System.setProperty("http.proxyHost","");
        System.setProperty("http.proxyPort","");
        System.setProperty("https.proxyHost","");
        System.setProperty("https.proxyPort","");

        if (obj == null) { obj = ""; };
        try {
            jRecord = new JSONObject(obj);
            System.out.println(jTags);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (transcripts == null) { transcripts = ""; };
        try {
            tRecord = new JSONObject(transcripts);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(nRecord);
        if (names_record == null) { names_record = ""; };
        try {
            nRecord = new JSONObject(names_record);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println(obj);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("MyHealthMyRecord");
        setSupportActionBar(toolbar);
        mRecyclerView = findViewById(R.id.my_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        if (checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}, CameraApi.REQUEST_CAMERA_PERMISSION_RESULT);
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CameraApi.REQUEST_EXTERNAL_STORAGE_PERMISSION_RESULT);
            requestPermissions(new String[]{Manifest.permission.INTERNET}, CameraApi.REQUEST_INTERNET_RESULT);
        }

        //myDataset = populateList("names");
        Iterator<String> keys = nRecord.keys();
        namelist = new ArrayList<String>();
        datelist = new ArrayList<String>();
        keys.forEachRemaining(namelist::add);
        Iterator<String> keys2 = nRecord.keys();
        keys2.forEachRemaining(key -> {
            try {
                datelist.add((String) nRecord.get(key));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        
        myDataset = namelist.toArray(new String[0]);
        myDate = datelist.toArray(new String[0]);

        System.out.println("DATES: " + Arrays.toString(myDate));

        Arrays.sort(myDataset, Collections.reverseOrder());
        Arrays.sort(myDate, Collections.reverseOrder());
        //myDate = populateDate(myDataset);

        mAdapter = new MyAdapter(myDataset, myDate, this);
        mRecyclerView.setAdapter(mAdapter);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        NavigationView navigationView = findViewById(R.id.nav_view);
        mDrawerLayout = findViewById(R.id.drawer);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            VectorDrawableCompat indicator
                    = VectorDrawableCompat.create(getResources(), R.drawable.ic_menu_black_24dp, getTheme());
            indicator.setTint(ResourcesCompat.getColor(getResources(), R.color.colorAccent, getTheme()));
            supportActionBar.setHomeAsUpIndicator(indicator);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    // This method will trigger on item Click of navigation menu
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        int id = menuItem.getItemId();
                        menuItem.setChecked(true);

                        if (id == R.id.nav_myvideos) {
                            Intent mainIntent = new Intent(MainActivity.this, MainActivity.class);
                            startActivity(mainIntent);
                        }
                        else if (id == R.id.nav_share) {
                            Intent shareIntent = new Intent(MainActivity.this, shareCircleActivity.class);
                            startActivity(shareIntent);
                        }
                        else if (id == R.id.nav_myv) {
                            Intent dataIntent = new Intent(MainActivity.this, WordCloudActivity.class);
                            startActivity(dataIntent);
                        }
                        else if (id == R.id.nav_annot) {
                            Intent annotationIntent = new Intent(MainActivity.this, AnnotationActivity.class);
                            startActivity(annotationIntent);
                        }
                        else if (id == R.id.nav_settings) {

                        }
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    }
                });

        //Floating action button to create a dialogue
        FloatingActionButton myFab = findViewById(R.id.fab);
        myFab.setBackgroundColor(Color.RED);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog myAlert = new dialog();
                myAlert.show(getFragmentManager(), "dialog1");
            }
        });
        // use a linear layout manager
    }
    String[] populateDate(String[] list) {
        MediaMetadataRetriever infoVideo = new MediaMetadataRetriever();
        File folder = new File(getExternalFilesDir(null).getAbsolutePath() + "/Video/");
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles == null) {
            String[] empty = {""};
            return empty;
        }
        int count = 0;
        String[] items = new String[list.length];
        while (count < list.length) {
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile() && listOfFiles[i].getName().equals(list[count])) {
                    Date lastModDate = new Date(listOfFiles[i].lastModified());
                    System.out.println("Last modified date:" + lastModDate.toString());
                    items[count] = lastModDate.toString();
                    count++;
                    break;
                }
            }
        }
        return items;
    }

    String[] populateList(String g) {
        MediaMetadataRetriever infoVideo = new MediaMetadataRetriever();
        File folder = new File(getExternalFilesDir(null).getAbsolutePath() + "/Video/");
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles == null) {
            String[] empty = {""};
            return empty;
        }
        System.out.println(listOfFiles);
        int count = 0;
        for (int i = 0; i < listOfFiles.length; i++) {
            if(listOfFiles[i].isFile() && !listOfFiles[i].getName().endsWith(".mp3")) {
                count++;
            }
        }
        int newcount = 0;
        String[] items = new String[count];
        if (g == "date") {
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    // infoVideo.setDataSource(listOfFiles[i].getAbsolutePath());
                    Date lastModDate = new Date(listOfFiles[i].lastModified());
                    String name = listOfFiles[i].getName();
                    System.out.println("Last modified date:" + lastModDate.toString());
                    if (!name.endsWith(".mp3")) {
                        items[newcount] = lastModDate.toString();
                        newcount++;
                    }
                }
            }
            return items;
        } else if (g == "names") {
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    //  infoVideo.setDataSource(listOfFiles[i].getAbsolutePath());
                    Date lastModDate = new Date(listOfFiles[i].lastModified());
                    System.out.println("Last modified date:" + lastModDate.toString());
                    String name = listOfFiles[i].getName();
                    if (!name.endsWith(".mp3")) {
                        items[newcount] = name;
                        newcount++;
                    }
                }
            }
            return items;
        }
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        //noinspection SimplifiableIfStatement

        if (id == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }

        return super.onOptionsItemSelected(item);
    }


    public void displayTranscript(String title, String text) throws JSONException {
        StringBuilder transcript = new StringBuilder();

        System.out.println("RECEIVE" + text);
        while (text == null) {System.out.println("RESULT" + text);}
        int index = 0;
        int end_index = 0;
        while (index != -1) {
            index = text.indexOf("\"transcript\"");
            if (index != -1) {
                text = text.substring(index + 15);
                System.out.println(text.substring(0, text.indexOf("\"")));
                transcript.append(text.substring(0, text.indexOf("\"")));
            }
        }
        text = transcript.toString();
        tRecord.put(title, text);
        System.out.println(tRecord.toString());
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("transcriptRecord", tRecord.toString()).apply();
        analyzeTone(text);
        Intent intent = new Intent(this, VideoTranscript.class);
        intent.putExtra("TRANSCRIPT",title);
        startActivity(intent);
    }

    void analyzeTone(String transcript) {
        Authenticator authenticator = new IamAuthenticator("6dWIfcCzOVS9tn1uTeIkJGJ1ueiABxQ6_5zf8gRQ-uDh");
        ToneAnalyzer ta = new ToneAnalyzer("2020-02-24", authenticator);
        ta.setServiceUrl("https://api.us-south.tone-analyzer.watson.cloud.ibm.com/instances/36bff9fe-705a-49ed-bee3-38b0a78e454d");


        ToneOptions toneOptions = new ToneOptions.Builder()
                .text(transcript)
                .build();

        ToneAnalysis toneAnalysis = ta.tone(toneOptions).execute().getResult();
        System.out.println(toneAnalysis);
    }

     void watsonSend(String videoTitle) {

        Authenticator authenticator =  new IamAuthenticator("ilPJZOJKW6OhAKjLVEnq2cQXYWjnf73vZWy1dJSUt0Am");
        SpeechToText service = new SpeechToText(authenticator);

        InputStream audio = null;

        try {
            audio = new FileInputStream(cfileName.replace(".mp4",".mp3"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        RecognizeOptions options = new RecognizeOptions.Builder()
                .audio(audio)
                .contentType(HttpMediaType.AUDIO_MP3)
                .interimResults(false)
                .build();

        service.recognizeUsingWebSocket(options, new BaseRecognizeCallback() {

            @Override
            public void onConnected() {
                super.onConnected();
                System.out.println("CONNECTED TO WATSON *_*_*_*_*_*_*_*_*__*_**_*_*_**__");
            }

            @Override
            public void onTranscription(SpeechRecognitionResults speechResults) {
                super.onTranscription(speechResults);
                if (speechResults.getResults() != null && !speechResults.getResults().isEmpty()) {
                    System.out.println(speechResults.getResults());
                    String text = speechResults.getResults().get(0).getAlternatives().get(0).getTranscript();
                    transcript = speechResults.toString();
                    System.out.println(transcript);
                    try {
                        displayTranscript(videoTitle, transcript);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

// wait 20 seconds for the asynchronous response
        try {
            Thread.sleep(5000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void dispatchTakeVideoIntent() throws IOException {
//        Intent intent = new Intent(MainActivity.this, FaceTrackerActivity.class);
//        startActivity(intent);
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        fileUri = getOutputMediaFileUri();
        System.out.println(fileUri);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            takeVideoIntent.addFlags(FLAG_GRANT_READ_URI_PERMISSION|FLAG_GRANT_WRITE_URI_PERMISSION);
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }

    }

    void createVideoFolder(){
        //finds the folder in the device where videos are normally stored
        File movieFile = new File(getExternalFilesDir(null).getAbsolutePath());
        mVideoFolder = new File(movieFile, "Video"); //creates our folder in movies direcotry in device
        //mVideoFolder = new File(getExternalFilesDir(null).getAbsolutePath() + "/Video/");
        if(!mVideoFolder.exists()){
            mVideoFolder.mkdirs(); //only creates folder if it hasnt been already created
            //dont need to create one every time we run it
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {

            Uri videoUri = getOutputMediaFileUri();
            Log.i("NativeCameraStatus",  "Captured video with uri: "+videoUri+" at "+LocalDateTime.now());

            createVideoFolder();

            if (videosExist()) {
                myDataset = namelist.toArray(new String[0]);
                Arrays.sort(myDataset, Collections.reverseOrder());
                myDate = datelist.toArray(new String[0]);
            }

            mAdapter = new MyAdapter(myDataset, myDate, this);
            mRecyclerView.setAdapter(mAdapter);
            System.out.println("hwllo");
            System.out.println("hwllo" + globName.replaceAll(".+/", ""));

            Intent intent = new Intent(MainActivity.this, CurateVideo.class);
            int position = 0;
            for (int i = 0 ; i < myDataset.length; i++) { if (myDataset[i].equals(globName)) { position = i;}}
            intent.putExtra("position", position);
            intent.putExtra("name", globName);
            startActivityForResult(intent, 2);

        }
//      STEP 2 AFTER CURATION
        if (requestCode == 2) {
            String replacer = data.getStringExtra("result");
            String name = data.getStringExtra("name");
            int position = data.getIntExtra("position", -1);
            System.out.println(data.getStringExtra("tags"));

            JSONObject jTags = null;
            try {
                jTags = new JSONObject (data.getStringExtra("tags"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                String id = (String) nRecord.get(name);
                nRecord.remove(name);
                nRecord.put(replacer, id);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            System.out.println(nRecord);

            PreferenceManager.getDefaultSharedPreferences(this).edit().putString("nameRecord", nRecord.toString()).apply();

            myDataset = namelist.toArray(new String[0]);;
            Arrays.sort(myDataset, Collections.reverseOrder());
            myDate = datelist.toArray(new String[0]);

            mAdapter.notifyDataSetChanged();

            mAdapter = new MyAdapter(myDataset, myDate, this);
            mRecyclerView.setAdapter(mAdapter);
            try {
                jRecord.remove(name);
                jRecord.put(replacer, jTags);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            System.out.println("JTAGS" + jTags);
            PreferenceManager.getDefaultSharedPreferences(this).edit().putString("tagRecord", jRecord.toString()).apply();
            PreferenceManager.getDefaultSharedPreferences(this).edit().putString("nameRecord", nRecord.toString()).apply();

            renameIt(getExternalFilesDir(null).getAbsolutePath()+"/Video/"+name, getExternalFilesDir(null).getAbsolutePath()+"/Video/"+replacer);
            System.out.println("myDataset" + Arrays.toString(myDataset));

            FFmpeg ffmpeg = FFmpeg.getInstance(this);
            try {
                //Load the binary
                ffmpeg.loadBinary(new LoadBinaryResponseHandler() {
                    @Override
                    public void onStart() {}
                    @Override
                    public void onFailure() {}
                    @Override
                    public void onSuccess() {}
                    @Override
                    public void onFinish() {}
                });
            } catch (FFmpegNotSupportedException e) {
                // Handle if FFmpeg is not supported by device
            }
            try {
                String[] command = {"-i", cfileName , "-ab", "128k", "-ac", "2", "-ar", "44100", "-vn", cfileName.replace(".mp4",".mp3")};
                ffmpeg.execute(command, new ExecuteBinaryResponseHandler() {

                    @Override
                    public void onStart() {
                        System.out.println("FFMPEG COMMAND STARTS");
                    }
                    @Override
                    public void onProgress(String message) {
                        System.out.println("FFMPEG COMMAND IN PROGRESS: " + message);
                    }
                    @Override
                    public void onFailure(String message) {
                        System.out.println("FFMPEG COMMAND FAILED" + message);
                    }
                    @Override
                    public void onSuccess(String message) {
                        Log.i("SUCCESS", message);
                    }
                    @Override
                    public void onFinish() {
                        System.out.println("HELLO");
                        watsonSend(replacer);
                    }
                });
            } catch (FFmpegCommandAlreadyRunningException e) {
                // Handle if FFmpeg is already running
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Video.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    public boolean videosExist() {
        File folder = new File(getExternalFilesDir(null).getAbsolutePath() + "/Encrypted/");
        if (folder.exists() && (folder.list().length > 0)) return true;
        else if (folder.exists() && (folder.list().length == 0)) return false;
        else folder.mkdir();
        return false;
    }

    public void deleteIt(String path) {
        File folder = new File(path + ".mp4");
        System.out.println(folder);
        jRecord.remove(path.replaceAll(".+/", ""));
        if (folder.exists())
            folder.delete();
    }

    public void renameIt(String path, String replacer) {
        File folder = new File(path);
        File folder2 = new File(replacer);
        System.out.println(path);
        System.out.println(replacer);
        if (folder.exists())
            folder.renameTo(folder2);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private File getOutputMediaFile()
    {
        LocalDateTime now = LocalDateTime.now();
        //Formatting current date time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm");
        String newFilename = now.format(formatter);
        try {
            nRecord.put(newFilename, newFilename);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        File mediaStorageDir = new File(getExternalFilesDir(null).getAbsolutePath()+"/Video/");

        if (! mediaStorageDir.exists()){
            System.out.println(mediaStorageDir);
            if (! mediaStorageDir.mkdirs()){
                Log.d("ca.imdc.newp", "failed to create directory");
                return null;
            }
        }
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                newFilename + ".mp4");
        System.out.println(mediaStorageDir.getPath() + File.separator +
                newFilename + ".mp4");
        cfileName = mediaFile.getAbsolutePath();
        globName = newFilename;
        return mediaFile;
    }

    /** Create a file Uri for saving an image or video */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private Uri getOutputMediaFileUri()
    {
        File f = getOutputMediaFile();
        System.out.println(f);
        return FileProvider.getUriForFile(
                MainActivity.this,"ca.imdc.newp.fileprovider", f);
    }
    private class GraphicFaceTrackerFactory
            implements MultiProcessor.Factory<Face> {
        @Override
        public Tracker<Face> create(Face face) {
            return new GraphicFaceTracker(mGraphicOverlay);
        }
    }
    private class GraphicFaceTracker extends Tracker<Face> {
        // other stuff
        private GraphicOverlay mOverlay;
        private FaceGraphic mFaceGraphic;

        GraphicFaceTracker(GraphicOverlay overlay) {
            mOverlay = overlay;
            mFaceGraphic = new FaceGraphic(overlay);
        }

        @Override
        public void onNewItem(int faceId, Face face) {
            mFaceGraphic.setId(faceId);
        }

        @Override
        public void onUpdate(FaceDetector.Detections<Face> detectionResults,
                             Face face) {
            mOverlay.add(mFaceGraphic);
            mFaceGraphic.updateFace(face);
        }

        @Override
        public void onMissing(FaceDetector.Detections<Face> detectionResults) {
            mOverlay.remove(mFaceGraphic);
        }

        @Override
        public void onDone() {
            mOverlay.remove(mFaceGraphic);
        }
    }


    //=====================================DIALOGS==================================================
    //==============================================================================================

    public static class dialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstance) {

            // Use the Builder class for convenient dialog construction

            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            builder.setTitle("Who are you recording?")
                    .setPositiveButton("SELF", new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        public void onClick(DialogInterface dialog, int id) {
                            clicked=true;
                            try {
                                ((MainActivity)getActivity()).dispatchTakeVideoIntent();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            mAdapter.notifyDataSetChanged();
                        }
                    })
                    .setNegativeButton("EXISTING", new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        public void onClick(DialogInterface dialogInterface, int i) {
                            try {
                                ((MainActivity)getActivity()).dispatchTakeVideoIntent();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    })
                    .setNeutralButton("OTHER", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            MainActivity.isOther = true;
                            clicked= false;
                            dialog2 myAlert2 = new dialog2();
                            myAlert2.show(getFragmentManager(), "dialog2");
                            // User cancelled the dialog
                        }
                    });
            return builder.create();
        }
    }

    public static class dialog2 extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstance) {

            // Use the Builder class for convenient dialog construction
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            builder.setView(inflater.inflate(R.layout.dialog_text, null))
                    .setTitle("Consent")
                    .setPositiveButton("Disagree", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // FIRE ZE MISSILES!
                        }
                    })
                    .setNeutralButton("Agree", new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        public void onClick(DialogInterface dialog, int id) {

                            try {
                                ((MainActivity)getActivity()).dispatchTakeVideoIntent();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            mAdapter.notifyDataSetChanged();
                            // User cancelled the dialog
                        }
                    });
            return builder.create();
        }
    }

    //==============================================================================================
    //==============================================================================================
    //==============================================================================================
    //==============================================================================================
    //==============================================================================================
    //==============================================================================================
    //==============================================================================================
    //==============================================================================================
    //==============================================================================================
    //==============================================================================================
    //==============================================================================================
    //==============================================================================================
    //==============================================================================================
    //==============================================================================================

    public static class dialog4 extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstance) {

            // Use the Builder class for convenient dialog construction
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            builder.setView(inflater.inflate(R.layout.share_dialog, null));

            return builder.create();
        }
    }




}
