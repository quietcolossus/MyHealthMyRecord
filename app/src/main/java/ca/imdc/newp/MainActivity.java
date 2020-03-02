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
import android.os.Bundle;
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

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.ibm.cloud.sdk.core.http.HttpMediaType;
import com.ibm.cloud.sdk.core.security.Authenticator;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.developer_cloud.android.library.audio.MicrophoneInputStream;

import com.ibm.watson.speech_to_text.v1.SpeechToText;
import com.ibm.watson.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.speech_to_text.v1.model.SpeechRecognitionResults;
import com.ibm.watson.speech_to_text.v1.websocket.BaseRecognizeCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;

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

    public JSONObject jTags;

    public static boolean clicked=false;
    public static boolean isOther = false;
    protected static String encfileName;

    protected static File mencVideoFolder;


    public boolean videosExist;
    private File mVideoFolder;

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    public static final String EXTRA_MESSAGE = "ca.imdc.newp.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        String obj = PreferenceManager.getDefaultSharedPreferences(this).getString("tagRecord", null);



        if (obj == null) { obj = ""; };
        try {
            jRecord = new JSONObject(obj);
            System.out.println(jTags);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        System.out.println(obj);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setLogo(R.drawable.logo);
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


        // specify an adapter (see also next example)
        if (videosExist()) {

            myDataset = populateList("names");
            myDate = populateList("date");
        }
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

                        if (id == R.id.nav_share) {
                            Intent shareIntent = new Intent(MainActivity.this, shareCircleActivity.class);
                            startActivity(shareIntent);
                        }
                    else if (id == R.id.nav_myv) {

                    } else if (id == R.id.nav_settings) {

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

    String[] populateList(String g) {
        MediaMetadataRetriever infoVideo = new MediaMetadataRetriever();
        File folder = new File(getExternalFilesDir(null).getAbsolutePath() + "/Encrypted/");
        File[] listOfFiles = folder.listFiles();
        String[] items = new String[listOfFiles.length];
        if (g == "date") {
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    // infoVideo.setDataSource(listOfFiles[i].getAbsolutePath());
                    Date lastModDate = new Date(listOfFiles[i].lastModified());
                    System.out.println("Last modified date:" + lastModDate.toString());
                    items[i] = lastModDate.toString();
                }
            }
            return items;
        } else if (g == "names") {
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    //  infoVideo.setDataSource(listOfFiles[i].getAbsolutePath());
                    Date lastModDate = new Date(listOfFiles[i].lastModified());
                    System.out.println("Last modified date:" + lastModDate.toString());
                    items[i] = listOfFiles[i].getName();
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement

        if (id == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }

        return super.onOptionsItemSelected(item);
    }

    public void dispatchUploadVideoIntent() {

        Intent intent = new Intent();
        intent.setType("video/");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Video"),REQUEST_TAKE_GALLERY_VIDEO);
    }

    private void saveVideoToInternalStorage (String filePath) {

        File newfile;

        try {

            File currentFile = new File(filePath);
            String fileName = currentFile.getName();

            ContextWrapper cw = new ContextWrapper(getApplicationContext());
            File directory = cw.getDir("videoDir", Context.MODE_PRIVATE);


            newfile = new File(directory, fileName+".mp3");

            if(currentFile.exists()){

                InputStream in = new FileInputStream(currentFile);
                OutputStream out = new FileOutputStream(newfile);

                // Copy the bits from instream to outstream
                byte[] buf = new byte[1024];
                int len;

                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }

                in.close();
                out.close();

                Log.v("", "Video file saved successfully.");

            }else{
                Log.v("", "Video saving failed. Source file missing.");
            }



        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void displayTranscript(String text){
        String transcript = text;
        Intent intent = new Intent(this, DisplayActivity.class);
        intent.putExtra("TRANSCRIPT",transcript);
        startActivity(intent);
    }

     void watsonSend() {
        // START OF WATSON TEST ------------------------------------------
        Authenticator authenticator =  new IamAuthenticator("ilPJZOJKW6OhAKjLVEnq2cQXYWjnf73vZWy1dJSUt0Am");
        SpeechToText service = new SpeechToText(authenticator);

        InputStream audio = null;
        try {
            audio = new FileInputStream(CameraApi.cfileName.replace(".mp4",".mp3"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        RecognizeOptions options = new RecognizeOptions.Builder()
                .audio(audio)
                .contentType(HttpMediaType.AUDIO_MP3)
                .interimResults(true)
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
                System.out.println(speechResults);
                if (speechResults.getResults() != null && !speechResults.getResults().isEmpty()) {
                    String text = speechResults.getResults().get(0).getAlternatives().get(0).getTranscript();
                    displayTranscript(speechResults.toString());
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




    public void dispatchTakeVideoIntent() throws IOException {
        //startActivity(new Intent(MainActivity.this, CameraApi.class));
        //int a;
        //Random random = new Random();
        //a = random.nextInt(70) + 1;
        Intent openCameraIntent = new Intent(MainActivity.this, CameraApi.class);

        //CameraApi.createVideoFolder();
//        String extStorage = Environment.getExternalStorageState();
//        mencVideoFolder = new File(extStorage + "/data/app/ca.imdc.newp-2");
        MainActivity mainActivity = new MainActivity();




        //if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
        if (openCameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(openCameraIntent, REQUEST_VIDEO_CAPTURE);

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        //super.onActivityResult(requestCode, resultCode, data);
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




//            HashMap<String, ArrayList<Object>> tags = (HashMap<String, ArrayList<Object>>) data.getSerializableExtra("tags");

            ArrayList<String> list = new ArrayList<>(Arrays.asList(myDataset));
            if (position != -1) {
                list.set(position, replacer);
            } else {
                System.out.println("Hello");
            }

            myDataset = list.toArray(new String[list.size()]);
            mAdapter.notifyDataSetChanged();

            mAdapter = new MyAdapter(myDataset, myDate, this);
            mRecyclerView.setAdapter(mAdapter);
            try {
                jRecord.remove(name);
                jRecord.put(replacer, jTags);
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            for (String keyName: tagRecord.keySet()){
//                String key = keyName;
//                HashMap<String, ArrayList<Object>> value = tagRecord.get(keyName);
//                System.out.println(key + " " + value);
//            }

//            JSONObject obj = new JSONObject(tagRecord);
//            System.out.println(obj);
            System.out.println("JTAGS" + jTags);
            PreferenceManager.getDefaultSharedPreferences(this).edit().putString("tagRecord", jRecord.toString()).apply();
////
            renameIt(getExternalFilesDir(null).getAbsolutePath()+"/Encrypted/"+name,  getExternalFilesDir(null).getAbsolutePath()+"/Encrypted/"+replacer);
            renameIt(getExternalFilesDir(null).getAbsolutePath()+"/Video/"+name.replace(".encrypt",""), getExternalFilesDir(null).getAbsolutePath()+"/Video/"+replacer.replace(".encrypt",""));
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
                // to execute "ffmpeg -version" command you just need to pass "-version"
                // Now, you can execute your command here

                String[] command = {"-i", CameraApi.cfileName , "-ab", "128k", "-ac", "2", "-ar", "44100", "-vn", CameraApi.cfileName.replace(".mp4",".mp3")};
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
                        watsonSend();


                    }
                });
            } catch (FFmpegCommandAlreadyRunningException e) {
                // Handle if FFmpeg is already running
            }
        }


        if (requestCode == REQUEST_TAKE_GALLERY_VIDEO) {
            Uri selectedImageUri = data.getData();
            String path  = getPath(selectedImageUri);
            try {

                FileInputStream fis;
                fis = new FileInputStream(new File(path));

                System.out.println(path);

                //this is where you set whatever path you want to save it as:

                File tmpFile = new File("/storage/emulated/0/Android/data/ca.imdc.newp/files/Encrypted/","VideoFile.mp4");

                //save the video to the File path
                FileOutputStream fos = new FileOutputStream(tmpFile);

                byte[] buf = new byte[1024];
                int len;
                while ((len = fis.read(buf)) >= 0) {
                    fos.write(buf, 0, len);
                }
                fis.close();
                fos.close();
            } catch (IOException io_e) {
                // TODO: handle error
            }
            if (videosExist()) {
                myDataset = populateList("names");
                System.out.println("myDataset" + Arrays.toString(myDataset));
                myDate = populateList("date");
                System.out.println("myDate" + Arrays.toString(myDate));
            }
            mAdapter = new MyAdapter(myDataset, myDate, this);
            mRecyclerView.setAdapter(mAdapter);
        }

        if (requestCode == REQUEST_VIDEO_CAPTURE) {

            if (resultCode == RESULT_CANCELED) {
                crypto();

                if (videosExist()) {
                    myDataset = populateList("names");
                    System.out.println("myDataset" + Arrays.toString(myDataset));
                    myDate = populateList("date");
                    System.out.println("nyDate" + Arrays.toString(myDate));
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
                //deleteAllVids();
            }
            if (CameraApi.isAnother == 1) {
                CameraApi.isAnother = 0;

                if (clicked == false && isOther == true){
                    clicked = true;
                    isOther = false;
                }
                else if(clicked == false && isOther == false)
                {
                    //do nothing
                }

                try {
                    dispatchTakeVideoIntent();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
//
        }
    }
    private void createEncVideoFileName() throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String prepend = "VIDEO_ENC_" + timeStamp + "_";
        //File  encvideoFile = File.createTempFile(prepend,"ENC.mp4.encrypt",mencVideoFolder); //creates the encrypted file
        //encfileName = encvideoFile.getAbsolutePath(); //name of file name is stored
        encfileName = mencVideoFolder.getAbsolutePath()+ "/" + prepend + "ENC.mp4";

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

    /*@Override
    public void onConfigurationChanged (Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        System.out.println("IN CONFIG IN MAIN");
    }*/

    private void crypto() {
        final String key = "1111111111111111";
        final cryptoHash halo = new cryptoHash();
        try {
            String name = createfile("encrypt");
            globName = name.replaceAll(".+/", "");
            System.out.println("ENCRYPTED NAME:::::::::::::::: " + name);
            final File encryptedFile = new File(name);
            final File inputFile = new File(CameraApi.cfileName);
            t = new Thread(new Runnable() {
                public void run() {
                    try {
                        halo.encrypt(key, inputFile, encryptedFile);
                    } catch (CryptoException e) {
                        e.printStackTrace();
                    }
                }
            });
            t.start();
            t.join();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteAllVids() {
        File folder = new File(getExternalFilesDir(null).getAbsolutePath() + "/Video/");
        //File folder = new File(getExternalFilesDir(null).getAbsolutePath() + "/Videos/CameraApiVideos");
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                System.out.println("File " + listOfFiles[i].getName());
                listOfFiles[i].delete();
                System.out.println("Deleted: " + listOfFiles[i].getName()); //its deleting them but its
                // deleting the last videos not the video your are currently taking
            }
        }
    }

    public boolean videosExist() {
        File folder = new File(getExternalFilesDir(null).getAbsolutePath() + "/Encrypted/");
        if (folder.exists() && (folder.list().length > 0)) return true;
        else if (folder.exists() && (folder.list().length == 0)) return false;
        else folder.mkdir();
        return false;
    }

    public void deleteIt(String path) {
        File folder = new File(path);
        jRecord.remove(path.replaceAll(".+/", ""));
        if (folder.exists())
            folder.delete();
    }

    public void renameIt(String path, String replacer) {
        File folder = new File(path);
        File folder2 = new File(replacer);
        if (folder.exists())
            folder.renameTo(folder2);
    }

    public String decrypt(String name) {
        final String key = "1111111111111111";
        final cryptoHash halo = new cryptoHash();
        String a = name.replaceAll(".encrypt", "");

        final File decr = new File("/storage/emulated/0/Android/data/ca.imdc.newp/files/Video/" + a);
        System.out.println("File Path decr: " + decr.getAbsolutePath());
        try {
            decr.createNewFile();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        final File encryptedFile = new File("/storage/emulated/0/Android/data/ca.imdc.newp/files/Encrypted/" + name);
        System.out.println("File Path decr: " + encryptedFile.getAbsolutePath());
        try {
            t = new Thread(new Runnable() {
                public void run() {
                    try {
                        halo.decrypt(key, encryptedFile, decr);
                    } catch (CryptoException e) {
                        e.printStackTrace();
                    }
                }
            });
            t.start();
            t.join();
        } catch (Exception c) {
            System.out.println(c.getMessage());
        }
        return decr.getAbsolutePath();
    }

    public void halo(String ubc) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ubc));
        intent.addFlags(FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(Uri.parse(ubc), "video/mp4");
        startActivity(intent);
    }

    private String createfile(String type) {
        String fName = null;
        if (type == "decrypt") {
            fName = CameraApi.encfileName;
        } else if (type == "encrypt") {
            fName = CameraApi.encfileName + ".encrypt";
        } else
            return fName;
        File file = new File(fName);
        try {

            file.createNewFile();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return fName;
    }




    public class dialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstance) {

            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            builder.setTitle("Who are you recording?")
                    .setPositiveButton("SELF", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            clicked=true;

                            try {
                                dispatchTakeVideoIntent();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            mAdapter.notifyDataSetChanged();
                        }
                    })
                    .setNegativeButton("EXISTING", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dispatchUploadVideoIntent();

                        }
                    })
                    .setNeutralButton("OTHER", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //MainActivity.isOther = true;
                            clicked= false;
                            dialog2 myAlert2 = new dialog2();
                            myAlert2.show(getFragmentManager(), "dialog2");
                            // User cancelled the dialog
                        }
                    });
            return builder.create();
        }
    }

    public class dialog2 extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstance) {

            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            builder.setView(inflater.inflate(R.layout.dialog_text, null))
                    .setTitle("Consent")
                    .setPositiveButton("Disagree", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // FIRE ZE MISSILES!
                        }
                    })
                    .setNeutralButton("Agree", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            try {
                                dispatchTakeVideoIntent();
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
    public static class dialog4 extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstance) {

            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            builder.setView(inflater.inflate(R.layout.share_dialog, null));

            return builder.create();
        }
    }

    public int parseAndSend(final RegisterActivity.VolleyCallback callback, String gmail, String name, JSONObject tags, String ownerid){

        final String googleEmail = gmail;
        final String googleName = name;
        final String URL = "http://141.117.145.178:3000/videos";
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        final JSONObject jtags = tags;
        final String ownerid1 = ownerid;

        // Video Tag JSONObject Positions and their data:
        // 1->Arousal  2->Valence 3->Location 4->Activity 5->Sharing


        new Thread(new Runnable() {
            public void run() {
                try {
                    StringRequest postRequest = new StringRequest(Request.Method.POST, URL,
                            new Response.Listener<String>()
                            {
                                @Override
                                public void onResponse(String response) {
                                    // response
                                    Log.d("Response", response);
                                    callback.onSuccess(response);
                                }
                            },
                            new Response.ErrorListener()
                            {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // error
                                }
                            }
                    ) {
                        @Override
                        protected Map<String, String> getParams()
                        {
                            final HashMap<String, String> params = new HashMap<String, String>();
                            // Order to send the params in is: ownername, owneremail, videoid, videodata, valence, arousal, location, activity, sharing, ownerid

                            params.put("ownername", googleName);
                            params.put("owneremail", googleEmail);
                            params.put("placeholder-video-id", "placeholder");
                            params.put("placeholder-video-data", "placeholder");
                            try {
                                params.put("valence", String.valueOf(jtags.getJSONObject("Valence")));
                                params.put("arousal", String.valueOf(jtags.getJSONObject("Arousal")));
                                params.put("location", String.valueOf(jtags.getJSONObject("Location")));
                                params.put("activity", String.valueOf(jtags.getJSONObject("Activity")));
                                params.put("sharing", String.valueOf(jtags.getJSONObject("Sharing")));
                                params.put("ownerid", ownerid1);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            return params;
                        }
                    };

                    requestQueue.add(postRequest);


                } catch ( Exception e ) {
                    System.err.println( e.getClass().getName()+": "+ e.getMessage() );
                }

            }


        }).start();

        return 0;
    }

}
