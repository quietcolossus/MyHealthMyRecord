package ca.imdc.newp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class SummaryActivity extends AppCompatActivity{
    public Button mWordCloud;
    public Button mBarGraph;
    public TextView mSummary;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary);

        mWordCloud = findViewById(R.id.wordcloud_button);
        mBarGraph = findViewById(R.id.bar_button);
        mSummary = findViewById(R.id.mSummary);

        mWordCloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent wcIntent = new Intent(SummaryActivity.this, WordCloudActivity.class);
                startActivity(wcIntent);
            }
        });

        mBarGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bgIntent = new Intent(SummaryActivity.this, BarGraphActivity.class);
                startActivity(bgIntent);
            }
        });

        MainActivity mainact = new MainActivity();
        JSONObject rTags = mainact.tRecord;
        Iterator<String> keys = rTags.keys();
        StringBuilder allwords = new StringBuilder();
        while (keys.hasNext()) {
            String key = keys.next();
            try {
                allwords.append((String) rTags.get(key));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        String meta = allwords.toString();
        meta = meta.replace("'", "");
        meta = meta.replace("%", "");

        String[] wordCloud = meta.split(" ");

        Map<String, Integer> map = new HashMap<>();
        for (String w : wordCloud) {
            Integer n = map.get(w);
            n = (n == null) ? 1 : ++n;
            map.put(w, n);
        }
        System.out.println("HEY LOOK" + map);

        List<Integer> l = new ArrayList<Integer>(map.values());
        Collections.sort(l);
        Collections.reverse(l);
        System.out.println(l);
        l = l.subList(0,5);
        List<String> topWords = new ArrayList<String>();
        for (Integer num : l) {
            for (String key : map.keySet()) {
                if (map.get(key) == num && !topWords.contains(key)) {topWords.add(key); break;}
            }
        }
        System.out.println(topWords);
        System.out.println(l);

        StringBuilder ts = new StringBuilder();

        int count = 0;

        for (String word : topWords) {
            ts.append("The patient mentioned the word: " + word + " a total of " + l.get(count) + " times.\n\n");
            count++;
        }

        String output = ts.toString();

        mSummary.setText(output);


        Object[] freq = l.toArray();
        Object[] words = topWords.toArray();
        System.out.println(freq);
        System.out.println(words);
    }
}
