package ca.imdc.newp;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;

public class VideoTranscript extends AppCompatActivity {
    //public TextView transcript = (TextView) findViewById(R.id.message);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String message = intent.getStringExtra("TRANSCRIPT");
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                transcript.setText(message);
//
//            }
//        });
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_transcript);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}
