package ca.imdc.newp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mordred.wordcloud.WordCloud;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class dataNavigationActivity extends AppCompatActivity {
    //public TextView transcript = (TextView) findViewById(R.id.message);
    public TextView mWordCloud;
    public TextView mLineGraph;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        Intent intent = getIntent();

        mLineGraph = findViewById(R.id.linegraph);
        mWordCloud = findViewById(R.id.wordcloud);

        mLineGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent graphIntent = new Intent(dataNavigationActivity.this, GraphActivity.class);
                startActivity(graphIntent);
            }
        });

        mWordCloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent wcIntent = new Intent(dataNavigationActivity.this, WordCloudActivity.class);
                startActivity(wcIntent);
            }
        });
    }
}
