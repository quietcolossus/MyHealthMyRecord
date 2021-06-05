package ca.imdc.newp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
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
    private DrawerLayout mDrawerLayout;

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

        mLineGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent lgIntent = new Intent(WordCloudActivity.this, GraphActivity.class);
                lgIntent.putExtra("WORD", "pain");
                startActivity(lgIntent);
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
        String[] wordCloud = meta.split(" ");
        System.out.println("HEY LOOK" + Arrays.toString(wordCloud));

        final MobileWebView d3 = findViewById(R.id.wordcloud_web);
        d3.getSettings().setLoadWithOverviewMode(true);
        d3.getSettings().setUseWideViewPort(true);

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



