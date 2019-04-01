package ca.imdc.newp;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.HashMap;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //final ArrayList<User> user= RegisterActivity.users;
        final EditText mUsername= (EditText) findViewById(R.id.usernameTextF);
        final EditText  mPassword=(EditText) findViewById(R.id.passwordTextF);
        final TextView mRegisterLink = (TextView) findViewById(R.id.RegisterTV);
        final Button mLoginBtn= (Button) findViewById(R.id.LoginBTN);

 final String username = mUsername.getText().toString();
 final String password =mPassword.getText().toString();
        mRegisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
    });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                mainIntent.putExtra("username", username);
                startActivity(mainIntent);*/
                int check = sqlConn();


                if (check == 1) {
                    Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                    mainIntent.putExtra("username", username);
                    startActivity(mainIntent);
                }
            }
        });}
public int sqlConn(){
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://141.117.145.178:5432/mhmr?currentSchema=UserAccount",
                            "postgres", "1mdCu53R");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM USERINFO;" );
            while ( rs.next() ) {
                int id = rs.getInt("UserId");
                String  name = rs.getString("UserName");
                System.out.println( "ID = " + id );
                System.out.println( "NAME = " + name );
                System.out.println();
            }
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }
        System.out.println("Operation done successfully");
    return 1;
}
}

