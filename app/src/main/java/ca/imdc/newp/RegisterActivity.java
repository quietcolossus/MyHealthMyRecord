package ca.imdc.newp;


        import android.content.Intent;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.app.AlertDialog;

        import com.android.volley.Request;
        import com.android.volley.RequestQueue;
        import com.android.volley.Response;
        import com.android.volley.VolleyError;
        import com.android.volley.VolleyLog;
        import com.android.volley.toolbox.JsonObjectRequest;
        import com.android.volley.toolbox.StringRequest;
        import com.android.volley.toolbox.Volley;

        import org.json.JSONException;
        import org.json.JSONObject;
        import android.widget.Spinner;
        import android.widget.Toast;

        import java.lang.reflect.Method;
        import java.sql.Connection;
        import java.sql.DriverManager;
        import java.sql.ResultSet;
        import java.sql.Statement;
        import java.util.HashMap;
        import java.util.Map;


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
                final int check;
                username = mUsernameTF.getText().toString();
                password = mPasswordTF.getText().toString();
                fName = mFirstName.getText().toString();
                lName = mLastName.getText().toString();
                email = mEmail.getText().toString();
                type = mUserType.getSelectedItem().toString();

                if (username.equals("") | password.equals("") | fName.equals("") | lName.equals("") | email.equals("") | type.equals("")){
                    Toast.makeText(v.getContext(), "You must fill out all of the required fields.",Toast.LENGTH_LONG).show();
                }
                else{
                    check = register(username, fName, lName, password, email,type);
                    register(username, fName, lName, password, email,type);
                    if (check == 1) {
                        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                        mainIntent.putExtra("username", username);
                        startActivity(mainIntent);
                    }
                }



            }

        });




    }


    public int register(String username, String firstname, String lastname, String password, String email, String type){

        final String usernameR = username;
        final String passwordR = password;
        final String firstnameR = firstname;
        final String lastnameR = lastname;
        final String emailR = email;
        final String typeR = type;
        final String URL = "http://141.117.145.178:3000/users/";

        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        final Response.Listener<JSONObject> listener = new Response.Listener() {
            @Override
            public void onResponse(Object response) {

            }
        };
        new Thread(new Runnable() {
            public void run() {
                try {
                    StringRequest postRequest = new StringRequest(Request.Method.POST, URL,
                            new Response.Listener<String>()
                            {
                                @Override
                                public void onResponse(String response) {
                                    // response
                                    Log.d("Response", response);
                                }
                            },
                            new Response.ErrorListener()
                            {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // error
                                }
                            }
                    ) {
                        @Override
                        protected Map<String, String> getParams()
                        {
                            final HashMap<String, String> params = new HashMap<String, String>();
                            params.put("UserName",usernameR);
                            params.put("Password", passwordR);
                            params.put("FirstName", firstnameR);
                            params.put("LastName", lastnameR);
                            params.put("Email", emailR);
                            params.put("AccountType",typeR);

                            return params;
                        }
                    };

                    requestQueue.add(postRequest);

                } catch ( Exception e ) {
                    System.err.println( e.getClass().getName()+": "+ e.getMessage() );
                }

            }


        }).start();

        System.out.println("*************************************Operation done successfully*************************************");
        return 0;
    }

}


