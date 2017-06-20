package ca.imdc.newp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.util.ArrayList;


public class RegisterActivity extends AppCompatActivity {
    public static String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final ArrayList<User> users= new ArrayList <>();
        final EditText mUsername= (EditText) findViewById(R.id.userNameTextF);
        final EditText  mPassword=(EditText) findViewById(R.id.passwordTextF);
        final EditText mFirstName= (EditText) findViewById(R.id.firstNameTextF);
        final EditText  mLastName=(EditText) findViewById(R.id.lastNameTextF);
        final EditText mEmail= (EditText) findViewById(R.id.emailTextF);

        final Button mregisterBtn= (Button) findViewById(R.id.registerSubmitButton);
 username = mUsername.getText().toString();
        mregisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user= new User(username, mPassword.getText().toString(), mFirstName.getText().toString(),mLastName.getText().toString(),mEmail.getText().toString());
                users.add(user);
                Intent mIntent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(mIntent);
            }
        });




    }

}
