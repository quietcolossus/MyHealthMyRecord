package ca.imdc.newp;

/**
 * Created by Win8user on 2017-06-22.
 */
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "https://sashanka.000webhostapp.com/Register.php";
    private Map<String, String> user;

    public RegisterRequest(String fname, String lname, String email, String type, String username, String password, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        user = new HashMap<>();
        user.put("first name", fname);
        user.put("last name", lname);
        user.put("email", email);
        user.put("type", type);
        user.put("username", username);
        user.put("password", password);
    }

    @Override
    public Map<String, String> getParams() {
        return user;
    }
}
