package ca.imdc.newp;

import android.content.ClipData;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.DragEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.google.android.material.navigation.NavigationView;

public class VideoPlay extends AppCompatActivity {

    ProgressBar mProgressBar;
    VideoView mVideoView;

    ImageButton happy_drag;
    ImageButton neutral_drag;
    ImageButton sad_drag;
    ImageButton angry_drag;
    ImageButton surprise_drag;
    ImageButton disgust_drag;
    ImageButton fear_drag;

    ImageButton happy_label;
    ImageButton neutral_label;
    ImageButton sad_label;
    ImageButton angry_label;
    ImageButton surprise_label;
    ImageButton disgust_label;
    ImageButton fear_label;
    String msg;
    private DrawerLayout mDrawerLayout;
    private android.widget.LinearLayout.LayoutParams layoutParams;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        //noinspection SimplifiableIfStatement

        if (id == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_new);
        String path = Environment.getExternalStorageDirectory().toString();
        //String filename = "/hr.3gp";

        Intent intent = getIntent();
        String video_name = intent.getStringExtra("filename");
        String prev = intent.getStringExtra("prev");
        int rawId = getResources().getIdentifier(video_name,  "raw", getPackageName());
        mProgressBar = (ProgressBar) findViewById(R.id.Progressbar);
        mProgressBar.setProgress(0);
        mProgressBar.setMax(100);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Drag to add / click to remove.");

