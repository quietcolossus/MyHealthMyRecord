package ca.imdc.newp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class CurateVideo extends AppCompatActivity {

    public EditText mRenameVideo;
    public Button mSubmitButton;

    public SeekBar mValence;
    public SeekBar mArousal;

    public CheckBox mHome;
    public CheckBox mWorkplace;
    public CheckBox mInstitution;
    public CheckBox mOutdoors;

    public CheckBox mLeisure;
    public CheckBox mWork;
    public CheckBox mExercise;
    public CheckBox mTravel;

    public CheckBox mFamily;
    public CheckBox mFriends;
    public CheckBox mMedical;
    public CheckBox mEveryone;

    public HashMap<String, ArrayList<Object>> tags;

    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.curate2);
        tags = new HashMap<>();
        tags.put("Arousal", new ArrayList<>());
        tags.put("Valence", new ArrayList<>());
        tags.put("Location", new ArrayList<>());
        tags.put("Activity", new ArrayList<>());
        tags.put("Sharing", new ArrayList<>());

        mSubmitButton = findViewById(R.id.submit_button);
        mRenameVideo = findViewById(R.id.nameentry);

        mValence = findViewById(R.id.valence_bar);
        mArousal = findViewById(R.id.arousal_bar);

        mHome = findViewById(R.id.home_check);
        mWorkplace = findViewById(R.id.workplace_check);
        mInstitution = findViewById(R.id.institution_check);
        mOutdoors = findViewById(R.id.outdoors_check);

        mLeisure = findViewById(R.id.leisure_check);
        mWork = findViewById(R.id.working_check);
        mExercise = findViewById(R.id.exercise_check);
        mTravel = findViewById(R.id.travel_check);

        mFamily = findViewById(R.id.family_check);
        mFriends = findViewById(R.id.friends_check);
        mMedical = findViewById(R.id.medical_check);
        mEveryone = findViewById(R.id.everyone_check);

        final Bundle b = getIntent().getExtras();
        System.out.println(b);

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name;
                int position;
                String stringToPassBack = mRenameVideo.getText().toString();
                if (b != null) {
                    name = (String) b.get("name");
                    position = (int) b.get("position");
                }
                else { name = "default123"; position = -1;}
                int valence = mValence.getProgress();
                int arousal = mArousal.getProgress();

                tags.get("Valence").add((valence));
                tags.get("Arousal").add((arousal));

                if (mHome.isChecked()) tags.get("Location").add("Home");
                if (mInstitution.isChecked()) tags.get("Location").add("Institution");
                if (mOutdoors.isChecked()) tags.get("Location").add("Outdoors");
                if (mWorkplace.isChecked()) tags.get("Location").add("Workplace");

                if (mExercise.isChecked()) tags.get("Activity").add("Exercise");
                if (mWork.isChecked()) tags.get("Activity").add("Work");
                if (mLeisure.isChecked()) tags.get("Activity").add("Leisure");
                if (mTravel.isChecked()) tags.get("Activity").add("Travel");

                if (mEveryone.isChecked()) tags.get("Sharing").add("Everyone");
                if (mFamily.isChecked()) tags.get("Sharing").add("Family");
                if (mFriends.isChecked()) tags.get("Sharing").add("Friends");
                if (mMedical.isChecked()) tags.get("Sharing").add("Medical");

                Intent output = new Intent();
                output.putExtra("tags", tags);
                output.putExtra("result", stringToPassBack);
                output.putExtra("position", position);
                output.putExtra("name", name);

                setResult(RESULT_OK, output);
                finish();
            }
        });
    }
}
