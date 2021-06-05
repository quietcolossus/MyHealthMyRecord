package ca.imdc.newp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

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

public class BarGraphActivity extends AppCompatActivity{
    public Button mWordCloud;
    public Button mBarGraph;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wordcloud);

        mWordCloud = findViewById(R.id.wordcloud_button);
        mBarGraph = findViewById(R.id.bar_button);

        mWordCloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent wcIntent = new Intent(BarGraphActivity.this, WordCloudActivity.class);
                startActivity(wcIntent);
            }
        });

        mBarGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bgIntent = new Intent(BarGraphActivity.this, BarGraphActivity.class);
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
        l = l.subList(0,10);
        List<String> topWords = new ArrayList<String>();
        for (Integer num : l) {
            for (String key : map.keySet()) {
                if (map.get(key) == num && !topWords.contains(key)) {topWords.add(key); break;}
            }
        }
        System.out.println(topWords);
        System.out.println(l);

        final MobileWebView d3 = (MobileWebView) findViewById(R.id.wordcloud_web);

        WebSettings ws = d3.getSettings();
        ws.setJavaScriptEnabled(true);
        d3.loadUrl("file:///android_asset/bar.html");
        Object[] freq = l.toArray();
        Object[] words = topWords.toArray();
        d3.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                Intent intent = new Intent(BarGraphActivity.this, GraphActivity.class);
                intent.putExtra("WORD",url.replace("file:///android_asset/", ""));
                startActivity(intent);
                return true;
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                StringBuffer sb = new StringBuffer();
                sb.append("graph([");
                for (int i = 0; i < freq.length; i++) {
                    sb.append("'").append(freq[i]).append("'");
                    if (i < freq.length - 1) {
                        sb.append(",");
                    }
                }

                sb.append("], [");
                for (int i = 0; i < words.length; i++) {
                    sb.append("'").append(words[i]).append("'");
                    if (i < words.length - 1) {
                        sb.append(",");
                    }
                }
                sb.append("])");

                System.out.println(sb.toString());

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    d3.evaluateJavascript(sb.toString(), null);
                } else {
                    d3.loadUrl("javascript:" + sb.toString());
                }
            }
        });



    }
}
