package ca.imdc.newp;

import android.content.Intent;
import android.os.Bundle;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Iterator;

import androidx.appcompat.app.AppCompatActivity;

public class GraphActivity extends AppCompatActivity{
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph);
        Intent intent = getIntent();
        GraphView graph = (GraphView) findViewById(R.id.graph);
        JSONObject rTags = MainActivity.tRecord;
        Iterator<String> keys = rTags.keys();

        int count = 0;

        while (keys.hasNext()) {
            String key = keys.next();
            count += 1;
        }
        DataPoint[] blah = new DataPoint[count];
        Iterator<String> keys2 = rTags.keys();

        count = 1;
        while (keys2.hasNext()) {
            String key = keys2.next();
            String transcript = null;
            try {
                transcript = (String) rTags.get(key);
                System.out.println(transcript);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            DataPoint v = new DataPoint(count, transcript.split("pain", -1).length - 1);
            blah[count-1] = v;
            count++;
        }
        System.out.println("Hello" + Arrays.toString(blah));


        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(blah);
        GridLabelRenderer gridLabel = graph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle("X Axis Title");
        series.setTitle("foo");
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        graph.addSeries(series);
    }
}
