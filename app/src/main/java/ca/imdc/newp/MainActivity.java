package ca.imdc.newp;
import android.Manifest;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.Context;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ProviderInfo;
import android.content.res.Configuration;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.graphics.Camera;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.CamcorderProfile;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.VideoView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.ibm.watson.developer_cloud.service.security.IamOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
import static com.google.android.gms.common.util.IOUtils.copyStream;
import static java.security.AccessController.getContext;

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

    protected Uri videoUri;
    private VideoView videoView;
    private Uri fileUri;
    private String videoFilePath;
    public Uri videoURI;
    public FileOutputStream fos;

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


        IamOptions options = new IamOptions.Builder()
                .apiKey("{VRHKcB5jKWLzBdIFjk6D6wQDYRPQhAUZzYGzOPOukj_m}")
                .build();
        VisualRecognition visualRecognition = new VisualRecognition("{1.1.1}", options);

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


    /** Create a file Uri for saving an image or video */
//    private static Uri getOutputMediaFileUri(int type){
//        return Uri.fromFile(getOutputMediaFile(type));
//    }
//
//    /** Create a File for saving an image or video */
//    private static File getOutputMediaFile(int type){
//        // To be safe, you should check that the SDCard is mounted
//        // using Environment.getExternalStorageState() before doing this.
//
//        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_PICTURES), "MHMR_videos");
//        // This location works best if you want the created images to be shared
//        // between applications and persist after your app has been uninstalled.
//
//        // Create the storage directory if it does not exist
//        if (! mediaStorageDir.exists()){
//            if (! mediaStorageDir.mkdirs()){
//                Log.d("MHMR", "failed to create directory");
//                return null;
//            }
//        }
//
//        // Create a media file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        File mediaFile;
//        if (type == MEDIA_TYPE_IMAGE){
//            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
//                    "IMG_"+ timeStamp + ".jpg");
//        } else if(type == MEDIA_TYPE_VIDEO) {
//            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
//                    "VID_"+ timeStamp + ".mp4");
//        } else {
//            return null;
//        }
//
//        return mediaFile;
//    }

    private File createVideoFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "VID_" + timeStamp + "_";
        File storageDir =
                getExternalFilesDir(Environment.DIRECTORY_DCIM);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".gp3",         /* suffix */
                storageDir      /* directory */
        );

        videoFilePath = image.getAbsolutePath();
        return image;
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

    private void loadVideoFromInternalStorage(String filePath){

        Uri uri = Uri.parse(Environment.getExternalStorageDirectory()+filePath);
        videoView.setVideoURI(uri);

    }
    public void dispatchTakeVideoIntent() throws IOException {
        //startActivity(new Intent(MainActivity.this, CameraApi.class));
        //int a;
        //Random random = new Random();
        //a = random.nextInt(70) + 1;


        //CameraApi.createVideoFolder();
//        String extStorage = Environment.getExternalStorageState();
//        mencVideoFolder = new File(extStorage + "/data/app/ca.imdc.newp-2");
        MainActivity mainActivity = new MainActivity();


        //createEncVideoFileName();
        //System.out.println(mencVideoFolder + "--------------------------------------------------->>>>>>>>>>>>");
        //File newVideo = new File(mVideoFolder, encfileName);
        Intent openCameraIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(openCameraIntent,REQUEST_VIDEO_CAPTURE);
        openCameraIntent.addFlags(FLAG_GRANT_READ_URI_PERMISSION|FLAG_GRANT_WRITE_URI_PERMISSION);


        String fName = "VideoFileName.mp4";
        File f = new File(fName);
        openCameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));

        //openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        //Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
       // takeVideoIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        openCameraIntent.setFlags(FLAG_GRANT_READ_URI_PERMISSION);
        openCameraIntent.setFlags(FLAG_GRANT_WRITE_URI_PERMISSION);

        //File outputFile = new File("/data/app/ca.imdc.newp-1");
        //videoUri = Uri.fromFile(outputFile);
        //takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile((new File(cfileName))));


        //openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,file);
        //if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
        if (openCameraIntent.resolveActivity(getPackageManager()) != null) {
            //startActivityForResult(openCameraIntent, REQUEST_VIDEO_CAPTURE);

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       super.onActivityResult(requestCode, resultCode, data);
        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File (sdCard.getAbsolutePath() + "/Android/data/app/ca.imdc.newp-2");
        dir.mkdirs();
        File file = new File(dir, "filename");
        //String videoUri1 = videoUri.getPath();
        //Intent video = new Intent(MainActivity.this, viewVideo.class);
        Bundle bundle = new Bundle();
        //video.putExtras(bundle);
        //String test = (String) data.getExtras().get("data");
        //Uri test = data.getData();
        //String test = videoURI.getPath();
//        bundle.putParcelable("uri", test);
//        bundle.putString("uri", test);
//        video.setData(test);
       // video.putExtra("uri", test);
        // -------------------------




        Uri contentUri = data.getData();



        //video.putExtra("uri",contentUri);

        //startActivity(video);

        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK && contentUri != null) {
            // do what you want with videoUri
            System.out.println("------------------------------------------------------------------------------------------------------------------");
            Intent videoIntent = new Intent(getApplicationContext(), viewVideo.class);
            //videoIntent.setAction(Intent.ACTION_SEND);

            videoIntent.setData(contentUri);

            getBaseContext().getApplicationContext().startActivity(videoIntent);


        }


        try {
            Date date= new Date();
            android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", date);
            String Video_DIRECTORY = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + getString(R.string.app_name) + "/video/";
            File storeDirectory = new File(Video_DIRECTORY);

            try {
                if (storeDirectory.exists() == false) {
                    storeDirectory.mkdirs();
                }


            } catch (Exception e) {
                e.printStackTrace();

            }
            File storeDirectory12 = new File(storeDirectory,date+".mp3");
            InputStream inputStream = getContentResolver().openInputStream(contentUri);
            FileOutputStream fileOutputStream = new FileOutputStream(storeDirectory12);
            copyStream(inputStream, fileOutputStream);
            fileOutputStream.close();
            inputStream.close();
        } catch (FileNotFoundException e) {
            Log.e("Exception", "" + e);
        } catch (IOException e) {
            Log.e("Exception", "" + e);
        }


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
            } catch (JSONException    e) {
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

            String email = getIntent().getStringExtra("account");
            String firstname = getIntent().getStringExtra("name");
            String ownerid = getIntent().getStringExtra("id");

            parseAndSend(new RegisterActivity.VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    System.out.println("SENT JTAGS BACK TO DB");
                }
            }, email,firstname,jTags, ownerid);


            PreferenceManager.getDefaultSharedPreferences(this).edit().putString("tagRecord", jRecord.toString()).apply();
////
            renameIt(getExternalFilesDir(null).getAbsolutePath()+"/Encrypted/"+name,  getExternalFilesDir(null).getAbsolutePath()+"/Encrypted/"+replacer);
            renameIt(getExternalFilesDir(null).getAbsolutePath()+"/Video/"+name.replace(".encrypt",""), getExternalFilesDir(null).getAbsolutePath()+"/Video/"+replacer.replace(".encrypt",""));
            System.out.println("myDataset" + Arrays.toString(myDataset));
        }

        //if (requestCode == REQUEST_TAKE_GALLERY_VIDEO) {
        if(resultCode == RESULT_OK){
            //Uri selectedImageUri = data.getData();
            Uri selectedImageUri = contentUri; // i changed this to test a fix for null poiner exception -Manuel
            //String path  = getPath(selectedImageUri);
            String path = contentUri.toString();
            try {

                FileInputStream fis;
                fis = new FileInputStream(new File(path));

                System.out.println(path);

                //this is where you set whatever path you want to save it as:

                File tmpFile = new File("/storage/emulated/0/Android/data/ca.imdc.newp/files/Encrypted/","VideoFile.mp4");
                tmpFile.canWrite();
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
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
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
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
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
