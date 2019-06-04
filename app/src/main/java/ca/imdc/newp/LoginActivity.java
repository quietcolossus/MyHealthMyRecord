package ca.imdc.newp;

        import android.app.ProgressDialog;

        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.content.Intent;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.TextView;

        import com.android.volley.Request;
        import com.android.volley.RequestQueue;
        import com.android.volley.Response;
        import com.android.volley.VolleyError;
        import com.android.volley.toolbox.JsonArrayRequest;

        import com.android.volley.toolbox.Volley;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;


        import java.sql.Connection;
        import java.sql.DriverManager;
        import java.sql.ResultSet;
        import java.sql.Statement;


public class LoginActivity extends AppCompatActivity{
    Button btnHit;
    TextView txtJson;
    ProgressDialog pd;
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
        final String password = mPassword.getText().toString();








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
                loginVolley(new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONArray result) {
                        System.out.println(result);
                        System.out.println("--------------------------------SUCCESS----------");
                        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                        mainIntent.putExtra("username", username);
                        startActivity(mainIntent);
                    }
                }, mUsername, mPassword);



                System.out.println("Password entered---------------------->"+ mPassword.getText().toString());

            }
        });}
    public interface VolleyCallback{
        void onSuccess(JSONArray result);
    }
    public int loginVolley(final VolleyCallback callback, final EditText username, final EditText password){
        final String url = "http://141.117.145.178:3000/users";
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        final JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try{
                            // Loop through the array elements
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject user = response.getJSONObject(i);


                                String firstName = user.getString("FirstName");
                                String lastName = user.getString("LastName");
                                String userName = user.getString("UserName");
                                String passWord = user.getString("Password");

                                if(userName.equals(username.getText().toString()) && passWord.equals(password.getText().toString())){
                                    System.out.println("Verified through volley request.");
                                    callback.onSuccess(response);
                                }


                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Failure Callback
                        Log.e("ERROR", "Error occurred ", error);
                    }
                });
        requestQueue.add(jsonObjReq);
        return 0;
    }





}

