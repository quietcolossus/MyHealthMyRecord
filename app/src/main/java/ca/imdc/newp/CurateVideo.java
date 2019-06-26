package ca.imdc.newp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;

import org.json.JSONException;
import org.json.JSONObject;

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
    public CheckBox mIndoors;
    public CheckBox mSchool;

    public CheckBox mLeisure;
    public CheckBox mWork;
    public CheckBox mExercise;
    public CheckBox mTravel;
    public CheckBox mChores;
    public CheckBox mCulture;
    public CheckBox mFood;
    public CheckBox mMeds;

    public CheckBox mFamily;
    public CheckBox mFriends;
    public CheckBox mMedical;
    public CheckBox mEveryone;

    public JSONObject tags;

    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.curate2);
        tags = new JSONObject();
        try {
            tags.put("Arousal", new JSONObject());
            tags.put("Valence", new JSONObject());
            tags.put("Location", new JSONObject());
            tags.put("Activity", new JSONObject());
            tags.put("Sharing", new JSONObject());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println(tags);
        MainActivity mainact = new MainActivity();
        mSubmitButton = findViewById(R.id.submit_button);
        mRenameVideo = findViewById(R.id.nameentry);

        JSONObject rTags = mainact.jRecord;

        System.out.println(mainact.jRecord);

        mValence = findViewById(R.id.valence_bar);
        mArousal = findViewById(R.id.arousal_bar);

        mHome = findViewById(R.id.home_check);
        mWorkplace = findViewById(R.id.workplace_check);
        mInstitution = findViewById(R.id.institution_check);
        mOutdoors = findViewById(R.id.outdoors_check);
        mIndoors = findViewById(R.id.indoors_check);
        mSchool = findViewById(R.id.school_check);

        mLeisure = findViewById(R.id.leisure_check);
        mWork = findViewById(R.id.working_check);
        mExercise = findViewById(R.id.exercise_check);
        mTravel = findViewById(R.id.travel_check);
        mChores = findViewById(R.id.chores_check);
        mCulture = findViewById(R.id.cultural_check);
        mFood = findViewById(R.id.food_check);
        mMeds = findViewById(R.id.meds_check);

        mFamily = findViewById(R.id.family_check);
        mFriends = findViewById(R.id.friends_check);
        mMedical = findViewById(R.id.medical_check);
        mEveryone = findViewById(R.id.everyone_check);

        final Bundle b = getIntent().getExtras();

        final String name;
        final int position;
        if (b != null) {
            name = (String) b.get("name");
            position = (int) b.get("position");
        }
        else { name = "default123"; position = -1;}

        if (rTags.has(name)) {
            JSONObject vidMemory = null;
            try {
                vidMemory = (JSONObject) rTags.get(name);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mRenameVideo.setHint(name);
            try {
                mValence.setProgress( (Integer) vidMemory.get("Valence"));
                mArousal.setProgress( (Integer) vidMemory.get("Arousal"));

                mHome.setChecked(((JSONObject) vidMemory.get("Location")).has("Home"));
                mWorkplace.setChecked(((JSONObject) vidMemory.get("Location")).has("Workplace"));
                mOutdoors.setChecked(((JSONObject) vidMemory.get("Location")).has("Outdoors"));
                mInstitution.setChecked(((JSONObject) vidMemory.get("Location")).has("Institution"));
                mIndoors.setChecked(((JSONObject) vidMemory.get("Location")).has("Indoors"));
                mSchool.setChecked(((JSONObject) vidMemory.get("Location")).has("School"));

                mLeisure.setChecked(((JSONObject) vidMemory.get("Activity")).has("Leisure"));
                mTravel.setChecked(((JSONObject) vidMemory.get("Activity")).has("Travel"));
                mWork.setChecked(((JSONObject) vidMemory.get("Activity")).has("Working"));
                mExercise.setChecked(((JSONObject) vidMemory.get("Activity")).has("Exercise"));
                mChores.setChecked(((JSONObject) vidMemory.get("Activity")).has("Chores"));
                mCulture.setChecked(((JSONObject) vidMemory.get("Activity")).has("Culture"));
                mFood.setChecked(((JSONObject) vidMemory.get("Activity")).has("Food"));
                mMeds.setChecked(((JSONObject) vidMemory.get("Activity")).has("Meds"));

                mFamily.setChecked(((JSONObject) vidMemory.get("Sharing")).has("Family"));
                mMedical.setChecked(((JSONObject) vidMemory.get("Sharing")).has("Medical"));
                mFriends.setChecked(((JSONObject) vidMemory.get("Sharing")).has("Friends"));
                mEveryone.setChecked(((JSONObject) vidMemory.get("Sharing")).has("Everyone"));
            } catch (JSONException e) {
                e.printStackTrace();
            }


            System.out.println("IT's WORKING SO FAR");
        }

        System.out.println(b);



        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stringToPassBack = mRenameVideo.getText().toString().equals("") ? name : mRenameVideo.getText().toString();

                int valence = mValence.getProgress();
                int arousal = mArousal.getProgress();

                JSONObject locationList = null;
                JSONObject activityList = null;
                JSONObject sharingList = null;

                try {
                    locationList = (JSONObject) tags.get("Location");
                    activityList = (JSONObject) tags.get("Activity");
                    sharingList = (JSONObject) tags.get("Sharing");
                } catch (JSONException e) {
                    e.printStackTrace();
                }



                try {
                    tags.put("Valence", valence);
                    tags.put("Arousal", arousal);
                    if (mHome.isChecked()) locationList.put("Home", true);
                    if (mInstitution.isChecked()) locationList.put("Institution", true);
                    if (mOutdoors.isChecked()) locationList.put("Outdoors", true);
                    if (mWorkplace.isChecked()) locationList.put("Workplace", true);
                    if (mIndoors.isChecked()) locationList.put("Indoors", true);
                    if (mSchool.isChecked()) locationList.put("School", true);

                    if (mExercise.isChecked()) activityList.put("Exercise", true);
                    if (mWork.isChecked()) activityList.put("Working", true);
                    if (mLeisure.isChecked()) activityList.put("Leisure", true);
                    if (mTravel.isChecked()) activityList.put("Travel", true);
                    if (mChores.isChecked()) activityList.put("Chores", true);
                    if (mCulture.isChecked()) activityList.put("Culture", true);
                    if (mFood.isChecked()) activityList.put("Food", true);
                    if (mMeds.isChecked()) activityList.put("Meds", true);

                    if (mEveryone.isChecked()) sharingList.put("Everyone", true);
                    if (mFamily.isChecked()) sharingList.put("Family", true);
                    if (mFriends.isChecked()) sharingList.put("Friends", true);
                    if (mMedical.isChecked()) sharingList.put("Medical", true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    tags.put("Location", locationList);
                    tags.put("Activity", activityList);
                    tags.put("Sharing", sharingList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Intent output = new Intent();

                System.out.println("TAGS: " + tags.toString());
                output.putExtra("tags", tags.toString());
                output.putExtra("result", stringToPassBack);
                output.putExtra("position", position);
                output.putExtra("name", name);

                setResult(RESULT_OK, output);
                finish();
            }
        });
    }
}
