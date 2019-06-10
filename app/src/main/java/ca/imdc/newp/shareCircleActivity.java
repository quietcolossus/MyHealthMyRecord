package ca.imdc.newp;

/**
 * Created by santh on 2017-10-19.
 */


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class shareCircleActivity extends AppCompatActivity {
    public static float x;
    public static float y;
public static Integer click;
    public static TextView tv1;

    public static TextView tv4;
    public static TextView tv5;
    public static TextView tv6;
    public static TextView tv7;
    public static TextView tv8;
    public static TextView tv9;
    public static TextView tv2;
    public static TextView tv3;
    private String i;

public SharedPreferences mPrefs;



    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharecircle);
       final SharedPreferences mPrefs = getSharedPreferences("db", 0);
        String back = mPrefs.getString("color",null);
       /* if(back.equals("level1circle")){
            tv2.setBackground(getDrawable(R.drawable.level1_circle));
        }
        if(back.equals("level2circle")){
            tv2.setBackground(getDrawable(R.drawable.level2_circle));
        }
        if(back.equals("level3circle")){
            tv2.setBackground(getDrawable(R.drawable.level3_circle));
        } */





tv1= new TextView(this);

        tv4= new TextView(this);
        tv5= new TextView(this);
        tv6= new TextView(this);
        tv7= new TextView(this);
        tv8= new TextView(this);

       tv9= (TextView) findViewById(R.id.contact9);
       tv2= (TextView) findViewById(R.id.contact2);
        tv3= (TextView) findViewById(R.id.contact3);

        final FloatingActionButton c1 = (FloatingActionButton) findViewById(R.id.contact1);
        final FloatingActionButton c4 = (FloatingActionButton) findViewById(R.id.contact4);
        final FloatingActionButton c5 = (FloatingActionButton) findViewById(R.id.contact5);
        final FloatingActionButton c6 = (FloatingActionButton) findViewById(R.id.contact6);
        final FloatingActionButton c7 = (FloatingActionButton) findViewById(R.id.contact7);
        final FloatingActionButton c8 = (FloatingActionButton) findViewById(R.id.contact8);





        final AlertDialog.Builder builder1 = new AlertDialog.Builder(this);


        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addCIntent = new Intent(shareCircleActivity.this, addContactActivity.class);
                startActivity(addCIntent);
                x=c1.getX();
                y=c1.getY();
              click=1;
                c1.setVisibility(View.INVISIBLE);
                RelativeLayout ll = (RelativeLayout) findViewById(R.id.sharecircle);
                ll.addView(tv1);
            }
        });
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                builder1.setMessage("Contact Info");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Edit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        "Delete",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                tv1.setVisibility(View.INVISIBLE);
                                c1.setVisibility(View.VISIBLE);
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addCIntent = new Intent(shareCircleActivity.this, addContactActivity.class);
                startActivity(addCIntent);
                click=2;
                if(tv2.getBackground()==getResources().getDrawable(R.drawable.level1_circle) ){
                    i = "level1circle";
                }
                if(tv2.getBackground()==getResources().getDrawable(R.drawable.level2_circle)) {
                    i = "level2circle";
                }
                if(tv2.getBackground()==getResources().getDrawable(R.drawable.level1_circle)) {
                    i = "level3circle";
                }
                SharedPreferences.Editor edit = mPrefs.edit();
                edit.putString("color",i);
                edit.apply();
            }
        });
        /*tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                builder1.setMessage("Contact Info");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Edit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        "Delete",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                tv2.setVisibility(View.INVISIBLE);
                                tv2.setVisibility(View.VISIBLE);
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        }); */
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addCIntent = new Intent(shareCircleActivity.this, addContactActivity.class);
                startActivity(addCIntent);

                click=3;

            }
        });
       /* tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                builder1.setMessage("Contact Info");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Edit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        "Delete",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                tv3.setVisibility(View.INVISIBLE);
                                tv3.setVisibility(View.VISIBLE);
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        }); */
        c4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addCIntent = new Intent(shareCircleActivity.this, addContactActivity.class);
                startActivity(addCIntent);
                x=c4.getX();
                y=c4.getY();
                click=4;
                c4.setVisibility(View.INVISIBLE);
                RelativeLayout ll = (RelativeLayout) findViewById(R.id.sharecircle);
                ll.addView(tv4);
            }
        });
        tv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                builder1.setMessage("Contact Info");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Edit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        "Delete",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                tv4.setVisibility(View.INVISIBLE);
                                c4.setVisibility(View.VISIBLE);
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });

        c5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addCIntent = new Intent(shareCircleActivity.this, addContactActivity.class);
                startActivity(addCIntent);
                x=c5.getX();
                y=c5.getY();
                click=5;
                c5.setVisibility(View.INVISIBLE);
                RelativeLayout ll = (RelativeLayout) findViewById(R.id.sharecircle);
                ll.addView(tv5);
            }
        });
        tv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                builder1.setMessage("Contact Info");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Edit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        "Delete",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                tv5.setVisibility(View.INVISIBLE);
                                c5.setVisibility(View.VISIBLE);
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });
        c6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addCIntent = new Intent(shareCircleActivity.this, addContactActivity.class);
                startActivity(addCIntent);
                x=c6.getX();
                y=c6.getY();
                click=6;
                c6.setVisibility(View.INVISIBLE);
                RelativeLayout ll = (RelativeLayout) findViewById(R.id.sharecircle);
                ll.addView(tv6);
            }
        });
        tv6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                builder1.setMessage("Contact Info");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Edit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();


                            }
                        });

                builder1.setNegativeButton(
                        "Delete",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                tv6.setVisibility(View.INVISIBLE);
                                c6.setVisibility(View.VISIBLE);
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });
        c7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addCIntent = new Intent(shareCircleActivity.this, addContactActivity.class);
                startActivity(addCIntent);
                x=c7.getX();
                y=c7.getY();
                click=7;
                c7.setVisibility(View.INVISIBLE);
                RelativeLayout ll = (RelativeLayout) findViewById(R.id.sharecircle);
                ll.addView(tv7);
            }
        });
        tv7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                builder1.setMessage("Contact Info");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Edit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        "Delete",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                tv7.setVisibility(View.INVISIBLE);
                                c7.setVisibility(View.VISIBLE);
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });
        c8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addCIntent = new Intent(shareCircleActivity.this, addContactActivity.class);
                startActivity(addCIntent);
                x=c8.getX();
                y=c8.getY();
                click=8;
                c8.setVisibility(View.INVISIBLE);
                RelativeLayout ll = (RelativeLayout) findViewById(R.id.sharecircle);
                ll.addView(tv8);
            }
        });
        tv8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                builder1.setMessage("Contact Info");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Edit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        "Delete",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                tv8.setVisibility(View.INVISIBLE);
                                c8.setVisibility(View.VISIBLE);
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });
        tv9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addCIntent = new Intent(shareCircleActivity.this, addContactActivity.class);
                startActivity(addCIntent);
                click=9;
            }
        });
       /* tv9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                builder1.setMessage("Contact Info");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Edit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        "Delete",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                tv9.setVisibility(View.INVISIBLE);
                                tv9.setVisibility(View.VISIBLE);
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        }); */

    }
    }