        mVideoView = (VideoView) findViewById(R.id.my_Video_View);
        mVideoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + rawId));
        MyAsync task = new MyAsync();
        task.execute();


        neutral_drag=(ImageButton)findViewById(R.id.neutral_drag);
        fear_drag=(ImageButton)findViewById(R.id.fear_drag);
        happy_drag=(ImageButton)findViewById(R.id.happy_drag);
        sad_drag=(ImageButton)findViewById(R.id.sad_drag);
        surprise_drag=(ImageButton)findViewById(R.id.surprise_drag);
        disgust_drag=(ImageButton)findViewById(R.id.disgust_drag);
        angry_drag=(ImageButton)findViewById(R.id.angry_drag);

        neutral_label=(ImageButton)findViewById(R.id.neutral_label);
        fear_label=(ImageButton)findViewById(R.id.fear_label);
        happy_label=(ImageButton)findViewById(R.id.happy_label);
        sad_label=(ImageButton)findViewById(R.id.sad_label);
        surprise_label=(ImageButton)findViewById(R.id.surprise_label);
        disgust_label=(ImageButton)findViewById(R.id.disgust_label);
        angry_label=(ImageButton)findViewById(R.id.angry_label);

        neutral_label.setVisibility(View.GONE);
        fear_label.setVisibility(View.GONE);
        happy_label.setVisibility(View.GONE);
        sad_label.setVisibility(View.GONE);
        surprise_label.setVisibility(View.GONE);
        disgust_label.setVisibility(View.GONE);
        angry_label.setVisibility(View.GONE);

        setSupportActionBar(toolbar);

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
                        task.cancel(true);
                        if (id == R.id.nav_myvideos) {

                            Intent mainIntent = new Intent(VideoPlay.this, MainActivity.class);
                            startActivity(mainIntent);
                        }
                        else if (id == R.id.nav_share) {
                            Intent shareIntent = new Intent(VideoPlay.this, shareCircleActivity.class);
                            startActivity(shareIntent);
                        }
                        else if (id == R.id.nav_myv) {
                            Intent dataIntent = new Intent(VideoPlay.this, WordCloudActivity.class);
                            startActivity(dataIntent);

                        } else if (id == R.id.nav_settings) {

                        }
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    }
                });

        neutral_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                neutral_label.setVisibility(View.GONE);
            }
        });
        fear_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fear_label.setVisibility(View.GONE);
            }
        });
        happy_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                happy_label.setVisibility(View.GONE);
            }
        });
        sad_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sad_label.setVisibility(View.GONE);
            }
        });
        surprise_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                surprise_label.setVisibility(View.GONE);
            }
        });
        disgust_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disgust_label.setVisibility(View.GONE);
            }
        });
        angry_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                angry_label.setVisibility(View.GONE);
            }
        });


        neutral_drag.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                switch(event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        layoutParams = (LinearLayout.LayoutParams)v.getLayoutParams();
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_STARTED");

                        // Do nothing
                        break;

                    case DragEvent.ACTION_DRAG_ENTERED:
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_ENTERED");
                        int x_cord = (int) event.getX();
                        int y_cord = (int) event.getY();
                        break;

                    case DragEvent.ACTION_DRAG_EXITED :
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_EXITED");
                        x_cord = (int) event.getX();
                        y_cord = (int) event.getY();
                        layoutParams.leftMargin = x_cord;
                        layoutParams.topMargin = y_cord;
                        if (v == neutral_drag) {
                            neutral_label.setVisibility(View.VISIBLE); }
                        if (v == angry_drag) {
                            angry_label.setVisibility(View.VISIBLE); }
                        if (v == sad_drag) {
                            sad_label.setVisibility(View.VISIBLE); }
                        if (v == happy_drag) {
                            happy_label.setVisibility(View.VISIBLE); }
                        if (v == disgust_drag) {
                            disgust_label.setVisibility(View.VISIBLE); }
                        if (v == fear_drag) {
                            fear_label.setVisibility(View.VISIBLE); }
                        if (v == surprise_drag) {
                            surprise_label.setVisibility(View.VISIBLE); }
                        System.out.println(mProgressBar.getProgress());
                        v.setLayoutParams(layoutParams);
                        break;

                    case DragEvent.ACTION_DRAG_LOCATION  :
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_LOCATION");
                        x_cord = (int) event.getX();
                        y_cord = (int) event.getY();
                        break;

                    case DragEvent.ACTION_DRAG_ENDED   :
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_ENDED");

                        // Do nothing
                        break;

                    case DragEvent.ACTION_DROP:
                        Log.d(msg, "ACTION_DROP event");
                        // Do nothing
                        break;
                    default: break;
                }
                return true;
            }
        });

        neutral_drag.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ClipData data = ClipData.newPlainText("", "");
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(neutral_drag);

                    neutral_drag.startDrag(data, shadowBuilder, neutral_drag, 0);
                    return true;
                } else {
                    return false;
                }
            }
        });
        angry_drag.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                switch(event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        layoutParams = (LinearLayout.LayoutParams)v.getLayoutParams();
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_STARTED");

                        // Do nothing
                        break;

                    case DragEvent.ACTION_DRAG_ENTERED:
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_ENTERED");
                        int x_cord = (int) event.getX();
                        int y_cord = (int) event.getY();
                        break;

                    case DragEvent.ACTION_DRAG_EXITED :
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_EXITED");
                        x_cord = (int) event.getX();
                        y_cord = (int) event.getY();
                        layoutParams.leftMargin = x_cord;
                        layoutParams.topMargin = y_cord;
                        if (v == neutral_drag) {
                            neutral_label.setVisibility(View.VISIBLE); }
                        if (v == angry_drag) {
                            angry_label.setVisibility(View.VISIBLE); }
                        if (v == sad_drag) {
                            sad_label.setVisibility(View.VISIBLE); }
                        if (v == happy_drag) {
                            happy_label.setVisibility(View.VISIBLE); }
                        if (v == disgust_drag) {
                            disgust_label.setVisibility(View.VISIBLE); }
                        if (v == fear_drag) {
                            fear_label.setVisibility(View.VISIBLE); }
                        if (v == surprise_drag) {
                            surprise_label.setVisibility(View.VISIBLE); }
                        System.out.println(mProgressBar.getProgress());
                        v.setLayoutParams(layoutParams);
                        break;

                    case DragEvent.ACTION_DRAG_LOCATION  :
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_LOCATION");
                        x_cord = (int) event.getX();
                        y_cord = (int) event.getY();
                        break;

                    case DragEvent.ACTION_DRAG_ENDED   :
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_ENDED");

                        // Do nothing
                        break;

                    case DragEvent.ACTION_DROP:
                        Log.d(msg, "ACTION_DROP event");
                        // Do nothing
                        break;
                    default: break;
                }
                return true;
            }
        });

        angry_drag.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ClipData data = ClipData.newPlainText("", "");
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(angry_drag);

                    angry_drag.startDrag(data, shadowBuilder, angry_drag, 0);
                    return true;
                } else {
                    return false;
                }
            }
        });
        happy_drag.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                switch(event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        layoutParams = (LinearLayout.LayoutParams)v.getLayoutParams();
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_STARTED");

                        // Do nothing
                        break;

                    case DragEvent.ACTION_DRAG_ENTERED:
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_ENTERED");
                        int x_cord = (int) event.getX();
                        int y_cord = (int) event.getY();
                        break;

                    case DragEvent.ACTION_DRAG_EXITED :
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_EXITED");
                        x_cord = (int) event.getX();
                        y_cord = (int) event.getY();
                        layoutParams.leftMargin = x_cord;
                        layoutParams.topMargin = y_cord;
                        if (v == neutral_drag) {
                            neutral_label.setVisibility(View.VISIBLE); }
                        if (v == angry_drag) {
                            angry_label.setVisibility(View.VISIBLE); }
                        if (v == sad_drag) {
                            sad_label.setVisibility(View.VISIBLE); }
                        if (v == happy_drag) {
                            happy_label.setVisibility(View.VISIBLE); }
                        if (v == disgust_drag) {
                            disgust_label.setVisibility(View.VISIBLE); }
                        if (v == fear_drag) {
                            fear_label.setVisibility(View.VISIBLE); }
                        if (v == surprise_drag) {
                            surprise_label.setVisibility(View.VISIBLE); }
                        System.out.println(mProgressBar.getProgress());
                        v.setLayoutParams(layoutParams);
                        break;

                    case DragEvent.ACTION_DRAG_LOCATION  :
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_LOCATION");
                        x_cord = (int) event.getX();
                        y_cord = (int) event.getY();
                        break;

                    case DragEvent.ACTION_DRAG_ENDED   :
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_ENDED");

                        // Do nothing
                        break;

                    case DragEvent.ACTION_DROP:
                        Log.d(msg, "ACTION_DROP event");
                        // Do nothing
                        break;
                    default: break;
                }
                return true;
            }
        });

        happy_drag.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ClipData data = ClipData.newPlainText("", "");
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(happy_drag);

                    happy_drag.startDrag(data, shadowBuilder, happy_drag, 0);
                    return true;
                } else {
                    return false;
                }
            }
        });
        sad_drag.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                switch(event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        layoutParams = (LinearLayout.LayoutParams)v.getLayoutParams();
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_STARTED");

                        // Do nothing
                        break;

                    case DragEvent.ACTION_DRAG_ENTERED:
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_ENTERED");
                        int x_cord = (int) event.getX();
                        int y_cord = (int) event.getY();
                        break;

                    case DragEvent.ACTION_DRAG_EXITED :
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_EXITED");
                        x_cord = (int) event.getX();
                        y_cord = (int) event.getY();
                        layoutParams.leftMargin = x_cord;
                        layoutParams.topMargin = y_cord;
                        if (v == neutral_drag) {
                            neutral_label.setVisibility(View.VISIBLE); }
                        if (v == angry_drag) {
                            angry_label.setVisibility(View.VISIBLE); }
                        if (v == sad_drag) {
                            sad_label.setVisibility(View.VISIBLE); }
                        if (v == happy_drag) {
                            happy_label.setVisibility(View.VISIBLE); }
                        if (v == disgust_drag) {
                            disgust_label.setVisibility(View.VISIBLE); }
                        if (v == fear_drag) {
                            fear_label.setVisibility(View.VISIBLE); }
                        if (v == surprise_drag) {
                            surprise_label.setVisibility(View.VISIBLE); }
                        System.out.println(mProgressBar.getProgress());
                        v.setLayoutParams(layoutParams);
                        break;

                    case DragEvent.ACTION_DRAG_LOCATION  :
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_LOCATION");
                        x_cord = (int) event.getX();
                        y_cord = (int) event.getY();
                        break;

                    case DragEvent.ACTION_DRAG_ENDED   :
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_ENDED");
                        // Do nothing
                        break;

                    case DragEvent.ACTION_DROP:
                        Log.d(msg, "ACTION_DROP event");
                        // Do nothing
                        break;
                    default: break;
                }
                return true;
            }
        });

        sad_drag.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ClipData data = ClipData.newPlainText("", "");
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(sad_drag);

                    sad_drag.startDrag(data, shadowBuilder, sad_drag, 0);
                    return true;
                } else {
                    return false;
                }
            }
        });
        fear_drag.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                switch(event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        layoutParams = (LinearLayout.LayoutParams)v.getLayoutParams();
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_STARTED");

                        // Do nothing
                        break;

                    case DragEvent.ACTION_DRAG_ENTERED:
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_ENTERED");
                        int x_cord = (int) event.getX();
                        int y_cord = (int) event.getY();
                        break;

                    case DragEvent.ACTION_DRAG_EXITED :
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_EXITED");
                        x_cord = (int) event.getX();
                        y_cord = (int) event.getY();
                        layoutParams.leftMargin = x_cord;
                        layoutParams.topMargin = y_cord;
                        if (v == neutral_drag) {
                            neutral_label.setVisibility(View.VISIBLE); }
                        if (v == angry_drag) {
                            angry_label.setVisibility(View.VISIBLE); }
                        if (v == sad_drag) {
                            sad_label.setVisibility(View.VISIBLE); }
                        if (v == happy_drag) {
                            happy_label.setVisibility(View.VISIBLE); }
                        if (v == disgust_drag) {
                            disgust_label.setVisibility(View.VISIBLE); }
                        if (v == fear_drag) {
                            fear_label.setVisibility(View.VISIBLE); }
                        if (v == surprise_drag) {
                            surprise_label.setVisibility(View.VISIBLE); }
                        System.out.println(mProgressBar.getProgress());
                        v.setLayoutParams(layoutParams);
                        break;

                    case DragEvent.ACTION_DRAG_LOCATION  :
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_LOCATION");
                        x_cord = (int) event.getX();
                        y_cord = (int) event.getY();
                        break;

                    case DragEvent.ACTION_DRAG_ENDED   :
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_ENDED");

                        // Do nothing
                        break;

                    case DragEvent.ACTION_DROP:
                        Log.d(msg, "ACTION_DROP event");
                        // Do nothing
                        break;
                    default: break;
                }
                return true;
            }
        });

        fear_drag.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ClipData data = ClipData.newPlainText("", "");
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(fear_drag);

                    fear_drag.startDrag(data, shadowBuilder, fear_drag, 0);
                    return true;
                } else {
                    return false;
                }
            }
        });
        surprise_drag.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                switch(event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        layoutParams = (LinearLayout.LayoutParams)v.getLayoutParams();
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_STARTED");

                        // Do nothing
                        break;

                    case DragEvent.ACTION_DRAG_ENTERED:
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_ENTERED");
                        int x_cord = (int) event.getX();
                        int y_cord = (int) event.getY();
                        break;

                    case DragEvent.ACTION_DRAG_EXITED :
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_EXITED");
                        x_cord = (int) event.getX();
                        y_cord = (int) event.getY();
                        layoutParams.leftMargin = x_cord;
                        layoutParams.topMargin = y_cord;
                        if (v == neutral_drag) {
                            neutral_label.setVisibility(View.VISIBLE); }
                        if (v == angry_drag) {
                            angry_label.setVisibility(View.VISIBLE); }
                        if (v == sad_drag) {
                            sad_label.setVisibility(View.VISIBLE); }
                        if (v == happy_drag) {
                            happy_label.setVisibility(View.VISIBLE); }
                        if (v == disgust_drag) {
                            disgust_label.setVisibility(View.VISIBLE); }
                        if (v == fear_drag) {
                            fear_label.setVisibility(View.VISIBLE); }
                        if (v == surprise_drag) {
                            surprise_label.setVisibility(View.VISIBLE); }
                        System.out.println(mProgressBar.getProgress());
                        v.setLayoutParams(layoutParams);
                        break;

                    case DragEvent.ACTION_DRAG_LOCATION  :
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_LOCATION");
                        x_cord = (int) event.getX();
                        y_cord = (int) event.getY();
                        break;

                    case DragEvent.ACTION_DRAG_ENDED   :
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_ENDED");

                        // Do nothing
                        break;

                    case DragEvent.ACTION_DROP:
                        Log.d(msg, "ACTION_DROP event");
                        // Do nothing
                        break;
                    default: break;
                }
                return true;
            }
        });

        surprise_drag.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ClipData data = ClipData.newPlainText("", "");
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(surprise_drag);

                    surprise_drag.startDrag(data, shadowBuilder, surprise_drag, 0);
                    return true;
                } else {
                    return false;
                }
            }
        });
        disgust_drag.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                switch(event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        layoutParams = (LinearLayout.LayoutParams)v.getLayoutParams();
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_STARTED");

                        // Do nothing
                        break;

                    case DragEvent.ACTION_DRAG_ENTERED:
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_ENTERED");
                        int x_cord = (int) event.getX();
                        int y_cord = (int) event.getY();
                        break;

                    case DragEvent.ACTION_DRAG_EXITED :
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_EXITED");
                        x_cord = (int) event.getX();
                        y_cord = (int) event.getY();
                        layoutParams.leftMargin = x_cord;
                        layoutParams.topMargin = y_cord;
                        if (v == neutral_drag) {
                            neutral_label.setVisibility(View.VISIBLE); }
                        if (v == angry_drag) {
                            angry_label.setVisibility(View.VISIBLE); }
                        if (v == sad_drag) {
                            sad_label.setVisibility(View.VISIBLE); }
                        if (v == happy_drag) {
                            happy_label.setVisibility(View.VISIBLE); }
                        if (v == disgust_drag) {
                            disgust_label.setVisibility(View.VISIBLE); }
                        if (v == fear_drag) {
                            fear_label.setVisibility(View.VISIBLE); }
                        if (v == surprise_drag) {
                            surprise_label.setVisibility(View.VISIBLE); }
                        System.out.println(mProgressBar.getProgress());
                        v.setLayoutParams(layoutParams);
                        break;

                    case DragEvent.ACTION_DRAG_LOCATION  :
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_LOCATION");
                        x_cord = (int) event.getX();
                        y_cord = (int) event.getY();
                        break;

                    case DragEvent.ACTION_DRAG_ENDED   :
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_ENDED");

                        // Do nothing
                        break;

                    case DragEvent.ACTION_DROP:
                        Log.d(msg, "ACTION_DROP event");
                        // Do nothing
                        break;
                    default: break;
                }
                return true;
            }
        });

        disgust_drag.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ClipData data = ClipData.newPlainText("", "");
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(disgust_drag);

                    disgust_drag.startDrag(data, shadowBuilder, disgust_drag, 0);
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    private class MyAsync extends AsyncTask<Void, Integer, Void>
    {
        int duration = 0;
        int current = 0;
        @Override
        protected Void doInBackground(Void... params) {

            mVideoView.start();
            mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                public void onPrepared(MediaPlayer mp) {
                    duration = mVideoView.getDuration();
                }
            });

            do {
                current = mVideoView.getCurrentPosition();
                System.out.println("duration - " + duration + " current- "
                        + current);
                try {
                    publishProgress((int) (current * 100 / duration));
                    if(mProgressBar.getProgress() >= 100){
                        break;
                    }
                } catch (Exception e) {
                }
            } while (mProgressBar.getProgress() <= 100);
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            System.out.println(values[0]);
            mProgressBar.setProgress(values[0]);
        }
    }
}