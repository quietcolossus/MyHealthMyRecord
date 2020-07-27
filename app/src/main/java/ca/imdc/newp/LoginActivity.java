package ca.imdc.newp;

        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;
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

        import com.android.volley.toolbox.StringRequest;
        import com.android.volley.toolbox.Volley;
        import com.firebase.ui.auth.AuthUI;
        import com.firebase.ui.auth.IdpResponse;
        import com.google.android.gms.auth.api.signin.GoogleSignIn;
        import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
        import com.google.android.gms.auth.api.signin.GoogleSignInClient;
        import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
        import com.google.android.gms.common.SignInButton;
        import com.google.android.gms.common.api.ApiException;
        import com.google.android.gms.common.api.GoogleApiClient;
        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.Arrays;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;

        import static android.widget.Toast.*;


public class LoginActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 1;
    GoogleApiClient mGoogleApiClient;
    String googleEmail = "";
    String googleName = "";

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                updateUI(user);

                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Choose authentication providers
        super.onCreate(savedInstanceState);
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build()
                );

// Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    public void updateUI(FirebaseUser user){
        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
        mainIntent.putExtra("email", user.getEmail());
        mainIntent.putExtra("name", user.getDisplayName());
        mainIntent.putExtra("name", user.getUid());
        startActivity(mainIntent);
    }
    public interface VolleyCallback{
        void onSuccess(JSONArray result);
        void onFailure();
    }




    public int sendInfo(final RegisterActivity.VolleyCallback callback, String gmail, String name){

        final String googleEmail = gmail;
        final String googleName = name;
        final String URL = "http://141.117.145.178:3000/videos";
        final RequestQueue requestQueue = Volley.newRequestQueue(this);

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
                                    callback.onSuccess(response);
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
                            params.put("ownername", googleName);
                            params.put("owneremail", googleEmail);
                            params.put("placeholder-video-id", "placeholder");
                            params.put("placeholder-video-data", "placeholder");

                            return params;
                        }
                    };

                    requestQueue.add(postRequest);


                } catch ( Exception e ) {
                    System.err.println( e.getClass().getName()+": "+ e.getMessage() );
                }

            }


        }).start();

        return 0;
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

