package ca.imdc.newp;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;


/**
 * Created by santh on 2017-11-06.
 */

public class addContactActivity extends AppCompatActivity {
    public static CharSequence text;
    public static boolean saveState1=false;
    public static boolean rb1check=false;
    public static boolean rb2check=false;
    public static boolean rb3check=false;
    public static RadioButton rb1;
    public static RadioButton rb2;
    public static RadioButton rb3;
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcontact);

        final Button addContactBtn = findViewById(R.id.addContactBTN);


        final TextView cNameTV = findViewById(R.id.FnameTV);
        text = cNameTV.getText();
        final Integer click = shareCircleActivity.click;

        /* Create contact Text View*/
        final TextView tv1 = shareCircleActivity.tv1;

        final TextView tv4 = shareCircleActivity.tv4;
        final TextView tv5 = shareCircleActivity.tv5;
        final TextView tv6 = shareCircleActivity.tv6;
        final TextView tv7 = shareCircleActivity.tv7;
        final TextView tv8 = shareCircleActivity.tv8;

        final TextView tv9 = shareCircleActivity.tv9;
        final TextView tv2 = shareCircleActivity.tv2;
        final TextView tv3 = shareCircleActivity.tv3;
        rb1 = findViewById(R.id.level1rb);
        rb2 = findViewById(R.id.level2rb);
        rb3 = findViewById(R.id.level3rb);

      /*If certain TextView clicked sets integer click to position number, based on which TV clicked creates a new circle resource in that position */
        addContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (click==1) {
                    tv1.setText(addContactActivity.text);
                    tv1.setTextSize(14);
                    tv1.setTextColor(getResources().getColor(R.color.colorAccent));
                    tv1.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                    tv1.setWidth(250);
                    tv1.setHeight(250);
                    tv1.setX(shareCircleActivity.x);
                    tv1.setY(shareCircleActivity.y - 50);
                    tv1.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.circle_share, null));
                }
                else if(click==2){
                    if(rb1.isChecked())
                    {
                        tv2.setBackground(getDrawable(R.drawable.level1_circle));


                    }
                    else if(rb2.isChecked())
                    {
                        tv2.setBackground(getDrawable(R.drawable.level2_circle));
                    }
                    else if (rb3.isChecked())
                    {
                        tv2.setBackground(getDrawable(R.drawable.level3_circle));
                    }
                }
                else if(click==3){
                    if(rb1.isChecked())
                    {
                        tv3.setBackground(getDrawable(R.drawable.level1_circle));
                    }
                    else if(rb2.isChecked())
                    {
                        tv3.setBackground(getDrawable(R.drawable.level2_circle));
                    }
                    else if (rb3.isChecked())
                    {
                        tv3.setBackground(getDrawable(R.drawable.level3_circle));
                    }
                }
                else if(click==4){
                    tv4.setText(addContactActivity.text);
                    tv4.setTextSize(14);
                    tv4.setTextColor(getResources().getColor(R.color.colorAccent));
                    tv4.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                    tv4.setWidth(250);
                    tv4.setHeight(250);
                    tv4.setX(shareCircleActivity.x);
                    tv4.setY(shareCircleActivity.y - 50);
                    tv4.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.circle_share, null));
                }
                else if(click==5){
                    tv5.setText(addContactActivity.text);
                    tv5.setTextSize(14);
                    tv5.setTextColor(getResources().getColor(R.color.colorAccent));
                    tv5.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                    tv5.setWidth(250);
                    tv5.setHeight(250);
                    tv5.setX(shareCircleActivity.x);
                    tv5.setY(shareCircleActivity.y - 50);
                    tv5.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.circle_share, null));
                }
                else if(click==6){
                    tv6.setText(addContactActivity.text);
                    tv6.setTextSize(14);
                    tv6.setTextColor(getResources().getColor(R.color.colorAccent));
                    tv6.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                    tv6.setWidth(250);
                    tv6.setHeight(250);
                    tv6.setX(shareCircleActivity.x);
                    tv6.setY(shareCircleActivity.y - 50);
                    tv6.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.circle_share, null));
                }
                else if(click==7){
                    tv7.setText(addContactActivity.text);
                    tv7.setTextSize(14);
                    tv7.setTextColor(getResources().getColor(R.color.colorAccent));
                    tv7.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                    tv7.setWidth(250);
                    tv7.setHeight(250);
                    tv7.setX(shareCircleActivity.x);
                    tv7.setY(shareCircleActivity.y - 50);
                    tv7.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.circle_share, null));
                }
                else if(click==8){
                    tv8.setText(addContactActivity.text);
                    tv8.setTextSize(14);
                    tv8.setTextColor(getResources().getColor(R.color.colorAccent));
                    tv8.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                    tv8.setWidth(250);
                    tv8.setHeight(250);
                    tv8.setX(shareCircleActivity.x);
                    tv8.setY(shareCircleActivity.y - 50);
                    tv8.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.circle_share, null));
                }
                else if(click==9){
                    if(rb1.isChecked())
                    {
                        tv9.setBackground(getDrawable(R.drawable.level1_circle));
                    }
                    else if(rb2.isChecked())
                    {
                        tv9.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.level2_circle, null));
                    }
                    else if (rb3.isChecked())
                    {
                        tv9.setBackground(getDrawable(R.drawable.level3_circle));
                    }
                }
                finish();

            }
        });

    }



}
