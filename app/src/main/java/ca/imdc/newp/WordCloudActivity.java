package ca.imdc.newp;

//change pain levels
//check data from Fatima
//layout from Sarah
//share circle from Rabia
//size scale, list underneath?
//information icon?

//more default data viz screen




import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.mordred.wordcloud.WordCloud;

import androidx.annotation.Dimension;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class WordCloudActivity extends AppCompatActivity {
    public Button mWordCloud;
    public Button mBarGraph;
    public Button mLineGraph;
    public Button mSummary;
    public Button mTextGraph;
    private DrawerLayout mDrawerLayout;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wordcloud);

        mWordCloud = findViewById(R.id.wordcloud_button);
        mBarGraph = findViewById(R.id.bar_button);
        mLineGraph = findViewById(R.id.line_button);
        mSummary = findViewById(R.id.summary_button);
        mTextGraph = findViewById(R.id.tg_button);
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
                Intent dataIntent = new Intent(WordCloudActivity.this, WordCloudActivity.class);
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

        mLineGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent lgIntent = new Intent(WordCloudActivity.this, GraphActivity.class);
                lgIntent.putExtra("WORD", "pain");
                startActivity(lgIntent);
            }
        });

        mTextGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("HELLO");
                Intent lgIntent = new Intent(WordCloudActivity.this, TextGraphActivity.class);
                lgIntent.putExtra("WORD", "pain");
                startActivity(lgIntent);
            }
        });

        mSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent wcIntent = new Intent(WordCloudActivity.this, SummaryActivity.class);
                startActivity(wcIntent);
            }
        });

        mWordCloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent wcIntent = new Intent(WordCloudActivity.this, WordCloudActivity.class);
                startActivity(wcIntent);
            }
        });

        mBarGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bgIntent = new Intent(WordCloudActivity.this, BarGraphActivity.class);
                startActivity(bgIntent);
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Word Cloud");
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

                        if (id == R.id.nav_myvideos) {
                            Intent mainIntent = new Intent(WordCloudActivity.this, MainActivity.class);
                            startActivity(mainIntent);
                        }
                        else if (id == R.id.nav_share) {
                            Intent shareIntent = new Intent(WordCloudActivity.this, shareCircleActivity.class);
                            startActivity(shareIntent);
                        }
                        else if (id == R.id.nav_myv) {
                            Intent dataIntent = new Intent(WordCloudActivity.this, WordCloudActivity.class);
                            startActivity(dataIntent);

                        } else if (id == R.id.nav_settings) {

                        }
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    }
                });

        MainActivity mainact = new MainActivity();
        JSONObject rTags = mainact.tRecord;
        Iterator<String> keys = rTags.keys();
        StringBuilder allwords = new StringBuilder();
        while (keys.hasNext()) {
            String key = keys.next();
            try {
                allwords.append((String) rTags.get(key) + " ");
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
        System.out.println("HEY LOOK" + Arrays.toString(wordCloud));

        final MobileWebView d3 = findViewById(R.id.wordcloud_web);
        d3.getSettings().setLoadWithOverviewMode(true);
        d3.getSettings().setUseWideViewPort(false);

        WebSettings ws = d3.getSettings();
        ws.setJavaScriptEnabled(true);
        d3.loadUrl("file:///android_asset/d3.html");
        d3.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                if (url.contains("viddate")) {

                }
                Intent intent = new Intent(WordCloudActivity.this, GraphActivity.class);
                intent.putExtra("WORD",url.replace("file:///android_asset/", ""));
                startActivity(intent);
                return true;
            }




            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                System.out.println(url);
                StringBuffer sb = new StringBuffer();
                sb.append("wordCloud([");
                for (int i = 0; i < wordCloud.length; i++) {
                    sb.append("'").append(wordCloud[i]).append("'");
                    if (i < wordCloud.length - 1) {
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



