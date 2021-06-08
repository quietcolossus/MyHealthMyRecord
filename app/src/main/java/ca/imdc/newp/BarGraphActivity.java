package ca.imdc.newp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

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
    public List<String> stopwords =  Arrays.asList("HESITATION","I", "i", "ive", "im", "id", "me", "my", "myself", "we", "our", "ours", "ourselves", "you", "your", "yours", "yourself", "yourselves", "he", "him", "his", "himself", "she", "her", "hers", "herself", "it", "its", "itself", "they", "them", "their", "theirs", "themselves", "what", "which", "who", "whom", "this", "that", "these", "those", "am", "is", "are", "was", "were", "be", "been", "being", "have", "has", "had", "having", "do", "does", "did", "doing", "a", "an", "the", "and", "but", "if", "or", "because", "as", "until", "while", "of", "at", "by", "for", "with", "about", "against", "between", "into", "through", "during", "before", "after", "above", "below", "to", "from", "up", "down", "in", "out", "on", "off", "over", "under", "again", "further", "then", "once", "here", "there", "when", "where", "why", "how", "all", "any", "both", "each", "few", "more", "most", "other", "some", "such", "no", "nor", "not", "only", "own", "same", "so", "than", "too", "very", "can", "will", "just", "dont", "should", "now");
    public List<String> medwords = Arrays.asList("hurt", "hurts", "hurting", "sore", "soreness", "dizzy", "dizziness", "vertigo", "light-headed", "chill", "chills", "diarrhea", "stiff", "stiffness", "pain", "painful", "nausea", "nauseous", "nauseate", "nauseated", "insomnia", "sick", "fever", "ache", "aches", "ached", "aching", "pains", "flu", "vomit", "vomiting", "cough", "coughing", "coughs", "coughed", "tired", "exhausted", "numb", "numbness", "numbed", "weak", "weakness", "tingle", "tingling", "tingles", "tingled", "fever", "shiver", "shivering", "shivered", "rash", "swell", "swollen", "sweat", "sweaty", "sweats", "fatigue", "fatigued", "heartburn", "headache", "headaches", "constipation", "constipated", "bloated", "bloating", "cramp", "cramps", "cramped", "cramping");
    public String[] destop(String[] cloud) {
        StringBuilder ds = new StringBuilder();
        for (String word : cloud) {
            if (!stopwords.contains(word)) {
                ds.append(word + " ");
            }
        }
        String dss = ds.toString();
        return dss.split(" ");
    }

    public String[] demed(String[] cloud) {
        StringBuilder ds = new StringBuilder();
        for (String word : cloud) {
            if (medwords.contains(word)) {
                ds.append(word + " ");
            }
        }
        String dss = ds.toString();
        return dss.split(" ");
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wordcloud);

        mWordCloud = findViewById(R.id.wordcloud_button);
        mBarGraph = findViewById(R.id.bar_button);

        Spinner dropdown = findViewById(R.id.spinner);

        String extra = null;

        try {
            Intent intent = getIntent();
            extra = intent.getStringExtra("filter");
        }
        catch (Exception e) {
            e.printStackTrace();
        }


        String[] items = new String[]{"Filter by:", "Language: Medical", "Language: Time", "Time: Last Week", "Time: Last Month"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Intent dataIntent = new Intent(BarGraphActivity.this, BarGraphActivity.class);
                System.out.println(position);
                switch (position) {

                    case 0:
                        // Whatever you want to happen when the first item gets selected
                        break;
                    case 1:
                        dataIntent.putExtra("filter", "lang_med");
                        startActivity(dataIntent);
                        break;
                    case 2:
                        dataIntent.putExtra("filter", "lang_time");
                        startActivity(dataIntent);
                        break;
                    case 3:
                        dataIntent.putExtra("filter", "time_week");
                        startActivity(dataIntent);
                        break;
                    case 4:
                        dataIntent.putExtra("filter", "time_month");
                        startActivity(dataIntent);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

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

        String[] wordCloud;
        if (!(extra == null) && extra.equals("lang_med")) {
            wordCloud = demed(destop(meta.split(" ")));
        }
        else {
            wordCloud = destop(meta.split(" "));
        }

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
        if (l.size() <10) { l = l.subList(0,l.size());}
        else {l = l.subList(0,10);}

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
