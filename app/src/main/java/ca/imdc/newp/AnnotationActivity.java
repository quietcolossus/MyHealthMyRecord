package ca.imdc.newp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class AnnotationActivity extends AppCompatActivity {

    // inside Activity
    LinearLayout txt_help_gest;
    LinearLayout sharing;
    LinearLayout ratings;
    LinearLayout tags;
    LinearLayout comments;
    TextView slide;
    TextView commenttxt;
    TextView sharetxt;
    TextView ratingtxt;
    TextView tagtxt;
    Button transcript_button;
    TextView transcript_read;
    EditText transcript_edit;
    Button comment_button;
    TextView comment_read;
    EditText comment_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.annotation);
        Spinner dropdown = findViewById(R.id.spinner);
        txt_help_gest = findViewById(R.id.slide_layout);

        transcript_button = findViewById(R.id.transcript_button);
        transcript_read = findViewById(R.id.transcript_read);
        transcript_edit = findViewById(R.id.transcript_edit);

        comment_button = findViewById(R.id.comment_button);
        comment_read = findViewById(R.id.comment_read);
        comment_edit = findViewById(R.id.comment_edit);
        sharing = findViewById(R.id.sharing_layout);
        sharetxt = findViewById(R.id.sharing_txt);
        tags = findViewById(R.id.tag_layout);
        tagtxt = findViewById(R.id.tag_txt);
        ratings = findViewById(R.id.rating_layout);
        ratingtxt = findViewById(R.id.rating_txt);
        slide = findViewById(R.id.slide_txt);
        commenttxt = findViewById(R.id.comment_txt);
        comments = findViewById(R.id.comment_layout);
        // hide until its title is clicked
        txt_help_gest.setVisibility(View.GONE);
        tags.setVisibility(View.GONE);
        ratings.setVisibility(View.GONE);
        sharing.setVisibility(View.GONE);
        comments.setVisibility(View.GONE);
        transcript_edit.setVisibility(View.GONE);
        comment_edit.setVisibility(View.GONE);

        transcript_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!transcript_button.getText().equals("Done")) {
                    transcript_edit.setVisibility(View.VISIBLE);
                    transcript_read.setVisibility(View.GONE);
                    transcript_button.setText("Done");
                }
                else {
                    transcript_edit.setVisibility(View.GONE);
                    transcript_read.setText(transcript_edit.getText());
                    transcript_read.setVisibility(View.VISIBLE);
                    transcript_button.setText("Edit Transcript");
                }
            }
        });

        comment_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!comment_button.getText().equals("Done")) {
                    comment_edit.setVisibility(View.VISIBLE);
                    comment_read.setVisibility(View.GONE);
                    comment_button.setText("Done");
                }
                else {
                    comment_edit.setVisibility(View.GONE);
                    comment_read.setText(comment_edit.getText());
                    comment_read.setVisibility(View.VISIBLE);
                    comment_button.setText("Edit");
                }
            }
        });

        slide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("HELLO");
                toggle_contents(txt_help_gest);
            }
        });
        commenttxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("HELLO");
                toggle_contents(comments);
            }
        });
        sharetxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("HELLO");
                toggle_contents(sharing);
            }
        });
        tagtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("HELLO");
                toggle_contents(tags);
            }
        });
        ratingtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("HELLO");
                toggle_contents(ratings);
            }
        });
        String[] items = new String[]{"Filter by:", "Language: Medical", "Language: Time", "Time: Last Week", "Time: Last Month"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Intent dataIntent = new Intent(AnnotationActivity.this, WordCloudActivity.class);
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
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkbox_none:
                if (checked) {
                    ((CheckBox) findViewById(R.id.checkbox_a)).setChecked(false);
                    ((CheckBox) findViewById(R.id.checkbox_b)).setChecked(false);
                    ((CheckBox) findViewById(R.id.checkbox_c)).setChecked(false);
                    ((CheckBox) findViewById(R.id.checkbox_d)).setChecked(false);
                    ((CheckBox) findViewById(R.id.checkbox_cheese)).setChecked(false);
                    ((CheckBox) findViewById(R.id.checkbox_e)).setChecked(false);
                    ((CheckBox) findViewById(R.id.checkbox_meat)).setChecked(false);
                }
            else
                // Remove the meat
                break;
            case R.id.checkbox_cheese:
                if (checked) {((CheckBox) findViewById(R.id.checkbox_none)).setChecked(false);}
                // Cheese me
            else
                // I'm lactose intolerant
                break;
            // TODO: Veggie sandwich
        }
    }

    public static void slide_down(Context ctx, View v){

        Animation a = AnimationUtils.loadAnimation(ctx, R.anim.slide_down);
        if(a != null){
            a.reset();
            if(v != null){
                v.clearAnimation();
                v.startAnimation(a);
            }
        }
    }

    public static void slide_up(Context ctx, View v){

        Animation a = AnimationUtils.loadAnimation(ctx, R.anim.slide_up);
        if(a != null){
            a.reset();
            if(v != null){
                v.clearAnimation();
                v.startAnimation(a);
            }
        }
    }



    public void toggle_contents(View v) {
        if(v.isShown()){
            slide_up(this, v);
            v.setVisibility(View.GONE);
        }
        else{
            v.setVisibility(View.VISIBLE);
            slide_down(this, v);
        }
    }
}