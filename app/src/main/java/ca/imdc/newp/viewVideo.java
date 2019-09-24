package ca.imdc.newp;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import java.io.IOException;

public class viewVideo extends MainActivity {
    private VideoView videoView;
    private VideoView myVideo;
    private RelativeLayout mask;


    //Bundle extras = getIntent().getExtras().getParcelable("uri");
    //String uri = extras.getString("uri");
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Uri uri = this.getIntent().getData();

        setContentView(R.layout.videoview);
        start(savedInstanceState);
    }
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        Uri uri = this.getIntent().getData();
       MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();

        try {
            mediaPlayer.setDataSource(getApplicationContext(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        videoView = findViewById(R.id.surface);
        videoView.setVideoURI(uri);
        videoView.start();
        videoView.setOnCompletionListener ( new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                videoView.start();
            }
        });

    }

    public void start(Bundle savedInstanceState){
        setContentView(R.layout.videoview);
        Uri uri = this.getIntent().getData();
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);


        try {
            mediaPlayer.setDataSource(getApplicationContext(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        videoView = findViewById(R.id.surface);
        videoView.setVideoURI(uri);
        videoView.start();
        videoView.setOnCompletionListener ( new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                videoView.start();
                //System.out.println("LOOOPLOOOPLOOOPLOOOPLOOOPLOOOPLOOOPLOOOPLOOOPLOOOPLOOOPLOOOPLOOOPLOOOPLOOOPLOOOPLOOOPLOOOPLOOOPLOOOPLOOOPLOOOPLOOOPLOOOPLOOOPLOOOPLOOOPLOOOPLOOOPLOOOPLOOOPLOOOPLOOOPLOOOP");
            }
        });
        System.out.println("start (viewVideo.java) got ran ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");



    }


}
