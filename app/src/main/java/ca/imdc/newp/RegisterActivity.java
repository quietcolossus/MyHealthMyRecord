package ca.imdc.newp;


        import android.content.Intent;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.app.AlertDialog;
        import com.android.volley.RequestQueue;
        import com.android.volley.Response;
        import com.android.volley.toolbox.Volley;

        import org.json.JSONException;
        import org.json.JSONObject;
        import android.widget.Spinner;



public class RegisterActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText mUsernameTF= (EditText) findViewById(R.id.usernameTF);
        final EditText  mPasswordTF=(EditText) findViewById(R.id.passwordTF);
        final EditText mFirstName= (EditText) findViewById(R.id.firstNameTextF);
        final EditText  mLastName=(EditText) findViewById(R.id.lastNameTextF);
        final EditText mEmail= (EditText) findViewById(R.id.emailTextF);
        final Spinner mUserType= (Spinner) findViewById(R.id.userTypeSpinner);

        final Button mregisterBtn= (Button) findViewById(R.id.registerSubmitButton);



        mregisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username;
                final String password;
                final String fName;
                final String lName;
                final String email;
                final String type;
                username = mUsernameTF.getText().toString();
                password = mPasswordTF.getText().toString();
                fName = mFirstName.getText().toString();
                lName = mLastName.getText().toString();
                email = mEmail.getText().toString();
                type = mUserType.getSelectedItem().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");


                            if (success) {
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                builder.setMessage("Register Failed")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                };
                    RegisterRequest registerRequest = new RegisterRequest(username, fName, password, email, type, lName, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(registerRequest);

            }

        });




    }
}


