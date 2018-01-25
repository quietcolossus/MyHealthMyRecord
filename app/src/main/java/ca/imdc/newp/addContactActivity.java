package ca.imdc.newp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;



/**
 * Created by santh on 2017-11-06.
 */

public class addContactActivity extends Activity {
    public static CharSequence text;
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcontact);

        final Button addContactBtn= (Button) findViewById(R.id.addContactBTN);


        final TextView cNameTV=(TextView)  findViewById(R.id.FnameTV);
        text=cNameTV.getText();
        final Integer click= shareCircleActivity.click;

        /* Create contact Text View*/
final TextView tv1 = shareCircleActivity.tv1;
        final TextView tv2 = shareCircleActivity.tv2;
        final TextView tv3 = shareCircleActivity.tv3;
        final TextView tv4 = shareCircleActivity.tv4;
        final TextView tv5 = shareCircleActivity.tv5;
        final TextView tv6 = shareCircleActivity.tv6;
        final TextView tv7 = shareCircleActivity.tv7;
        final TextView tv8 = shareCircleActivity.tv8;
        final TextView tv9 = shareCircleActivity.tv9;


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
                    tv2.setText(addContactActivity.text);
                    tv2.setTextSize(14);
                    tv2.setTextColor(getResources().getColor(R.color.colorAccent));
                    tv2.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                    tv2.setWidth(250);
                    tv2.setHeight(250);
                    tv2.setX(shareCircleActivity.x);
                    tv2.setY(shareCircleActivity.y - 50);
                    tv2.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.circle_share, null));
                }
                else if(click==3){
                    tv3.setText(addContactActivity.text);
                    tv3.setTextSize(14);
                    tv3.setTextColor(getResources().getColor(R.color.colorAccent));
                    tv3.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                    tv3.setWidth(250);
                    tv3.setHeight(250);
                    tv3.setX(shareCircleActivity.x);
                    tv3.setY(shareCircleActivity.y - 50);
                    tv3.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.circle_share, null));
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
                    tv9.setText(addContactActivity.text);
                    tv9.setTextSize(14);
                    tv9.setTextColor(getResources().getColor(R.color.colorAccent));
                    tv9.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                    tv9.setWidth(250);
                    tv9.setHeight(250);
                    tv9.setX(shareCircleActivity.x);
                    tv9.setY(shareCircleActivity.y - 50);
                    tv9.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.circle_share, null));
                }
                finish();

            }
        });

    }



}
