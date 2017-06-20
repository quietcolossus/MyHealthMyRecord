package ca.imdc.newp;

/**
 * Created by Win8user on 2017-06-19.
 */

public class User {
    private String  _username, _password, _firstName, _lastName, _email;


    public User(String username, String password, String firstName, String lastName, String email)
    {

        _username = username;
        _password = password;
        _firstName =firstName;
        _lastName=lastName;
        _email=email;

    }

    public String get_username()
    {
        return _username;
    }
    public String get_password()
    {
        return _password;
    }
    public String get_email()
    {
        return _email;
    }
    public String get_firstName()
    {
        return _firstName;
    }
    public String get_lastName()
    {
        return _lastName;
    }
}
