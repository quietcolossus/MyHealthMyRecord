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
        import android.widget.Toast;

        import com.android.volley.Request;
        import com.android.volley.RequestQueue;
        import com.android.volley.Response;
        import com.android.volley.VolleyError;
        import com.android.volley.toolbox.JsonArrayRequest;

        import com.android.volley.toolbox.Volley;
        import com.google.android.gms.auth.api.Auth;
        import com.google.android.gms.auth.api.signin.GoogleSignIn;
        import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
        import com.google.android.gms.auth.api.signin.GoogleSignInClient;
        import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
        import com.google.android.gms.auth.api.signin.GoogleSignInResult;
        import com.google.android.gms.auth.api.signin.SignInAccount;
        import com.google.android.gms.common.SignInButton;
        import com.google.android.gms.common.api.ApiException;
        import com.google.android.gms.tasks.Task;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import static android.widget.Toast.*;


public class LoginActivity extends AppCompatActivity{
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == 1) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            //updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("ERROR:", "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText mUsername= findViewById(R.id.usernameTextF);
        final EditText  mPassword= findViewById(R.id.passwordTextF);
        final TextView mRegisterLink = findViewById(R.id.RegisterTV);
        final Button mLoginBtn= findViewById(R.id.LoginBTN);
        final SignInButton googleButton= findViewById(R.id.google_signin);

        final String username = mUsername.getText().toString();
        final String password = mPassword.getText().toString();
        final GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        //updateUI(account);

        //Requests user's informations
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken("91115775985-7thudski6t1mhj1a1aj9lufkugsu0elr.apps.googleusercontent.com")
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        final GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 1);
            }

        });




        mRegisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });



        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                loginVolley(new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONArray result) {
                        System.out.println(result);
                        System.out.println("--------------------------------SUCCESS----------");
                        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                        mainIntent.putExtra("username", username);
                        startActivity(mainIntent);
                    }
                    public void onFailure(){
                        if(mUsername.getText().toString().isEmpty() || mPassword.getText().toString().isEmpty()){
                            Toast toast = makeText(v.getContext(), "Username and/or password has not been entered.", LENGTH_SHORT);
                            toast.show();
                        }
                        else{
                            Toast toast = makeText(v.getContext(), "Username and/or password is incorrect.", LENGTH_SHORT);
                            toast.show();
                        }
                    }

                }, mUsername, mPassword);



                System.out.println("Password entered---------------------->"+ mPassword.getText().toString());

            }
        });}

    public void updateUI(GoogleSignInAccount account){
        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);

        startActivity(mainIntent);
    }
    public interface VolleyCallback{
        void onSuccess(JSONArray result);
        void onFailure();
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
                                else{
                                    callback.onFailure();
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
                        System.err.println(error);
                    }
                });
        requestQueue.add(jsonObjReq);
        return 0;
    }





}

