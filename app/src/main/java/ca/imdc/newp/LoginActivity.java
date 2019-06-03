package ca.imdc.newp;

        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.content.Intent;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.TextView;

        import java.sql.Connection;
        import java.sql.DriverManager;
        import java.sql.ResultSet;
        import java.sql.Statement;
        import java.util.concurrent.atomic.AtomicBoolean;


public class LoginActivity extends AppCompatActivity{

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
                //Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                //mainIntent.putExtra("username", username);
                //startActivity(mainIntent);

                int check = verifyLogin(new Listener<Boolean>() {
                    @Override
                    public void on(Boolean arg) {
                        System.out.println("--------------------------------SUCCESS----------");
                        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                        mainIntent.putExtra("username", username);
                        startActivity(mainIntent);
                    }
                }, mUsername, mPassword);
                System.out.println("Password entered---------------------->"+ mPassword.getText().toString());

                if (check == 1) {
                    System.out.println("--------------------------------SUCCESS----------");
                    Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                    mainIntent.putExtra("username", username);
                    startActivity(mainIntent);
                }
            }
        });}
    public int sqlConn(){
        final Connection[] c = {null};
        final Statement[] stmt = {null};
        new Thread(new Runnable() {
            public void run() {
                try {
                    Class.forName("org.postgresql.Driver");
                    c[0] = DriverManager
                            .getConnection("jdbc:postgresql://141.117.145.178:5432/mhmr?currentSchema=UserAccount?sslmode=require",
                                    "postgres", "1mdCu53R");
                    c[0].setAutoCommit(false);
                    System.out.println("*\n**********************************\n***************************************Opened database successfully***********\n*************************************\n**************************");

                    stmt[0] = c[0].createStatement();
                    ResultSet rs = stmt[0].executeQuery( "SELECT * FROM \"UserAccount\".\"UserInfo\";" );
                    while ( rs.next() ) {
                        int id = rs.getInt("UserId");
                        String  name = rs.getString("UserName");
                        System.out.println( "ID = " + id );
                        System.out.println( "NAME = " + name );
                        System.out.println();
                    }
                    rs.close();
                    stmt[0].close();
                    c[0].close();
                } catch ( Exception e ) {
                    System.err.println( e.getClass().getName()+": "+ e.getMessage() );
                }

            }
        }).start();

        System.out.println("*************************************Operation done successfully*************************************");
        return 1;
    }


    public interface Listener<T> {
        void on(T arg);
    }

    public int verifyLogin(final Listener<Boolean> onCompleteListener, final EditText username, final EditText password) {


        final Connection[] c = {null};
        final Statement[] stmt = {null};
        //final LoginActivity.VerifiedCheck verify = new LoginActivity.VerifiedCheck();
        final int[] value = new  int[1];
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    Class.forName("org.postgresql.Driver");
                    c[0] = DriverManager
                            .getConnection("jdbc:postgresql://141.117.145.178:5432/mhmr?currentSchema=UserAccount?sslmode=require",
                                    "postgres", "1mdCu53R");
                    c[0].setAutoCommit(false);
                    System.out.println("Successfully connected to database and ready to verify login attempt.");
                    stmt[0] = c[0].createStatement();
                    ResultSet rs = stmt[0].executeQuery( "SELECT * FROM \"UserAccount\".\"UserInfo\";" );
                    String name1 = null;
                    String password1 = null;
                    while ( rs.next() ) {
                        int id = rs.getInt("UserId");
                        name1 = rs.getString("UserName");
                        password1 = rs.getString("Password");
                        System.out.println( "NAME = " + name1 );
                        System.out.println();

                    }

                    if(username.getText().toString().equals(name1) && password.getText().toString().equals(password1)){
                        onCompleteListener.on(true);
                        System.out.println("Username and Password Verified.");
                        value[0]=  1;
                        //verify.verified.set(true);
                    }

                    stmt[0].close();
                    c[0].close();
                } catch ( Exception e ) {
                    System.err.println( e.getClass().getName()+": "+ e.getMessage() );
                }

            }
        }).start();

        System.out.println(value[0]);
        return value[0];
    }


}

