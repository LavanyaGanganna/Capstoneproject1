package com.example.android.capstoneproject1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.lavanya.myapplication.backend.usersApi.UsersApi;
import com.example.lavanya.myapplication.backend.usersApi.model.CollectionResponseUsers;
import com.example.lavanya.myapplication.backend.usersApi.model.Users;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;


import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SignUp extends AppCompatActivity {
    private static final String TAG = SignUp.class.getSimpleName();
    //  @InjectView(R.id.first_name) EditText firstname;
    //@InjectView(R.id.last_name) EditText lastname;
    @InjectView(R.id.input_email)
    EditText emailtext;
    @InjectView(R.id.input_password)
    EditText passwdtext;
    @InjectView(R.id.confirm_password)
    EditText confirmpasswd;
    @InjectView(R.id.country)
    EditText country;
    @InjectView(R.id.phone)
    EditText phones;
    @InjectView(R.id.btn_signup)
    Button signup;
    boolean valid = true;
    @InjectView(R.id.link_login)
    TextView loginLink;
    boolean paypals;
    double price;
    String errormsg = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        paypals = sharedPreferences.getBoolean(getString(R.string.paypalsh), false);
        if (paypals) {
            price = getIntent().getDoubleExtra(getString(R.string.price), 0);
        }
        ButterKnife.inject(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        validate();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Typeface darllarch = Typeface.createFromAsset(getAssets(), getString(R.string.personalttf));
        TextView mytitle = (TextView) toolbar.getChildAt(0);
        mytitle.setTypeface(darllarch);
        mytitle.setTextSize(30);
        getSupportActionBar().setTitle(R.string.signups);
        boolean tabletsize = getResources().getBoolean(R.bool.isTablet);
        if (tabletsize) {
            mytitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 45);
        }
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signupfunc();
            }
        });
        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(), SignIn.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void signupfunc() {
        // Log.d(TAG, "Signup");
        if (!validate()) {
            onSignupFailed();
            return;
        } else if (validate()) {
            signup.setEnabled(false);
            final ProgressDialog progressDialog = new ProgressDialog(SignUp.this,
                    R.style.AppTheme);
            progressDialog.setIndeterminate(true);
            progressDialog.setIcon(R.drawable.homelog4);
            progressDialog.setMessage(getString(R.string.creatingaccnt));

            String email = emailtext.getText().toString();
            String pswd = passwdtext.getText().toString();
            // String countr = country.getText().toString();
            String phoneno = phones.getText().toString();

            progressDialog.dismiss();
            new EndpointsAsyncTask().execute(email, pswd, phoneno);

        }
    }


    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), R.string.loginfailed, Toast.LENGTH_LONG).show();
        Toast.makeText(getBaseContext(), errormsg, Toast.LENGTH_LONG).show();
        signup.setEnabled(true);
    }

    public boolean validate() {

        emailtext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String email = emailtext.getText().toString();
                if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailtext.setError(getString(R.string.entervalid));

                } else {
                    emailtext.setError(null);
                }
            }
        });


        passwdtext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String pswd = passwdtext.getText().toString();

                if (pswd.isEmpty() || pswd.length() < 4 || pswd.length() > 10) {
                    passwdtext.setError(getString(R.string.betweenfour));

                } else {
                    passwdtext.setError(null);
                }
            }
        });


        confirmpasswd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String cpswd = confirmpasswd.getText().toString();

                if (!(cpswd.equals(passwdtext.getText().toString()))) {
                    confirmpasswd.setError(getString(R.string.passwordsdont));

                } else {
                    confirmpasswd.setError(null);
                }
            }
        });


        phones.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String phno = phones.getText().toString();


            }
        });


        boolean emailvalid = isValidEmail(emailtext.getText().toString());
        if (!emailvalid) {
            errormsg = getString(R.string.notvalid);
        }

        boolean passvalid = isValidPassword(passwdtext.getText().toString());
        if (!passvalid) {
            errormsg = getString(R.string.notvalidpass);
        }
        boolean cpass = isSamepasswd(confirmpasswd.getText().toString(), passwdtext.getText().toString());
        if (!cpass) {
            errormsg = getString(R.string.passdont);
        }
        boolean mobilevalid = isValidmobile(phones.getText().toString());
        if (!mobilevalid) {
            errormsg = getString(R.string.notvalidnum);
        }
        valid = emailvalid && passvalid && cpass && mobilevalid;
        return valid;
    }

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = getString(R.string.a_z)
                + getString(R.string.a_zz);

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isValidPassword(String pass) {
        if (pass != null && pass.length() > 4) {
            return true;
        }
        return false;
    }

    private boolean isSamepasswd(String pass1, String pass2) {
        if (pass1.compareTo(pass2) == 0) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isValidmobile(String phno) {
        if ((Patterns.PHONE.matcher(phno).matches())) {
            return true;
        } else {
            return false;
        }
    }

    class EndpointsAsyncTask extends AsyncTask<String, Void, Users> {
        private UsersApi myApiService = null;
        Users retuser;
        long retid;
        CollectionResponseUsers usersl;

        @Override
        protected Users doInBackground(String... strings) {
            if (myApiService == null) {  // Only do this once
                UsersApi.Builder builder = new UsersApi.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), null)
                        // options for running against local devappserver
                        // - 10.0.2.2 is localhost's IP address in Android emulator
                        // - turn off compression when running against local devappserver
                        // .setRootUrl("http://10.0.2.2:8080/_ah/api/")
                        .setRootUrl(getString(R.string.appspotsignup))
                        .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                            @Override
                            public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                                abstractGoogleClientRequest.setDisableGZipContent(true);
                            }
                        });
                // end options for devappserver

                myApiService = builder.build();
            }

            String email = strings[0];
            String pswrd = strings[1];
            String phno = strings[2];
           /* */
            try {
                //      User user = new User();
                // user.setEmail("lavanyabg@gmail.com");
                retuser = myApiService.checkUser(email).execute();

                if (retuser == null) {
                    Users users = new Users();
                    users.setEmail(email);
                    users.setPword(pswrd);
                    users.setPhno(phno);
                    Users returnuser = myApiService.insert(users).execute();
                }


            } catch (IOException e1) {
                e1.printStackTrace();
            }

            return retuser;
        }

        @Override
        protected void onPostExecute(Users result) {
//            Toast.makeText(SignUp.this, returnuser.getEmail(), Toast.LENGTH_LONG).show();
            if (result == null)
                Toast.makeText(SignUp.this, R.string.accntcreat, Toast.LENGTH_LONG).show();

            else {
                Toast.makeText(getApplicationContext(), R.string.useralready, Toast.LENGTH_LONG).show();
            }
            signup.setEnabled(true);
            setResult(RESULT_OK, null);
            Intent intent = new Intent(SignUp.this, SignIn.class);
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SignUp.this);
            paypals = sharedPreferences.getBoolean(getString(R.string.paypalsh), false);
            if (paypals) {
                intent.putExtra(getString(R.string.price), price);
            }
            startActivity(intent);

        }
    }
}
