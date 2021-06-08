package ca.imdc.newp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class ActivitySplash extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            splashPlayer();
        } catch (Exception ex) {
            jumpMain("main", "");
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }

    public void splashPlayer() {
        VideoView videoHolder = new VideoView(this);
        setContentView(videoHolder);
        Intent intent = getIntent();
        String video_name = intent.getStringExtra("filename");
        String prev = intent.getStringExtra("prev");
        String word = null;
        if (prev.equals("graph")) {word = intent.getStringExtra("word");}

        int rawId = getResources().getIdentifier(video_name,  "raw", getPackageName());
        Uri video = Uri.parse("android.resource://" + getPackageName() + "/"
                + rawId);
        videoHolder.setVideoURI(video);
        String finalWord = word;
        videoHolder.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                jumpMain(prev, finalWord);
            }

        });
        videoHolder.start();
        String finalWord1 = word;
        videoHolder.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((VideoView) v).stopPlayback();
                jumpMain(prev, finalWord1);
                return true;
            }
        });
    }

    private synchronized void jumpMain(String prev, String word) {
        if (prev.equals("graph")) {
            Intent intent = new Intent(ActivitySplash.this, GraphActivity.class);
            intent.putExtra("WORD", word);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(ActivitySplash.this, MainActivity.class);
            startActivity(intent);
        }
        finish();
    }
}