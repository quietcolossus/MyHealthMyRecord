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
                Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                mainIntent.putExtra("username", username);
                startActivity(mainIntent);
             /*  String username=mUsername.getText().toString();
                String password=mPassword.getText().toString();
if(RegisterActivity.users.size()==0)
{
    Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
    startActivity(registerIntent);

}
else {


    String uPass=RegisterActivity.users.get(username);
            if (password.equals(uPass)) {
                Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(mainIntent);
            }
            else{
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setMessage("Login Failed")
                        .setNegativeButton("Retry", null)
                        .create()
                        .show();
            }

        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
            builder.setMessage("Login Failed")
                    .setNegativeButton("Retry", null)
                    .create()
                    .show();
        }

}
for(int i=0; i<user.size()-1; i++ ) {
    User u=user.get(i);

    if (username.equals("s") &&password.equals("t"))
        startActivity(mainIntent);
    }*/
            }
        });

}
}
