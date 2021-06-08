//package ca.imdc.newp;
//
//import android.content.Intent;
//import android.os.Bundle;
//
//import com.jjoe64.graphview.GraphView;
//import com.jjoe64.graphview.GridLabelRenderer;
//import com.jjoe64.graphview.LegendRenderer;
//import com.jjoe64.graphview.series.DataPoint;
//import com.jjoe64.graphview.series.LineGraphSeries;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.Arrays;
//import java.util.Iterator;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//public class GraphActivity extends AppCompatActivity{
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.graph);
//        Intent intent = getIntent();
//        GraphView graph = (GraphView) findViewById(R.id.graph);
//        JSONObject rTags = MainActivity.tRecord;
//        Iterator<String> keys = rTags.keys();
//
//        int count = 0;
//
//        while (keys.hasNext()) {
//            String key = keys.next();
//            count += 1;
//        }
//        DataPoint[] blah = new DataPoint[count];
//        Iterator<String> keys2 = rTags.keys();
//
//        count = 1;
//        while (keys2.hasNext()) {
//            String key = keys2.next();
//            String transcript = null;
//            try {
//                transcript = (String) rTags.get(key);
//                System.out.println(transcript);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            DataPoint v = new DataPoint(count, transcript.split("pain", -1).length - 1);
//            blah[count-1] = v;
//            count++;
//        }
//        System.out.println("Hello" + Arrays.toString(blah));
//
//
//        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(blah);
//        GridLabelRenderer gridLabel = graph.getGridLabelRenderer();
//        gridLabel.setHorizontalAxisTitle("Videos");
//        gridLabel.setVerticalAxisTitle("Word Frequency");
//        series.setTitle("foo");
//        graph.getLegendRenderer().setVisible(true);
//        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
//        graph.addSeries(series);
//    }
//}

package ca.imdc.newp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

//implement weekday, weekend filter for line graph - coloured backgrounds

public class GraphActivity extends AppCompatActivity {
    public Button mWordCloud;
    public Button mBarGraph;
    public Button mLineGraph;
    public Button mSummary;
    private DrawerLayout mDrawerLayout;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<LocalDate> getDatesBetweenUsingJava8(
            LocalDate startDate, LocalDate endDate) {

        long numOfDaysBetween = ChronoUnit.DAYS.between(startDate, endDate);
        return IntStream.iterate(0, i -> i + 1)
                .limit(numOfDaysBetween)
                .mapToObj(i -> startDate.plusDays(i))
                .collect(Collectors.toList());
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wordcloud);
        Intent intent = getIntent();
        String word = intent.getStringExtra("WORD");

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Word Frequency of \"" + word + "\" over time.");
        System.out.println("HEYHEYHEYEHYHEYEYEUHJHJKDHJK");

        mWordCloud = findViewById(R.id.wordcloud_button);
        mBarGraph = findViewById(R.id.bar_button);

        mWordCloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent wcIntent = new Intent(GraphActivity.this, WordCloudActivity.class);
                startActivity(wcIntent);
            }
        });

        mBarGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bgIntent = new Intent(GraphActivity.this, BarGraphActivity.class);
                startActivity(bgIntent);
            }
        });

        MainActivity mainact = new MainActivity();
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
                            Intent mainIntent = new Intent(GraphActivity.this, MainActivity.class);
                            startActivity(mainIntent);
                        }
                        else if (id == R.id.nav_share) {
                            Intent shareIntent = new Intent(GraphActivity.this, shareCircleActivity.class);
                            startActivity(shareIntent);
                        }
                        else if (id == R.id.nav_myv) {
                            Intent dataIntent = new Intent(GraphActivity.this, WordCloudActivity.class);
                            startActivity(dataIntent);

                        } else if (id == R.id.nav_settings) {

                        }
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    }
                });
        JSONObject rTags = mainact.tRecord;
        JSONObject dates = mainact.nRecord;
        System.out.println(rTags);
        Iterator<String> keys = rTags.keys();
        Iterator<String> date_keys = dates.keys();
        String first = date_keys.next();
        String first_date = null;
        try {
            first_date = (String) dates.get(first);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String[] date_time = first_date.split(" ");
        String[] date_comp = date_time[0].split("-");
        LocalDate start = LocalDate.of(Integer.parseInt(date_comp[0]), Integer.parseInt(date_comp[1]) , Integer.parseInt(date_comp[2]) );
        LocalDate stop = LocalDate.now( ZoneId.of( "America/Montreal" ) );
        List<LocalDate> alldates = getDatesBetweenUsingJava8(start, stop);

        System.out.println("HEYHEYHEYEHYHEYEYEUHJHJKDHJK");
        System.out.println(alldates);
        System.out.println(alldates.get(0).toString());
        StringBuilder frequency = new StringBuilder();
        for (LocalDate d : alldates) {
            String date_string  = d.toString();
            Iterator<String> date_keys2 = dates.keys();
            StringBuilder meta = new StringBuilder();
            while (date_keys2.hasNext()) {
                String entry = date_keys2.next();
                String entry_date = null;
                try {
                    entry_date = (String) dates.get(entry);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String comp_date = entry_date.split(" ")[0];
                if (comp_date.equals(date_string)) {
                    String t = null;
                    try {
                        t = (String) rTags.get(entry);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    meta.append(t);
                }

            }
            String singleString = meta.toString();
            frequency.append( singleString.split(word, -1).length - 1 + " ");
            System.out.println(date_string + ": " + singleString);

        }


//        while (keys.hasNext()) {
//            String transcript = null;
//            String key = keys.next();
//            try {
//                transcript = (String) rTags.get(key);
//                System.out.println(transcript);
//                frequency.append( transcript.split(word, -1).length - 1 + " ");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }

        String meta = frequency.toString();
        String[] freq = meta.split(" ");


        final WebView d3 = (WebView) findViewById(R.id.wordcloud_web);

//        d3.setOnTouchListener(new View.OnTouchListener() {
//
//            public boolean onTouch(View v, MotionEvent event) {
//                return (event.getAction() == MotionEvent.ACTION_MOVE);
//            }
//        });

        WebSettings ws = d3.getSettings();
        ws.setJavaScriptEnabled(true);
        d3.loadUrl("file:///android_asset/plot.html");
        d3.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                Intent splash = new Intent(GraphActivity.this, ActivitySplash.class);
                Iterator<String> date_keys = dates.keys();
                String comp = url.replace("file:///android_asset/", "");
                while (date_keys.hasNext()) {
                    String d2 = date_keys.next();
                    String d3 = null;
                    try {
                        d3 = dates.get(d2).toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String fd = d3.split(" ")[0];
                    if (fd.equals(comp)) {
                        splash.putExtra("filename", d2);
                        splash.putExtra("prev", "graph");
                        splash.putExtra("word", word);
                        startActivity(splash);
                        return false;
                    }
                }
                Intent intent = new Intent(GraphActivity.this, GraphActivity.class);
                intent.putExtra("WORD",word);
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
                sb.append("],[");
                for (LocalDate d : alldates) {
                    sb.append("'").append(d.toString()).append("'");
                    if (!(d == stop)) {
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



